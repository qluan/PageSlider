package douban.pageslider.manager;

import android.app.Activity;

import cn.domob.android.ads.DomobInterstitialAd;
import douban.pageslider.Constants;

/**
 * Created by luanqian on 2014/8/8.
 */
public class DuoMengAdManager {

    private static DuoMengAdManager sInstance;
    public DuoMengAdManager(){}
    public static DuoMengAdManager getInstance(){
        if (null == sInstance) {
            synchronized (DuoMengAdManager.class) {
                if (null == sInstance){
                    sInstance = new DuoMengAdManager();
                }
            }
        }
        return sInstance;
    }
}
