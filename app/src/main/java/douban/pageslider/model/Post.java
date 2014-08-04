package douban.pageslider.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by luanqian on 2014/7/29.
 */
public class Post {

    @Expose
    @SerializedName("id")
    public int id;

    @Expose
    @SerializedName("title")
    public String mTitle;

    @Expose
    @SerializedName("abstract")
    public String mShortDescription;

    @Expose
    @SerializedName("original_url")
    public String mOriginUrl;

}
