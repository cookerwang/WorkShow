package me.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextSwitcher;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import me.avva.study.R;
import me.ui.base.ToolbarActivity;


public class WebActivity extends ToolbarActivity {

    private static final String EXTRA_URL = "extra_url";
    private static final String EXTRA_TITLE = "extra_title";

    //@Bind(R.id.progressbar) NumberProgressBar mProgressbar;
    @Bind(R.id.webView)
    WebView mWebView;
    @Bind(R.id.tv_title)
    TextSwitcher mTextSwitcher;

    private String mUrl, mTitle;


    @Override
    protected int provideContentViewId() {
        return R.layout.activity_web;
    }


    @Override
    public boolean canBack() {
        return true;
    }


    /**
     * Using newIntent trick, return WebActivity Intent, to avoid `public static`
     * constant
     * variable everywhere
     *
     * @return Intent to start WebActivity
     */
    public static Intent newIntent(Context context, String extraURL, String extraTitle) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(EXTRA_URL, extraURL);
        intent.putExtra(EXTRA_TITLE, extraTitle);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mUrl = getIntent().getStringExtra(EXTRA_URL);
        mTitle = getIntent().getStringExtra(EXTRA_TITLE);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        mWebView.setWebChromeClient(new ChromeClient());
        mWebView.setWebViewClient(new LoveClient());

        mWebView.loadUrl(mUrl);

        /**
         <style name="WebTitle"
         parent="@android:style/TextAppearance">
         <item name="android:textSize">18dp</item>
         <item name="android:textColor">@color/white</item>
         <item name="android:ellipsize">marquee</item>
         <item name="android:marqueeRepeatLimit">marquee_forever</item>
         <item name="android:scrollHorizontally">true</item>
         <item name="android:singleLine">true</item>
         </style>
         */
        mTextSwitcher.setFactory(() -> {
            TextView textView = new TextView(this);
            textView.setTextAppearance(this, R.style.WebTitle);
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textView.postDelayed(() -> textView.setSelected(true), 1738);
            return textView;
        });
        mTextSwitcher.setInAnimation(this, android.R.anim.fade_in);
        mTextSwitcher.setOutAnimation(this, android.R.anim.fade_out);
        if (mTitle != null) setTitle(mTitle);
    }


    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTextSwitcher.setText(title);
    }


    private void refresh() {
        mWebView.reload();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    }
                    else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*switch (id) {
            case R.id.action_refresh:
                refresh();
                return true;
            case R.id.action_copy_url:
                String copyDone = getString(R.string.tip_copy_done);
                AndroidUtils.copyToClipBoard(this, mWebView.getUrl(), copyDone);
                return true;
            case R.id.action_open_url:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(mUrl);
                intent.setData(uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    ToastUtils.showLong(R.string.tip_open_fail);
                }
                return true;
        }*/
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) mWebView.destroy();
        ButterKnife.unbind(this);
    }


    @Override
    protected void onPause() {
        if (mWebView != null) mWebView.onPause();
        super.onPause();
        //MobclickAgent.onPause(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) mWebView.onResume();
        //MobclickAgent.onResume(this);
    }


    private class ChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            //mProgressbar.setProgress(newProgress);
            if (newProgress == 100) {
                //mProgressbar.setVisibility(View.GONE);
            }
            else {
                //mProgressbar.setVisibility(View.VISIBLE);
            }
        }


        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setTitle(title);
        }
    }

    private class LoveClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null) view.loadUrl(url);
            return true;
        }
    }
}

/**
 <?xml version="1.0" encoding="utf-8"?>
 <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
 xmlns:app="http://schemas.android.com/apk/res-auto"
 android:layout_width="match_parent"
 android:layout_height="match_parent"
 android:orientation="vertical">

 <include layout="@layout/view_toolbar_with_title_view"/>

 <com.daimajia.numberprogressbar.NumberProgressBar
 android:id="@+id/progressbar"
 style="@style/NumberProgressBar_Funny_Orange"
 android:layout_width="match_parent"
 app:progress_reached_bar_height="2dp"
 app:progress_text_size="0sp"
 app:progress_text_visibility="invisible"
 app:progress_unreached_bar_height="2dp"/>

 <WebView
 android:id="@+id/webView"
 android:layout_width="match_parent"
 android:layout_height="match_parent"/>

 </LinearLayout>
 **/

/**
 <android.support.design.widget.AppBarLayout xmlns:android="http://schemas.android.com/apk/res/android"
 xmlns:app="http://schemas.android.com/apk/res-auto"
 android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"
 android:id="@+id/app_bar_layout"
 android:layout_width="match_parent"
 android:layout_height="wrap_content"
 app:elevation="4dp">

 <android.support.v7.widget.Toolbar
 android:id="@+id/toolbar"
 android:layout_width="match_parent"
 android:layout_height="wrap_content"
 android:background="?attr/colorPrimary"
 android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"
 app:popupTheme="@style/ThemeOverlay.AppCompat.Light"
 app:layout_scrollFlags="scroll|enterAlways">

 <TextSwitcher
 android:id="@+id/tv_title"
 android:layout_width="match_parent"
 android:layout_height="match_parent"/>
 </android.support.v7.widget.Toolbar>

 </android.support.design.widget.AppBarLayout>
**/