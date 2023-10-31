package com.example.storyappsubmission.view.addstory

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyappsubmission.view.MainRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import com.example.storyappsubmission.view.Result


class AddStoryViewModel(private val mainRepository: MainRepository): ViewModel() {
    private val liveData = MutableLiveData<Result<AddStoryResponse>>()
    private val token = MutableLiveData<String?>()

    fun addNewStory(
        token: String,
        image: MultipartBody.Part,
        desc: RequestBody
    ): LiveData<Result<AddStoryResponse>>{
        viewModelScope.launch {
            val result = mainRepository.addNewStory(token, image, desc)
            liveData.value = result
        }
        return liveData
    }

    fun setPreference(token: String, context: Context) = mainRepository.savePreference(token, context)

    fun getPreference(context: Context): LiveData<String?>{
        val userToken = mainRepository.getPreference(context)
        token.value = userToken
        return token
    }
}