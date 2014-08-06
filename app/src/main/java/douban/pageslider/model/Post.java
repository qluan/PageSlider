package douban.pageslider.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by luanqian on 2014/7/29.
 */
public class Post implements Parcelable{

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
    @SerializedName("url")
    public String mOriginUrl;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.mTitle);
        dest.writeString(this.mShortDescription);
        dest.writeString(this.mOriginUrl);
    }

    public Post() {
    }

    private Post(Parcel in) {
        this.id = in.readInt();
        this.mTitle = in.readString();
        this.mShortDescription = in.readString();
        this.mOriginUrl = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}

