# abi Studio
- Admob
- Mediation Admob (Facebook, Applovin, Vungle, Pangle, Mintegral)
- Adjust
- Firebase auto log tracking event, tROAS
# Import Module
~~~
    maven { url "https://jitpack.io" }
        maven {
            url 'https://artifact.bytedance.com/repository/pangle/'
        }
        maven {
            url 'https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea'
        }
        
        // Firebase
  implementation platform('com.google.firebase:firebase-bom:33.1.1')
  implementation 'com.google.firebase:firebase-analytics-ktx'
  implementation 'com.google.firebase:firebase-messaging-ktx'
  implementation 'com.google.firebase:firebase-crashlytics-ktx'
  implementation 'com.google.firebase:firebase-config-ktx'
// Ads
  implementation 'com.github.abi:abiAds:+'
  implementation 'com.github.abi:Module-Update-GDPR:+'
  implementation 'com.google.android.play:review:2.0.1'
  implementation 'com.google.android.play:review-ktx:2.0.1'
  implementation 'com.facebook.shimmer:shimmer:0.5.0'
  implementation 'com.google.android.gms:play-services-ads:23.2.0'
  implementation 'androidx.multidex:multidex:2.0.1'
  implementation "com.android.billingclient:billing:7.0.0" 

~~~
# Setup environment with id ads for project
~~~    
      productFlavors {
      debug {
                manifestPlaceholders = [ad_app_id: "ca-app-pub-3940256099942544~3347511713"]
                buildConfigField "String", "inter", "\"ca-app-pub-3940256099942544/1033173712\""
                buildConfigField "String", "banner", "\"ca-app-pub-3940256099942544/6300978111\""
                buildConfigField "String", "native", "\"ca-app-pub-3940256099942544/2247696110\""
                buildConfigField "String", "open_resume", "\"ca-app-pub-3940256099942544/3419835294\""
                buildConfigField "String", "RewardedAd", "\"ca-app-pub-3940256099942544/5224354917\""
                buildConfigField "Boolean", "build_debug", "true"
           }
       release {
            // ADS CONFIG BEGIN (required)
                manifestPlaceholders = [ad_app_id: "ca-app-pub-3940256099942544~3347511713"]
                buildConfigField "String", "inter", "\"ca-app-pub-3940256099942544/1033173712\""
                buildConfigField "String", "banner", "\"ca-app-pub-3940256099942544/6300978111\""
                buildConfigField "String", "native", "\"ca-app-pub-3940256099942544/2247696110\""
                buildConfigField "String", "open_resume", "\"ca-app-pub-3940256099942544/3419835294\""
                buildConfigField "String", "RewardedAd", "\"ca-app-pub-3940256099942544/5224354917\""
                buildConfigField "Boolean", "build_debug", "false"
            // ADS CONFIG END (required)
           }
      }
~~~
**AndroidManifest.xml**
~~~
        <meta-data
            android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="${ad_app_id}" />

        // Config SDK Facebook
        <meta-data
            android:name="com.facebook.sdk.ApplicationId"
            android:value="@string/facebook_app_id" />
        <meta-data
            android:name="com.facebook.sdk.ClientToken"
            android:value="@string/facebook_client_token" />

        <meta-data android:name="com.facebook.sdk.AutoInitEnabled"
            android:value="true"/>
        <meta-data android:name="com.facebook.sdk.AutoLogAppEventsEnabled"
            android:value="true"/>

        <meta-data android:name="com.facebook.sdk.AdvertiserIDCollectionEnabled"
            android:value="true"/>
~~~

# Create class Application
~~~
public class App extends AdsMultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        initAds();
    }

    private void initAds() {
        String environment = BuildConfig.DEBUG ? AdConfig.ENVIRONMENT_DEVELOP : AdConfig.ENVIRONMENT_PRODUCTION;
        mAbiTechAdConfig = new AdConfig(this, environment);

        AdjustConfig adjustConfig = new AdjustConfig(true,getString(R.string.adjust_token));
        mAbiTechAdConfig.setAdjustConfig(adjustConfig);
        mAbiTechAdConfig.setFacebookClientToken(getString(R.string.facebook_client_token));
        mAbiTechAdConfig.setAdjustTokenTiktok(getString(R.string.tiktok_token));

        mAbiTechAdConfig.setIdAdResume("");

        AbiAd.getInstance().init(this, mAbiTechAdConfig);
        Admob.getInstance().setDisableAdResumeWhenClickAds(true);
        Admob.getInstance().setOpenActivityAfterShowInterAds(true);
        AppOpenManager.getInstance().disableAppResumeWithActivity(MainActivity.class);
    }
}
~~~

