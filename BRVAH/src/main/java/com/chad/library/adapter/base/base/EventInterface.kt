package com.chad.library.adapter.base.base

/**
 * Created by joseph on 2017/10/19.
 */

interface EventInterface {

    /**
     * 绑定
     */
    fun registerEvent()

    /**
     * 解绑
     */
    fun unregisterEvent()

    /**
     * 根据需要重写，如果返回True，这代表该type的Event你是要接受的 会回调
     * Has need event boolean.
     *
     * @param type the type
     * @return the boolean
     */
    fun hasNeedEvent(type: Int): Boolean

    /**
     * 回调
     *
     * @param e
     */
    fun onEventMainThread(e: RxEvent)

}
