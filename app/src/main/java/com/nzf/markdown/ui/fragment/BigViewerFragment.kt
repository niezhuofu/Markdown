package com.nzf.markdown.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.nzf.markdown.R
import com.nzf.markdown.view.WebMarkView

/**
 * Created by joseph on 2017/11/14.
 */
class BigViewerFragment : DialogFragment() {
    private lateinit var imagePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Light_NoTitleBar_Fullscreen)
        imagePath = arguments?.getString(WebMarkView.IMG_PATH) ?: ""

        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setWindowAnimations(R.style.dialog_exit_traslate_down)
        val view: View? = inflater.inflate(R.layout.fragment_big_viewer, container, false)

        val image = view!!.findViewById<ImageView>(R.id.iv_img)

        Glide.with(this)
            .load(imagePath)
            .placeholder(R.mipmap.ic_launcher)
            .into(image)

        view.findViewById<ImageView>(R.id.iv_close).setOnClickListener {
            dismiss()
        }
        return view
    }

}