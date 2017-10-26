package com.chad.library.adapter.base.base

import android.content.Context
import android.os.Bundle
import android.view.*
import com.chad.library.BuildConfig
import com.chad.library.adapter.base.app.MyApplication
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1

/**
 * Created by joseph on 2017/10/19.
 */

abstract class BaseFragment : BaseStateFragment(), BaseViewInterface, EventInterface {


    protected var mContext: Context? = null
    protected var rootView: View? = null
    protected var application: MyApplication? = null


    private var isFirstFocused = true


    //用于接收事件
    private var mSubscribe: Subscription? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = activity
        application = mContext!!.applicationContext as MyApplication
        if (rootView == null) {
            rootView = View.inflate(activity, layoutId, null)
            if (rootView == null)
                throw IllegalStateException(this.javaClass.simpleName + ":LayoutID找不到对应的布局")

        }
        registerEvent()
        return rootView
    }

    override fun onResume() {
        super.onResume()
        if (isFirstFocused) {
            isFirstFocused = false
            initData()
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        //注销EventBus
        unregisterEvent()
        mContext = null
        rootView = null


    }

    override fun onDestroy() {
        super.onDestroy()
        if (BuildConfig.DEBUG) {//Debug的时候检查内存泄露
            val refWatcher = MyApplication.getRefWatcher(mContext)
            if (refWatcher != null) {
                refWatcher!!.watch(this)
            }
        }
    }

    override fun onFirstLaunched() {
        super.onFirstLaunched()
        //包含菜单到所在Activity
        setHasOptionsMenu(hasMenu())
        onCreateAfter(null!!)
    }

    override fun registerEvent() {
        //订阅
        mSubscribe = RxEventBus.getInstance().toObserverable()
                .filter { it is RxEvent }//只接受RxEvent
                .map { it as RxEvent }
                .filter { r -> hasNeedEvent(r.type) }//只接受type = 1和type = 2的东西
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Action1<RxEvent> { this.onEventMainThread(it) })
    }

    override fun unregisterEvent() {
        if (mSubscribe != null) {
            mSubscribe!!.unsubscribe()
        }
    }

    /**
     * 根据需要重写，如果返回True，这代表该type的Event你是要接受的 会回调
     * Has need event boolean.
     *
     * @param type the type
     * @return the boolean
     */
    override fun hasNeedEvent(type: Int): Boolean = type == RxEvent.TYPE_FINISH

    override fun onEventMainThread(e: RxEvent) {
        if (e.type == RxEvent.TYPE_FINISH && e.o.size > 0) {
            //xxxx执行响应的操作
        }
    }


    fun hasMenu(): Boolean = false


    /**
     * 返回键，预留给所在activity调用
     * On back pressed boolean.
     *
     * @return the boolean
     */
    fun onBackPressed(): Boolean = false

    /**
     * 需要重写hasMenu() 返回True，才会创建菜单
     *
     * @param menu     the menu
     * @param inflater the inflater
     */
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * 需要重写hasMenu() 返回True，才会回调
     * On options item selected boolean.
     *
     * @param item the item
     * @return the boolean
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean = super.onOptionsItemSelected(item)


    override fun onSaveState(outState: Bundle) {
        super.onSaveState(outState)
    }


    override fun onRestoreState(savedInstanceState: Bundle) {
        super.onRestoreState(savedInstanceState)
    }
}
