package com.example.poch5;

import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.poch5.jsbridge.JSBridge;
import com.example.poch5.jsbridge.ServiceModule;


public class MainActivity extends AppCompatActivity {

    private String localH5Url = "file:///android_asset/test/myfile.html";
    private WebView myWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppUtil.setApplication(this.getApplication());
        initWebView();



    }

    private void initWebView() {
        myWebView = findViewById(R.id.wb_test);

        registerJSModule();
        WebView.setWebContentsDebuggingEnabled(true);
        myWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                JSBridge.injectJs(view);
            }
        });

        WebSettings settings = myWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        myWebView.loadUrl(localH5Url);
    }

    private void registerJSModule() {
        JSBridge.getConfig().setProtocol("AndroidBridge")
               .setLoadReadyFuncName("onJsReady")
               .registerModule(ServiceModule.class);
    }


}