package douban.pageslider;

import android.os.Bundle;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;

import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;

import douban.pageslider.activity.BaseActivity;
import douban.pageslider.model.Post;

/**
 * Created by luanqian on 2014/8/4.
 */
public class DetailActivity extends BaseActivity{

    public static final String KEY_EXTRA_POST = "post";

    WebPageFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
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
                }
            });
        }
        setupView();
    }

    private void setupView() {
        mFragment = WebPageFragment.newInstance((Post)getIntent().getParcelableExtra(KEY_EXTRA_POST));
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, mFragment);
        ft.commitAllowingStateLoss();
    }
}
