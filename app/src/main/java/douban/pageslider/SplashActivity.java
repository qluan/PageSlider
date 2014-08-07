package douban.pageslider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.qq.e.splash.SplashAd;
import com.qq.e.splash.SplashAdListener;

import net.youmi.android.AdManager;
import net.youmi.android.spot.SpotManager;

import cn.domob.android.ads.DomobRTSplashAd;
import cn.domob.android.ads.DomobRTSplashAdListener;
import cn.domob.android.ads.DomobSplashAd;
import cn.domob.android.ads.DomobSplashAdListener;


/**
 * Created by luanqian on 2014/8/2.
 */
public class SplashActivity extends Activity {

    private Handler mHandler;
    private long mStartTime;
    private static final int INTERVAL = 1000 * 3; // 3s

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (Constants.ENABLE_YouMi_AD) {
            AdManager.getInstance(this).init(Constants.YouMi_APPId, Constants.YouMi_SpotPosId, false);
            SpotManager.getInstance(this).loadSpotAds();
        }

        mHandler = new Handler(Looper.getMainLooper());
        mStartTime = System.currentTimeMillis();
        if (Constants.ENABLE_GDT_AD) {
            bindGDTSplashScreen();
        } else if (Constants.ENABLE_DuoMeng_AD) {
            bindDuoMengSplashScreen();
        } else {
            jump();
        }
    }

    private void bindGDTSplashScreen() {
        FrameLayout container = (FrameLayout) this
                .findViewById(R.id.splashcontainer);

        new SplashAd(this, container, Constants.GDT_APPId, Constants.GDT_SplashPosId,
                new SplashAdListener() {

                    @Override
                    public void onAdPresent() {
                        Log.i("test", "present");
                    }

                    @Override
                    public void onAdFailed(int arg0) {
                        Log.i("test", "fail" + arg0);
                        long endTime = System.currentTimeMillis();
                        if (endTime - mStartTime > INTERVAL) {
                            jump();
                        } else {
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    jump();
                                }
                            }, INTERVAL - endTime + mStartTime);
                        }
                    }

                    @Override
                    public void onAdDismissed() {
                        Log.i("test", "dismiss");
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        SplashActivity.this.finish();
                    }
                }
        );
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    DomobSplashAd splashAd;
    DomobRTSplashAd rtSplashAd;

    /**
     * DomobSplashMode.DomobSplashModeFullScreen 请求开屏广告的尺寸为全屏
     * DomobSplashMode.DomobSplashModeSmallEmbed 请求开屏广告的尺寸不是全屏，根据设备分辨率计算出合适的小屏尺寸
     * DomobSplashMode.DomobSplashModeBigEmbed 请求开屏广告的尺寸不是全屏，更具设备分辨率计算出合适的相对SmallMode的尺寸
     */
    private void bindDuoMengSplashScreen() {
        if (!Constants.ENABLE_REAL_TIME_SPLASH) {
            //缓存开屏广告
            //Cache splash ad
            splashAd = new DomobSplashAd(this, Constants.DuoMeng_APPId, Constants.DuoMeng_SplashId, DomobSplashAd.DomobSplashMode.DomobSplashModeFullScreen);
            //setSplashTopMargin is available when you choose non-full-screen splash mode.
            //splashAd.setSplashTopMargin(200);
            splashAd.setSplashAdListener(new DomobSplashAdListener() {
                @Override
                public void onSplashPresent() {
                    Log.i("DomobSDKDemo", "onSplashStart");
                }

                @Override
                public void onSplashDismiss() {
                    Log.i("DomobSDKDemo", "onSplashClosed");
                    //开屏回调被关闭时，立即进行界面跳转，从开屏界面到主界面。
                    //When splash ad is closed, jump to the next(main) Activity immediately.
                    jump();
                    //如果应用没有单独的闪屏Activity，需要调用closeSplash方法去关闭开屏广告
                    //If you do not carry a separate advertising activity, you need to call closeRTSplash way to close the splash ad

                    //splashAd.closeSplash();
                }

                @Override
                public void onSplashLoadFailed() {
                    Log.i("DomobSDKDemo", "onSplashLoadFailed");
                }
            });

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (splashAd.isSplashAdReady()) {
                        splashAd.splash(SplashActivity.this, SplashActivity.this.findViewById(R.id.splashcontainer));
                    } else {
                        Toast.makeText(SplashActivity.this, "Splash ad is NOT ready.", Toast.LENGTH_SHORT).show();
                        jump();
                    }
                }
            }, 1);
        } else {
            //实时开屏广告
            //Real-time splash ad
            rtSplashAd = new DomobRTSplashAd(this, Constants.DuoMeng_APPId, Constants.DuoMeng_SplashId, DomobSplashAd.DomobSplashMode.DomobSplashModeFullScreen);
            //setRTSplashTopMargin is available when you choose non-full-screen splash mode.
            //rtSplashAd.setRTSplashTopMargin(200);
            rtSplashAd.setRTSplashAdListener(new DomobRTSplashAdListener() {
                @Override
                public void onRTSplashDismiss() {
                    Log.i("DomobSDKDemo", "onRTSplashClosed");
                    //开屏回调被关闭时，立即进行界面跳转，从开屏界面到主界面。
                    //When rtSplash ad is closed, jump to the next(main) Activity immediately.
                    jump();
                    //如果应用没有单独的闪屏Activity，需要调用closeRTSplash方法去关闭开屏广告
                    //If you do not carry a separate advertising activity, you need to call closeRTSplash way to close the splash ad

                    //rtSplashAd.closeRTSplash();
                }

                @Override
                public void onRTSplashLoadFailed() {
                    Log.i("DomobSDKDemo", "onRTSplashLoadFailed");
                }

                @Override
                public void onRTSplashPresent() {
                    Log.i("DomobSDKDemo", "onRTSplashStart");
                }

            });

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    rtSplashAd.splash(SplashActivity.this, SplashActivity.this.findViewById(R.id.splashcontainer));
                }
            }, 1);
        }
    }

    private void jump() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        SplashActivity.this.finish();
    }
}
