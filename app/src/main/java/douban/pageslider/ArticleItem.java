package douban.pageslider;

import android.app.Fragment;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.InjectView;
import douban.pageslider.controller.PageSwitchController;
import douban.pageslider.model.Post;

/**
 * Used to show detail page info
 * <p/>
 * Created by luanqian on 2014/7/24.
 */
public class ArticleItem extends FrameLayout {
    public static final boolean DEBUG = BuildConfig.DEBUG;
    public static final String TAG = ArticleItem.class.getSimpleName();

    @InjectView(R.id.root)
    ViewGroup mRoot;
    @InjectView(R.id.webview)
    PullToRefreshWebPageView mPullToRefreshWebPageView;
    WebPageView mWebView;

    private WeakReference<Fragment> mAttachedFragment = new WeakReference<Fragment>(null);

    private Post mPost;

    private boolean mIsPreloaded = false;

    public ArticleItem(Context context) {
        super(context);
    }

    public ArticleItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArticleItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setupViews();
    }

    private void setupViews() {
        ButterKnife.inject(this, this);
        mWebView = mPullToRefreshWebPageView.getRefreshableView();
    }

    /**
     * Called before the item is showing
     *
     * @param fragment
     */
    public void setAttachedFragment(Fragment fragment) {
        if (null != fragment) {
            mAttachedFragment = new WeakReference<Fragment>(fragment);
        }
    }

    /**
     * Called before the item is showing
     *
     * @param post
     */
    public void setPost(Post post) {
        mPost = post;
    }

    public void enablePullToRefresh() {
        mPullToRefreshWebPageView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
    }

    public void disablePullToRefresh() {
        mPullToRefreshWebPageView.setMode(PullToRefreshBase.Mode.DISABLED);
    }

    /**
     * Called when the item is showing
     */
    public void startLoad(Fragment fragment) {
        setVisibility(View.VISIBLE);
        if (!mIsPreloaded) {
            showWebPage();
        }
    }

    /**
     * Used to pre-load data
     */
    public void startPreLoad() {
        if (mIsPreloaded) {
            return;
        }
        showWebPage();
    }

    /**
     * Called before the item is showing
     */
    public void setupNextPage() {
        if (PageSwitchController.getInstance().hasNextItem()) {
            mPullToRefreshWebPageView.setFooterLayout((Post) PageSwitchController.getInstance().getNextPost());
            mPullToRefreshWebPageView.setDoScroll(false);
        } else {
            mPullToRefreshWebPageView.setFooterLayout(null);
            mPullToRefreshWebPageView.setDoScroll(true);
        }
    }

    /**
     * Called when the item is created
     *
     * @param listener
     */
    public void setOnRefreshListener(PullToRefreshBase.OnRefreshListener listener) {
        mPullToRefreshWebPageView.setOnRefreshListener(listener);
    }

    public void destroyWebView() {
        mRoot.removeView(mWebView);
        mWebView.destroy();
        mWebView = null;
    }

    public void onResume() {
        if (null != mWebView) {
            mWebView.onResume();
        }
    }

    public void onPause() {
        if (null != mWebView) {
            mWebView.onPause();
        }
    }

    /**
     * Called when this view is back to background to reset some state
     */
    public void onBackToStack() {
        mPullToRefreshWebPageView.onRefreshComplete();
        mPullToRefreshWebPageView.setDoScroll(true);
        mPullToRefreshWebPageView.setFooterLayout(null);

        setVisibility(View.GONE);

        mIsPreloaded = false;
    }

    public void onDestroy() {
        destroyWebView();
    }

    private Fragment getAttachedFragment() {
        if (null != mAttachedFragment) {
            return mAttachedFragment.get();
        }
        return null;
    }

    private void showWebPage() {
        if (mWebView != null) {
            mWebView.loadUrl(mPost.mOriginUrl);
        }
    }
}
