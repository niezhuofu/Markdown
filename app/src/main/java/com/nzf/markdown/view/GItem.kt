package com.nzf.markdown.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

/**
 * Created by joseph on 2017/11/16.
 */
class GItem : View{
    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    private var mNumber: Int = 0
    private var mNumberVal: String = ""
    private val mPaint: Paint = Paint()
    private var number : Int = 0

    private var mBound: Rect? = null

    fun setNumber(number : Int){
        mNumber = number
        mNumberVal = mNumber.toString() + ""
        mPaint.textSize = 30.0f
        mBound = Rect()
        mPaint.getTextBounds(mNumberVal, 0, mNumberVal.length, mBound)
        invalidate()
    }

    fun getNumber(): Int = mNumber


    override fun onDraw(canvas: Canvas?) {
        val mBgColor : String = when (mNumber) {
            0 -> "#CCC0B3"
            2 -> "#EEE4DA"
            4 -> "#EDE0C8"
            8 -> "#F2B179"
            16 -> "#F49563"
            32 -> "#F5794D"
            64 -> "#F55D37"
            128 -> "#EEE863"
            256 -> "#EDB04D"
            512 -> "#ECB04D"
            1024 -> "#EB9437"
            2048 -> "#EA7821"
            else -> "#EA7821"
        }

        mPaint.color = Color.parseColor(mBgColor)
        mPaint.style = Paint.Style.FILL
        canvas!!.drawRect(0f, 0f, width.toFloat(), height.toFloat(), mPaint)

        if (mNumber != 0)
            drawText(canvas)
    }

    private fun drawText(canvas: Canvas?) {
        mPaint.color = Color.BLACK
        val x = ((width - mBound!!.width()) / 2).toFloat()
        val y = (height / 2 + mBound!!.height() / 2).toFloat()
        canvas!!.drawText(mNumberVal, x, y, mPaint)
    }

}