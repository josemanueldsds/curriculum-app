package com.josemanueldsds.curriculum.api.interfaces;

import java.util.ArrayList;

/**
 * Adapter Change Listener
 *
 * @author Jose Manuel De Sousa
 * @version 0.0.1
 */
public interface AdapterChangeListener<M> {

    /**
     * It is called when the dataSet inside any adapter has been changed.
     *
     * @param newDataSet ArrayList of Object Model
     */
    void onDataSetChange(ArrayList<M> newDataSet);

}
