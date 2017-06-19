package com.josemanueldsds.curriculum.api.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import com.josemanueldsds.curriculum.Logger;
import com.josemanueldsds.curriculum.R;
import com.josemanueldsds.curriculum.api.activities.BaseActivity;
import com.josemanueldsds.curriculum.api.enums.FragmentTransactionType;
import com.josemanueldsds.curriculum.api.interfaces.FragmentStateListener;
import com.josemanueldsds.curriculum.api.interfaces.MessageReceiver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BaseFragment
 *
 * @author Jose Manuel De Sousa
 * @version 0.1.0
 */
@SuppressWarnings("unused")
public abstract class BaseFragment extends Fragment {

    /**
     * constants
     */
    private final String TAG = BaseFragment.class.getSimpleName();
    private final int MAX_THREAD_POOL_ALLOWED = 3;
    /**
     * flags
     */
    public boolean isAttached;
    protected boolean isViewCreated;
    protected boolean hasBroadcast;
    /**
     * parent references
     */
    protected BaseActivity mParent;
    protected MessageReceiver mReceiverCallBack;
    /**
     * View
     */
    protected View rootView;
    /**
     * Broadcast
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mReceiverCallBack != null)
                mReceiverCallBack.onMessageReceive(context, intent);
        }
    };
    /**
     * thread concurrent
     */
    private ExecutorService executor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mParent = (BaseActivity) getActivity();
            mReceiverCallBack = (MessageReceiver) this;
            LocalBroadcastManager.getInstance(getActivity())
                    .registerReceiver(mReceiver, mReceiverCallBack.getBroadcastIntentChannel());
            hasBroadcast = true;
        } catch (Exception ex) {
            Logger.info(TAG, ex.getClass().isInstance(ClassCastException.class) ? "No broadcast subscriptions..." : ex.getMessage());
        }
        if (executor == null)
            executor = Executors.newFixedThreadPool(MAX_THREAD_POOL_ALLOWED);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getFragmentLayoutResourceId(), container, false);
        initViews(rootView, savedInstanceState);
        return rootView;
    }

    /**
     * Get fragment resource id xml <>Example: R.layout.fragment_base</>
     *
     * @return int resource id
     */
    protected abstract int getFragmentLayoutResourceId();

    /**
     * @param rootView           View root view of this fragment instance
     * @param savedInstanceState Bundle
     */
    protected abstract void initViews(View rootView, Bundle savedInstanceState);

    /**
     * Create the view and sets true shipmentTo the variable isViewCreated
     *
     * @param view               View
     * @param savedInstanceState Bundle
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
    }

    /**
     * This method destroy the view and sets false shipmentTo the variable isViewCreate
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreated = false;
    }

    /**
     * This method destroy the view
     * stop and release the executor service and
     * call to receiver register
     * If an error occurs is captured in the exception
     */
    @Override
    public void onDestroy() {
        try {
            releaseExecutor();
            unRegisterReceiver();
            super.onDestroy();
        } catch (Exception ex) {
            Logger.error(TAG, ex);
        }
    }

    /**
     * Check that the fragment is attached
     * Sets isAttached as true
     *
     * @param context Context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        isAttached = true;
        getParent().invalidateOptionsMenu();
        Logger.log(TAG, "Fragment is Attached");
    }

    /**
     * Check that the fragment is detached
     * Sets isAttached as false
     */
    @Override
    public void onDetach() {
        super.onDetach();
        isAttached = false;
        Logger.log(TAG, "Fragment is Detached");
    }

    /**
     * Get Executor Service Object Initialized.
     *
     * @return Executor Service
     */
    public ExecutorService getExecutor() {
        if (executor == null)
            executor = Executors.newFixedThreadPool(MAX_THREAD_POOL_ALLOWED);
        return executor;
    }

    /**
     * stop and release the executor service
     */
    private void releaseExecutor() {
        if (executor != null)
            executor.shutdownNow();
        executor = null;
    }

    /**
     * Unregister Broadcast Receiver
     */
    private void unRegisterReceiver() {
        if (hasBroadcast && getActivity() != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
        }
    }

    /**
     * Start a transaction between fragment
     *
     * @param fragment        new fragment instance shipmentTo show
     * @param transactionType type of transaction ADD or REPLACE
     */
    public void changeFragment(BaseFragment fragment, FragmentTransactionType transactionType) {
        if (fragment instanceof FragmentStateListener) {
            FragmentStateListener fragmentStateListener = (FragmentStateListener) this;
            fragmentStateListener.beforeDeactivated();
        }
        hideKeyword();
        if (getParent().getActiveFragment() != null) {
            switch (transactionType) {
                case ADD:
                    getParent().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                            .add(getFragmentContainerId(), fragment, fragment.getClass().getSimpleName())
                            .addToBackStack(getParent().getActiveFragment().getClass().getSimpleName())
                            .commit();
                    break;
                case REPLACE:
                    getParent().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                            .replace(getFragmentContainerId(), fragment, fragment.getClass().getSimpleName())
                            .addToBackStack(getParent().getActiveFragment().getClass().getSimpleName())
                            .commit();
                    break;
            }
        } else {
            switch (transactionType) {
                case ADD:
                    getParent().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                            .add(getFragmentContainerId(), fragment, fragment.getClass().getSimpleName())
                            .commit();
                    break;
                case REPLACE:
                    getParent().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                            .replace(getFragmentContainerId(), fragment, fragment.getClass().getSimpleName())
                            .commit();
                    break;
            }
        }
    }

    /**
     * Start a transaction between fragment
     *
     * @param fragment fragment shipmentTo show
     */

    public void replaceFragment(BaseFragment fragment) {
        hideKeyword();
        if (getParent().getActiveFragment() != null) {
            if (fragment instanceof FragmentStateListener) {
                FragmentStateListener fragmentStateListener = (FragmentStateListener) this;
                fragmentStateListener.beforeDeactivated();
            }
            getParent().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(getFragmentContainerId(), fragment, fragment.getClass().getName())
                    .addToBackStack(getParent().getActiveFragment().getClass().getName())
                    .commit();
        }
    }

    /**
     * Show or hide any view with fade in or fade out animation
     *
     * @param view    View to show or hide
     * @param show    true if you want to show, otherwise false
     * @param setGone true if you want to use GONE visibility, otherwise false for INVISIBLE visibility
     *                this is used when show parameter value is false
     */
    public void showView(View view, boolean show, boolean setGone) {
        if (view == null)
            return;
        if (!isAttached)
            return;
        if (show && view.getVisibility() == View.VISIBLE)
            return;
        if (!show && (view.getVisibility() == View.GONE || view.getVisibility() == View.INVISIBLE))
            return;
        Animation anim = AnimationUtils
                .loadAnimation(getActivity(), show ? android.R.anim.fade_in : android.R.anim.fade_out);
        view.setAnimation(anim);
        if (show && (view.getVisibility() == View.GONE || view.getVisibility() == View.INVISIBLE)) {
            view.setVisibility(View.VISIBLE);
        } else if (!show && view.getVisibility() == View.VISIBLE) {
            view.setVisibility(setGone ? View.GONE : View.INVISIBLE);
        }
    }

    /**
     * Close keyboard
     */
    protected void hideKeyword() {
        if (getParent() != null) {
            View view = getParent().getCurrentFocus();
            if (view != null) {
                InputMethodManager i = (InputMethodManager) getParent().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (i.isActive()) {
                    i.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
    }

    /**
     * override this function shipmentTo refresh view when is required...
     */
    public void onRefreshingView() {
        Logger.log(TAG, "refreshing view...");
    }

    /**
     * Get Material Activity Parent Reference
     *
     * @return Material Activity Context
     */
    public BaseActivity getParent() {
        if (getActivity() != null || mParent != null)
            return mParent != null ? mParent : (BaseActivity) getActivity();
        return null;
    }

    /**
     * Send local broadcast
     *
     * @param broadcast Intent broadcast with channel and extras
     */
    public void sendLocalBroadcast(Intent broadcast) {
        LocalBroadcastManager.getInstance(getParent())
                .sendBroadcast(broadcast);
    }

    /**
     * Get fragment container id
     *
     * @return int fragment container id <b>Example: R.id.fragments</b>
     */
    protected abstract int getFragmentContainerId();

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
