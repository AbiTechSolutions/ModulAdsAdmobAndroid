package com.abi.tech.ads.sample;

import com.abi.tech.ads.admob.Admob;
import com.abi.tech.ads.admob.AppOpenManager;
import com.abi.tech.ads.ads.AbiAd;
import com.abi.tech.ads.application.AdsMultiDexApplication;
import com.abi.tech.ads.billing.AppPurchase;
import com.abi.tech.ads.config.AbiTechAdConfig;
import com.abi.tech.ads.config.AdjustConfig;
import com.abi.tech.ads.sample.BuildConfig;

import java.util.ArrayList;
import java.util.List;

public class App extends AdsMultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        initAds();
        initBilling();
    }

    private void initAds() {
        String environment = BuildConfig.DEBUG ? AbiTechAdConfig.ENVIRONMENT_DEVELOP : AbiTechAdConfig.ENVIRONMENT_PRODUCTION;
        mAbiTechAdConfig = new AbiTechAdConfig(this, environment);

        AdjustConfig adjustConfig = new AdjustConfig(true, getString(R.string.adjust_token));
        mAbiTechAdConfig.setAdjustConfig(adjustConfig);
        mAbiTechAdConfig.setFacebookClientToken(getString(R.string.facebook_client_token));
        mAbiTechAdConfig.setAdjustTokenTiktok(getString(R.string.tiktok_token));

        mAbiTechAdConfig.setIdAdResume("");

        AbiAd.getInstance().init(this, mAbiTechAdConfig);
        Admob.getInstance().setDisableAdResumeWhenClickAds(true);
        Admob.getInstance().setOpenActivityAfterShowInterAds(true);
        AppOpenManager.getInstance().disableAppResumeWithActivity(MainActivity.class);
    }

    private void initBilling() {
        List<String> listIAP = new ArrayList<>();
        listIAP.add("android.test.purchased");
        List<String> listSub = new ArrayList<>();
        AppPurchase.getInstance().initBilling(this, listIAP, listSub);
    }
}
