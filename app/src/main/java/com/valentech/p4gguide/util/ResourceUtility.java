package com.valentech.p4gguide.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.TypedValue;

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

    public static int getPixelsFromDP(float dp) {
        return Float.valueOf(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics())).intValue();
    }

    public static void savePreference(Context context, String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor preferenceEditor = preferences.edit();
        preferenceEditor.putString(key, value);
        preferenceEditor.apply();
    }

    public static String getPreference(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
}
