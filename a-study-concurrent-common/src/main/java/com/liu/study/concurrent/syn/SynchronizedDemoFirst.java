package com.liu.study.concurrent.syn;

import org.openjdk.jol.info.ClassLayout;

/**
 * @author lwa
 * @version 1.0.0
 * @createTime 2021/2/24 13:19
 */
public class SynchronizedDemoFirst {

    /**
     * 1（byte） = 8位
     * 8（byte） = 64位
     * loss due to the next object alignment：由于下一个对象对齐而造成的损失。
     *
     * 整体对象的纯大小为20bytes，不能满足8bytes对齐，所以需要补位。补位4bytes。需要是8的倍数。
     *
     * 可以看出：一个TestObject占用16（byte）
     *
     * 对于64bits的JVM，开启指针压缩的对象头占12bytes（指针压缩将8bytes的reference类型压缩成了4bytes，
     * 本来对象头包括MarkWord和一个指向对象类型的reference类型，32bitsJVM的MarkWord占32bits，64bitsJVM
     * 的MarkWord占64bits，即8bytes，加上压缩指针后的对象类型指针，就是12bytes）
     *
     * -XX:-UseCompressedOops 关闭指针压缩。
     *
     *
     *
     * 分析：
     * com.liu.study.concurrent.syn.SynchronizedDemoFirst$TestObject object internals:
     *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
     *       0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
     *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     *       8     4        (object header)                           80 1c c6 1b (10000000 00011100 11000110 00011011) (465968256)
     *      12     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
     *
     * 第一行：00000001：描述GC信息与锁信息。
     *
     *
     * @param args
     */
    public static void main(String[] args) {
        TestObject testObject = new TestObject();

        System.out.println(ClassLayout.parseInstance(testObject).toPrintable());

        System.out.println("-------------------------------------------------------");
        System.out.println("十六进制hashcode： " + Integer.toHexString(testObject.hashCode()));
        System.out.println("十进制hashcode：" + testObject.hashCode());
        System.out.println(ClassLayout.parseInstance(testObject).toPrintable());

    }

    /**
     * 十六进制hashcode： 2173f6d9
     * 十进制hashcode：561247961  -->  10 0001 0111 0011 1111 0110 1101 1001
     *
     *
     *
     *
     * 00000001 -> 1是最后一个bit。
     * com.liu.study.concurrent.syn.SynchronizedDemoFirst$TestObject object internals:
     *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
     *       0     4        (object header)                           01 d9 f6 73 (00000001 11011001 11110110 01110011) (1945557249)   // 左边是开始，右边是结束
     *       4     4        (object header)                           21 00 00 00 (00100001 00000000 00000000 00000000) (33)
     *       8     4        (object header)                           70 1e 86 1c (01110000 00011110 10000110 00011100) (478551664)
     *      12     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
     */

    static class TestObject {

    }

}
