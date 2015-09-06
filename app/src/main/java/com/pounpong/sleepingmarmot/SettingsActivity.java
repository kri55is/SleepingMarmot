package com.pounpong.sleepingmarmot;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class SettingsActivity extends Activity {

    SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if(settingsFragment == null) {
            settingsFragment =  new SettingsFragment();
        }
        transaction.replace(R.id.settingsFragmentContainer, settingsFragment);
        transaction.commit();

    }
}
