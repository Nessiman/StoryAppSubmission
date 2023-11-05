package com.example.storyappsubmission

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyappsubmission.di.ApiRetro
import com.example.storyappsubmission.maps.MapsViewModel
import com.example.storyappsubmission.view.MainRepository
import com.example.storyappsubmission.view.addstory.AddStoryViewModel
import com.example.storyappsubmission.view.main.MainViewModel

class ViewModelFactory(private val mainRepository: MainRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(mainRepository) as T
        }
        if(modelClass.isAssignableFrom(AddStoryViewModel::class.java)){
            return AddStoryViewModel(mainRepository) as T
        }
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(mainRepository) as T
        }
        throw IllegalArgumentException("ViewModelclass tidak ditemuka " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(ApiRetro.provideRepository(context))
            }.also { INSTANCE = it }
    }
}