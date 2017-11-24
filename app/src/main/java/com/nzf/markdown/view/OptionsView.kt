package com.nzf.markdown.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.nzf.markdown.R



/**
 * Created by joseph on 2017/11/15.
 */
class OptionsView : View,View.OnTouchListener {
    private var totalWidth : Int = 0
    private var unitWidth : Float= 0f
    private var isWhite : Boolean = true

    private val MAX_LINE: Int by lazy { 15 }
    private val linePaint by lazy { Paint() }

    private val whiteArray : ArrayList<Point> by lazy { ArrayList<Point>() }
    private val blackArray : ArrayList<Point> by lazy { ArrayList<Point>() }

    private var ratioPic = 3 * 1.0f / 4

    private var whitePic : Bitmap
    private var blackPic : Bitmap

    constructor(context : Context?):this(context,null)

    constructor(context: Context?,attrs : AttributeSet?):this(context,attrs,0)

    constructor(context: Context?,attrs: AttributeSet?,defStyle: Int):super(context,attrs,defStyle){
        setBackgroundResource(R.drawable.bg)
        linePaint.isDither = true
        linePaint.isAntiAlias = true
        linePaint.color = Color.GRAY
        linePaint.strokeWidth = 3f
        linePaint.style = Paint.Style.STROKE
        whitePic = BitmapFactory.decodeResource(resources,R.drawable.stone_w2)
        blackPic = BitmapFactory.decodeResource(resources,R.drawable.stone_b1)
        setOnTouchListener(this@OptionsView)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)

        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        var width = Math.min(widthSize,heightSize)

        if(widthMode != MeasureSpec.EXACTLY){
            width = heightSize
        }else if(heightMode != MeasureSpec.EXACTLY){
            width = widthSize
        }
        setMeasuredDimension(width,width)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        totalWidth = w
        unitWidth = w * 1f/ MAX_LINE
        val piceWidth : Int = (unitWidth * ratioPic).toInt()
        whitePic = Bitmap.createScaledBitmap(whitePic,piceWidth,piceWidth,false)
        blackPic = Bitmap.createScaledBitmap(blackPic,piceWidth,piceWidth,false)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
          if(event!!.action == MotionEvent.ACTION_DOWN){
              return true
          }

          if(event.action == MotionEvent.ACTION_MOVE){

          }

          if(event.action == MotionEvent.ACTION_UP){
              val x : Int = event.x.toInt()
              val y : Int = event.y.toInt()

              val point = getValidPoint(x,y)
              if(whiteArray.contains(point) || blackArray.contains(point)){
                  return false
              }

              if(isWhite){
                  whiteArray.add(point)
              }else{
                  blackArray.add(point)
              }
              invalidate()
              isWhite = !isWhite
              return true
          }
       return super.onTouchEvent(event)
    }

    private fun getValidPoint(x: Int, y: Int): Point {
        return Point((x / unitWidth).toInt(), (y / unitWidth).toInt())
    }


    override fun onDraw(canvas: Canvas?) {
        drawGrids(canvas)

        drawpice(canvas)


    }


    private fun drawGrids(canvas: Canvas?) {
         for(i in 0 .. MAX_LINE){
             val startX = unitWidth / 2
             val endX = totalWidth - unitWidth / 2
             val y = unitWidth / 2 + i * unitWidth
             canvas!!.drawLine(startX,y,endX,y,linePaint)
         }

        for(i in 0 .. MAX_LINE){
             val startY = unitWidth / 2
             val endY = totalWidth - unitWidth / 2
             val x = unitWidth / 2 + i * unitWidth
            canvas!!.drawLine(x,startY,x,endY,linePaint)
        }
    }

    private fun drawpice(canvas: Canvas?) {
        (0 until whiteArray.size)
                .map { whiteArray[it] }
                .forEach {
                    canvas!!.drawBitmap(whitePic, (it.x + (1 - ratioPic) / 2) * unitWidth,
                            (it.y + (1 - ratioPic) / 2) * unitWidth, null)
                }

        (0 until blackArray.size)
                .map { blackArray[it] }
                .forEach {
                    canvas!!.drawBitmap(blackPic, (it.x + (1 - ratioPic) / 2) * unitWidth,
                            (it.y + (1 - ratioPic) / 2) * unitWidth, null)
                }
     }

}