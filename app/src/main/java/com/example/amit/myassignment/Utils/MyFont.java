package com.example.amit.myassignment.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by @mit on 17/4/16.
 */
public class MyFont {
    public static final class TYPEFACE {
        public static final Typeface FontThin(Context ctx){
            Typeface typeface                       =   Typeface.createFromAsset(ctx.getAssets(), "fonts/TitilliumWeb-Regular.ttf");
            return typeface;
        }

        public static final Typeface FontLight(Context ctx){
            Typeface typeface                       =   Typeface.createFromAsset(ctx.getAssets(), "fonts/TitilliumWeb-SemiBold.ttf");
            return typeface;
        }

        public static final Typeface FontLight2(Context ctx){
            Typeface typeface                       =   Typeface.createFromAsset(ctx.getAssets(), "fonts/OpenSans-Light.ttf");
            return typeface;
        }

        public static final Typeface FontRegular(Context ctx){
            Typeface typeface                       =   Typeface.createFromAsset(ctx.getAssets(), "fonts/Roboto-Regular.ttf");
            return typeface;
        }

        public static final Typeface FontPiruLen(Context ctx){
            Typeface typeface                       =   Typeface.createFromAsset(ctx.getAssets(), "fonts/pirulen_rg.ttf");
            return typeface;
        }


    }

    public static void applyCustomFont(ViewGroup list, Typeface customTypeface) {
        for (int i = 0; i < list.getChildCount(); i++) {
            View view                               =   list.getChildAt(i);
            if (view instanceof ViewGroup) {
                applyCustomFont((ViewGroup) view, customTypeface);
            } else if (view instanceof TextView) {
                ((TextView) view).setTypeface(customTypeface);
            }
        }
    }
}
