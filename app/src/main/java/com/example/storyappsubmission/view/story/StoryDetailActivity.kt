package com.example.storyappsubmission.view.story

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.storyappsubmission.R
import com.example.storyappsubmission.ViewModelFactory
import com.example.storyappsubmission.databinding.ActivityStoryDetailBinding
import com.example.storyappsubmission.view.DateConverter
import com.example.storyappsubmission.view.Result
import com.example.storyappsubmission.view.main.MainViewModel

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_detail)

        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 33) {
            val id = intent.getStringExtra("id")
            if (id != null) {
                loadDetail(mainViewModel, id)
            }
        } else {
            val id = intent.getStringExtra("id") as String
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                loadDetail(mainViewModel, id)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDetailData(data: Story) {
        binding.tvName.text = data.name
        binding.tvDesc.text = data.description
        binding.date.text = data.createdAt

        if (data.createdAt != null) {
            binding.date.text = DateConverter(data.createdAt)
        }
        Glide.with(this)
            .load(data.photoUrl)
            .into(binding.ivPhotoDetail)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadDetail(mainViewModel: MainViewModel, id: String) {
        val token = getToken()
        if (token != null) {
            mainViewModel.getStoryDetail("Bearer $token", id)
                .observe(this@StoryDetailActivity) { detailData ->
                    if (detailData != null) {
                        when (detailData) {
                            is Result.Success -> {
                                val data = detailData.data.story
                                if (data != null) {
                                    setDetailData(data)
                                }
                            }

                            is Result.Error -> {
                                Toast.makeText(this, detailData.error, Toast.LENGTH_SHORT).show()
                            }

                            else -> {}
                        }
                    }
                }
        }
    }

    private fun getToken(): String? {
        return mainViewModel.getPreference(this).value
    }
}