package com.example.storyappsubmission.view.register

import android.animation.ObjectAnimator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.storyappsubmission.ViewModelFactory
import com.example.storyappsubmission.databinding.ActivitySignUpBinding
import com.example.storyappsubmission.view.Result
import com.example.storyappsubmission.view.main.MainViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
        setupViewModel()
    }

    private fun setupViewModel(){
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -50f, 50f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE //agar animasi bergerak loop
        }.start()
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

    private fun setupAction() {
        val name = binding.nameEditText.text
        val email = binding.emailEditText.text
        val password = binding.passwordEditText.text
        binding.signupButton.setOnClickListener {
            mainViewModel.userRegister(
                name.toString(),
                email.toString(),
                password.toString()
            ).observe(this){result->
                when (result) {
                    is Result.Loading ->{
                        isLoading()
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val response = result.data
                        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                    }

                    is Result.Error -> {
                        val errormessage = result.error
                        Toast.makeText(this, errormessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun isLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }
}