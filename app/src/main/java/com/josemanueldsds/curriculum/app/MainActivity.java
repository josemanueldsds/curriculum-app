package com.josemanueldsds.curriculum.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.josemanueldsds.curriculum.R;
import com.josemanueldsds.curriculum.api.activities.BaseActivity;
import com.josemanueldsds.curriculum.api.enums.FragmentTransactionType;
import com.josemanueldsds.curriculum.app.fragments.AppFragment;
import com.josemanueldsds.curriculum.app.fragments.ProfileFragment;
import com.josemanueldsds.curriculum.app.fragments.StudiesFragment;
import com.josemanueldsds.curriculum.app.fragments.WorkFragment;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.inflateMenu(R.menu.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        changeFragment(ProfileFragment.newInstance(), FragmentTransactionType.REPLACE, true);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public int getToolBarResourceId() {
        return 0;
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.fragments;
    }

    @Override
    protected int getAppBarTitleId() {
        return 0;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_profile:
                if (!(getActiveFragment() instanceof ProfileFragment))
                    changeFragment(ProfileFragment.newInstance(), FragmentTransactionType.REPLACE, true);
                return true;
            case R.id.navigation_works:
                if (!(getActiveFragment() instanceof WorkFragment))
                    changeFragment(WorkFragment.newInstance(), FragmentTransactionType.REPLACE, true);
                return true;
            case R.id.navigation_apps:
                if (!(getActiveFragment() instanceof AppFragment))
                    changeFragment(AppFragment.newInstance(), FragmentTransactionType.REPLACE, true);
                return true;
            case R.id.navigation_studies:
                if (!(getActiveFragment() instanceof StudiesFragment))
                    changeFragment(StudiesFragment.newInstance(), FragmentTransactionType.REPLACE, true);
                return true;
        }
        return false;
    }
}
