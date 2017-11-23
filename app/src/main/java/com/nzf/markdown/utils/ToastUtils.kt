package com.nzf.markdown.utils

import android.content.Context
import android.widget.Toast
import com.nzf.markdown.app.MDApplication

/**
 * Created by niezhuofu on 17-11-15.
 */
class ToastUtils {

    companion object {
        private var toast: Toast? = null

        private fun initToast(message: CharSequence, duration: Int): Toast? {
            if (toast == null) {
                toast = Toast.makeText(MDApplication.getContext(), message, duration)
            } else {
                toast!!.setText(message)
                toast!!.duration = duration
            }
            return toast
        }

        /**
         * 短时间显示Toast
         *
         * @param message
         */
        fun showShort(message: CharSequence) {
            initToast(message, Toast.LENGTH_SHORT)!!.show()
        }


        /**
         * 短时间显示Toast
         *
         * @param strResId
         */
        fun showShort(strResId: Int) {
            //		Toast.makeText(context, strResId, Toast.LENGTH_SHORT).show();
            initToast(MDApplication.getContext()!!.getResources().getText(strResId), Toast.LENGTH_SHORT)!!.show()
        }

        /**
         * 长时间显示Toast
         *
         * @param message
         */
        fun showLong(message: CharSequence) {
            initToast(message, Toast.LENGTH_LONG)!!.show()
        }

        /**
         * 长时间显示Toast
         *
         * @param strResId
         */
        fun showLong(strResId: Int) {
            initToast(MDApplication.getContext()!!.getResources().getText(strResId), Toast.LENGTH_LONG)!!.show()
        }

        /**
         * 自定义显示Toast时间
         *
         * @param message
         * @param duration
         */
        fun show(message: CharSequence, duration: Int) {
            initToast(message, duration)!!.show()
        }

        /**
         * 自定义显示Toast时间
         *
         * @param context
         * @param strResId
         * @param duration
         */
        fun show(context: Context, strResId: Int, duration: Int) {
            initToast(context.resources.getText(strResId), duration)!!.show()
        }
    }

}