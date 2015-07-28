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
            int muteState = audioManager.getRingerMode();
            int wifiState = wifiManager.getWifiState();
            if(((wifiState != wifiManager.WIFI_STATE_ENABLED) || (wifiState != wifiManager.WIFI_STATE_ENABLING)) && (muteState == audioManager.RINGER_MODE_VIBRATE)) {
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
        stopCellularData();
    }

    private void wakeUp(){
        unMute();
        startWifi();
        startCellularData();
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

    private void startCellularData(){
        //setMobileDataEnabled(true);
        //setMobileDataState(true);
    }

    private void stopCellularData(){
        //setMobileDataEnabled(false);
        //setMobileDataState(false);
    }
/*
    public void setMobileDataState(boolean mobileDataEnabled)
    {
        try
        {
            Context context = getActivity();
            TelephonyManager telephonyService = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            Method setMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("setDataEnabled", boolean.class);

            if (null != setMobileDataEnabledMethod)
            {
                setMobileDataEnabledMethod.invoke(telephonyService, mobileDataEnabled);
            }
        }
        catch (Exception ex)
        {
            Log.d(TAG, "Error setting mobile data state", ex);
        }
    }*/
/*
    private void setMobileDataEnabled(boolean enabled) {

        Context context = getActivity();
        final ConnectivityManager conman =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            final Class conmanClass = Class.forName(conman.getClass().getName());
            final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            final Object iConnectivityManager = iConnectivityManagerField.get(conman);
            final Class iConnectivityManagerClass = Class.forName(
                    iConnectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = iConnectivityManagerClass
                    .getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);

            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
*/
}
