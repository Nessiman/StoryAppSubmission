package com.example.storyappsubmission.view.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import com.example.storyappsubmission.ViewModelFactory
import com.example.storyappsubmission.databinding.ActivityWelcomeBinding
import com.example.storyappsubmission.view.login.LoginActivity
import com.example.storyappsubmission.view.main.MainActivity
import com.example.storyappsubmission.view.main.MainViewModel
import com.example.storyappsubmission.view.register.SignUpActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
        isLogin(this)

    }

            private fun playAnimation() {
                ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -50f, 50f).apply {
                    duration = 6000
                    repeatCount = ObjectAnimator.INFINITE
                    repeatMode = ObjectAnimator.REVERSE //agar animasi bergerak loop
                }.start()

                val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)
                val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)
                val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)


                //supaya bisa berhalan bersama dan bergantian
                val together = AnimatorSet().apply {
                    playTogether(login, signup) //untuk memunculkan 2 Button dengan animasi bersamaan
                }

                AnimatorSet().apply {
            playSequentially(title, together)
            start()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun isLogin(context: Context){
        mainViewModel.getPreference(context).observe(this){token ->
            if (token?.isEmpty() == false){
                val mainActivity = Intent(this@WelcomeActivity, MainActivity::class.java)
                startActivity(mainActivity)
                finish()
            }
        }
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.signupButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}