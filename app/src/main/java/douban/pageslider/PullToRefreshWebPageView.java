/*******************************************************************************
 * modified version of PullToRefreshWebView
 *******************************************************************************/
package douban.pageslider;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.OverscrollHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import douban.pageslider.model.Post;

public class PullToRefreshWebPageView extends PullToRefreshBase<WebPageView> {

    private boolean mDoScroll = true; // for default

    private static final OnRefreshListener<WebView> DEFAULT_ON_REFRESH_LISTENER = new OnRefreshListener<WebView>() {

        @Override
        public void onRefresh(PullToRefreshBase<WebView> refreshView) {
            refreshView.getRefreshableView().reload();
        }

    };

    public PullToRefreshWebPageView(Context context) {
        super(context);
        setUp(context);
    }

    public PullToRefreshWebPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp(context);
    }

    public PullToRefreshWebPageView(Context context, Mode mode) {
        super(context, mode);
        setUp(context);
    }

    public PullToRefreshWebPageView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
        setUp(context);
    }

    @Override
    protected void onRefreshing(boolean doScroll) {
        super.onRefreshing(mDoScroll);
    }

    public void setDoScroll(boolean doScroll) {
        mDoScroll = doScroll;
    }

    private void setUp(Context context) {
        setMode(Mode.PULL_FROM_END);
    }

    public void setFooterLayout(Post post){
        ILoadingLayout footerLayout = getFooterLayout();
        String title = "";
        if (null == post) {
            title = getResources().getString(R.string.page_nav_no_next_page_title);
            // if the sub-title is null or length == 0, the text will not be set
            footerLayout.setLastUpdatedLabel(" ");
        } else {
            title = getResources().getString(R.string.page_nav_next_page_title_format, post.mTitle);
            footerLayout.setLastUpdatedLabel(getResources().getString(R.string.page_nav_pull_to_next_page));
        }
        footerLayout.setPullLabel(title);
        footerLayout.setRefreshingLabel(title);
        footerLayout.setReleaseLabel(title);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // update style of loading layout
        int pullTitleId = getResources().getIdentifier("pull_to_refresh_text", "id", getContext().getPackageName());
        TextView titleView = (TextView) findViewById(pullTitleId);
        titleView.setEllipsize(TextUtils.TruncateAt.END);
        LinearLayout linearLayout = (LinearLayout) titleView.getParent();
        int paddingHorizontal = getResources().getDimensionPixelOffset(R.dimen.dimen_45);
        linearLayout.setPadding(paddingHorizontal, 0, paddingHorizontal, 0);
    }

    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected WebPageView createRefreshableView(Context context, AttributeSet attrs) {
        WebPageView webView = new InternalWebView(context, attrs);
        webView.setId(com.handmark.pulltorefresh.library.R.id.webview);
        return webView;
    }

    @Override
    protected boolean isReadyForPullStart() {
        return getRefreshableView().getScrollY() == 0;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        float exactContentHeight = FloatMath.floor(getRefreshableView().getContentHeight() * getRefreshableView().getScale());
        return getRefreshableView().getScrollY() >= (exactContentHeight - getRefreshableView().getHeight());
    }

    @Override
    protected void onPtrRestoreInstanceState(Bundle savedInstanceState) {
        super.onPtrRestoreInstanceState(savedInstanceState);
        getRefreshableView().restoreState(savedInstanceState);
    }

    @Override
    protected void onPtrSaveInstanceState(Bundle saveState) {
        super.onPtrSaveInstanceState(saveState);
        getRefreshableView().saveState(saveState);
    }

    final class InternalWebView extends WebPageView {

        // WebView doesn't always scroll back to it's edge so we add some
        // fuzziness
        static final int OVER_SCROLL_FUZZY_THRESHOLD = 2;

        // WebView seems quite reluctant to overScroll so we use the scale
        // factor to scale it's value
        static final float OVER_SCROLL_SCALE_FACTOR = 1.5f;

        public InternalWebView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
                                       int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

            final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                    scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

            // Does all of the hard work...
            OverscrollHelper.overScrollBy(PullToRefreshWebPageView.this, deltaX, scrollX, deltaY, scrollY,
                    getScrollRange(), OVER_SCROLL_FUZZY_THRESHOLD, OVER_SCROLL_SCALE_FACTOR, isTouchEvent);

            return returnValue;
        }

        private int getScrollRange() {
            return (int) Math.max(0, FloatMath.floor(getRawContentHeight()) - getRawHeight());
        }
    }
}
