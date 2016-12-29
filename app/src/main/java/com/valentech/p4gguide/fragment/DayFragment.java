package com.valentech.p4gguide.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import com.valentech.p4gguide.R;
import com.valentech.p4gguide.model.calendar.Day;
import com.valentech.p4gguide.model.calendar.Month;
import com.valentech.p4gguide.util.AbstractAsyncTask;
import com.valentech.p4gguide.util.ResourceUtility;
import com.valentech.p4gguide.util.WalkthroughWebClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
/**
 * Fragment that displays a walkthrough page to the user.
 *
 * Created by JD on 12/11/2016.
 */
public class DayFragment extends Fragment {
    private boolean pageLoaded = false;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View dayView = inflater.inflate(R.layout.day_layout, container, false);

        final WebView view = (WebView) dayView.findViewById(R.id.day_web);
        view.setWebViewClient(new WalkthroughWebClient(getActivity()){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        largeLog("loading is", ResourceUtility.getLoading(getActivity()));
        view.loadDataWithBaseURL("file:///android_asset/", ResourceUtility.getLoading(getActivity()), "text/html", "utf-8", null);

        final String date;
        if(getArguments() != null) {
            date = getArguments().getString("date", "April_11th");
        } else {
            date = "April_11th";
        }

        Button nextDay = (Button) dayView.findViewById(R.id.next_day_button);
        if(nextDay != null) {
            nextDay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!pageLoaded) {
                        return;
                    }

                    Bundle args = new Bundle();
                    args.putString("date", Month.getNextDay(Day.fromString(date)).toString());

                    DayFragment fragment = new DayFragment();
                    fragment.setArguments(args);

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "walkthrough fragment").commit();
                }
            });
        }

        Button previous = (Button) dayView.findViewById(R.id.previous_day_button);
        if(previous != null) {
            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!pageLoaded) {
                        return;
                    }

                    Bundle args = new Bundle();
                    args.putString("date", Month.getPreviousDay(Day.fromString(date)).toString());

                    DayFragment fragment = new DayFragment();
                    fragment.setArguments(args);

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "walkthrough fragment").commit();
                }
            });
        }

        //if the page is cached, load it
        if(ResourceUtility.getPreference(getActivity(), date) != null) {
            view.loadDataWithBaseURL("file:///android_asset/", ResourceUtility.getPreference(getActivity(), date), "text/html", "utf-8", null);
            pageLoaded = true;
            return dayView;
        }

        //otherwise retrieve and cache
        Runnable loadPage = new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("http://m.ign.com/wikis/shin-megami-tensei-persona-4-golden/" + date).
                            userAgent("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Mobile Safari/537.36").get();


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
                    ResourceUtility.savePreference(getActivity(), date, html);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
                            pageLoaded = true;
                        }
                    });
                } catch (IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.loadData(ResourceUtility.getDayNotFoundHtml(getActivity()), "text/html", "utf-8");
                            pageLoaded = true;
                        }
                    });
                    e.printStackTrace();
                }
            }
        };

        new AbstractAsyncTask().run(loadPage).execute();
        return dayView;
    }

    private static void largeLog(String tag, String content) {
        if (content.length() > 4000) {
            Log.d(tag, content.substring(0, 4000));
            largeLog(tag, content.substring(4000));
        } else {
            Log.d(tag, content);
        }
    }
}


