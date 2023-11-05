package com.example.storyappsubmission.maps

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyappsubmission.view.MainRepository

class MapsViewModel(private val mainRepository: MainRepository): ViewModel() {
    private val token = MutableLiveData<String?>()

    fun getStoryWithLocation(token: String) = mainRepository.getStoryWithLocation(token)

    fun getPreference(context: Context): LiveData<String?>{
        val userToken = mainRepository.getPreference(context)
        token.value = userToken
        return token
    }
}