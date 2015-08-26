package com.pounpong.sleepingmarmot;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private final String TAG = "MainActivityFragment";

    private ToggleButton btnSwitchSleep;
    private boolean isSwitchSleepBtnClicked = false;

    private SettingsInfo sleepingSetting;
    private SettingsInfo wakingSetting;

    private AudioManager audioManager;
    private WifiManager wifiManager;

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

        if(sleepingSetting == null){
            sleepingSetting = new SettingsInfo();
            resetSleepingSettings();
        }
        if(wakingSetting == null){
            wakingSetting = new SettingsInfo();
            resetWakingSettings();
        }

        initialisation();
        return v;
    }
/*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_settings, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
               FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.add(R.id.fragmentContainer, new SettingsFragment());
                transaction.commit();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
*/
    private void resetSleepingSettings(){
        sleepingSetting.setAllSettingsInfo(AudioManager.RINGER_MODE_VIBRATE, false, false, 255);
    }
    private void resetWakingSettings(){
        wakingSetting.setAllSettingsInfo(AudioManager.RINGER_MODE_NORMAL, true, false, 255);
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
    }

    private void changeSwitchSleepStatus(){
        if (isSwitchSleepBtnClicked) {
            isSwitchSleepBtnClicked = false;
            btnSwitchSleep.setChecked(false);
            wakeUp();
        }
        else {
            isSwitchSleepBtnClicked = true;
            btnSwitchSleep.setChecked(true);
            sleep();
        }
    }

    private void sleep(){
        changeRingerMode(sleepingSetting);
        changeWifiMode(sleepingSetting);
        //TODO change brightness
    }

    private void wakeUp(){
        changeRingerMode(wakingSetting);
        changeWifiMode(wakingSetting);
        //TODO change brightness
    }

    private void changeRingerMode(SettingsInfo settings){
        try {
            if (audioManager != null) {
                int state = audioManager.getRingerMode();
                if (state != settings.getRinger_mode())
                    audioManager.setRingerMode(settings.getRinger_mode());
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private boolean changeWifiMode(SettingsInfo settings){
        boolean result = false;
        if(wifiManager != null){
            try {
                int current_state = wifiManager.getWifiState();
                boolean isUseWifi = settings.isWifi();
                if(!isUseWifi) {
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

    public SettingsInfo getSleepingSetting() {
        return sleepingSetting;
    }

    public void setSleepingSetting(SettingsInfo sleepingSetting) {
        this.sleepingSetting = sleepingSetting;
    }

    public SettingsInfo getWakingSetting() {
        return wakingSetting;
    }

    public void setWakingSetting(SettingsInfo wakingSetting) {
        this.wakingSetting = wakingSetting;
    }
}
