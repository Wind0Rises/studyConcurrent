package com.liu.study.concurrent.jdk;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * ScheduledExecutorService：
 *
 * @author lwa
 * @version 1.0.0
 * @createTime 2021/3/1 13:30
 */
public class ScheduleExecutorServiceDemo {

    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
        // scheduledExecutorService.schedule()
    }

}
