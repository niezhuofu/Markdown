package com.nzf.markdown.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.nzf.markdown.R;
import com.nzf.markdown.utils.FilesUtils;
import com.nzf.markdown.view.GLayout;
import com.nzf.markdown.web.ResultWebViewActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements GLayout.OnGListener{
    private GLayout gLayout;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        copyAssetsToDst(this,"","");


        tv = findViewById(R.id.tv);
        gLayout = findViewById(R.id.glayout);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                startActivity(new Intent(MainActivity.this, ResultWebViewActivity.class));
            }
        },1000);
    }

    private void initView(){
        String path = this.getExternalCacheDir().getPath();
        FilesUtils.Companion.getInstance().showAllMDDir(path);
    }


    private void copyAssetsToDst(Context context, String srcPath, String dstPath) {
        try {
            String fileNames[] = context.getAssets().list(srcPath);
            if (fileNames.length > 0) {
                File file = new File(Environment.getExternalStorageDirectory(), dstPath);
                if (!file.exists()) file.mkdirs();
                for (String fileName : fileNames) {
                    if (!srcPath.equals("")) { // assets 文件夹下的目录
                        copyAssetsToDst(context, srcPath + File.separator + fileName, dstPath + File.separator + fileName);
                    } else { // assets 文件夹
                        copyAssetsToDst(context, fileName, dstPath + File.separator + fileName);
                    }
                }
            } else {
                File outFile = new File(Environment.getExternalStorageDirectory(), dstPath);
                InputStream is = context.getAssets().open(srcPath);
                FileOutputStream fos = new FileOutputStream(outFile);
                byte[] buffer = new byte[1024];
                int byteCount;
                while ((byteCount = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.gao){
            startActivity(new Intent(MainActivity.this, ResultWebViewActivity.class));
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public void onScoreChange(int score) {
        tv.setText("score:" + score);
    }

    @Override
    public void onGameOver() {
        Toast.makeText(this,"GAME-OVER", Toast.LENGTH_SHORT).show();
    }
}
