package com.liu.study.concurrent.syn;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * @author lwa
 * @version 1.0.0
 * @createTime 2021/2/24 14:31
 */
public class SynchronizedDemoSecond {

    public static void main(String[] args) throws Exception {
        System.out.println(VM.current().details());
        System.out.println();
        System.out.println(ClassLayout.parseClass(Second.class).toPrintable());
    }

    public static class Second {
        long value;
    }

}
