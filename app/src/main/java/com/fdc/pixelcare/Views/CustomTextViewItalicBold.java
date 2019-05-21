package com.fdc.pixelcare.Views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by lenovo on 17-03-2017.
 */

public class CustomTextViewItalicBold  extends AppCompatTextView {

    public CustomTextViewItalicBold(Context context) {
        super(context);
        setFont();
    }
    public CustomTextViewItalicBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public CustomTextViewItalicBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-BoldItalic.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
