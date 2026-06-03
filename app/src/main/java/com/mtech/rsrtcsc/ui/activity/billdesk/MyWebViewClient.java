package com.mtech.rsrtcsc.ui.activity.billdesk;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URISyntaxException;

public class MyWebViewClient extends WebViewClient {
    private Activity activity;

    public MyWebViewClient(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith("upi://pay") || url.startsWith("bhim://upi")) {
            try {
                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                if (intent != null) {
                    PackageManager pm = activity.getPackageManager();
                    ResolveInfo info = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    if (info != null) {
                        activity.startActivity(intent);
                    } else {
                        String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                        if (fallbackUrl != null && !fallbackUrl.isEmpty()) {
                            view.loadUrl(fallbackUrl);
                        } else {
                            Log.e("WebViewClient", "No fallback URL");
                        }
                    }
                    return true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
