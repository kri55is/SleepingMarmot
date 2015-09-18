package com.pounpong.sleepingmarmot;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private final String TAG = "MainActivityFragment";

    private ToggleButton btnSwitchSleep;
    private boolean isSwitchSleepBtnClicked = false;

    private AudioManager audioManager;
    private WifiManager wifiManager;

    private RelativeLayout layout;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        //Remove title bar
        btnSwitchSleep = (ToggleButton) v.findViewById(R.id.btnSwitchSleep);
        btnSwitchSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSwitchSleepStatus();

            }
        });

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        if(audioManager == null){
            Log.d(TAG, "No audioManager found");
        }
        wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        if(wifiManager == null){
            Log.d(TAG, "No wifiManager found");
        }

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i(TAG, getClass().getSimpleName() + ":entered onResume()");
        initialisation();
    }

    private void initialisation() {
        //if we are totally sleeping, the button is checked and proposes to wake up
        if(audioManager != null) {
            // 0.RINGER_MODE_SILENT, 1.RINGER_MODE_VIBRATE, 2.RINGER_MODE_NORMAL
            int muteState = audioManager.getRingerMode();
            // 0.WIFI_STATE_DISABLING, 1.WIFI_STATE_DISABLED, 2.WIFI_STATE_ENABLING, 3.WIFI_STATE_ENABLED, 4.WIFI_STATE_UNKNOWN
            int wifiState = wifiManager.getWifiState();
            if(((wifiState == wifiManager.WIFI_STATE_DISABLING) || (wifiState == wifiManager.WIFI_STATE_DISABLED)) && (muteState == audioManager.RINGER_MODE_VIBRATE)) {
                btnSwitchSleep.setChecked(true);
                isSwitchSleepBtnClicked = true;
            }
        }

        updateBackgroundImage();
    }

    private void updateBackgroundImage(){
        //Determine screen orientation
        int orientation = getResources().getConfiguration().orientation;
        boolean landscape = false;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            landscape = true;
        }

        layout = (RelativeLayout) getActivity().findViewById(R.id.layout);
        if(layout != null) {
            //Check state we are on :Sleeping or Awake
            if (isSwitchSleepBtnClicked) {
                if(!landscape)
                    layout.setBackgroundResource(R.drawable.backgroundsleepingmarmot);
                else
                    layout.setBackgroundResource(R.drawable.backgroundsleepingmarmotland);
            }
            else{
                if(!landscape)
                    layout.setBackgroundResource(R.drawable.backgroundawakemarmot);
                else
                    layout.setBackgroundResource(R.drawable.backgroundawakemarmotland);
            }
        }
        else{
            Log.e(TAG, "Error, no view");
        }

    }

    private void changeSwitchSleepStatus(){

        /* Find out the new status and put it in settings var*/
        SettingsInfo settings = new SettingsInfo();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        Preference preference;
        String PrefRingerModeValue;
        Boolean pref_wifi;

        /*Read from the saved preferences to know the new desired status*/
        if (isSwitchSleepBtnClicked) {
            isSwitchSleepBtnClicked = false;
            btnSwitchSleep.setChecked(false);

            PrefRingerModeValue = preferences.getString("pref_a_ring_mode", "Normal");
            pref_wifi = preferences.getBoolean("pref_a_wifi", true);
        }
        else {
            isSwitchSleepBtnClicked = true;
            btnSwitchSleep.setChecked(true);

            PrefRingerModeValue = preferences.getString("pref_s_ring_mode", "Vibrate");
            pref_wifi = preferences.getBoolean("pref_s_wifi", false);

        }
        settings.setWifi(pref_wifi);

        /* Translate ringerMode from String to int*/
        Integer pref_ringerMode = 1;
        if(PrefRingerModeValue.equals("Silence")) {
            pref_ringerMode = 0;
        }
        else{
            if (PrefRingerModeValue.equals("Vibrate"))
                pref_ringerMode = 1;
            else {
                if (PrefRingerModeValue.equals("Normal"))
                    pref_ringerMode = 2;
            }
        }

        settings.setRinger_mode(pref_ringerMode);

        /* apply the new desired settings */
        setChanges(settings);

        /* Update the background depending on the state and the phone orientation */
        updateBackgroundImage();
    }

    private void setChanges(SettingsInfo settings){
        try {
            changeRingerMode(settings.getRinger_mode());
            changeWifiMode(settings.getWifi());
            //TODO change brightness
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    private void changeRingerMode(Integer ringerMode){
        try {
            if (audioManager != null) {
                int state = audioManager.getRingerMode();
                if (state != ringerMode)
                    audioManager.setRingerMode(ringerMode);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private boolean changeWifiMode(Boolean wifi){
        boolean result = false;
        if(wifiManager != null){
            try {
                int current_state = wifiManager.getWifiState();
                if(!wifi) {
                    if ((current_state != wifiManager.WIFI_STATE_DISABLED) && (current_state != wifiManager.WIFI_STATE_DISABLING))
                        result = wifiManager.setWifiEnabled(false);
                }
                else{
                    if ((current_state != wifiManager.WIFI_STATE_ENABLED) && (current_state != wifiManager.WIFI_STATE_ENABLING))
                        result = wifiManager.setWifiEnabled(true);
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
