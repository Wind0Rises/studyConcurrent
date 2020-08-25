package com.liu.study.concurrent.custom.pool;

/**
 * @author lwa
 * @version 1.0.0
 * @createTime 2020/8/24 13:10
 */
public interface CustomExecutor {

    /**
     * 线程池顶层接口。
     *
     * @param runnable
     */
    public void execut(Runnable runnable);



}