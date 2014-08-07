package douban.pageslider;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import douban.pageslider.controller.PageSwitchController;
import douban.pageslider.model.Post;

public class WebPageFragment extends Fragment {

    public static final String KEY_EXTRA_POST = "post";

    @InjectView(R.id.article_container)
    ArticleContainer mArticleContainer;

    private Post mPost;

    public static WebPageFragment newInstance(Post post) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_EXTRA_POST, post);
        WebPageFragment fragment = new WebPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mPost = args.getParcelable(KEY_EXTRA_POST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_articlecontainer, container, false);
        ButterKnife.inject(this, view);
        if (getActivity() instanceof ArticleContainer.PageSwitchListener) {
            setPageSwitchListener((ArticleContainer.PageSwitchListener) getActivity());
        }
        if (PageSwitchController.getInstance().isEmpty()){
            mArticleContainer.disablePageSwitch();
        } else {
            mArticleContainer.enablePageSwitch();
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startLoad();
    }

    public void startLoad() {
        mArticleContainer.startLoad(mPost, this);
    }

    public void setPageSwitchListener(ArticleContainer.PageSwitchListener listener){
        mArticleContainer.setPageSwitchListener(listener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mArticleContainer.onResume();
    }

    @Override
    public void onPause() {
        mArticleContainer.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mArticleContainer.onDestroy();
        super.onDestroy();
    }
}
