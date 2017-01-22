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
import com.valentech.p4gguide.util.LoadWalkthroughPageRunnable;
import com.valentech.p4gguide.util.ResourceUtility;
import com.valentech.p4gguide.util.WalkthroughWebClient;

import java.io.IOException;
/**
 * Fragment that displays a walkthrough page to the user.
 *
 * Created by JD on 12/11/2016.
 */
public class DayFragment extends Fragment {
    private Boolean pageLoaded = false;
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

        //show loading animation while loading
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

        //if we have a local version of the page, show it
        String cachedPage = ResourceUtility.getDailyHtml(getActivity(), date.toLowerCase());

        //otherwise, check the cache for the page
        if(cachedPage == null || cachedPage.isEmpty()) {
            ResourceUtility.getPreference(getActivity(), date);
        }
        if(cachedPage != null && !cachedPage.isEmpty()) {
            view.loadDataWithBaseURL("file:///android_asset/", cachedPage, "text/html", "utf-8", null);
            pageLoaded = true;
            return dayView;
        }

        //otherwise retrieve from the internet and then add to cache
        Runnable loadPage = new LoadWalkthroughPageRunnable(getActivity(), date, view);
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


