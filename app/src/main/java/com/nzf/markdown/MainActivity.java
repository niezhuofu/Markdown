package com.nzf.markdown;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nzf.markdown.utils.FilesUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        String path = this.getExternalCacheDir().getPath();
        FilesUtils.Companion.getInstance().showAllMDDir(path);
    }
}
