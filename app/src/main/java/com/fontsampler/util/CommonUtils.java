package com.fontsampler.util;

import android.content.Context;

/**
 * Created by sebastian on 26.06.14.
 */
public class CommonUtils {

    public static float pixelsToSp(Context context, float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }
}
