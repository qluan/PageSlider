package douban.pageslider.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by luanqian on 2014/7/29.
 */
public class Stream{

    @Expose
    @SerializedName("date")
    public String mDate;

    @Expose
    @SerializedName("total")
    public int mTotal;

    @Expose
    @SerializedName("posts")
    public List<Post> mPosts;
}
