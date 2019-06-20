package org.seniorlaguna.calculator.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class ExtendedViewPager(context: Context, attrs : AttributeSet) : ViewPager(context, attrs) {

    companion object {
        val HISTORY_TAB = 0
        val DISPLAY_TAB = 1
    }

    var canSwipe = true

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return canSwipe and super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return canSwipe and super.onInterceptTouchEvent(ev)
    }
}