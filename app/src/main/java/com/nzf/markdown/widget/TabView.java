package com.nzf.markdown.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nzf.markdown.R;


/**
 * Created by joseph on 2017/10/19.
 */

public class TabView extends HorizontalScrollView{
    private LinearLayout mLayout;
    private LayoutInflater mInflater;

    public TabView(Context context) {
        this(context,null);
    }

    public TabView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setOverScrollMode(OVER_SCROLL_NEVER);
        this.setHorizontalScrollBarEnabled(false);

        mInflater = LayoutInflater.from(getContext());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayout = new LinearLayout(getContext());
        mLayout.setPadding(1, 0, 1, 0);
        mLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(mLayout, params);
    }


    public void addTab(String title, OnClickListener onClickListener) {
        View view = mInflater.inflate(R.layout.item_tab_text, mLayout, false);
        TextView textView = (TextView) view.findViewById(R.id.file_name);
        textView.setOnClickListener(onClickListener);
        textView.setText(title);
        textView.setTag(R.id.tag, mLayout.getChildCount());
        if (mLayout.getChildCount() <= 0) {
            //第一个就隐藏箭头
            view.findViewById(R.id.arrow).setVisibility(View.GONE);
        } else {
            //设置前一个的字体颜色
            TextView lastTitle = (TextView) mLayout.getChildAt(mLayout.getChildCount() - 1).findViewById(R.id.file_name);
            lastTitle.setTextColor(0x88ffffff);
        }
        mLayout.addView(view, mLayout.getChildCount());

        //滑到最右边
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                smoothScrollBy(1000,0);
            }
        },5);

    }


    public boolean removeTab() {
        int count = mLayout.getChildCount();
        if (count > 1) {
            //移除最后一个
            mLayout.removeViewAt(count - 1);
            View lastView = mLayout.getChildAt(mLayout.getChildCount() - 1);
            //设置最后一个的字体颜色为白色
            TextView lastTitle = (TextView) lastView.findViewById(R.id.file_name);
            lastTitle.setTextColor(0xffffffff);
            return true;
        }

        if (mLayout.getChildCount() == 1) {
            View lastView = mLayout.getChildAt(mLayout.getChildCount() - 1);
            lastView.findViewById(R.id.arrow).setVisibility(View.GONE);
        }
        return false;
    }
}



//Error:Unable to find method 'com.android.build.gradle.api.BaseVariant.getOutputs()Ljava/util/List;'.
//        Possible causes for this unexpected error include:<ul><li>Gradle's dependency cache may be corrupt (this sometimes occurs after a network connection timeout.)
//<a href="syncProject">Re-download dependencies and sync project (requires network)</a></li><li>The state of a Gradle build process (daemon) may be corrupt. Stopping all Gradle daemons may solve this problem.
//<a href="stopGradleDaemons">Stop Gradle build processes (requires restart)</a></li><li>Your project may be using a third-party plugin which is not compatible with the other plugins in the project or the version of Gradle requested by the project.</li></ul>In the case of corrupt Gradle processes, you can also try closing the IDE and then killing all Java processes.

