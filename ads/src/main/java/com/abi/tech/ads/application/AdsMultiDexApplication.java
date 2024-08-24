package com.abi.tech.ads.application;

import androidx.multidex.MultiDexApplication;


import com.abi.tech.ads.config.AbiTechAdConfig;
import com.abi.tech.ads.util.AppUtil;
import com.abi.tech.ads.util.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class AdsMultiDexApplication extends MultiDexApplication {

    protected AbiTechAdConfig mAbiTechAdConfig;
    protected List<String> listTestDevice;

    @Override
    public void onCreate() {
        super.onCreate();
        listTestDevice = new ArrayList<String>();
        mAbiTechAdConfig = new AbiTechAdConfig(this);
        if (SharePreferenceUtils.getInstallTime(this) == 0) {
            SharePreferenceUtils.setInstallTime(this);
        }
        AppUtil.currentTotalRevenue001Ad = SharePreferenceUtils.getCurrentTotalRevenue001Ad(this);
    }


}
