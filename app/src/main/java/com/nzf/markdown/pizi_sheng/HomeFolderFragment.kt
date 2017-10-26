package com.nzf.markdown.pizi_sheng

import android.os.Bundle
import com.chad.library.adapter.base.base.BaseFragment

/**
 * Created by joseph on 2017/10/20.
 */
class HomeFolderFragment(override val layoutId: Int) : BaseFragment(){
    override fun onCreateAfter(savedInstanceState: Bundle) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun initData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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