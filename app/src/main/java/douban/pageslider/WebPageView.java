package douban.pageslider;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.Map;

public class WebPageView extends WebView {

    private boolean mDestroyed;

    public WebPageView(Context context) {
        super(context);
        initialize(context);
    }

    public WebPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public WebPageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context) {
        setUp();
    }

    @Override
    public void destroy() {
        mDestroyed = true;
        super.destroy();
    }

    @Override
    public void loadUrl(final String url, final Map<String, String> additionalHttpHeaders) {
        if (mDestroyed) {
            return;
        }
        super.loadUrl(url, additionalHttpHeaders);
    }

    @Override
    public void loadUrl(final String url) {
        if (mDestroyed) {
            return;
        }
        super.loadUrl(url);
    }

    @Override
    public void loadData(final String data, final String mimeType, final String encoding) {
        if (mDestroyed) {
            return;
        }
        super.loadData(data, mimeType, encoding);
    }

    @Override
    public void loadDataWithBaseURL(final String baseUrl, final String data, final String mimeType, final String encoding, final String historyUrl) {
        if (mDestroyed) {
            return;
        }
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    @TargetApi(VERSION_CODES.KITKAT)
    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
    private void setUp() {
        clearCache(true);
        setBackgroundColor(Color.TRANSPARENT);
        setHorizontalScrollBarEnabled(false);
        setScrollbarFadingEnabled(true);
        setVerticalScrollBarEnabled(true);
        setVerticalScrollbarOverlay(true);
        setOverScrollMode(WebView.OVER_SCROLL_NEVER);

        WebSettings settings = getSettings();
        settings.setSupportZoom(false);
        settings.setDisplayZoomControls(false);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        setWebViewClient();
        mDestroyed = false;
    }

    private void setWebViewClient() {
        final WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        };
        final WebChromeClient webChromeClient = new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public boolean onConsoleMessage(final ConsoleMessage cm) {
                return true;
            }
        };
        setWebViewClient(webViewClient);
        setWebChromeClient(webChromeClient);
    }

    /**
     * 获取WebView内容的实际高度
     *
     * @return real content height
     */
    public float getRawContentHeight() {
        return getContentHeight() * getScale();
    }

    /**
     * 获取当前View的实际高度，排除padding
     *
     * @return real view height
     */
    public float getRawHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

}
