package ru.DmN.bpl.test

import org.junit.jupiter.api.Assertions
import org.objectweb.asm.Opcodes
import ru.DmN.bpl.BytecodeUtils
import ru.DmN.bpl.CallBuilder
import ru.DmN.bpl.FieldBuilder
import ru.DmN.bpl.annotations.*
import ru.DmN.uu.Unsafe
import java.lang.invoke.CallSite
import java.lang.invoke.ConstantCallSite
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
import java.lang.reflect.Modifier

@BytecodeProcessor
object KotlinTests {
    @FMRename(desc = "Ljava/lang/Class;")
    @Modifiers(Modifier.PUBLIC or Modifier.STATIC or Modifier.FINAL)
    @JvmStatic
    var `test$field0`: Any? = null

    init {
        val clazz = KotlinTests::class.java
        FieldBuilder("ru/DmN/bpl/test/KotlinTests", "test\$field0", "Ljava/lang/Class;").set(clazz)
    }

    /**
     * Удаление кода №1
     */
    @JvmStatic
    fun test0() {
        BytecodeUtils.`spec$startDelete`()
        Assertions.fail<Any>()
        BytecodeUtils.`spec$endDelete`()
    }

    /**
     * Удаление кода №2
     */
    @DeleteLines(start = [44], end = [45])
    @JvmStatic
    fun test17() {
        Assertions.fail<Any>()
    }

    /**
     * Вставка `nop` опкода
     */
    @JvmStatic
    fun test1() {
        BytecodeUtils.`spec$opcode`(0x0) // NOP
    }

    /**
     * Вставка `goto` и `labels`
     */
    @JvmStatic
    fun test2() {
        var i = 0
        BytecodeUtils.`spec$label`("loop") // loop:
        i++
        if (i < 100)
            BytecodeUtils.jmp("loop") // goto loop
        Assertions.assertEquals(i, 100)
    }

    /**
     * CallBuilder (static)
     */
    @JvmStatic
    fun test3() {
        val a = 12
        val b = 21
        val c = CallBuilder("add", "(II)I", "ru/DmN/bpl/test/KotlinTests\$TestClass0")
            .arg(a)
            .arg(b)
            .invokeStatic(false)
            .endI()
        Assertions.assertEquals(a + b, c)
    }

    /**
     * CallBuilder (virtual)
     */
    @JvmStatic
    fun test4() {
        val a = TestClass1(4)
        val b = 202
        val c = CallBuilder("add", "(I)I", "ru/DmN/bpl/test/KotlinTests\$TestClass1")
            .arg(a)
            .arg(b)
            .invokeVirtual()
            .endI()
        Assertions.assertEquals(a.value + b, c)
    }

    /**
     * CallBuilder (indy) №1
     */
    @JvmStatic
    fun test5() {
        val a = 707
        val b = 70
        val c = CallBuilder("add", "(II)I", "ru/DmN/bpl/test/KotlinTests")
            .arg(a)
            .arg(b)
            .invokeDynamic("bootstrapA", "(Ljava/lang/invoke/MethodHandles\$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;")
            .endI()
        Assertions.assertEquals(a + b, c)
    }

    /**
     * CallBuilder (indy) №2
     */
    @JvmStatic
    fun test6() {
        val a = TestClass1(400)
        val b = 4
        val c = CallBuilder("add", "(Lru/DmN/bpl/test/KotlinTests\$TestClass1;I)I", "ru/DmN/bpl/test/KotlinTests")
            .arg(a)
            .arg(b)
            .invokeDynamic("bootstrap$", "(Ljava/lang/invoke/MethodHandles\$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/Class;ILjava/lang/Class;)Ljava/lang/invoke/CallSite;", 3)
            .arg(TestClass1::class.java)
            .arg(0)
            .arg(KotlinTests::class.java)
            .endI()
        Assertions.assertEquals(a.value + b, c)
    }

    /**
     * CallBuilder (alloc)
     */
    @JvmStatic
    fun test18() {
        val obj = CallBuilder("<init>", "(I)V", "ru/DmN/bpl/test/KotlinTests\$TestClass1")
            .alloc()
            .arg(202)
            .invokeSpecial(false)
            .endA()
        Assertions.assertEquals((obj as TestClass1).value, 202)
    }

    /**
     * FieldBuilder №1
     * Получение статического поля
     */
    @JvmStatic
    fun test7() {
        val value = FieldBuilder("java/lang/System", "out", "Ljava/io/PrintStream;").a
        Assertions.assertEquals(System.out, value)
    }

    /**
     * FieldBuilder №2
     * Получение обычного поля
     */
    @JvmStatic
    fun test8() {
        val obj = TestClass1(777)
        val value = FieldBuilder("ru/DmN/bpl/test/KotlinTests\$TestClass1", "value", "I", obj).i
        Assertions.assertEquals(obj.value, value)
    }

    /**
     * FieldBuilder №3
     * Установка статического поля
     */
    // Котлин не вытащил запроса статического поля в классе, поэтому пользуем кодом из java.
    @JvmStatic
    fun test9() {
        FieldBuilder("ru/DmN/bpl/test/JavaTests\$TestClass2", "GLOBAL_COUNTER", "I").set(202)
        Assertions.assertEquals(JavaTests.TestClass2.GLOBAL_COUNTER, 202)

        FieldBuilder("ru/DmN/bpl/test/JavaTests\$TestClass2", "GLOBAL_COUNTER", "I").set(303)
        Assertions.assertEquals(JavaTests.TestClass2.GLOBAL_COUNTER, 303)
    }

