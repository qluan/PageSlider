package douban.pageslider.datasource;

import douban.pageslider.model.Post;

/**
 * Created by luanqian on 2014/7/25.
 */
public class CacheDataSource<T> extends BaseDataSource<T> {

    public CacheDataSource(){
        mHasMoreData = true;
    }

    @Override
    public void loadMore() {
        super.loadMore();
        // TODO load more
    }
}
