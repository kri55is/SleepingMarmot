package com.pounpong.sleepingmarmot;

import android.util.Log;

/**
 * Created by Emilie on 18/08/2015.
 */
public class SettingsInfo {

    private final static String TAG = "SettingsInfo";
    private int ringer_mode; // 0.RINGER_MODE_SILENT, 1.RINGER_MODE_VIBRATE, 2.RINGER_MODE_NORMAL
    private boolean wifi;   // 0.WIFI_STATE_DISABLING, 1.WIFI_STATE_DISABLED, 2.WIFI_STATE_ENABLING, 3.WIFI_STATE_ENABLED, 4.WIFI_STATE_UNKNOWN
    private boolean changeBrightness;
    private int brightness; // has to be between 0 and 255

    void SettingInfo() {
        ringer_mode = -1;
        wifi = false;
        changeBrightness = false;
        int brightness = 255;
    }

    public int getRinger_mode() {
        return ringer_mode;
    }

    public void setRinger_mode(int ringer_mode) {
        if((ringer_mode >= -1) && (ringer_mode <= 2)){
            this.ringer_mode = ringer_mode;
        }
        else {
            Log.e(TAG, "Ringer mode set does not exist.");
        }
    }

    public boolean getWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean getChangeBrightness() {
        return changeBrightness;
    }

    public void setChangeBrightness(boolean changeBrightness) {
        this.changeBrightness = changeBrightness;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        if((brightness >= 0)  && (brightness <= 255)) {
            this.brightness = brightness;
        }
        else {
            Log.e(TAG, "Brightness set out of boundaries.");
        }
    }

    public  void setAllSettingsInfo(int ringer_mode, boolean wifi, boolean changeBrightness, int brightness) {
        this.ringer_mode = ringer_mode;
        this.wifi = wifi;
        this.changeBrightness = changeBrightness;
        this.brightness = brightness;
    }

    public void print() {
        Log.d(TAG, "Ringer Mode :" + this.ringer_mode);
        Log.d(TAG, "Wifi :" + this.wifi);
        Log.d(TAG, "Change Brightness :" + this.changeBrightness);
        Log.d(TAG, "Brightness :" + this.brightness);
    }
}
;
