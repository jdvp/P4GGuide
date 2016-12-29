package com.valentech.p4gguide.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by JD on 12/16/2016.
 */

public class WalkthroughWebClient extends WebViewClient {
    private Context context;

    public WalkthroughWebClient(Context context) {
        super();
        this.context = context;
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        if(!url.startsWith("mailto:"))
            return false;

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity( intent );
        return true;
    }
}
