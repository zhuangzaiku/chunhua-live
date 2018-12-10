package com.quootta.mdate.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.quootta.mdate.R;
import com.quootta.mdate.base.BaseActivity;
import com.quootta.mdate.base.BaseApp;
import com.quootta.mdate.ui.dialog.ShareDialog;
import com.quootta.mdate.utils.ActivityUtil;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import butterknife.Bind;


public class BannerActivity extends BaseActivity {

    @Bind(R.id.tv_title_bar)
    TextView tv_title_bar;

    @Bind(R.id.iv_share_title_bar)
    ImageView ivShare;

    @Bind(R.id.iv_back_title_bar)
    ImageView ivBack;

    @Bind(R.id.wv_banner)
    WebView webView;

    private String strId;
    private String strURL;
    private String strTitle;
    private String strContent;
    private String strShare_url;
    private String strThumbnail;
    private RequestQueue requestQueue;

    private ImageLoader imageLoader;

    private String title = "分享", content = "内容", url = "http://baidu.com";

    private onOpenShareDialog onOpenShareDialog;

    @Override
    protected void init() {
        onOpenShareDialog=new onOpenShareDialog();
        strId=getIntent().getStringExtra("id");
        strURL = getIntent().getStringExtra("url");
        strTitle=getIntent().getStringExtra("title");
        strContent=getIntent().getStringExtra("content");
        strShare_url=getIntent().getStringExtra("share_url");
        strThumbnail=getIntent().getStringExtra("thumbnail");
        requestQueue = BaseApp.getRequestQueue();
        imageLoader = BaseApp.getImageLoader();
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
        tv_title_bar.setText("");
        tv_title_bar.setTextSize(16);
        ivShare.setVisibility(View.VISIBLE);
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
        webView.loadUrl(strURL);
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
            if (url.substring(0,7).equals("chunhua")){
                if (url.substring(10,url.length()).equals("invitation")){
                    onOpenShareDialog.openShareDialog();
                }else {
                    Intent webIntent=new Intent(BannerActivity.this,PersonalDetailsActivity.class);
                    webIntent.putExtra("user_id", url.substring(url.indexOf("=")+1,url.length()));
                    startActivity(webIntent);
                    BannerActivity.this.finish();
                }


            }else {

                view.loadUrl(url);

            }
                return true;

            }
        });

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

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开分享面板(定制界面，不使用友盟默认)

            }


        });
    }


    class onOpenShareDialog{
        private void openShareDialog() {

            ShareDialog shareDialog = new ShareDialog(baseContext, new ShareDialog.onClickback() {
                @Override
                public void onShare(int id) {
//                    String str = infoDetailNonStatic.getShare().getThumbnail();
                }
            });
            shareDialog.show();
            shareDialog.setShare_link(strShare_url);
//
//            new ShareDialog(BannerActivity.this, new ShareDialog.onClickback() {
//
//                @Override
//                public void onShare(int id) {
//
//                    UMImage image=new UMImage(BannerActivity.this,strThumbnail);
//
//                    switch (id) {
//                        case 1://微信好友
////
//                            if (tv_title_bar.getText() !=null) {
//                            }
//                            new ShareAction(BannerActivity.this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
//                                    .withMedia(image)
//                                    .withTargetUrl(strShare_url)
//                                    .withText(strTitle)
//                                    .withText(strContent)
//                                    .share();
//                            break;
//                        case 2://朋友圈
//                            platform=SHARE_MEDIA.WEIXIN_CIRCLE;
//                            if (tv_title_bar.getText() !=null) {
//                            }
//                            new ShareAction(BannerActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
//                                    .withMedia(image)
//                                    .withTitle(strTitle)
//                                    .withTargetUrl(strShare_url)
//                                    .withText(strContent)
//                                    .share();
//                            break;
//                        case 3:
//                            platform=SHARE_MEDIA.QQ;
//                            if (tv_title_bar.getText()!=null){
//
//                            }
//                            new ShareAction(BannerActivity.this).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
//                                    .withMedia(image)
//                                    .withTitle(strTitle)
//                                    .withTargetUrl(strShare_url)
//                                    .withText(strContent)
//                                    .share();
//                            break;
//                        case 4:
//                            platform=SHARE_MEDIA.QZONE;
//                            if (tv_title_bar.getText()!=null){
//
//                            }
//                            new ShareAction(BannerActivity.this).setPlatform(SHARE_MEDIA.QZONE).setCallback(umShareListener)
//                                    .withMedia(image)
//                                    .withTitle(strTitle)
//                                    .withTargetUrl(strShare_url)
//                                    .withText(strContent)
//                                    .share();
//                            break;
//                    }
//                }
//
//                private void ShareTOMedia(SHARE_MEDIA share_media) {
//
//                }
//            }).show();
        }
    }

}
