package douban.pageslider;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import java.lang.ref.WeakReference;
import butterknife.ButterKnife;
import butterknife.InjectView;
import douban.pageslider.controller.PageSwitchController;
import douban.pageslider.model.Post;

/**
 * Created by luanqian on 2014/7/24.
 */
public class ArticleContainer extends ViewFlipper implements PullToRefreshBase.OnRefreshListener{

    static final boolean DEBUG = BuildConfig.DEBUG;
    static final String TAG = ArticleContainer.class.getSimpleName();

    @InjectView(R.id.current_article)
    ArticleItem mCurrentArticle;
    @InjectView(R.id.next_article)
    ArticleItem mNextArticle;

    private Post mCurrentPost;

    private boolean mIsCurrentArticleFront = true;
    private WeakReference<Fragment> mFragment = new WeakReference<Fragment>(null);

    private boolean mEnablePageSwitch = true; //for default

    public ArticleContainer(Context context) {
        super(context);
    }

    public ArticleContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setupViews();
    }

    private void setupViews(){
        ButterKnife.inject(this, this);
        setInAnimation(getContext(), R.anim.slide_in_from_bottom);
        Animation outAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_to_top);
        outAnimation.setAnimationListener(mOutAnimationListener);
        setOutAnimation(outAnimation);
    }

    /**
     * Called to init the article container
     *
     * @param post
     * @param fragment
     */
    public void startLoad(Post post, Fragment fragment){
        if (null == post) {
            throw new IllegalArgumentException("post can not be null");
        }
        mCurrentPost = post;

        mFragment = new WeakReference<Fragment>(fragment);

        mCurrentArticle.setAttachedFragment(fragment);
        mCurrentArticle.setOnRefreshListener(this);

        mNextArticle.setAttachedFragment(fragment);
        mNextArticle.setOnRefreshListener(this);

        mCurrentArticle.setPost(mCurrentPost);
        mCurrentArticle.setupNextPage();

        // show view
        mCurrentArticle.startLoad(fragment);

        if (PageSwitchController.getInstance().hasNextItem()) {
            Post nextPost = (Post) PageSwitchController.getInstance().getNextPost();
            mNextArticle.setPost(nextPost);
            mNextArticle.startPreLoad();
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase pullToRefreshBase) {
        if (PageSwitchController.getInstance().hasNextItem()) {
            showNextPage();
        } else {
            pullToRefreshBase.onRefreshComplete();
        }
    }

    /**
     * Used to enable the pageSwitch
     *
     * True is default
     */
    public void enablePageSwitch(){
        mEnablePageSwitch = true;
        mCurrentArticle.enablePullToRefresh();
        mNextArticle.enablePullToRefresh();
    }

    /**
     * Used to disable the pageSwitch
     */
    public void disablePageSwitch(){
        mEnablePageSwitch = false;
        mCurrentArticle.disablePullToRefresh();
        mNextArticle.disablePullToRefresh();
    }

    private void showNextPage(){
        PageSwitchController.getInstance().stepNext();
        mCurrentPost = (Post) PageSwitchController.getInstance().getCurrentPost();

        if (mIsCurrentArticleFront) {
            mNextArticle.setPost(mCurrentPost);
            mNextArticle.setupNextPage();
            mIsCurrentArticleFront = false;
            showNext();
        } else {
            mCurrentArticle.setPost(mCurrentPost);
            mCurrentArticle.setupNextPage();
            mIsCurrentArticleFront = true;
            showPrevious();
        }
    }

    private Animation.AnimationListener mOutAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            // do nothing
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (mIsCurrentArticleFront) {
                mNextArticle.onBackToStack();
                mCurrentArticle.startLoad(mFragment.get());
            } else {
                mCurrentArticle.onBackToStack();
                mNextArticle.startLoad(mFragment.get());
            }

            // used to pre-load data
            post(new Runnable() {
                @Override
                public void run() {
                    if (PageSwitchController.getInstance().hasNextItem()) {
                        Post nextPost = (Post) PageSwitchController.getInstance().getNextPost();
                        if (mIsCurrentArticleFront) {
                            mNextArticle.setPost(nextPost);
                            mNextArticle.startPreLoad();
                        } else {
                            mCurrentArticle.setPost(nextPost);
                            mCurrentArticle.startPreLoad();
                        }
                    }
                }
            });

            notifyPageSwitch();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // do nothing
        }
    };

    public void onResume(){
        mCurrentArticle.onResume();
        mNextArticle.onResume();
    }

    public void onPause(){
        mCurrentArticle.onPause();
        mNextArticle.onPause();
    }

    public void onDestroy(){
        mCurrentArticle.onDestroy();
        mNextArticle.onDestroy();
    }

    /**
     * Page change listener
     */
    public interface PageSwitchListener {
        public void onPageSwitch();
    }

    private WeakReference<PageSwitchListener> mPageSwitchListener = new WeakReference<PageSwitchListener>(null);

    public void setPageSwitchListener(PageSwitchListener listener){
        if (null != listener) {
            mPageSwitchListener = new WeakReference<PageSwitchListener>(listener);
        }
    }

    /**
     * Called to notify that the page has been changed
     */
    private void notifyPageSwitch(){
        if (null != mPageSwitchListener) {
            PageSwitchListener listener = mPageSwitchListener.get();
            if (null != listener) {
                listener.onPageSwitch();
            }
        }
    }
}
