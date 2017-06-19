package com.josemanueldsds.curriculum.app.fragments;

import android.os.Bundle;
import android.view.View;

import com.josemanueldsds.curriculum.R;
import com.josemanueldsds.curriculum.api.fragments.BaseFragment;

/**
 * App Fragment
 *
 * @author Jose Mahuel De Sousa
 * @version 0.1.0
 */

public class AppFragment extends BaseFragment {

    public static BaseFragment newInstance() {
        return new AppFragment();
    }

    @Override
    protected int getFragmentLayoutResourceId() {
        return R.layout.fragment_app;
    }

    @Override
    protected void initViews(View rootView, Bundle savedInstanceState) {

    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }
}
