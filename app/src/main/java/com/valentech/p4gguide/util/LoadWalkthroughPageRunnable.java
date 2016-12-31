package com.valentech.p4gguide.util;

import android.app.Activity;
import android.webkit.WebView;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * Created by JD on 12/31/2016.
 */

public class LoadWalkthroughPageRunnable implements Runnable {
    
    private Activity context;
    private String date;
    private WebView view;

    public LoadWalkthroughPageRunnable(Activity contextIn, String dateIn, WebView viewIn) {
        context = contextIn;
        date = dateIn;
        view = viewIn;
    }

    @Override
    public void run() {
        try {
            Document document = ResourceUtility.getWalkthroughPageForDate(date);

            Element body = document.getElementById("wiki-wrapper");
            Element ad = body.getElementById("queen");
            if(ad != null) {
                ad.remove();
            }
            Element nav = body.getElementById("bottom-nav");
            if(nav != null) {
                nav.remove();
            }
            for(Element img : body.getElementsByTag("img")) {
                String src = img.attributes().get("data-original");
                if(src != null) {
                    img.attr("src", src);
                    img.attr("data-original", "");
                } else {
                    img.remove();
                }
                img.attr("class", "");
                img.attr("width", "100%");
                img.attr("max-width", "100%");
                img.attr("height", "");

            }

            final String html = "<link rel=\"stylesheet\" type=\"text/css\" href=\"dayfragment.css\" />" + body.toString();
            ResourceUtility.savePreference(context, date, html);

            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
                }
            });
        } catch (IOException e) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view.loadData(ResourceUtility.getDayNotFoundHtml(context), "text/html", "utf-8");
                }
            });
            e.printStackTrace();
        }
    }
}
