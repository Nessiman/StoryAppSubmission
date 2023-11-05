package com.example.storyappsubmission.di

import android.content.Context
import com.example.storyappsubmission.data.database.StoryDatabase
import com.example.storyappsubmission.data.network.ApiConfig
import com.example.storyappsubmission.view.MainRepository

object ApiRetro {
    fun provideRepository(context: Context): MainRepository {
        val ServiceApi = ApiConfig.getApiService()
        val storyDatabase = StoryDatabase.getInstance(context)
        return MainRepository.getInstance(ServiceApi, storyDatabase)
    }
}
