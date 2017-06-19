package com.josemanueldsds.curriculum.api.workers;

import android.app.Activity;

import com.josemanueldsds.curriculum.Logger;
import com.josemanueldsds.curriculum.api.interfaces.BackgroundWorkerCallBack;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BackgroundWorker Worker
 *
 * @author Jose Manuel De Sousa
 */
@SuppressWarnings({"unused", "WeakerAccess", "FieldCanBeLocal"})
public abstract class BackgroundWorker extends Thread implements com.josemanueldsds.curriculum.api.interfaces.BackgroundWorker {

    private static final int RESPONSE_LIMIT_TIME = 1000;
    private final String TAG = "BackgroundWorker";
    protected Activity context;
    protected BackgroundWorkerCallBack callBack;
    private boolean isRunning = false;

    /**
     * Constructor
     */
    public BackgroundWorker() {
    }

    /**
     * Constructor
     *
     * @param context  Activity Context
     * @param callBack WievenWorkerListener instance object
     */
    public BackgroundWorker(Activity context, BackgroundWorkerCallBack callBack) {
        setContext(context);
        setCallBack(callBack);
    }

    /**
     * Execute a task single
     *
     * @return the result of service executor
     */
    @Override
    public ExecutorService execute() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(this);
        return executor;
    }

    /**
     * Executes thread pool executor
     *
     * @param threadPoolExecutor ThreadPool Executor Service when is running the background worker
     * @return ExecutorService where is running this task execution
     */
    @Override
    public ExecutorService execute(ExecutorService threadPoolExecutor) {
        if (threadPoolExecutor == null)
            return null;
        threadPoolExecutor.execute(this);
        return threadPoolExecutor;
    }

    @Override
    public void run() {
        try {
            if (context != null) {
                isRunning = true;
            }
            final Object result = doInBackground();
            if (!stopResult()) {
                isRunning = false;
                if (context != null) {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (callBack != null) {
                                callBack.onPostExecution(result);
                            }
                        }
                    });
                }
            }

        } catch (final Exception ex) {
            Logger.error(TAG, ex);
            isRunning = false;
            if (context != null)
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null) {
                            callBack.onErrorOccurred(ex);
                        }
                    }
                });
        }
    }

    /**
     * override this function to stop the result after complete doInBackground algorithm
     *
     * @return false if you want the default return and notificationOnDemand through call back on post execution,
     * otherwise return true if you want to notify through your custom callbacks.
     */
    public boolean stopResult() {
        return false;
    }

    /**
     * set worker context
     *
     * @param context Activity context
     */
    public void setContext(Activity context) {
        this.context = context;
    }

    /**
     * set worker callback
     *
     * @param callBack WorkerCallBack
     */
    public void setCallBack(BackgroundWorkerCallBack callBack) {
        this.callBack = callBack;
    }
}
