package com.example.storyappsubmission.data.pref

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)
