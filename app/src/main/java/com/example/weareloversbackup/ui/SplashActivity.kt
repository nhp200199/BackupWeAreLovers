package com.example.weareloversbackup.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weareloversbackup.R
import com.example.weareloversbackup.data.constant.PREF_YOUR_NAME
import com.example.weareloversbackup.databinding.ActivitySplashBinding
import com.example.weareloversbackup.ui.a.IniActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

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