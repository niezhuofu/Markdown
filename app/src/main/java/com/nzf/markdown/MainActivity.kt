package com.nzf.markdown

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.chad.library.adapter.base.app.MyApplication
import com.chad.library.adapter.base.utils.FileUtils
import com.nzf.markdown.view.MaterialMenuDrawable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.localhost){
            if(item.itemId == currentMenuId){
                return false
            }
            currentMenuId = item.itemId
            dl_main_body.closeDrawer(GravityCompat.START)
            return true
        }

        if(onOptionsItemSelected(item)){
            dl_main_body.closeDrawer(GravityCompat.START)
        }
        return false
    }




    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.localhost,

            R.id.other,

            R.id.menu_helper,

            R.id.menu_update,

            R.id.menu_about ->
                Toast.makeText(this@MainActivity,"正在开发中...",Toast.LENGTH_SHORT).show()
        }
        return true

    }


    var isOpen: Boolean = false
    var materialMenu: MaterialMenuDrawable? = null
    var exitTime: Long = 0

    var currentMenuId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(tb_main_title)
        initView()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun initView() {
        initMaterialMenu()

        ll_main_setting.setNavigationItemSelectedListener(this@MainActivity)
        ll_main_setting.setCheckedItem(R.id.localhost)

        val fileUtils = MyApplication().getFileUtils()
        fileUtils.showFileDir(fileUtils.ROOT_PATH!!.path)
    }


    private fun showAddDailog(mContext: Context) {
        val editText = EditText(mContext)
        AlertDialog.Builder(this)
                .setTitle("添加文件夹")
                .setView(editText)
                .setPositiveButton("确认", DialogInterface.OnClickListener {
                    _, _ ->
                    if (TextUtils.isEmpty(editText.text.toString().trim())) {
                        Toast.makeText(this, "文件夹名不能为空。", Toast.LENGTH_SHORT).show()
                        return@OnClickListener
                    }
                    val dirName: String = editText.text.toString().trim()

                    Log.i("Main : = ", MyApplication().getAppContext().toString())
                    val file: FileUtils = MyApplication().getFileUtils()
                    val dirSuccess: Boolean = file.newMDDir(dirName)
                    if (dirSuccess) {
                        Toast.makeText(this, "文件夹创建成功", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "文件夹创建失败", Toast.LENGTH_SHORT).show()
                    }
                })
                .setNegativeButton("取消", null)
                .show()

    }

    private fun initMaterialMenu() {
        //初始化侧边栏按钮
        materialMenu = MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN)

        tb_main_title.navigationIcon = materialMenu
        tb_main_title.showOverflowMenu()
        tb_main_title.inflateMenu(R.menu.menu_main)
        tb_main_title.setNavigationOnClickListener {
            if (isOpen) {
                dl_main_body.closeDrawer(ll_main_setting)
                Log.i("main", "...........close!")
            } else {
                dl_main_body.openDrawer(ll_main_setting)
                Log.i("main", "...........open!")
            }

        }

        //添加按钮初始化
        tb_main_title.setOnMenuItemClickListener {
            item: MenuItem? ->
            when (item?.itemId) {
                R.id.item_main_add ->
                    Toast.makeText(this, "add", Toast.LENGTH_LONG).show()
                R.id.item_main_addtype ->
                    showAddDailog(this)
            }
            return@setOnMenuItemClickListener true
        }

        @Suppress("DEPRECATION")
        dl_main_body.setDrawerListener(object : DrawerLayout.SimpleDrawerListener() {

            override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {
                val slideoff: Float
                if (isOpen) {
                    slideoff = 2 - slideOffset
                } else {
                    slideoff = slideOffset
                }
                materialMenu?.setTransformationOffset(
                        MaterialMenuDrawable.AnimationState.BURGER_ARROW,
                        slideoff
                )

            }

            override fun onDrawerClosed(drawerView: View?) {
                isOpen = false
            }

            override fun onDrawerOpened(drawerView: View?) {
                isOpen = true
            }

        })

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show()
                exitTime = System.currentTimeMillis()
            } else {
                finish()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}