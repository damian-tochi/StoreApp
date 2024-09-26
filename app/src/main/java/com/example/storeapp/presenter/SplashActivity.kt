package com.example.storeapp.presenter

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.storeapp.databinding.ActivitySplash2Binding
import com.example.storeapp.config.SessionState



class SplashActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySplash2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplash2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initViews()

    }

    private fun initViews() {
        val animation = binding.img
        animation.playAnimation()
        animation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }
            override fun onAnimationEnd(animation: Animator) {
                login()
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }

        })
    }

    private fun login() {
        SessionState.instance.readValuesFromPreferences(this)
        val isPrevLogin = SessionState.instance.isLogin

        if (isPrevLogin) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, OnboardActivity::class.java)
            startActivity(intent)
            finish()
        }

    }


}