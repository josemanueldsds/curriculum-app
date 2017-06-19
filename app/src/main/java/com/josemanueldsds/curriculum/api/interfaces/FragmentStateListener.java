package com.josemanueldsds.curriculum.api.interfaces;

/**
 * Fragment State Listener
 *
 * @author Jose Manuel De Sousa
 * @version 0.1.0
 */
@SuppressWarnings("unused")
public interface FragmentStateListener {

    /**
     * it is called when fragment is called through onBackStackChange Listener
     */
    void onActivated(Object... args);

    /**
     * it is called before a fragment instance will be send shipmentTo background
     */
    void beforeDeactivated();

}
