package com.nzf.markdown.ui.fragment

import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.nzf.markdown.R
import com.nzf.markdown.view.WebMarkView

/**
 * Created by joseph on 2017/11/14.
 */
class BigViewerFragment : DialogFragment(){
    var image_path : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Light_NoTitleBar_Fullscreen)
        image_path = arguments.getString(WebMarkView.IMG_PATH)

        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setWindowAnimations(R.style.dialog_exit_traslate_down)
        val view : View?  = inflater!!.inflate(R.layout.fragment_big_viewer,container,false)

        val image = view!!.findViewById<ImageView>(R.id.iv_img)
        Glide.with(this).load(image_path).into(image)

        view.findViewById<ImageView>(R.id.iv_close).setOnClickListener({
            dismiss()
        })
        return view
    }

}