package douban.pageslider;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import java.util.ArrayList;
import butterknife.ButterKnife;
import butterknife.InjectView;
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

    private void setupViews(){
        mAdapter = new CurrentAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mEmptyView);

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

    static class CurrentAdapter extends BaseAdapter {

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
            holder.bind(getItem(i));
            return view;
        }

    }

    static class Holder {
        @InjectView(R.id.post_title_text)
        TextView mTitle;
        @InjectView(R.id.post_content_text)
        TextView mContent;

        public Holder(View view) {
            ButterKnife.inject(this, view);
        }

        public void bind(Post post) {
            if (null == post) {
                return;
            }
            mTitle.setText(post.mTitle);
            mContent.setText(post.mShortDescription);
        }
    }
}
