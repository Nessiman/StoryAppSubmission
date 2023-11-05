package com.example.storyappsubmission.view.main

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyappsubmission.R
import com.example.storyappsubmission.ViewModelFactory
import com.example.storyappsubmission.databinding.ActivityMainBinding
import com.example.storyappsubmission.maps.MapsActivity
import com.example.storyappsubmission.view.addstory.AddStory
import com.example.storyappsubmission.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private val  mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainAdapter = MainAdapter()
        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
        loadAllStories(this@MainActivity)

        setupView()
        binding.fabAddStory.setOnClickListener{
            val intent = Intent(this@MainActivity, AddStory::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadAllStories(context: Context){
        binding.rvStory.adapter = mainAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                mainAdapter.retry()
            }
        )
        mainViewModel.getAllStories(context).observe(this@MainActivity) {
            mainAdapter.submitData(lifecycle, it)

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
            R.id.map -> {
                startActivity(Intent(this@MainActivity, MapsActivity::class.java))
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