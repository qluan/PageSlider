package douban.pageslider.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to manage page switch in PageNavActivity
 * <p/>
 * Created by luanqian on 2014/7/23.
 */
public class PageIndex<T> {

    public static final int THRESHOLD_NUMBER = 3;

    public ArrayList<T> mData = new ArrayList<T>();
    public int mCurrentIndex = 0;

    public PageIndex() {
    }

    public PageIndex(T data) {
        if (null == data) {
            return;
        }
        mData.clear();
        mData.add(data);
    }

    public PageIndex(List<T> data) {
        if (null == data || data.size() == 0) {
            return;
        }
        mData.clear();
        mData.addAll(data);
    }

    public void add(T data) {
        if (null == data) {
            return;
        }
        mData.add(data);
    }

    public void addAll(List<T> data) {
        if (null != data && data.size() > 0) {
            mData.addAll(data);
        }
    }

    public boolean isEmpty() {
        return null == mData || mData.size() == 0;
    }

    public void setStartIndex(int index) {
        mCurrentIndex = index;
    }

    public boolean hasPreviousItem() {
        return !isEmpty() && mCurrentIndex > 0;
    }

    public void stepPrevious() {
        if (hasPreviousItem()) {
            mCurrentIndex--;
        } else {
            throw new IllegalArgumentException("has not previous item");
        }
    }

    public boolean hasNextItem() {
        return !isEmpty() && mCurrentIndex < mData.size() - 1;
    }

    public void stepNext() {
        if (hasNextItem()) {
            mCurrentIndex++;
        } else {
            throw new IllegalArgumentException("has not next item");
        }
    }

    public T getCurrent() {
        if (isEmpty()) {
            throw new IllegalArgumentException("make sure the pages is not empty");
        }
        return mData.get(mCurrentIndex);
    }

    public T getNext() {
        if (isEmpty() || !hasNextItem()) {
            throw new IllegalArgumentException("make sure the pages is not empty");
        }
        return mData.get(mCurrentIndex + 1);
    }

    public T getPrevious() {
        if (isEmpty() || !hasPreviousItem()) {
            throw new IllegalArgumentException("make sure the pages is not empty");
        }
        return mData.get(mCurrentIndex - 1);
    }

    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    public boolean needPreLoadMore(){
        return mData.size() - mCurrentIndex - 1 <= THRESHOLD_NUMBER;
    }

}