    /**
     * FieldBuilder №4
     * Установка обычного поля
     */
    @JvmStatic
    fun test10() {
        val obj = TestClass2(228)
        FieldBuilder("ru/DmN/bpl/test/KotlinTests\$TestClass2", "value", "I", obj).set(337)
        Assertions.assertEquals(obj.value, 337)
    }

    /**
     * InstanceOf
     */
    @JvmStatic
    fun test11() {
        val f = BytecodeUtils.instanceOf(Any(), "java/lang/String")
        Assertions.assertFalse(f)
        val t = BytecodeUtils.instanceOf("", "java/lang/String")
        Assertions.assertTrue(t)
    }

    /**
     * CheckCast
     */
    @JvmStatic
    fun test12() {
        val obj: Any = 12
        val i = BytecodeUtils.checkcast<Int>(obj, "java/lang/Integer")
        Assertions.assertEquals(i, 12)
    }


    /**
     * Throw
     */
    @JvmStatic
    fun test13() {
        try {
            val throwable = Throwable("Success!")
            BytecodeUtils.athrow(throwable) // throw throwable
            Assertions.fail<Any>()
        } catch (throwable: Throwable) {
            Assertions.assertEquals(throwable.message, "Success!")
        }
    }

    /**
     * Ldc №1
     * Class
     */
    @JvmStatic
    fun test14() {
        val cfn = Class.forName("java.lang.Object")
        val ldc = BytecodeUtils.`ldc$class`<Any>("java/lang/Object")
        Assertions.assertEquals(cfn, ldc)
    }

    /**
     * Ldc №2
     * MethodType
     */
    @JvmStatic
    fun test15() {
        val obj = MethodType.methodType(Void.TYPE, Int::class.javaPrimitiveType)
        val ldc = BytecodeUtils.`ldc$mt`("(I)V")
        Assertions.assertEquals(obj, ldc)
    }

    /**
     * Ldc №3
     * MethodHandle
     */
    @JvmStatic
    fun test16() {
        val obj = MethodHandles.lookup().findStatic(TestClass0::class.java, "add", MethodType.methodType(Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType))
        val ldc = BytecodeUtils.`ldc$mh`(Opcodes.H_INVOKESTATIC, "ru/DmN/bpl/test/KotlinTests\$TestClass0", "add", "(II)I", false)
        Assertions.assertNotEquals(obj, ldc) // obj ptr != ldc ptr
        Assertions.assertEquals(obj.toString(), ldc.toString()) // obj method == ldc method
    }

    /**
     * Множественное наследование
     */
    @JvmStatic
    fun test20() {
        val obj0 = TestClass3()
        val obj1 = TestClass4()
        Assertions.assertEquals(obj0.foo(), 21)
        Unsafe.unsafe.putLong(TestClass4::class.java, 16L, Unsafe.unsafe.getLong(Runnable::class.java, 16L))
        Assertions.assertTrue(TestClass4::class.java.isInterface)
        Assertions.assertEquals(obj1.foo(), 33)
    }

    @JvmStatic
    fun bootstrapA(lookup: MethodHandles.Lookup, name: String?, type: MethodType?): CallSite =
        ConstantCallSite(lookup.findStatic(TestClass0::class.java, name, type))

    @JvmStatic
    fun `bootstrap$`(lookup: MethodHandles.Lookup, name: String, type: MethodType, clazz: Class<*>?, calltype: Int, caller: Class<*>?): CallSite =
        ConstantCallSite(
            when {
                name == "<init>" -> lookup.findConstructor(clazz, type)
                calltype == 0 -> lookup.findVirtual(clazz, name, type.dropParameterTypes(0, 1))
                calltype == 1 -> lookup.findStatic(clazz, name, type)
                calltype == 2 -> lookup.findSpecial(clazz, name, type.dropParameterTypes(0, 1), caller)
                else -> throw IllegalStateException("Unexpected value: $calltype")
            }
        )

    object TestClass0 {
        @JvmStatic
        fun add(a: Int, b: Int): Int {
            return a + b
        }
    }

    @JvmRecord
    data class TestClass1(@JvmField val value: Int) {
        fun add(b: Int): Int {
            return value + b
        }
    }

    class TestClass2(@JvmField var value: Int) {
        fun add(b: Int): Int {
            return value + b
        }
    }

    open class TestClass3 {
        open fun foo(): Int {
            return 21
        }
    }

    @BytecodeProcessor
    @Extends(extend = "ru/DmN/bpl/test/KotlinTests\$TestClass3")
    class TestClass4 {
        @Modifiers(Modifier.PUBLIC)
        @MakeConstructor
        fun init() {
            CallBuilder("<init>", "()V", "ru/DmN/bpl/test/KotlinTests\$TestClass3")
                .arg(this)
                .invokeSpecial(false)
                .end()
        }

        fun foo(): Int =
            CallBuilder("foo", "()I", "ru/DmN/bpl/test/KotlinTests\$TestClass3")
                .arg(this)
                .invokeSpecial(false)
                .endI() + 12
    }
}
