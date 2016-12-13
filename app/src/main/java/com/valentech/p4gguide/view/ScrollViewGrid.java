package com.valentech.p4gguide.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by JD on 12/12/2016.
 */
class ScrollGridView extends GridView {
    public ScrollGridView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return e.getAction() == MotionEvent.ACTION_MOVE || super.onTouchEvent(e);
    }

    @Override
    protected void onMeasure(int width, int height) {
        int expand = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(width, expand);
    }
}