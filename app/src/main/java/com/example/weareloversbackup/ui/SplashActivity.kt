package com.example.weareloversbackup.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import com.example.weareloversbackup.R
import com.example.weareloversbackup.coupleInstantiation.ui.IniActivity
import com.example.weareloversbackup.data.constant.PREF_YOUR_NAME
import com.example.weareloversbackup.databinding.ActivitySplashBinding
import com.example.weareloversbackup.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun getClassTag(): String {
        return this.javaClass.simpleName
    }

    override fun getViewBindingClass(inflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(inflater)
    }

    override fun setupView() {
    }

    override fun setViewListener() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade)
        binding.tvWelcome.animation = animation
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        Handler(mainLooper).postDelayed({
            val intent = if (sharedPreferences.getString(PREF_YOUR_NAME, "")!!.isEmpty()) {
                Intent(applicationContext, IniActivity::class.java)
            } else {
                Intent(applicationContext, MainActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, DELAY_SPLASH)
    }

    companion object {
        const val DELAY_SPLASH = 3000L
    }
}