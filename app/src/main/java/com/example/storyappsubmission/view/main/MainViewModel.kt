package com.example.storyappsubmission.view.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyappsubmission.view.MainRepository
import com.example.storyappsubmission.view.Result
import com.example.storyappsubmission.view.register.data.RegisterResponse
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val liveData = MutableLiveData<Result<RegisterResponse>>()
    private val token = MutableLiveData<String?>()

    fun userRegister(name: String, email: String, password: String) : LiveData<Result<RegisterResponse>>{
        viewModelScope.launch {
            val result = mainRepository.register(name, email, password)
            liveData.value = result
        }
        return liveData
    }

    fun loginNewUser(email: String, password: String) = mainRepository.login(email, password)

    fun setPreference(token: String, context: Context) = mainRepository.savePreference(token, context)

    fun getPreference(context: Context): LiveData<String?>{
        val userToken = mainRepository.getPreference(context)
        token.value = userToken
        return token
    }

    fun getAllStories(token: String) = mainRepository.getStory(token)

    fun getStoryDetail(token: String, id: String) = mainRepository.getStoruDetail(token, id)

    fun logout(context: Context) {
        mainRepository.logout(context)
    }
}