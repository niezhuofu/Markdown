package com.nzf.markdown.adapter

import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import android.util.SparseArray
import android.view.View

/**
 * Created by niezhuofu on 17-11-21.
 */
open class BaseSingleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var mView: SparseArray<View>? = null

    init {
        mView = SparseArray()
    }

    fun getItemView(): View {
        return itemView
    }

    fun getView(@IdRes viewId: Int): View {
        var view: View? = mView!!.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            mView!!.put(viewId, view)
        }
        return view!!
    }


}