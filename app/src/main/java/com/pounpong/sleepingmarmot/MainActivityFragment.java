package com.pounpong.sleepingmarmot;

import android.app.Fragment;
import android.content.Context;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private final String TAG = "MainActivityFragment";

    ToggleButton btnSwitchSleep;
    boolean isSwitchSleepBtnClicked = false;

    AudioManager audioManager;
    WifiManager wifiManager;

    public MainActivityFragment() {
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

        initialisation();

        return v;
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
        mute();
        stopWifi();
    }

    private void wakeUp(){
        unMute();
        startWifi();
    }

    private void mute(){
        try {
            if (audioManager != null) {
                int state = audioManager.getRingerMode();
                if (state != audioManager.RINGER_MODE_VIBRATE)
                    audioManager.setRingerMode(audioManager.RINGER_MODE_VIBRATE);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private void unMute(){
        try {
            if (audioManager != null) {
                int state = audioManager.getRingerMode();
                if (state != audioManager.RINGER_MODE_NORMAL)
                    audioManager.setRingerMode(audioManager.RINGER_MODE_NORMAL);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private boolean stopWifi(){
        boolean result = false;
        if(wifiManager != null){
            try {
                int state = wifiManager.getWifiState();
                if ((state != wifiManager.WIFI_STATE_DISABLED) && (state != wifiManager.WIFI_STATE_DISABLING))
                    result = wifiManager.setWifiEnabled(false);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    private boolean startWifi(){
        boolean result = false;
        try {
            int state = wifiManager.getWifiState();
            if((state != wifiManager.WIFI_STATE_ENABLED) && (state != wifiManager.WIFI_STATE_ENABLING))
                result = wifiManager.setWifiEnabled(true);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return result;

    }
}
