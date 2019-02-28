package com.murataktay.murataktay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    private WebView webView;
    SwipeRefreshLayout mySwipeRefreshLayout;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomWebViewClient customWebViewClient = new CustomWebViewClient();
        mySwipeRefreshLayout = this.findViewById(R.id.swipeContainer);
        webView =findViewById(R.id.webView);
        //webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(customWebViewClient);

        String url = "https://murataktay.com";
        webView.loadUrl(url);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mySwipeRefreshLayout.setRefreshing(false);

                        if (isOnline()){
                            webView.reload();
                        }else{
                            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                            alert.setTitle("İnternet Bağlantısı Sorunu!");
                            alert.setMessage("İnternet bağlantısı yok. Lütfen kontrol edip tekrar deneyin!");
                            alert.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            alert.setCancelable(false);
                            alert.show();
                        }

                    }
                }
        );
    }

    private class CustomWebViewClient extends WebViewClient{

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon){
            super.onPageStarted(view, url, favicon);
            mySwipeRefreshLayout.setRefreshing(true);

        }

        public void onPageFinished(WebView view, String url){
            super.onPageFinished(view, url);
            mySwipeRefreshLayout.setRefreshing(false);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);

            if (isOnline()){
                webView.loadUrl(url);
                return true;
            }else{
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("İnternet Bağlantısı Sorunu!");
                alert.setMessage("İnternet bağlantısı yok. Lütfen kontrol edip tekrar deneyin!");
                alert.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alert.setCancelable(false);
                alert.show();
            }
            return false;
        }
    }
    public boolean isOnline(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isAvailable() && manager.getActiveNetworkInfo().isConnected();
    }
    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Çıkmak İstiyormusunuz?");
            alert.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alert.show();
        }
    }
}