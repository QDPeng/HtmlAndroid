package com.liusp.html;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

public class MainActivity extends AppCompatActivity {
    BridgeWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (BridgeWebView) findViewById(R.id.web_view);
        webView.clearCache(true);

        webView.loadUrl("file:///android_asset/demo.html");

        webView.registerHandler("jsCallJava", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                Toast.makeText(MainActivity.this, data, Toast.LENGTH_LONG).show();
                if ("100".equals(data)) {
                    function.onCallBack(getString(R.string.text));
                } else if ("101".equals(data)) {
                    Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    chooserIntent.setType("image/*");
                    startActivityForResult(chooserIntent, 101);
                }

            }

        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 101) {
            webView.callHandler("javaCallJS", "http://www.sinaimg.cn/dy/slidenews/31_img/2016_49/28379_756225_280945.jpg", new CallBackFunction() {
                @Override
                public void onCallBack(String data) {
                    Toast.makeText(MainActivity.this, data, Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.clearCache(true);
            webView.clearHistory();
            webView.destroyDrawingCache();
            webView.destroy();
        }
        super.onDestroy();
    }
}
