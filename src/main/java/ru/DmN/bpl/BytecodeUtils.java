package ru.DmN.bpl;

import ru.DmN.bpl.annotations.Const;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

public final class BytecodeUtils {
    public static final String CLASS_NAME = BytecodeUtils.class.getName().replace('.', '/');

    public static void spec$startDelete() {
        throw THROW;
    }

    public static void spec$endDelete() {
        throw THROW;
    }

    public static void spec$label(@Const String name) {
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

    public static MethodType ldc$mt(@Const String desc) {
        throw THROW;
    }

    public static <T> Class<T> ldc$class(@Const String name) {
        throw THROW;
    }

    public static void load(Object v) {
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

    public static void pop() {
        throw THROW;
    }

    public static void pop2() {
        throw THROW;
    }

    public static void dup() {
        throw THROW;
    }

    public static void dup_x1() {
        throw THROW;
    }

    public static void dup_x2() {
        throw THROW;
    }

    public static void dup2() {
        throw THROW;
    }

    public static void dup2_x1() {
        throw THROW;
    }

    public static void dup2_x2() {
        throw THROW;
    }

    public static void swap() {
        throw THROW;
    }

    /// GOTO
    public static void jmp(@Const String name) {
        throw THROW;
    }

    /// NEW
    public static <T> T alloc(@Const String clazz) {
        throw THROW;
    }

    public static void athrow(Throwable throwable) {
        throw THROW;
    }

    public static <T> T checkcast(Object obj, @Const String desc) {
        throw THROW;
    }

    public static boolean instanceOf(Object obj, @Const String desc) {
        throw THROW;
    }

    public static void monitorenter(Object ref) {
        throw THROW;
    }

    public static void monitorexit(Object ref) {
        throw THROW;
    }

    public static final Error THROW = new Error("Не обработанная вставка!");
}
