package com.example.storyappsubmission.data.pref

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.storyappsubmission.data.network.ApiService
import com.example.storyappsubmission.view.login.data.LoginResponse
import com.example.storyappsubmission.view.register.data.RegisterResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map



//val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
internal class UserPreference (context: Context){
    private val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun saveUser(userToken: String){
        val editUser = preference.edit()
        editUser.putString(TOKEN, userToken)
        editUser.apply()
    }

    fun getUser(): String? = preference.getString(TOKEN, "")

    fun clearUser(context: Context){
        val editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
    }

    companion object{
        private const val PREFERENCE_NAME = "user_preference"
        private const val TOKEN = "user_token"
    }

}