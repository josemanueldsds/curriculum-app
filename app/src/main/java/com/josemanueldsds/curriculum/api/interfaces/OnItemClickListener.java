package com.josemanueldsds.curriculum.api.interfaces;

import android.view.View;

/**
 * OnItemClickListener contract
 * It is used for handle the click listener over items inside of any recycler view object
 *
 * @param <T> T Generic type object model
 * @author Jose Manuel De Sousa
 * @version 0.1.0
 */
public interface OnItemClickListener<T> {

    /**
     * onItemClick method
     * It is called when an row item is clicked over recycler view
     *
     * @param view     View clicked
     * @param entity   T entity object model in the position clicked
     * @param position Integer position clicked
     */
    void onItemClick(View view, T entity, int position);
}
