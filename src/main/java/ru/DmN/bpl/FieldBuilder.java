package ru.DmN.bpl;

import org.jetbrains.annotations.ApiStatus;
import ru.DmN.bpl.annotations.Const;
import ru.DmN.bpl.annotations.NoExpression;
import ru.DmN.bpl.annotations.SingleLine;
@SuppressWarnings("unused")
@SingleLine
public final class FieldBuilder {
    @ApiStatus.Internal
    public static final String CLASS_NAME = FieldBuilder.class.getName().replace('.', '/');

    /**
     * @param owner "Класс-Владелец" поля
     * @param name Имя поля
     * @param desc Дескриптор поля
     */
    public FieldBuilder(@Const String owner, @Const String name, @Const String desc) {
        throw BytecodeUtils.THROW;
    }

    /**
     * @param owner "Класс-Владелец" поля
     * @param name Имя поля
     * @param desc Дескриптор поля
     * @param instance Объект
     */
    public FieldBuilder(@Const String owner, @Const String name, @Const String desc, @NoExpression Object instance) {
        throw BytecodeUtils.THROW;
    }

    public void set(@NoExpression Object v) {
        throw BytecodeUtils.THROW;
    }

    public void set(@NoExpression double v) {
        throw BytecodeUtils.THROW;
    }

    public void set(@NoExpression float v) {
        throw BytecodeUtils.THROW;
    }

    public void set(@NoExpression long v) {
        throw BytecodeUtils.THROW;
    }

    public void set(@NoExpression int v) {
        throw BytecodeUtils.THROW;
    }

    public void set(@NoExpression short v) {
        throw BytecodeUtils.THROW;
    }

    public void set(@NoExpression byte v) {
        throw BytecodeUtils.THROW;
    }

    public void set(@NoExpression boolean v) {
        throw BytecodeUtils.THROW;
    }

    public Object getA() {
        throw BytecodeUtils.THROW;
    }

    public double getD() {
        throw BytecodeUtils.THROW;
    }

    public float getF() {
        throw BytecodeUtils.THROW;
    }

    public long getL() {
        throw BytecodeUtils.THROW;
    }

    public int getI() {
        throw BytecodeUtils.THROW;
    }

    public char getC() {
        throw BytecodeUtils.THROW;
    }

    public short getS() {
        throw BytecodeUtils.THROW;
    }

    public byte getB() {
        throw BytecodeUtils.THROW;
    }

    public boolean getZ() {
        throw BytecodeUtils.THROW;
    }

    public void get() {
        throw BytecodeUtils.THROW;
    }
}
