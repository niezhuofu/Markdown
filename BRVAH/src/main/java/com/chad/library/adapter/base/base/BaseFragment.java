package com.chad.library.adapter.base.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.BuildConfig;
import com.chad.library.adapter.base.app.MyApplication;
import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by joseph on 2017/10/27.
 */

public abstract class BaseFragment extends BaseStateFragment implements BaseViewInterface, EventInterface {
    protected Context mContext;
    protected View rootView;
    protected MyApplication application;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        application = (MyApplication) mContext.getApplicationContext();
        if (rootView == null) {
            rootView = View.inflate(getActivity(),getLayoutId(), null);
            if (rootView == null)
                throw new IllegalStateException(this.getClass().getSimpleName() + ":LayoutID找不到对应的布局");

        }
        ButterKnife.bind(this, rootView);
        registerEvent();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstFocused) {
            isFirstFocused = false;
            initData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //注销EventBus
        unregisterEvent();
        mContext = null;
        rootView = null;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (BuildConfig.DEBUG) {//Debug的时候检查内存泄露
            RefWatcher refWatcher = MyApplication.Companion.getRefWatcher(mContext);
            if (refWatcher != null) {
                refWatcher.watch(this);
            }
        }
    }

    @Override
    protected void onFirstLaunched() {
        super.onFirstLaunched();
        //包含菜单到所在Activity
        setHasOptionsMenu(hasMenu());
        onCreateAfter(null);
    }


    private boolean isFirstFocused = true;


    //用于接收事件
    private Subscription mSubscribe;

    @Override
    public void registerEvent() {
        //订阅
        mSubscribe = RxEventBus.getInstance().toObserverable()
                .filter(o -> o instanceof RxEvent)//只接受RxEvent
                .map(o -> (RxEvent) o)
                .filter(r -> hasNeedEvent(r.type))//只接受type = 1和type = 2的东西
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onEventMainThread);
    }

    @Override
    public void unregisterEvent() {
        if (mSubscribe != null) {
            mSubscribe.unsubscribe();
        }
    }

    /**
     * 根据需要重写，如果返回True，这代表该type的Event你是要接受的 会回调
     * Has need event boolean.
     *
     * @param type the type
     * @return the boolean
     */
    @Override
    public boolean hasNeedEvent(int type) {
        return type == RxEvent.TYPE_FINISH;
    }

    @Override
    public void onEventMainThread(RxEvent e) {
        if (e.type == RxEvent.TYPE_FINISH && e.o.length > 0) {
            //xxxx执行响应的操作
        }
    }


    public boolean hasMenu() {
        return false;
    }


    /**
     * 返回键，预留给所在activity调用
     * On back pressed boolean.
     *
     * @return the boolean
     */
    public boolean onBackPressed() {
        return false;
    }

    /**
     * 需要重写hasMenu() 返回True，才会创建菜单
     *
     * @param menu     the menu
     * @param inflater the inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * 需要重写hasMenu() 返回True，才会回调
     * On options item selected boolean.
     *
     * @param item the item
     * @return the boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    /**
     * Save Fragment's State here
     */
    @Override
    protected void onSaveState(Bundle outState) {
        super.onSaveState(outState);
    }

    /**
     * Restore Fragment's State here
     */
    @Override
    protected void onRestoreState(Bundle savedInstanceState) {
        super.onRestoreState(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return 0;
    }
}