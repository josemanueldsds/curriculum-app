package com.josemanueldsds.curriculum.api.interfaces;

import java.util.concurrent.ExecutorService;

/**
 * Define a custom thread background worker
 *
 * @author Jose Manuel De Sousa
 * @version 0.1.0
 */
public interface BackgroundWorker {

    /**
     * It is used for execute any algorithm in a thread pool executor
     * to far away from the MainThread.
     */
    Object doInBackground() throws Exception;

    /**
     * Call this function to star running the background worker
     *
     * @return ThreadPool Executor Service when is running the background worker
     */
    ExecutorService execute();

    /**
     * Call this function to star running the background worker
     *
     * @param threadPoolExecutor ThreadPool Executor Service when is running the background worker
     * @return ThreadPool Executor Service when is running the background worker
     */
    ExecutorService execute(ExecutorService threadPoolExecutor);

}
