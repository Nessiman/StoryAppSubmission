package com.example.storyappsubmission.data.network

import com.example.storyappsubmission.view.addstory.AddStoryResponse
import com.example.storyappsubmission.view.login.data.LoginData
import com.example.storyappsubmission.view.login.data.LoginResponse
import com.example.storyappsubmission.view.register.data.RegisterData
import com.example.storyappsubmission.view.register.data.RegisterResponse
import com.example.storyappsubmission.view.story.DetailStoryResponse
import com.example.storyappsubmission.view.story.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("register")
    suspend fun register(
        @Body requestBody: RegisterData
    ): RegisterResponse

    @POST("login")
    suspend fun login(
       @Body requesBody: LoginData
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
    ): StoryResponse

    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Header ("Authorization") token: String,
        @Path("id") id:String
    ): DetailStoryResponse

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): AddStoryResponse

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Header("Authorization") token: String,
        @Query("location") location: Int = 1,
    ): StoryResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): StoryResponse

}