package ru.DmN.bpl;

import org.jetbrains.annotations.ApiStatus;
import ru.DmN.bpl.annotations.Const;
import ru.DmN.bpl.annotations.NoExpression;
import ru.DmN.bpl.annotations.SingleLine;

@SuppressWarnings("unused")
@SingleLine
public final class CallBuilder {
    @ApiStatus.Internal
    public static final String CLASS_NAME = CallBuilder.class.getName().replace('.', '/');

    public CallBuilder(@Const String name, @Const String desc, @Const String clazz) {
        throw BytecodeUtils.THROW;
    }

    public CallBuilder alloc() {
        throw BytecodeUtils.THROW;
    }

    public CallBuilder arg(@NoExpression Object value) {
        throw BytecodeUtils.THROW;
    }

    public CallBuilder arg(@NoExpression double value) {
        throw BytecodeUtils.THROW;
    }

    public CallBuilder arg(@NoExpression float value) {
        throw BytecodeUtils.THROW;
    }

    public CallBuilder arg(@NoExpression long value) {
        throw BytecodeUtils.THROW;
    }

    public CallBuilder arg(@NoExpression int value) {
        throw BytecodeUtils.THROW;
    }

    public CallBuilder arg(@NoExpression short value) {
        throw BytecodeUtils.THROW;
    }

    public CallBuilder arg(@NoExpression byte value) {
        throw BytecodeUtils.THROW;
    }

    public CallBuilder arg(@NoExpression boolean value) {
        throw BytecodeUtils.THROW;
    }

    public CallBuilder invokeVirtual() {
        throw BytecodeUtils.THROW;
    }

    public CallBuilder invokeSpecial(@Const boolean isInterface) {
        throw BytecodeUtils.THROW;
    }

    public CallBuilder invokeStatic(@Const boolean isInterface) {
        throw BytecodeUtils.THROW;
    }

    public CallBuilder invokeInterface() {
        throw BytecodeUtils.THROW;
    }

    public CallBuilder invokeDynamic(@Const String bname, @Const String bdesc) {
        throw BytecodeUtils.THROW;
    }

    public CallBuilder invokeDynamic(@Const String bname, @Const String bdesc, @Const int argc) {
        throw BytecodeUtils.THROW;
    }

    public CallBuilder ldc(@Const String name, @Const String desc, @Const String clazz, @Const String bname, @Const String bdesc) {
        throw BytecodeUtils.THROW;
    }

    public CallBuilder ldc(@Const String name, @Const String desc, @Const String clazz, @Const String bname, @Const String bdesc, @Const int argc) {
        throw BytecodeUtils.THROW;
    }

    public Object endA() {
        throw BytecodeUtils.THROW;
    }

    public double endD() {
        throw BytecodeUtils.THROW;
    }

    public float endF() {
        throw BytecodeUtils.THROW;
    }

    public long endL() {
        throw BytecodeUtils.THROW;
    }

    public int endI() {
        throw BytecodeUtils.THROW;
    }

    public char endC() {
        throw BytecodeUtils.THROW;
    }

    public short endS() {
        throw BytecodeUtils.THROW;
    }

    public byte endB() {
        throw BytecodeUtils.THROW;
    }

    public boolean endZ() {
        throw BytecodeUtils.THROW;
    }

    public void end() {
        throw BytecodeUtils.THROW;
    }
}
