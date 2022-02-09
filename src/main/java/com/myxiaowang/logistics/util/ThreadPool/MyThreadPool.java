package com.myxiaowang.logistics.util.ThreadPool;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 手动创建线程池
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月08日 16:06:00
 */
public class MyThreadPool {
    /**
     * 线程核心数
     */
    private static final int CORE=5;
    /**
     * 线程能容纳的最大核心数量
     */
    private static final int MAX_IMUMPOOL_SIZE=5;

    private static volatile ThreadPoolExecutor threadPool;

    /**
     * 手动创建线程池
     * @param core 线程核心数量
     * @return 线程池
     */
    public static ThreadPoolExecutor createThreadPool(int core){
        if(core<=CORE){
            core+=CORE;
        }
        int temp=MAX_IMUMPOOL_SIZE;
        if(core>MAX_IMUMPOOL_SIZE){
           temp=core*2;
        }
        return new ThreadPoolExecutor(core, temp, 1L, TimeUnit.HOURS, new LinkedBlockingDeque<>(5), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 创建一个共享的线程池
     * @return ThreadPoolExecutor
     */
    public static ThreadPoolExecutor getThreadPool(){
        if(threadPool==null){
            synchronized (MyThreadPool.class){
              if(threadPool==null){
                  threadPool=createThreadPool(10);
              }
            }
        }

        return threadPool;
    }
}
