package com.example.storyappsubmission.view.login

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import com.example.storyappsubmission.ViewModelFactory
import com.example.storyappsubmission.databinding.ActivityLoginBinding
import com.example.storyappsubmission.view.main.MainActivity
import com.example.storyappsubmission.view.Result
import com.example.storyappsubmission.view.main.MainViewModel


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
   // private lateinit var mainViewModel: MainViewModel

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setupViewModel()
        //setupAction()
        setupView()
        playAnimation()
        isLogin(this)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val userPassword = binding.passwordEditText.text.toString()

            if (email.isBlank()) {
                binding.emailEditText.requestFocus()
                binding.emailEditText.error = "Insert Email"
            } else if (userPassword.isBlank()) {
                binding.passwordEditText.requestFocus()
                binding.passwordEditText.error = "insert password"
            } else {
                userLogin(email, userPassword)
            }
        }
    }

    private fun userLogin(email: String, password: String) {
        mainViewModel.loginNewUser(email, password).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading ->{
                        isLoading()
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility =View.GONE
                        val data = result.data
                        Toast.makeText(this@LoginActivity, data.message, Toast.LENGTH_SHORT).show()
                        if (data.loginResult?.token != null) {
                            mainViewModel.setPreference(data.loginResult.token, this)
                        }
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -50f, 50f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE //agar animasi bergerak loop
        }.start()
    }

    private fun setupView() {
        @Suppress("DEPRECATION") if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun isLogin(context: Context) {
        mainViewModel.getPreference(context).observe(this) { token ->
            if (token?.isEmpty() == false) {
                val mainActivity = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(mainActivity)
            }
        }
    }
    private fun isLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }
}


//            viewModel.saveSession(UserModel(email, "sample_token"))
//            AlertDialog.Builder(this).apply {//pesan ke pengguna jika berhasil login
//                setTitle("Yeah!")
//                setMessage("Anda berhasil login.")
//                setPositiveButton("Lanjut") { _, _ ->
//                    val intent = Intent(context, MainActivity::class.java) //intent untuk ke main activity
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                    startActivity(intent)
//                    finish()
//                }
//                create()
//                show()
//            }
