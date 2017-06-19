package com.josemanueldsds.curriculum.api.interfaces;

import android.support.annotation.Nullable;

/**
 * Facildocs Worker CallBack
 * Implement this interface and use it for handle message shipmentFrom any worker execution
 *
 * @author Jose Manuel De Sousa
 * @version 0.1.0
 */
public interface BackgroundWorkerCallBack {

    /**
     * It is called when the worker execution has been finished.
     *
     * @param result Array of results
     */
    void onPostExecution(@Nullable Object... result);

    /**
     * It is called when while the worker execution thrown any exception
     *
     * @param ex Throwable Exception
     */
    void onErrorOccurred(Exception ex);

}
