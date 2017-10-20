package com.nzf.markdown

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.AccelerateInterpolator
import com.chad.library.adapter.base.utils.PermissionUtils
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity(), PermissionUtils.PermissionGrant {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initView()
    }

    fun initView() {

        val alpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0f)
        val objectAnimator1 = ObjectAnimator.ofPropertyValuesHolder(iv_splash_icon, alpha)
        val objectAnimator2 = ObjectAnimator.ofPropertyValuesHolder(tv_splash_text, alpha)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(objectAnimator1, objectAnimator2)
        animatorSet.interpolator = AccelerateInterpolator()
        animatorSet.duration = 10
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                PermissionUtils.requestMultiPermissions(this@SplashActivity, this@SplashActivity)
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        animatorSet.start()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        var allPermissionSUCCESS = true
        for (i in grantResults.indices) {
            if (grantResults[i] == -1) {
                allPermissionSUCCESS = false
            }
        }
        if (allPermissionSUCCESS) {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        } else {
            @Suppress("UNCHECKED_CAST")
            PermissionUtils.requestPermissionsResult(this@SplashActivity, requestCode, permissions as Array<String>, grantResults, this)
        }
    }

    override fun onPermissionGranted(requestCode: Int) {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }

}
