package com.example.storyappsubmission.view

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyappsubmission.data.database.StoryDatabase
import com.example.storyappsubmission.data.database.StoryRemoteMediator
import com.example.storyappsubmission.data.network.ApiService
import com.example.storyappsubmission.data.pref.UserPreference
import com.example.storyappsubmission.view.addstory.AddStoryResponse
import com.example.storyappsubmission.view.login.data.LoginData
import com.example.storyappsubmission.view.login.data.LoginResponse
import com.example.storyappsubmission.view.main.StoryPagingSource
import com.example.storyappsubmission.view.register.data.RegisterData
import com.example.storyappsubmission.view.register.data.RegisterResponse
import com.example.storyappsubmission.view.story.DetailStoryResponse
import com.example.storyappsubmission.view.story.ListStoryItem
import com.example.storyappsubmission.view.story.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.HttpException

class MainRepository private constructor(private val apiService: ApiService, private val storyDatabase: StoryDatabase){
    fun login(email: String, password: String) : LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.login(LoginData(email, password))
            emit(Result.Success(result))
        }catch(e: Exception) {
            Log.d("UserRepositoy", "Login : ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun register(name: String, email : String, password: String): Result<RegisterResponse>{
        return try {
            val response = apiService.register(RegisterData(name, email, password))
            Result.Success(response)
        }catch (e : HttpException){
            val error = e.response()?.errorBody()?.string()
            val jsonObject = JSONObject(error!!)
            val errorMessage = jsonObject.getString("message")
            Result.Error(errorMessage)
        }catch (e : Exception){
            Result.Error(e.message.toString())
        }
    }

    fun savePreference(token: String, context: Context){
        val userPreference = UserPreference(context)
        return userPreference.saveUser(token)
    }

    fun getPreference(context: Context): String?{
        val userPreference = UserPreference(context)
        return userPreference.getUser()
    }

//    fun getStory(token: String): LiveData<Result<StoryResponse>> = liveData {
//        emit(Result.Loading)
//        try {
//            val response = apiService.getStories(token)
//            emit(Result.Success(response))
//        }catch (e: Exception){
//            emit(Result.Error(e.message.toString()))
//        }
//    }

    fun getStoruDetail(token: String, id:String): LiveData<Result<DetailStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailStory(token, id)
            emit(Result.Success(response))
        }catch (e: Exception){
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun addNewStory(
        token: String,
        image: MultipartBody.Part,
        desc: RequestBody
    ): Result<AddStoryResponse>{
        return try {
            val response = apiService.addStory(token, image, desc)
            Result.Success(response)
        }catch (e: HttpException){
            val error = e.response()?.errorBody()?.string()
            val jsonObject = JSONObject(error!!)
            val errorMessage = jsonObject.getString("message")
            Result.Error(errorMessage)
        }catch (e:Exception){
            Result.Error(e.message.toString())
        }
    }

    fun logout(context: Context) {
        val userPreference = UserPreference(context)
        userPreference.clearUser(context)
    }

    fun getStoryWithLocation(token: String): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoriesWithLocation(token, 1)
            emit(Result.Success(response))
        }catch (e: Exception){
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStory(context: Context): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, context),
            pagingSourceFactory = {
                storyDatabase.storyDao().getALlStory()
            }
        ).liveData
    }

    companion object{
        @Volatile
        private var INSTANCE: MainRepository? = null

        fun getInstance(
            apiservice : ApiService,
            storyDatabase: StoryDatabase): MainRepository = INSTANCE?: synchronized(this) {
            INSTANCE ?: MainRepository(apiservice, storyDatabase)
        }.also { INSTANCE = it }

    }
}