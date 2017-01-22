package com.valentech.p4gguide.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.util.TypedValue;

import com.valentech.p4gguide.model.calendar.Day;
import com.valentech.p4gguide.model.calendar.Month;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Utility class used for various calculations that are used OFTEN
 * Created by JD on 12/11/2016.
 */

public class ResourceUtility {
    public static final String PREFERENCE_LAST_SOCIAL_LINK = "preference last social link";
    public static String sanitizeItemName(String item) {
        return item.replaceAll("[^a-zA-Z]", "").toLowerCase().trim();
    }

    public static int getRawId(Context context, String name) {
        return context.getResources().getIdentifier(sanitizeItemName(name), "raw", context.getPackageName());
    }

    public static int getSocialLinkImgId(Context context, String name) {
        return context.getResources().getIdentifier("social_link_" + sanitizeItemName(name), "drawable", context.getPackageName());
    }

    public static String getDayNotFoundHtml(Context context) {
        return getGenericHtmlFromAssets(context, "day_not_found.html");
    }

    public static String getLoading(Context context) {
        return getGenericHtmlFromAssets(context, "loading.html");
    }

    public static String getDailyHtml(Context context, String day) {
        return getGenericHtmlFromAssets(context, day + ".txt");
    }

    private static String getGenericHtmlFromAssets(Context context, String string) {
        if(hasNullObjects(context, string)) {
            return "";
        }
        AssetManager manager  = context.getAssets();
        InputStream is = null;
        try {
            is = manager.open(string);
            Scanner s = new Scanner(is).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        } catch (IOException ignored) {ignored.printStackTrace();} finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                    ignored.printStackTrace();
                }
            }
        }
        return "";
    }

    public static int getPixelsFromDP(float dp) {
        return Float.valueOf(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics())).intValue();
    }

    public static void savePreference(Context context, String key, String value) {
        if(hasNullObjects(context, key, value)) {
            return;
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor preferenceEditor = preferences.edit();
        preferenceEditor.putString(key, value);
        preferenceEditor.apply();
    }

    public static String getPreference(Context context, String key) {
        if(hasNullObjects(context, key)) {
            return null;
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    private static boolean hasNullObjects(Object ... objects) {
        if(objects == null){
            return true;
        }
        for (Object object : objects) {
            if (object == null){
                return true;
            }
        }
        return false;
    }

    static Document getWalkthroughPageForDate(String date) throws IOException {
        Day day = Day.fromString(date);
        if(day.isAfter(new Day(Month.JUNE, 12)) && new Day(Month.MARCH, 19).isAfter(day)) {
            //todo add a new way of grabbing content in this period
        }

        return Jsoup.connect("http://m.ign.com/wikis/shin-megami-tensei-persona-4-golden/" + date).
                userAgent("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Mobile Safari/537.36").get();
    }
}
