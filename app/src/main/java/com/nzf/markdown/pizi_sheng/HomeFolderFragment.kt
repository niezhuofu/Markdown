package com.nzf.markdown.pizi_sheng

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nzf.markdown.R

/**
 * Created by joseph on 2017/10/20.
 */
class HomeFolderFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View? = View.inflate(activity, R.layout.fragment_home_folder,null)
        return view
    }
}