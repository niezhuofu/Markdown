package com.chad.library.adapter.base.base

import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * Created by joseph on 2017/10/19.
 */
open class BaseStateFragment : Fragment() {
   private var savedState : Bundle? = null

   companion object {
       private val SAVE_KEY = "SAVE_KEY_131231231239"
   }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!restoreStateFromArguments()) {
            onFirstLaunched()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 保存状态
        saveStateToArguments()
    }

    /**
     * On first launched.首次启动初始化，第二次如果没有销毁的话，直接在onRestoreState恢复数据就行了
     */
    protected open fun onFirstLaunched() {

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        // 保存状态
        saveStateToArguments()
    }

    ////////////////////
    // Don't Touch !!
    ////////////////////

    private fun saveStateToArguments() {
        if (view != null)
            savedState = saveState()
        if (savedState != null) {
            val b = arguments
            b?.putBundle(SAVE_KEY, savedState)

        }
    }

    private fun restoreStateFromArguments(): Boolean {
        val b = arguments ?: return false
        savedState = b.getBundle(SAVE_KEY)
        if (savedState == null) {
            return false
        }
        restoreState()
        return true
    }

    private fun restoreState() {
        if (savedState != null) {
            onRestoreState(savedState!!)
        }
    }


    private fun saveState(): Bundle {
        val state = Bundle()
        // For Example
        //state.putString("text", tv1.getText().toString());
        onSaveState(state)
        return state
    }

    /**
     * 子类重写，用于恢复状态保存，不用空判断
     * On restore state.
     *
     * @param savedInstanceState the saved instance state
     */
    protected open fun onRestoreState(savedInstanceState: Bundle) {
        // For Example
    }

    protected open fun onSaveState(outState: Bundle) {

    }

}