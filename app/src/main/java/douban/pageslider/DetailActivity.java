package douban.pageslider;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;

import cn.domob.android.ads.DomobAdManager;
import cn.domob.android.ads.DomobInterstitialAd;
import cn.domob.android.ads.DomobInterstitialAdListener;
import douban.pageslider.activity.BaseActivity;
import douban.pageslider.model.Post;

/**
 * Created by luanqian on 2014/8/4.
 */
public class DetailActivity extends BaseActivity {

    public static final String KEY_EXTRA_POST = "post";

    WebPageFragment mFragment;

    DomobInterstitialAd mInterstitialAd;
    Handler mUIHandler;
    Handler mQuitHandler;

    static final int MESSAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mQuitHandler = new Handler(Looper.getMainLooper());
        if (Constants.ENABLE_DuoMeng_AD) {
            mUIHandler = new Handler(Looper.getMainLooper()){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    mUIHandler.removeCallbacksAndMessages(null);
                    mUIHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mInterstitialAd.isInterstitialAdReady()) {
                                mInterstitialAd.showInterstitialAd(DetailActivity.this);
                            } else {
                                Log.i("DomobSDKDemo", "Interstitial Ad is not ready");
                                mInterstitialAd.loadInterstitialAd();
                            }
                        }
                    }, 2000);
                }
            };
        }

        setupView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mQuitHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DetailActivity.this.finish();
            }
        }, 8000);

        if (Constants.ENABLE_YouMi_AD) {
            MobclickAgent.onEvent(this, "[youmi][detailpage]oncreate");
            SpotManager.getInstance(this).showSpotAds(this, new SpotDialogListener() {
                @Override
                public void onShowSuccess() {
                    Log.i("Youmi", "onShowSuccess");
                    MobclickAgent.onEvent(DetailActivity.this, "[youmi][spotAds] onSuccessShow");
                }

                @Override
                public void onShowFailed() {
                    Log.i("Youmi", "onShowFailed");
                    MobclickAgent.onEvent(DetailActivity.this, "[youmi][spotAds] onFailShow");
                    DetailActivity.this.finish();
                }
            });
        } else if (Constants.ENABLE_DuoMeng_AD) {
            mInterstitialAd = new DomobInterstitialAd(this, Constants.DuoMeng_APPId,
                    Constants.DuoMeng_SpotId, DomobInterstitialAd.INTERSITIAL_SIZE_300X250);

            mInterstitialAd.setInterstitialAdListener(new DomobInterstitialAdListener() {
                @Override
                public void onInterstitialAdReady() {
                    Log.i("DomobSDKDemo", "onAdReady");
                    mInterstitialAd.showInterstitialAd(DetailActivity.this);
                }

                @Override
                public void onLandingPageOpen() {
                    Log.i("DomobSDKDemo", "onLandingPageOpen");
                }

                @Override
                public void onLandingPageClose() {
                    Log.i("DomobSDKDemo", "onLandingPageClose");
                    DetailActivity.this.finish();
                }

                @Override
                public void onInterstitialAdPresent() {
                    Log.i("DomobSDKDemo", "onInterstitialAdPresent");
                }

                @Override
                public void onInterstitialAdDismiss() {
                    // Request new ad when the previous interstitial ad was closed.
                    mInterstitialAd.loadInterstitialAd();
                    Log.i("DomobSDKDemo", "onInterstitialAdDismiss");
                    DetailActivity.this.finish();
                }

                @Override
                public void onInterstitialAdFailed(DomobAdManager.ErrorCode arg0) {
                    Log.i("DomobSDKDemo", "onInterstitialAdFailed");
                    DetailActivity.this.finish();
                }

                @Override
                public void onInterstitialAdLeaveApplication() {
                    Log.i("DomobSDKDemo", "onInterstitialAdLeaveApplication");
                    DetailActivity.this.finish();
                }

                @Override
                public void onInterstitialAdClicked(DomobInterstitialAd arg0) {
                    Log.i("DomobSDKDemo", "onInterstitialAdClicked");
                }
            });

            mInterstitialAd.loadInterstitialAd();

            mUIHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mInterstitialAd.isInterstitialAdReady()) {
                        mInterstitialAd.showInterstitialAd(DetailActivity.this);
                    } else {
                        Log.i("DomobSDKDemo", "Interstitial Ad is not ready");
                        mInterstitialAd.loadInterstitialAd();
                    }
                }
            }, 2000);
        }
    }

    private void setupView() {
        mFragment = WebPageFragment.newInstance((Post) getIntent().getParcelableExtra(KEY_EXTRA_POST));
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, mFragment);
        ft.commitAllowingStateLoss();
    }
}
