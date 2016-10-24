package com.fosung.usedemo.views;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.AutoCompleteTextView;

/**
 * Created by fosung on 2016/9/23.
 */
public class ZAutoCompleteTextView extends AutoCompleteTextView {
    public ZAutoCompleteTextView(Context context) {
        super(context);
    }
    public ZAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context,attrs);
    }
    public ZAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public boolean enoughToFilter() {
        return true;
    }
    @Override
    protected void onFocusChanged(boolean focused,int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction,previouslyFocusedRect);
        performFiltering(getText(), KeyEvent.KEYCODE_UNKNOWN);
    }
}
