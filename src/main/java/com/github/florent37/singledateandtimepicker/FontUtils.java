package com.github.florent37.singledateandtimepicker;

import android.content.Context;
import android.graphics.Typeface;

public abstract class FontUtils {

    public static Typeface regular, bold;

    public static Typeface getFont(Context appContext) {
        if (regular == null) {
            regular = Typeface.createFromAsset(appContext.getAssets(), "fonts/Proxima-Nova-Regular.ttf");
        }
        return regular;
    }


    public static Typeface getFontBold(Context appContext) {
        if (bold == null) {
            bold = Typeface.createFromAsset(appContext.getAssets(), "fonts/Proxima-Nova-Semibold.ttf");
        }
        return bold;
    }
}
