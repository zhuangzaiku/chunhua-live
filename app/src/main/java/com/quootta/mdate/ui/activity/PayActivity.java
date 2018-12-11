package com.quootta.mdate.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.constant.LocalUrl;
import com.quootta.mdate.utils.ActivityUtil;
import com.android.volley.toolbox.ImageLoader;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;


public class PayActivity extends BaseActivity {

    @Bind(R.id.tv_title_bar)
    TextView tv_title_bar;

//    @Bind(R.id.iv_share_title_bar)
//    ImageView ivShare;

    @Bind(R.id.iv_back_title_bar)
    ImageView ivBack;

    @Bind(R.id.wv_banner)
    WebView webView;

    private String strURL;
    private int num = 0;

    private ImageLoader imageLoader;

    private String title = "分享", content = "内容", url = "http://baidu.com";
    Map<String,String> headers=new HashMap<String,String>();

    @Override
    protected void init() {

        strURL = getIntent().getStringExtra("url");
        num = getIntent().getIntExtra("num",0);
        strURL = LocalUrl.GET_PAY + "&channel=" + BaseApp.getChannelName(this)+"&sum=" + num;
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_banner;
    }

    @Override
    protected void initData() {
        initTitle();
        initWebView();
    }

    private void initTitle() {
        tv_title_bar.setText("Pay");
        tv_title_bar.setTextSize(16);
//        ivShare.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
    }

    private void initWebView() {
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.requestFocus();
        Log.i("tag","bannerActivity--url-->"+strURL);
        BaseApp.getApplication().addSessionCookie(headers);
        webView.addJavascriptInterface(this, "js");
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.loadUrl(strURL,headers);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                tv_title_bar.setText(title);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("tag","----view--->"+view+"--url---"+url);
             if (url.startsWith("weixin://wap/pay?") || url.startsWith("alipays://") || url.startsWith("alipayqr://")) {
                 Intent intent = new Intent();
                 intent.setAction(Intent.ACTION_VIEW);
                 intent.setData(Uri.parse(url));
                 startActivity(intent);
                 return true;
            } else {
                 if (url.substring(0, 7).equals("chunhua")) {
                     if (url.substring(10, url.length()).equals("invitation")) {

                     } else {
                         Intent webIntent = new Intent(PayActivity.this, PersonalDetailsActivity.class);
                         webIntent.putExtra("user_id", url.substring(url.indexOf("=") + 1, url.length()));
                         startActivity(webIntent);
                         PayActivity.this.finish();
                     }

                 } else {

                     view.loadUrl(url);

                 }
             }

             return true;
            }
        });

    }

    @JavascriptInterface
    public void closeWin() {
        this.setResult(10000);
        this.finish();
    }


    @Override
    protected void setListener() {
        ivBack.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.finishActivty();
            }
        });

//        ivShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //打开分享面板(定制界面，不使用友盟默认)
//
//            }
//
//
//        });
    }


}
