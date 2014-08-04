package douban.pageslider.controller;

import java.util.List;
import douban.pageslider.datasource.BaseDataSource;
import douban.pageslider.datasource.NoMoreDataSource;
import douban.pageslider.model.PageIndex;
import douban.pageslider.model.Post;

/**
 * Created by luanqian on 2014/7/25.
 */
public class PageSwitchController<T> implements BaseDataSource.DataSourceListener<T> {

    private static PageSwitchController sInstance;

    private BaseDataSource mDataSource = new NoMoreDataSource<T>(); // for default
    private PageIndex mPageIndex = new PageIndex<T>();

    private PageSwitchController() {
    }

    public static PageSwitchController getInstance() {
        if (null == sInstance) {
            synchronized (PageSwitchController.class) {
                if (null == sInstance) {
                    // take careful about this
                    sInstance = new PageSwitchController<Post>();
                }
            }
        }
        return sInstance;
    }

    public static void close() {
        sInstance = null;
    }

    public static void registerDataSource(BaseDataSource dataSource) {
        if (null == dataSource) {
            return;
        }
        getInstance().mDataSource = dataSource;
        getInstance().mDataSource.setDataSourceListener(getInstance());
    }

    public void addInitialData(List<T> data, int startIndex) {
        mPageIndex.addAll(data);
        mPageIndex.setStartIndex(startIndex);
    }

    public void addInitialData(T data) {
        mPageIndex.add(data);
        mPageIndex.setStartIndex(0);
    }

    public boolean isEmpty() {
        return mPageIndex.isEmpty();
    }

    public boolean hasPreviousItem() {
        return mPageIndex.hasPreviousItem();
    }

    public boolean hasNextItem() {
        return mPageIndex.hasNextItem();
    }

    public void stepPrevious() {
        mPageIndex.stepPrevious();
    }

    public void stepNext() {
        mPageIndex.stepNext();
        if (mPageIndex.needPreLoadMore() && mDataSource.hasMorePosts()) {
            mDataSource.loadMore();
        }
    }

    public T getCurrentPost() {
        return (T) mPageIndex.getCurrent();
    }

    public T getPreviousPost() {
        return (T) mPageIndex.getPrevious();
    }

    public T getNextPost() {
        return (T) mPageIndex.getNext();
    }

    @Override
    public void onNewDataLoaded(List<T> data) {
        if (null != data) {
            // because we are pre-load so we can do not notify UI
            mPageIndex.addAll(data);
        }
    }
}
