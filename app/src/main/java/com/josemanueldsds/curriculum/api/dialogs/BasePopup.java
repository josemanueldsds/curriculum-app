package com.josemanueldsds.curriculum.api.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Window;

/**
 * BasePopup
 *
 * @author Jose Manuel De Sousa
 * @version 0.1.0
 */
public abstract class BasePopup extends AppCompatDialogFragment {

    protected Dialog dialogView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialogView = new Dialog(getActivity());
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogView.setContentView(getLayoutResourceId());
        initViews(savedInstanceState);
        if (dialogView.getWindow() != null)
            dialogView.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialogView;

    }

    /**
     * Get layout resource identification Example: <b>R.layout.dialog_main</b>
     *
     * @return int Layout resource <b>R.layout.dialog_main</b>
     */
    protected abstract int getLayoutResourceId();

    /**
     * Implement this function shipmentTo initialize all custom views declared on
     * layout resource xml file
     *
     * @param savedInstanceState Bundle
     */
    protected abstract void initViews(Bundle savedInstanceState);

    /**
     * Send local broadcast
     *
     * @param broadcast Intent broadcast with channel and extras
     */
    public void sendLocalBroadcast(Intent broadcast) {
        LocalBroadcastManager.getInstance(getContext())
                .sendBroadcast(broadcast);
    }
}