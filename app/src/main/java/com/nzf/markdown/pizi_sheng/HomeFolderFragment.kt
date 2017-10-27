package com.nzf.markdown.pizi_sheng

import android.os.Bundle
import com.chad.library.adapter.base.base.BaseFragment
import com.nzf.markdown.R

/**
 * Created by joseph on 2017/10/20.
 */
class HomeFolderFragment : BaseFragment(){

    override fun getLayoutId(): Int = R.layout.fragment_home_folder

    override fun onCreateAfter(savedInstanceState: Bundle) {

    }


    override fun initData() {

    }


//    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val view : View? = View.inflate(activity, R.layout.fragment_home_folder,null)
//        return view
//    }

    fun sum(a:Int,b:Int) = a + b


    fun delete(path : Any) : String{
        if(path !is String){
            return ""
        }
        return  "${path.length}"
    }





}