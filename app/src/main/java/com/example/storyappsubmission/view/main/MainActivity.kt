package com.example.storyappsubmission.view.main

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyappsubmission.R
import com.example.storyappsubmission.ViewModelFactory
import com.example.storyappsubmission.databinding.ActivityMainBinding
import com.example.storyappsubmission.view.Result
import com.example.storyappsubmission.view.addstory.AddStory
import com.example.storyappsubmission.view.login.LoginActivity
import com.example.storyappsubmission.view.story.ListStoryItem
import com.example.storyappsubmission.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private val  mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

        setupView()
        loadAllStories(mainViewModel)
        binding.fabAddStory.setOnClickListener{
            val intent = Intent(this@MainActivity, AddStory::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadAllStories(mainViewModel: MainViewModel){
        val token = getToken()
        if (token != null){
            mainViewModel.getAllStories("Bearer $token").observe(this){ story ->
                when(story){
                    is Result.Success -> {
                        story.data.listStory?.let { data ->
                            val mappedData = data.map { result ->
                                ListStoryItem(
                                    result?.photoUrl,
                                    result?.createdAt,
                                    result?.name,
                                    result?.description,
                                    result?.lon,
                                    result?.id,
                                    result?.lat
                                )
                            }
                            binding.rvStory.adapter = MainAdapter(mappedData)
                        }
                    }

                    is Result.Error ->{
                        Toast.makeText(this, story.error, Toast.LENGTH_SHORT)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun getToken(): String? {
        return mainViewModel.getPreference(this).value
    }

//    fun setupViewModel(){
//        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
//        mainViewModel.isLogin().observe(this){
//            if (!it){
//                startActivity(Intent(this, WelcomeActivity::class.java))
//                finish()
//            }
//        }
//    }

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
        supportActionBar?.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> {
                mainViewModel.logout(this)
                val loginActivity = Intent(this@MainActivity, WelcomeActivity::class.java)
                startActivity(loginActivity)
                finish()
            }
            R.id.setting ->{
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
        return super.onOptionsItemSelected(item)
    }

//    private fun setupAction() {
//        binding.logoutButton.setOnClickListener {
//            viewModel.logout()
//        }
//    }
}