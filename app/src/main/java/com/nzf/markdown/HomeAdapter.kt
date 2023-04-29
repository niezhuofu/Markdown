package com.nzf.markdown

import android.content.Context
import androidx.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nzf.markdown.adapter.BaseSingleAdapter
import com.nzf.markdown.adapter.BaseSingleViewHolder
import com.nzf.markdown.bean.MDFileBean

/**
 * Created by joseph on 2017/11/24.
 */
class HomeAdapter(mContext: Context?, mLayoutRes: Int?, mList: List<MDFileBean>?) :
        BaseSingleAdapter<MDFileBean, HomeAdapter.Companion.HomeFileHolder>(mContext, mLayoutRes, mList) {

    override fun convert(holder: HomeFileHolder, data: MDFileBean) {

        holder.setFileName(data.fileName!!).setFilePath(data.filePath!!)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFileHolder =
            HomeFileHolder.get(mContext,R.layout.activity_home_editor,parent)


    companion object {

        class HomeFileHolder(itemView: View) : BaseSingleViewHolder(itemView) {
            lateinit var filePath : String
            lateinit var fileName : String

           fun setFilePath(path : String): HomeFileHolder{
               filePath = path
               return this
           }

           fun setFileName(name : String): HomeFileHolder{
               fileName = name
               return this
           }

           companion object {
              fun get(mContext: Context?, @LayoutRes layoutRes: Int, parent: ViewGroup?): HomeFileHolder{
                  val view = LayoutInflater.from(mContext).inflate(layoutRes,parent,false)
                  return HomeFileHolder(view)
              }
           }

        }

    }

}