package com.josemanueldsds.curriculum.api.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.josemanueldsds.curriculum.Logger;
import com.josemanueldsds.curriculum.R;
import com.josemanueldsds.curriculum.api.enums.FragmentTransactionType;
import com.josemanueldsds.curriculum.api.fragments.BaseFragment;
import com.josemanueldsds.curriculum.api.interfaces.FragmentStateListener;
import com.josemanueldsds.curriculum.api.interfaces.MessageReceiver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Base Activity class
 *
 * @author Jose Manuel De Sousa
 * @version 0.1.0
 */

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * constant
     */
    private final String TAG = BaseActivity.class.getSimpleName();
    private final int OPEN_SETTINGS = 101;
    private final int MAX_THREAD_POOL_ALLOWED = 3;
    /**
     * ActionBar
     */
    protected CharSequence mTitle;
    /**
     * flags
     */
    protected boolean isAuthenticated;
    /**
     * thread concurrent
     */
    private ExecutorService executor;
    private boolean hasBroadcastReceiver;
    private Toolbar mToolBar;
    /**
     * Broadcast
     */
    private MessageReceiver mReceiverCallBack;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mReceiverCallBack != null && hasBroadcastReceiver) {
                mReceiverCallBack.onMessageReceive(context, intent);
            }
        }
    };
    private boolean isBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (setFullScreen()) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(getLayoutResource());
        setSupportToolBar(null);

        try {
            mReceiverCallBack = (MessageReceiver) this;
            LocalBroadcastManager.getInstance(this)
                    .registerReceiver(mReceiver, mReceiverCallBack.getBroadcastIntentChannel());
            hasBroadcastReceiver = true;
        } catch (ClassCastException ex) {
            Logger.info(TAG, "This activity does not have broadcast receiver");
            hasBroadcastReceiver = false;
        }
        if (executor == null)
            executor = Executors.newFixedThreadPool(MAX_THREAD_POOL_ALLOWED);
    }

    @Override
    public void finish() {
        hideKeyword();
        super.finish();
    }

    public void hideKeyword() {
        try {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive())
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * If receiver a broadcast then registers
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            releaseExecutor();
            if (hasBroadcastReceiver) {
                LocalBroadcastManager
                        .getInstance(this).unregisterReceiver(mReceiver);
            }
        } catch (Exception e) {
            Logger.error(TAG, e);
        }
    }

    /**
     * Get Executor Service Object Initialized.
     *
     * @return Executor Service
     */
    public ExecutorService getExecutor() {
        return executor;
    }

    private void releaseExecutor() {
        if (executor != null)
            executor.shutdownNow();
        executor = null;
    }

    /**
     * Sets values of toolbar
     *
     * @param toolBar Support v7 Toolbar
     */
    public void setSupportToolBar(Toolbar toolBar) {
        if (toolBar == null)
            mToolBar = (Toolbar) findViewById(getToolBarResourceId());
        else
            mToolBar = toolBar;
        if (getToolBar() != null) {
            Logger.info(TAG, "Setting up material toolbar");
            setSupportActionBar(getToolBar());
            try {
                if (setToolbarAutoPadding())
                    mToolBar.setPadding(0, getStatusBarHeight(), 0, 0);
            } catch (Exception ex) {
                Logger.error(TAG, ex);
            }
        }
        /* Set status bar color if it is available */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (setTranslucentStatus()) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }

    /**
     * Set drawable to ActionBar
     *
     * @param iconRes int resource drawable id to set on ActionBar
     */
    protected void setActionBarIcon(int iconRes) {
        if (getToolBar() != null)
            getToolBar().setNavigationIcon(iconRes);
    }

    /**
     * Set drawable to ActionBar
     *
     * @param bitmapDrawable BitmapDrawable to set on ActionBar
     */
    protected void setActionBarIcon(BitmapDrawable bitmapDrawable) {
        if (mToolBar != null)
            mToolBar.setNavigationIcon(bitmapDrawable);
    }

    /**
     * Set title to actionBar
     *
     * @param title CharSequence to set on ActionBar
     */
    public void setTitle(CharSequence title) {
        mTitle = title;
        TextView appBarTitle = (TextView) findViewById(getAppBarTitleId());
        if (appBarTitle != null) {
            if (title != null) {
                appBarTitle.setVisibility(View.VISIBLE);
                appBarTitle.setText(title);
            } else {
                appBarTitle.setVisibility(View.GONE);
            }
        } else if (getSupportActionBar() != null && mTitle != null)
            getSupportActionBar().setTitle(mTitle);
    }


    public CharSequence getSupportActionBarTitle() {
        return mTitle;
    }

    /**
     * Set subtitle to actionBar
     *
     * @param subTitle CharSequence to set on ActionBar under title
     */
    public void setSubTitle(CharSequence subTitle) {
        if (subTitle == null)
            return;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(subTitle);
        }
    }

    /**
     * Get current action bar
     *
     * @return Support v7 ToolBar object
     */
    public Toolbar getToolBar() {
        return mToolBar;
    }

    /**
     * Get the status bar height for kitkat, lollipop and m devices
     *
     * @return int height in pixels
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * Get Layout resource int value
     *
     * @return int layout resource <b>Example: R.layout.activity_main</b>
     */
    public abstract int getLayoutResource();

    /**
     * Get Toolbar layout id declared in the xml for this view
     *
     * @return int toolbar id <b>Example: R.id.toolbar</b>
     */
    public abstract int getToolBarResourceId();

    /**
     * Get fragment container id
     *
     * @return int resource id <b>Example: R.id.fragments</b>
     */
    public abstract int getFragmentContainerId();


    protected boolean setFullScreen() {
        return false;
    }

    /**
     * Start a transaction between fragment
     *
     * @param fragment        new fragment instance to show
     * @param transactionType type of transaction ADD or REPLACE
     */
    public void changeFragment(BaseFragment fragment, FragmentTransactionType transactionType, boolean closeKeyboard) {
        if (fragment instanceof FragmentStateListener)
            ((FragmentStateListener) fragment).beforeDeactivated();
        if (closeKeyboard)
            hideKeyword();
        if (getActiveFragment() != null) {
            switch (transactionType) {
                case ADD:
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                            .add(getFragmentContainerId(), fragment, fragment.getClass().getSimpleName())
                            .addToBackStack(getActiveFragment().getClass().getSimpleName())
                            .commit();
                    break;
                case REPLACE:
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                            .replace(getFragmentContainerId(), fragment, fragment.getClass().getSimpleName())
                            .commit();
                    break;
            }
        } else {
            switch (transactionType) {
                case ADD:
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                            .add(getFragmentContainerId(), fragment, fragment.getClass().getSimpleName())
                            .commit();
                    break;
                case REPLACE:
                    getSupportFragmentManager().beginTransaction()
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
     * @param fragment fragment to show
     */

    public void replaceFragment(BaseFragment fragment, boolean closeKeyboard) {
        if (fragment instanceof FragmentStateListener)
            ((FragmentStateListener) fragment).beforeDeactivated();
        if (closeKeyboard)
            hideKeyword();
        if (getActiveFragment() != null) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(getFragmentContainerId(), fragment, fragment.getClass().getName())
                    .addToBackStack(getActiveFragment().getClass().getName())
                    .commit();
        }
    }

    /**
     * Send local broadcast
     *
     * @param broadcast Intent broadcast with channel and extras
     */
    public void sendLocalBroadcast(Intent broadcast) {
        LocalBroadcastManager.getInstance(this)
                .sendBroadcast(broadcast);
    }

    public void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, OPEN_SETTINGS);
    }

    public boolean setToolbarAutoPadding() {
        return true;
    }

    public boolean setTranslucentStatus() {
        return true;
    }

    protected abstract int getAppBarTitleId();

    public BaseFragment getActiveFragment() {
        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(getFragmentContainerId());
        if (fragment != null)
            Logger.info(TAG, "Active fragment: " + fragment.getTag());
        return fragment;
    }

    /**
     * Show or hide empty view
     *
     * @param show     true if you want shipmentTo show the empty view, otherwise false
     * @param rootView View root view of this fragment instance
     */
    public void showEmpty(boolean show, View rootView) {
        if (rootView == null)
            return;
        ViewGroup group = (ViewGroup) rootView.findViewById(getEmptyViewGroupId());
        if (group == null)
            return;
        View emptyView = LayoutInflater.from(this).inflate(getEmptyViewResourceId(), null, false);
        if (emptyView == null)
            return;
        group.removeAllViews();
        group.addView(emptyView);
        emptyView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        if (show && (group.getVisibility() == View.GONE || group.getVisibility() == View.INVISIBLE)) {
            group.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
            group.setVisibility(View.VISIBLE);
        } else if (!show && group.getVisibility() == View.VISIBLE) {
            group.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
            group.setVisibility(View.GONE);
        }
    }

    /**
     * Get empty layout view resource id
     *
     * @return int resource id example <b>R.layout.empty_main</b>
     */
    public int getEmptyViewResourceId() {
        return 0;
    }

    /**
     * Get empty view group id shipmentTo inflate inside this parent view object
     *
     * @return int view group id example <b>R.id.empty_container</b>
     */
    public int getEmptyViewGroupId() {
        return 0;
    }
}

