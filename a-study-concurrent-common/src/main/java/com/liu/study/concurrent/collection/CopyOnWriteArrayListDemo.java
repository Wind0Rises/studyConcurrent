package com.liu.study.concurrent.collection;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * 写入时复制（CopyOnWrite，简称COW）思想是计算机程序设计领域中的一种优化策略。
 * 其核心思想是，如果有多个调用者（Callers）同时要求相同的资源（如内存或者是磁盘上的数据存储），他们会共同获取相同的指针指向相同的资源，
 * 直到某个调用者视图修改资源内容时，系统才会真正复制一份专用副本（private copy）给该调用者，而其他调用者所见到的最初的资源仍然保持不变。
 * 这过程对其他的调用者都是透明的（transparently）。此做法主要的优点是如果调用者没有修改资源，就不会有副本（private copy）被创建，
 * 因此多个调用者只是读取操作时可以共享同一份资源。
 *
 * 也就是在操作的时候回加锁。
 *
 * <note>
 *     1. 底层是用volatile transient声明的数组array。
 *     2. 内部使用ReentrantLock进行加锁。
 *     3. 读写分离，写时复制出一个新的数组，完成插入、修改或者移除操作后将新数组赋值给array。
 * </note>
 *
 *
 * <note>
 *     put()操作会加锁。
 *     get()操作不会加锁，
 * </note>
 *
 * @author lwa
 * @version 1.0.0
 * @createTime 2021/1/25 13:10
 */
public class CopyOnWriteArrayListDemo {

    public static void main(String[] args) {
        CopyOnWriteArrayList list = new CopyOnWriteArrayList();

        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        integers.add(4);

        integers.add(2, 49);
        System.out.println(integers);
    }

}
