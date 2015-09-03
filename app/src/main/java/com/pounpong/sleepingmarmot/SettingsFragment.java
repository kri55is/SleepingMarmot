package com.pounpong.sleepingmarmot;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG= "SettingsFragment";

    // 0.RINGER_MODE_SILENT, 1.RINGER_MODE_VIBRATE, 2.RINGER_MODE_NORMAL
    private RadioGroup mRGSleepRingerMode;

    private int mSleepRingerMode = 1;
    private ListPreference mListPreference;
    private SharedPreferences.OnSharedPreferenceChangeListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.fragment_preference_settings);

  /*      if (mListener == null){
            mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    if (key.equals("applicationUpdates")) {
                        Preference pref = findPreference(key);

                    }
                }
            };
        }*/
    }
/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       mListPreference = (ListPreference) getPreferenceManager().findPreference("pref_s_ring_mode");
        if(mListPreference != null){
            mListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.d(TAG, "pref_key_sleeping_mode changed");
                    return false;
                }
            });
        }
        return inflater.inflate(R.layout.fragment_settings,container, false);
    }
*/
    /*
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //mQuoteView = (TextView) getActivity().findViewById(R.id.quoteView);
        mRGSleepRingerMode = (RadioGroup) getActivity().findViewById(R.id.sleep_mode_rgRingerMode);
        mRGSleepRingerMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                SettingsInfo newSettings = new SettingsInfo();
                switch (checkedId) {
                    case R.id.sleep_mode_rbRingSilence:
                        Log.d(TAG, "Sleep RingerMode checked Silence.");
                        mSleepRingerMode = 0;
                        newSettings.setRinger_mode(0);
                        break;
                    case R.id.sleep_mode_rbRingVibrate:
                        Log.d(TAG, "Sleep RingerMode checked Vibrate.");
                        mSleepRingerMode = 1;
                        newSettings.setRinger_mode(1);
                        break;
                    case R.id.sleep_mode_rbRingNormal:
                        Log.d(TAG, "Sleep RingerMode checked Normal.");
                        mSleepRingerMode = 2;
                        newSettings.setRinger_mode(2);
                        break;
                    default:
                        Log.e(TAG, "Error in Sleep Ringer Mode checked.");
                        newSettings.setRinger_mode(-1);
                        break;
                }
                //mCallback.onSettingsChanged(newSettings);
            }
        });
    }*/

    @Override
    public void onAttach(Activity activity) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onAttach()");
        super.onAttach(activity);

    }

    @Override
    public void onStart() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onResume()");
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onPause() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onPause()");
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStop()");
        super.onStop();
    }

    @Override
    public void onDetach() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDetach()");
        super.onDetach();
    }


    @Override
    public void onDestroy() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}