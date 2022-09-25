module ru.DmN.bu.test {
    requires jdk.unsupported;
    requires org.objectweb.asm;
    requires org.objectweb.asm.tree;
    requires org.junit.jupiter.api;
    requires ru.DmN.uu;
    requires ru.DmN.bu;

    exports ru.DmN.bpl.test;
}