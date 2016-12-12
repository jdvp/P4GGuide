package com.valentech.p4gguide.util;

import android.content.Context;

/**
 * Created by JD on 12/11/2016.
 */

public class StringUtil {
    private static String sanitizeItemName(String item) {
        return item.replaceAll("[^a-zA-Z]", "").toLowerCase().trim();
    }

    public static int getRawId(Context context, String name) {
        return context.getResources().getIdentifier(sanitizeItemName(name), "raw", context.getPackageName());
    }


    public static int getSocialLinkImgId(Context context, String name) {
        return context.getResources().getIdentifier("social_link_" + sanitizeItemName(name), "drawable", context.getPackageName());
    }
}
