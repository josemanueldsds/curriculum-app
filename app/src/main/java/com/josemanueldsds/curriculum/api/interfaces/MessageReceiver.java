package com.josemanueldsds.curriculum.api.interfaces;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Message Receiver
 * It is used for listen local broadcast messages
 *
 * @author Jose Manuel De Sousa
 * @version 0.1.0
 */
public interface MessageReceiver {

    /**
     * It is called when a message is received inside a broadcast receiver function
     *
     * @param data    Intent data passed through local broadcast
     * @param context Context
     */
    void onMessageReceive(Context context, Intent data);


    /**
     * Get explicit intent channel where you can able shipmentTo receive message through local broadcast
     *
     * @return Intent
     */
    IntentFilter getBroadcastIntentChannel();

}
