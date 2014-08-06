package douban.pageslider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import com.qq.e.splash.SplashAd;
import com.qq.e.splash.SplashAdListener;


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
        mHandler = new Handler(Looper.getMainLooper());
        mStartTime = System.currentTimeMillis();
        bindSplashScreen();
    }

    private void bindSplashScreen() {
        FrameLayout container = (FrameLayout) this
                .findViewById(R.id.splashcontainer);

        new SplashAd(this, container, Constants.APPId, Constants.SplashPosId,
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
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            SplashActivity.this.finish();
                        } else {
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(0, 0);
                                    SplashActivity.this.finish();
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
}
