package ru.DmN.bpl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.DmN.bpl.annotations.Const;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

@SuppressWarnings("unused")
public final class BytecodeUtils {
    public static final String CLASS_NAME = BytecodeUtils.class.getName().replace('.', '/');

    public static void spec$startDelete() {
        throw THROW;
    }

    public static void spec$endDelete() {
        throw THROW;
    }

    public static void spec$label(@NotNull @Const String name) {
        throw THROW;
    }

    public static void spec$opcode(@Const int opcode) {
        throw THROW;
    }

    public static void nop() {
        throw THROW;
    }

    public static MethodHandle ldc$mh(@Const int tag, @Const String owner, @Const String name, @Const String desc, @Const boolean isInterface) {
        throw THROW;
    }

    public static MethodType ldc$mt(@NotNull @Const String desc) {
        throw THROW;
    }

    public static <T> Class<T> ldc$class(@NotNull @Const String name) {
        throw THROW;
    }

    public static void load(@Nullable Object v) {
        throw THROW;
    }

    public static void load(double v) {
        throw THROW;
    }

    public static void load(float v) {
        throw THROW;
    }

    public static void load(long v) {
        throw THROW;
    }

    public static void load(int v) {
        throw THROW;
    }

    public static void load(char v) {
        throw THROW;
    }

    public static void load(short v) {
        throw THROW;
    }

    public static void load(byte v) {
        throw THROW;
    }

    public static void load(boolean v) {
        throw THROW;
    }

    /// GOTO
    public static void jmp(@NotNull @Const String name) {
        throw THROW;
    }

    /// NEW
    public static <T> T alloc(@NotNull @Const String clazz) {
        throw THROW;
    }

    public static void athrow(@NotNull Throwable throwable) {
        throw THROW;
    }

    public static <T> T checkcast(@Nullable Object obj, @NotNull @Const String desc) {
        throw THROW;
    }

    public static boolean instanceOf(@Nullable Object obj, @NotNull @Const String desc) {
        throw THROW;
    }

    public static void monitorenter(@NotNull Object ref) {
        throw THROW;
    }

    public static void monitorexit(@NotNull Object ref) {
        throw THROW;
    }

    public static final Error THROW = new Error("Не обработанная вставка!");
}
