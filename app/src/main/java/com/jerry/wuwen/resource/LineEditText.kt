package com.jerry.wuwen.resource


//请在这里添加您的包名
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Half.toFloat

import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText


class LineEditText(context: Context, attrs: AttributeSet) :
    AppCompatEditText(context, attrs) {
    private val mPaint: Paint

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

//      画底线
        //canvas.drawLine(0f, (this.height - 1).toFloat(), (this.width - 1).toFloat(), (this.height - 1).toFloat(), mPaint)
    }

    /**
     * @param context
     * @param attrs
     */
    init {
        // TODO Auto-generated constructor stub
        mPaint = Paint()
        mPaint.setStyle(Paint.Style.STROKE)
        mPaint.setColor(Color.BLUE)
    }
}