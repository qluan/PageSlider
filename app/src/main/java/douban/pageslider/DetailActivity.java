package douban.pageslider;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;

import douban.pageslider.model.Post;

/**
 * Created by luanqian on 2014/8/4.
 */
public class DetailActivity extends Activity{

    public static final String KEY_EXTRA_POST = "post";

    WebPageFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        SpotManager.getInstance(this).showSpotAds(this, new SpotDialogListener() {
            @Override
            public void onShowSuccess() {
                Log.i("Youmi", "onShowSuccess");
            }

            @Override
            public void onShowFailed() {
                Log.i("Youmi", "onShowFailed");
            }
        });
        setupView();
    }

    private void setupView() {
        mFragment = WebPageFragment.newInstance((Post)getIntent().getParcelableExtra(KEY_EXTRA_POST));
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, mFragment);
        ft.commitAllowingStateLoss();
    }
}
