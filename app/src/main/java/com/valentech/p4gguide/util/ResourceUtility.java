package com.valentech.p4gguide.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by JD on 12/11/2016.
 */

public class ResourceUtility {
    private static String sanitizeItemName(String item) {
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
}
