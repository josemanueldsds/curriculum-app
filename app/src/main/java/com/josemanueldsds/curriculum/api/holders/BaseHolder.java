package com.josemanueldsds.curriculum.api.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Base Holder
 *
 * @author Jose manuel De Sousa
 * @version 0.1.0
 */
public abstract class BaseHolder extends RecyclerView.ViewHolder {

    /**
     * Base holder constructor
     *
     * @param itemView layout reference views
     */
    public BaseHolder(View itemView) {
        super(itemView);
    }
}
