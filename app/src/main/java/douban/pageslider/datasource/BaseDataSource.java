package douban.pageslider.datasource;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Used to load data
 * <p/>
 * Created by luanqian on 2014/7/25.
 */
public class BaseDataSource<T> {

    public interface DataSourceListener<T> {
        public void onNewDataLoaded(List<T> data);
    }

    protected WeakReference<DataSourceListener> mDataSourceListener = new WeakReference<DataSourceListener>(null);

    protected boolean mHasMoreData = false;
    protected boolean mIsLoadingMore = false;

    public void setDataSourceListener(DataSourceListener dataSourceListener) {
        if (null != dataSourceListener) {
            mDataSourceListener = new WeakReference<DataSourceListener>(dataSourceListener);
        }
    }

    /**
     * Used to judge if has more posts to show
     *
     * @return
     */
    public boolean hasMorePosts() {
        return mHasMoreData;
    }

    /**
     * Load more posts to show
     */
    public void loadMore(){
        if (mIsLoadingMore){
            return;
        }
        mIsLoadingMore = true;
    };

    /**
     * Notify by register listener
     *
     * @param data
     */
    protected void notifyDataSourceRefresh(List<T> data) {
        mIsLoadingMore = false;
        if (null != mDataSourceListener) {
            DataSourceListener listener = mDataSourceListener.get();
            if (null != listener) {
                listener.onNewDataLoaded(data);
            }
        }
    }
}
