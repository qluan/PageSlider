package douban.pageslider;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.douban.gsonvolley.ApiGsonRequest;
import com.douban.okvolley.OkVolley;
import com.google.gson.reflect.TypeToken;

import douban.pageslider.model.Stream;

/**
 * Created by luanqian on 2014/8/4.
 */
public class Api {

    static final String API_CURRENT = "http://moment.douban.com/api/stream/current";

    public static void getCurrentList(Context context, Response.Listener listener, Response.ErrorListener errorListener) {
        ApiGsonRequest<Stream> apiGsonRequest = new ApiGsonRequest<Stream>(Request.Method.GET, API_CURRENT,
                new TypeToken<Stream>() {
                }.getType(), listener, errorListener);
        OkVolley.getInstance(context).getRequestQueue().add(apiGsonRequest);
    }
}
