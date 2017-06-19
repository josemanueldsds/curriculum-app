package com.josemanueldsds.curriculum.app.fragments;

import android.os.Bundle;
import android.view.View;

import com.josemanueldsds.curriculum.R;
import com.josemanueldsds.curriculum.api.fragments.BaseFragment;

/**
 * Profile Fragment
 *
 * @author Jose Mahuel De Sousa
 * @version 0.1.0
 */

public class ProfileFragment extends BaseFragment {

    public static BaseFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    protected int getFragmentLayoutResourceId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void initViews(View rootView, Bundle savedInstanceState) {

    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }
}