# Ad Splash Interstitial
~~~   
AbiAd.getInstance().loadSplashInterstitialAds(this, BuildConfig.ad_interstitial_splash, 25000, 5000, new AdCallback() {
            @Override
            public void onNextAction() {
                super.onNextAction();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        });
~~~   

# Ad Banner
~~~   
AbiAd.getInstance().loadBanner(this, BuildConfig.ad_banner);
~~~   

# Ad Collapsible Banner
~~~   
AbiAd.getInstance().loadCollapsibleBanner(this, BuildConfig.ad_banner, AppConstant.CollapsibleGravity.BOTTOM, new AdCallback());
~~~   

# Native: Load And Show
~~~   
AbiAd.getInstance().loadNativeAd(this, BuildConfig.ad_native, R.layout.native_large, frAds, shimmerAds, new AdCallback() {
            @Override
            public void onAdFailedToLoad(@Nullable LoadAdError i) {
                super.onAdFailedToLoad(i);
                frAds.removeAllViews();
            }

            @Override
            public void onAdFailedToShow(@Nullable AdError adError) {
                super.onAdFailedToShow(adError);
                frAds.removeAllViews();
            }
        });
~~~   

# Native: Load
~~~   
private ApNativeAd mApNativeAd;
AbiAd.getInstance().loadNativeAdResultCallback(this, BuildConfig.ad_native, R.layout.native_large, new AdCallback() {
            @Override
            public void onNativeAdLoaded(@NonNull ApNativeAd nativeAd) {
                super.onNativeAdLoaded(nativeAd);

                mApNativeAd = nativeAd;
            }

            @Override
            public void onAdFailedToLoad(@Nullable LoadAdError i) {
                super.onAdFailedToLoad(i);

                mApNativeAd = null;
            }

            @Override
            public void onAdFailedToShow(@Nullable AdError adError) {
                super.onAdFailedToShow(adError);

                mApNativeAd = null;
            }
        });
~~~   

# Native: Show
~~~   
if (mApNativeAd != null) {
            AbiAd.getInstance().populateNativeAdView(this, mApNativeAd, frAds, shimmerAds);
        }
~~~
# Reward: Load
~~~
private RewardedAd rewardedAds;
AbiAd.getInstance().initRewardAds(this, BuildConfig.ad_reward, new AdCallback() {
                @Override
                public void onRewardAdLoaded(RewardedAd rewardedAd) {
                    super.onRewardAdLoaded(rewardedAd);
                    rewardedAds = rewardedAd;
                    Toast.makeText(MainActivity.this, "Ads Ready", Toast.LENGTH_SHORT).show();
                }
            });
~~~
# Reward: Show
~~~
private boolean isEarn = false;
btnShowReward.setOnClickListener(v -> {
            isEarn = false;
            AbiAd.getInstance().showRewardAds(MainActivity.this, rewardedAds, new RewardCallback() {
                @Override
                public void onUserEarnedReward(RewardItem var1) {
                    isEarn = true;
                }

                @Override
                public void onRewardedAdClosed() {
                    if (isEarn) {
                        // action intent
                    }
                }

                @Override
                public void onRewardedAdFailedToShow(int codeError) {

                }

                @Override
                public void onAdClicked() {

                }
            });
        });
~~~

# In-App Purchase
~~~
AppPurchase.getInstance().setPurchaseListener(new PurchaseListener() {
            @Override
            public void onProductPurchased(String productId, String transactionDetails) {
                Toast.makeText(MainActivity.this, "onProductPurchased", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void displayErrorMessage(String errorMsg) {
                Toast.makeText(MainActivity.this, "displayErrorMessage", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUserCancelBilling() {
                Toast.makeText(MainActivity.this, "onUserCancelBilling", Toast.LENGTH_SHORT).show();
            }
        });

btnIap.setOnClickListener(v -> AppPurchase.getInstance().purchase(MainActivity.this, "android.test.purchased"));
~~~#   A b i - M o d u l e - A d s  
 #   A b i T e c h A d s  
 #   A b i T e c h M o d u l A d s  
 #   A b i T e c h A d s  
 #   M o d u l A d s  
 #   M o d u l A d s  
 #   A b i M o d u l A d s A n d r o i d  
 #   A b i M o d u l A d s A n d r o i d  
 #   A b i M o d u l A d s A n d r o i d  
 #   A b i M o d u l A d s A n d r o i d  
 #   M o d u l A d s  
 #   M o d u l A n d r o i d A d s  
 #   M o d u l A d s A d m o b A n d r o i d  
 #   M o d u l A d s A d m o b A n d r o i d  
 