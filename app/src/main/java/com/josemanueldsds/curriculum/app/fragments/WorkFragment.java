package com.josemanueldsds.curriculum.app.fragments;

import android.os.Bundle;
import android.view.View;

import com.josemanueldsds.curriculum.R;
import com.josemanueldsds.curriculum.api.fragments.BaseFragment;

/**
 * Work Fragment
 *
 * @author Jose Mahuel De Sousa
 * @version 0.1.0
 */

public class WorkFragment extends BaseFragment {

    public static BaseFragment newInstance() {
        return new WorkFragment();
    }

    @Override
    protected int getFragmentLayoutResourceId() {
        return R.layout.fragment_work;
    }

    @Override
    protected void initViews(View rootView, Bundle savedInstanceState) {

    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }
}
