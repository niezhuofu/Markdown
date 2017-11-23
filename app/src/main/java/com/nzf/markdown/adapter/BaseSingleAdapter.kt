package com.nzf.markdown.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by niezhuofu on 17-11-21.
 */
abstract class BaseSingleAdapter<T, VH : RecyclerView.ViewHolder>() : RecyclerView.Adapter<VH>() {

    constructor(mContext: Context?, mLayoutRes: Int?, mList: List<T>?) : this() {
        this.mContext = mContext
        this.mLayoutRes = mLayoutRes
        this.mList = mList
    }

    protected var mContext: Context? = null
    protected var mLayoutRes: Int? = null
    protected var mList: List<T>? = null

    fun notifyDataListChange(mList: List<T>?) {
        this.mList = mList
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        convert(holder, mList!!.get(position))
    }

    abstract fun convert(holder: VH, data: T)

    override fun getItemCount(): Int {
        if (mList != null) {
            return 0
        }
        return mList!!.size
    }

}