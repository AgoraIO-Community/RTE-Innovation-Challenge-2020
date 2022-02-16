package com.hwm.together.view.activity;

import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hwm.together.R;

public class TetrisActivity extends BaseActivity {
    WebView wvTetrisGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetris);

        initView();
    }

    private void initView(){
        wvTetrisGame = findViewById(R.id.at_wv_tetris);

        wvTetrisGame.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        wvTetrisGame.setWebChromeClient(new WebChromeClient(){

        });
        wvTetrisGame.getSettings().setJavaScriptEnabled(true);
        wvTetrisGame.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        wvTetrisGame.getSettings().setUseWideViewPort(true);
        wvTetrisGame.getSettings().setDomStorageEnabled(true);
        wvTetrisGame.loadUrl("http://62.234.117.127/resources/");
    }
}