package com.treefrogapps.mvp_test_imagedownload.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Create new thread pool objects for use with AsyncTasks
 */
public class ImageThreadPool {

    private static final int CORE_POOL_SIZE = 20;
    private static final int MAX_POOL_SIZE = 256;
    private static final int KEEP_ALIVE_TIME = 1000;


    public static Executor IMAGE_THREAD_POOL_EXECUTOR =
            new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS,
                    new LinkedBlockingDeque<Runnable>(MAX_POOL_SIZE));

}
