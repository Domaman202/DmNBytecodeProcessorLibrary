package ru.DmN.bpl.test;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import ru.DmN.bpl.ClassProcessor;
import ru.DmN.uu.Unsafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

public class TestBootstrap {
    public static final SmartClassLoader loader = new SmartClassLoader(TestBootstrap.class.getClassLoader());

    @Test
    public void main() throws Throwable {
        var tests = loader.smartLoad("ru.DmN.bpl.test.Tests", true);
        for (var method : tests.getMethods()) {
            if (method.getName().startsWith("test")) {
                method.invoke(null);
            }
        }
    }

    @SuppressWarnings("removal")
    @Override
    protected void finalize() throws Throwable {
        try {
            loader.close();
        } finally {
            super.finalize();
        }
    }

    public static class SmartClassLoader extends URLClassLoader {
        public SmartClassLoader(ClassLoader parent) {
            super(new URL[0], parent);
            new File("dump").mkdir();
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            if (name.equals("ru.DmN.bpl.test.Tests$SmartClassLoader"))
                return this.getClass();
            if (name.startsWith("ru")) {
                try {
                    return this.smartLoad(name, true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return super.loadClass(name, resolve);
        }

        public Class<?> smartLoad(String name, boolean dump) throws IOException, ClassNotFoundException {
            return this.smartLoad(name, dump, null);
        }

        public Class<?> smartLoad(String name, boolean dump, Class<?> lookup) throws IOException, ClassNotFoundException {
            var file = name.replace('.', '/') + ".class";
            try (var stream = this.getResourceAsStream(file)) {
                if (stream == null)
                    throw new ClassNotFoundException(name);
                var bytes = process(stream.readAllBytes());
                if (dump) {
                    var dfile = new File("dump/" + name + ".class");
                    if (dfile.exists())
                        dfile.delete();
                    dfile.createNewFile();
                    try (var fos = new FileOutputStream(dfile)) {
                        fos.write(bytes);
                    }
                }
                if (lookup == null)
                    return this.defineClass(name, bytes, 0, bytes.length);
                return Unsafe.lookup.in(lookup).defineClass(bytes);
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            }
        }

        public static byte[] process(byte[] bytes) {
            var node = new ClassProcessor();
            new ClassReader(bytes).accept(node, 0);
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            node.accept(writer);
            return writer.toByteArray();
        }
    }
}