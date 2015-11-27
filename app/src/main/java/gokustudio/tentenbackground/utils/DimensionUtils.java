package gokustudio.tentenbackground.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by son on 9/22/15.
 */
public class DimensionUtils {
    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static int getUCLN(int a, int b){
        while(a!= b){
            if(a>b) a= a-b;
            else b= b-a;
        }
        return (a);
    }
}
