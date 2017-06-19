package com.josemanueldsds.curriculum.api.interfaces;

/**
 * Adapter scroll end listener call back
 *
 * @author Jose Manuel De Sousa
 * @version 0.1.0
 */
public interface AdapterScrollListener<M> {

    /**
     * It is called when a scroll render the last item position
     *
     * @param lastItem Object item
     * @param position Integer position
     */
    void onScrollEnded(M lastItem, int position);
}
