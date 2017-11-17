package com.nzf.markdown.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.RelativeLayout
import java.util.*

/**
 * Created by joseph on 2017/11/16.
 */
class GLayout : RelativeLayout{
    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        mMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                mMargin.toFloat(), resources.displayMetrics).toInt()
        mPadding = min(paddingLeft, paddingTop, paddingRight,
                paddingBottom)
        mGestureDetector = GestureDetector(context, MyGestureDetector())
    }

    private val mColumn = 5

    private var mGame2048Items: Array<GItem?>? = null

    private var mMargin = 10
    private var mPadding: Int
    private var mGestureDetector: GestureDetector

    // 用于确认是否需要生成一个新的值
    private var isMergeHappen = true
    private var isMoveHappen = true

    private var mScore: Int = 0

    interface OnGListener {
        fun onScoreChange(score: Int)

        fun onGameOver()
    }

    private var listener: OnGListener? = null

    fun setOnGListener(listener: OnGListener) {
        this.listener = listener
    }

    private enum class ACTION {
        LEFT, RIGHT, UP, DOWM
    }

    private fun action(action: ACTION) {
        // 行|列
        for (i in 0 until mColumn) {
            val row = ArrayList<GItem>()
            // 行|列
            //记录不为0的数字
            for (j in 0 until mColumn) {
                // 得到下标
                val index = getIndexByAction(action, i, j)

                val item = mGame2048Items!![index]
                // 记录不为0的数字
                if (item!!.getNumber() !== 0) {
                    row.add(item!!)
                }
            }

            //判断是否发生移动
            run {
                var j = 0
                while (j < mColumn && j < row.size) {
                    val index = getIndexByAction(action, i, j)
                    val item = mGame2048Items!![index]

                    if (item!!.getNumber() !== row[j].getNumber()) {
                        isMoveHappen = true
                    }
                    j++
                }
            }

            // 合并相同的
            mergeItem(row)

            // 设置合并后的值
            for (j in 0 until mColumn) {
                val index = getIndexByAction(action, i, j)
                if (row.size > j) {
                    mGame2048Items!![index]!!.setNumber(row[j].getNumber())
                } else {
                    mGame2048Items!![index]!!.setNumber(0)
                }
            }

        }
        //生成数字
        generateNum()

    }

    private fun getIndexByAction(action: ACTION, i: Int, j: Int): Int {
        var index  = when (action) {
            GLayout.ACTION.LEFT -> i * mColumn + j
            GLayout.ACTION.RIGHT -> i * mColumn + mColumn - j - 1
            GLayout.ACTION.UP -> i + j * mColumn
            GLayout.ACTION.DOWM -> i + (mColumn - 1 - j) * mColumn
        }
        return index
    }


    private fun mergeItem(row: List<GItem>) {
        if (row.size < 2)
            return

        for (j in 0 until row.size - 1) {
            val item1 = row[j]
            val item2 = row[j + 1]

            if (item1.getNumber() === item2.getNumber()) {
                isMergeHappen = true

                val `val` = item1.getNumber() + item2.getNumber()
                item1.setNumber(`val`)

                // 加分
                mScore += `val`
                if (listener != null) {
                    listener!!.onScoreChange(mScore)
                }

                // 向前移动
                for (k in j + 1 until row.size - 1) {
                    row[k].setNumber(row[k + 1].getNumber())
                }

                row[row.size - 1].setNumber(0)
                return
            }
        }
    }

    private fun min(vararg params: Int): Int {
        var min = params[0]
        for (param in params) {
            if (min > param) {
                min = param
            }
        }
        return min
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        mGestureDetector.onTouchEvent(event)
        return true
    }


    private var once: Boolean = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 获得正方形的边长
        val length = Math.min(measuredHeight, measuredWidth)
        // 获得Item的宽度
        val childWidth = (length - mPadding * 2 - mMargin * (mColumn - 1)) / mColumn

        if (!once) {
            if (mGame2048Items == null) {
                mGame2048Items = arrayOfNulls<GItem>(mColumn * mColumn)
            }
            // 放置Item
            for (i in mGame2048Items!!.indices) {
                val item = GItem(context)

                mGame2048Items!![i] = item
                item.setId(i + 1)
                val lp = RelativeLayout.LayoutParams(childWidth,
                        childWidth)
                // 设置横向边距,不是最后一列
                if ((i + 1) % mColumn != 0) {
                    lp.rightMargin = mMargin
                }
                // 如果不是第一列
                if (i % mColumn != 0) {
                    lp.addRule(RelativeLayout.RIGHT_OF, //
                            mGame2048Items!![i - 1]!!.getId())
                }
                // 如果不是第一行，//设置纵向边距，非最后一行
                if (i + 1 > mColumn) {
                    lp.topMargin = mMargin
                    lp.addRule(RelativeLayout.BELOW, //
                            mGame2048Items!![i - mColumn]!!.getId())
                }
                addView(item, lp)
            }
            generateNum()
        }
        once = true

        setMeasuredDimension(length, length)
    }

    private fun isFull(): Boolean {
        // 检测是否所有位置都有数字
        for (i in mGame2048Items!!.indices) {
            if (mGame2048Items!![i]!!.getNumber() === 0) {
                return false
            }
        }
        return true
    }


    private fun checkOver(): Boolean {
        // 检测是否所有位置都有数字
        if (!isFull()) {
            return false
        }

        for (i in 0 until mColumn) {
            for (j in 0 until mColumn) {

                val index = i * mColumn + j

                // 当前的Item
                val item = mGame2048Items!![index]
                // 右边
                if ((index + 1) % mColumn != 0) {
                    Log.e("TAG", "RIGHT")
                    // 右边的Item
                    val itemRight = mGame2048Items!![index + 1]
                    if (item!!.getNumber() === itemRight!!.getNumber())
                        return false
                }
                // 下边
                if (index + mColumn < mColumn * mColumn) {
                    Log.e("TAG", "DOWN")
                    val itemBottom = mGame2048Items!![index + mColumn]
                    if (item!!.getNumber() === itemBottom!!.getNumber())
                        return false
                }
                // 左边
                if (index % mColumn != 0) {
                    Log.e("TAG", "LEFT")
                    val itemLeft = mGame2048Items!![index - 1]
                    if (itemLeft!!.getNumber() === item!!.getNumber())
                        return false
                }
                // 上边
                if (index + 1 > mColumn) {
                    Log.e("TAG", "UP")
                    val itemTop = mGame2048Items!![index - mColumn]
                    if (item!!.getNumber() === itemTop!!.getNumber())
                        return false
                }

            }

        }
        return true
    }


    fun generateNum() {

        if (checkOver()) {
            Log.e("TAG", "GAME OVER")
            if (listener != null) {
                listener!!.onGameOver()
            }
            return
        }

        if (!isFull()) {
            if (isMoveHappen || isMergeHappen) {
                val random = Random()
                var next = random.nextInt(16)
                var item = mGame2048Items!![next]
                
                while (item!!.getNumber() !== 0) {
                    next = random.nextInt(16)
                    item = mGame2048Items!![next]
                }

                item!!.setNumber(if (Math.random() > 0.75) 4 else 2)

                isMoveHappen = false
                isMergeHappen = isMoveHappen
            }

        }
    }

    fun restart() {
        for (item in mGame2048Items!!) {
            item!!.setNumber(0)
        }
        mScore = 0
        if (listener != null) {
            listener!!.onScoreChange(mScore)
        }
        isMergeHappen = true
        isMoveHappen = isMergeHappen
        generateNum()
    }

    internal inner class MyGestureDetector : GestureDetector.SimpleOnGestureListener() {

        val FLING_MIN_DISTANCE = 50

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float,
                             velocityY: Float): Boolean {
            val x = e2.x - e1.x
            val y = e2.y - e1.y

            if (x > FLING_MIN_DISTANCE && Math.abs(velocityX) > Math.abs(velocityY)) {
                action(ACTION.RIGHT)
            } else if (x < -FLING_MIN_DISTANCE && Math.abs(velocityX) > Math.abs(velocityY)) {
                action(ACTION.LEFT)
            } else if (y > FLING_MIN_DISTANCE && Math.abs(velocityX) < Math.abs(velocityY)) {
                action(ACTION.DOWM)
            } else if (y < -FLING_MIN_DISTANCE && Math.abs(velocityX) < Math.abs(velocityY)) {
                action(ACTION.UP)
            }
            return true
        }
    }


}