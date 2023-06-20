package ru.DmN.bpl;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.DmN.bpl.annotations.Const;
import ru.DmN.bpl.exceptions.NotProcessedInsertionException;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

@SuppressWarnings("unused")
public final class BytecodeUtils {
    public static final String CLASS_NAME = BytecodeUtils.class.getName().replace('.', '/');

    /**
     * Точка начала удаления кода , удаляет код до {@link BytecodeUtils#spec$startDelete()}
     */
    public static void spec$startDelete() {
        throw THROW;
    }

    /**
     * Точка конца удаления кода
     */
    public static void spec$endDelete() {
        throw THROW;
    }

    /**
     * Метка
     * @param name Имя метки
     */
    public static void spec$label(@NotNull @Const String name) {
        throw THROW;
    }

    /**
     * Вставка опкода
     * @param opcode Код опкода
     */
    public static void spec$opcode(@Const int opcode) {
        throw THROW;
    }

    /**
     * Вставка ldc для {@link MethodHandle}
     * @param tag <a href="https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-4.html#jvms-4.4.8">Java спецификация</a>.
     * @param owner "Класс-Владелец"
     * @param name Имя
     * @param desc Дескриптор
     * @param isInterface Владелец - интерфейс?
     * @return Объект класса {@link MethodHandle}
     */
    public static MethodHandle ldc$mh(@Const int tag, @Const String owner, @Const String name, @Const String desc, @Const boolean isInterface) {
        throw THROW;
    }

    /**
     * Вставка ldc для {@link MethodType}
     * @param desc Дескриптор
     * @return Объект класса {@link MethodType}
     */
    public static MethodType ldc$mt(@NotNull @Const String desc) {
        throw THROW;
    }

    /**
     * Вставка ldc для загрузки класса
     * @param name Имя класса
     * @return Класс
     * @param <T> Класс
     */
    public static <T> Class<T> ldc$class(@NotNull @Const String name) {
        throw THROW;
    }

    /**
     * Загружает в стек объект
     * @param value Объект
     */
    public static void load(@Nullable Object value) {
        throw THROW;
    }

    /**
     * Загружает в стек число
     * @param value Число
     */
    public static void load(double value) {
        throw THROW;
    }

    /**
     * Загружает в стек число
     * @param value Число
     */
    public static void load(float value) {
        throw THROW;
    }

    /**
     * Загружает в стек число
     * @param value Число
     */
    public static void load(long value) {
        throw THROW;
    }

    /**
     * Загружает в стек число
     * @param value Число
     */
    public static void load(int value) {
        throw THROW;
    }

    /**
     * Загружает в стек число
     * @param value Число
     */
    public static void load(char value) {
        throw THROW;
    }

    /**
     * Загружает в стек число
     * @param value Число
     */
    public static void load(short value) {
        throw THROW;
    }

    /**
     * Загружает в стек число
     * @param value Число
     */
    public static void load(byte value) {
        throw THROW;
    }

    /**
     * Загружает в стек число
     * @param value Число
     */
    public static void load(boolean value) {
        throw THROW;
    }

    /**
     * Вставка перехода на метку
     * @param name Имя метки
     */
    public static void jmp(@NotNull @Const String name) {
        throw THROW;
    }

    /// NEW
    public static <T> T alloc(@NotNull @Const String clazz) {
        throw THROW;
    }

    /**
     * Кидает исключение
     * @param throwable Исключение
     */
    public static void athrow(@NotNull Throwable throwable) {
        throw THROW;
    }

    /**
     * Проводит приведение типов
     * @param obj Объект
     * @param desc Дескриптор (к чему приводить)
     * @return Приведённый к типу объект
     * @param <T> Тип к которому будет приведён объект
     */
    public static <T> T checkcast(@Nullable Object obj, @NotNull @Const String desc) {
        throw THROW;
    }

    /**
     * Вставка instanceof
     * @param obj Объект
     * @param desc Дескриптор (с чем сравнивать)
     * @return instanceof desc
     */
    public static boolean instanceOf(@Nullable Object obj, @NotNull @Const String desc) {
        throw THROW;
    }

    /**
     * Synchronized (начало)
     * @param ref Объект
     */
    public static void monitorenter(@NotNull Object ref) {
        throw THROW;
    }

    /**
     * Synchronized (Конец)
     * @param ref Объект
     */
    public static void monitorexit(@NotNull Object ref) {
        throw THROW;
    }

    @ApiStatus.Internal
    public static final NotProcessedInsertionException THROW = new NotProcessedInsertionException("Не обработанная вставка!");
}
