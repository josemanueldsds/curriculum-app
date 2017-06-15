package com.josemanueldsds.curriculum;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_profile:
                        mTextMessage.setText("Profile");
                        return true;
                    case R.id.navigation_works:
                        mTextMessage.setText("Works");
                        return true;
                    case R.id.navigation_apps:
                        mTextMessage.setText("Apps");
                        return true;
                    case R.id.navigation_studies:
                        mTextMessage.setText("Studies");
                        return true;
                }
                return false;
            }
        });
    }
}
