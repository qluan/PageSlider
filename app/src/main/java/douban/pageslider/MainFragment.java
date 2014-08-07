package douban.pageslider;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import butterknife.ButterKnife;
import butterknife.InjectView;
import douban.pageslider.controller.PageSwitchController;
import douban.pageslider.datasource.NoMoreDataSource;
import douban.pageslider.model.Post;
import douban.pageslider.model.Stream;

/**
 * Created by luanqian on 2014/8/4.
 */
public class MainFragment extends Fragment {

    @InjectView(R.id.list)
    ListView mListView;
    @InjectView(R.id.empty)
    ImageView mEmptyView;

    CurrentAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        setupViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("mainFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("mainFragment");
    }

    private void setupViews(){
        mAdapter = new CurrentAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mEmptyView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PageSwitchController.getInstance().registerDataSource(new NoMoreDataSource<Post>());
                PageSwitchController.getInstance().addInitialData(mAdapter.mData, i);

                final Post post = mAdapter.getItem(i);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(DetailActivity.KEY_EXTRA_POST, post);
                getActivity().startActivity(intent);
            }
        });

        // request api
        Api.getCurrentList(getActivity(), new Response.Listener<Stream>() {
            @Override
            public void onResponse(Stream o) {
                if (null == o) {
                    Toast.makeText(getActivity(), "data is null", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAdapter.setData(o.mPosts);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "data error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class CurrentAdapter extends BaseAdapter {

        public ArrayList<Post> mData = new ArrayList<Post>();

        public CurrentAdapter() {
        }

        public void setData(ArrayList<Post> data) {
            if (null == data || data.isEmpty()) {
                return;
            }
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Post getItem(int i) {
            return mData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Holder holder = null;
            if (null == view) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
                holder = new Holder(view);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }
            holder.bind(mData, i);
            return view;
        }

    }

    class Holder {
        @InjectView(R.id.post_title_text)
        TextView mTitle;
        @InjectView(R.id.post_content_text)
        TextView mContent;

        public Holder(View view) {
            ButterKnife.inject(this, view);
        }

        public void bind(final ArrayList<Post> data, final int position) {
            if (null == data || data.isEmpty()) {
                return;
            }
            final Post post = data.get(position);
            mTitle.setText(post.mTitle);
            mContent.setText(post.mShortDescription);
            mContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PageSwitchController.getInstance().registerDataSource(new NoMoreDataSource<Post>());
                    PageSwitchController.getInstance().addInitialData(mAdapter.mData, position);

                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra(DetailActivity.KEY_EXTRA_POST, post);
                    getActivity().startActivity(intent);
                }
            });
        }
    }
}
