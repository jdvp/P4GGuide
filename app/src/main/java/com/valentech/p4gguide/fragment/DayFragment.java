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
    private String date = "";
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

        if(getArguments() != null) {
            date = getArguments().getString("date", "April_11th");
        } else {
            date = "April_11th";
        }

        View.OnClickListener nextButtonClick = v -> {
            if (!pageLoaded) {
                return;
            }

            date = Month.getNextDay(Day.fromString(date)).toString();
            loadPage(date, view, dayView);
        };
        Button nextDay = (Button) dayView.findViewById(R.id.next_day_button);
        if(nextDay != null) {
            nextDay.setOnClickListener(nextButtonClick);
        }

        View.OnClickListener previousButtonClick = v -> {
            if (!pageLoaded) {
                return;
            }

            date = Month.getPreviousDay(Day.fromString(date)).toString();
            loadPage(date, view, dayView);
        };
        Button previous = (Button) dayView.findViewById(R.id.previous_day_button);
        if(previous != null) {
            previous.setOnClickListener(previousButtonClick);
        }

        return loadPage(date, view, dayView);
    }

    private static void largeLog(String tag, String content) {
        if (content.length() > 4000) {
            Log.d(tag, content.substring(0, 4000));
            largeLog(tag, content.substring(4000));
        } else {
            Log.d(tag, content);
        }
    }

    private View loadPage(String date, WebView view, View dayView) {
        pageLoaded = false;
        //hide or unhide the next and previous buttons if necessary
        View previousButton = null;
        View nextButton = null;
        if(dayView != null && dayView.findViewById(R.id.previous_day_button) != null) {
            previousButton = dayView.findViewById(R.id.previous_day_button);
        }
        if(dayView != null && dayView.findViewById(R.id.next_day_button) != null) {
            nextButton = dayView.findViewById(R.id.next_day_button);
        }

        if( previousButton != null && new Day(Month.APRIL, 11).toString().equals(date)) {
            previousButton.setVisibility(View.INVISIBLE);
        } else if (previousButton != null && previousButton.getVisibility() != View.VISIBLE) {
            previousButton.setVisibility(View.VISIBLE);
        }

        if( nextButton != null && new Day(Month.MARCH, 20).toString().equals(date)) {
            nextButton.setVisibility(View.INVISIBLE);
        } else if (nextButton != null && nextButton.getVisibility() != View.VISIBLE) {
            nextButton.setVisibility(View.VISIBLE);
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
        new AbstractAsyncTask().run(loadPage).andThen(() -> {pageLoaded = true;}).execute();
        return dayView;
    }
}


