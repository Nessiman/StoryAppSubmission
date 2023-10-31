package com.example.storyappsubmission.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyappsubmission.data.pref.UserModel
import com.example.storyappsubmission.data.pref.UserPreference
import kotlinx.coroutines.launch

//class LoginViewModel(private val pref: UserPreference) : ViewModel() {
//
//    fun setToken(token: String, isLogin: Boolean){
//        viewModelScope.launch {
//            pref.setToken(token, isLogin)
//        }
//    }
//
//    fun getToken(): LiveData<String>{
//        return pref.getToken().asLiveData()
//    }
//
//    fun login(email: String, password: String) = pref.login(email, password)
//}