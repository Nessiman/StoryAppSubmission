package com.example.storyappsubmission.view.addstory

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.storyappsubmission.R
import com.example.storyappsubmission.ViewModelFactory
import com.example.storyappsubmission.databinding.ActivityAddStoryBinding
import com.example.storyappsubmission.view.Result
import com.example.storyappsubmission.view.getImageUri
import com.example.storyappsubmission.view.main.MainActivity
import com.example.storyappsubmission.view.reduceFileImage
import com.example.storyappsubmission.view.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStory : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private var currentImageUri: Uri? = null

    private val addStoryViewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener{ startGallery() }
        binding.cameraButton.setOnClickListener{ startCamera() }
        binding.uploadButton.setOnClickListener{
            val token = addStoryViewModel.getPreference(this).value
            val addDescription = binding.addDescription
            if (token != null){
                addStory(addDescription, token)
            }
        }

    }

    private fun addStory(addDescription: EditText, token: String) {
        binding.progressBar.visibility = View.VISIBLE
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")

            val desc = addDescription.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val imgMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )

            addStoryViewModel.addNewStory("Bearer $token", imgMultipart, desc)
                .observe(this) { result ->
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val response = result.data
                            val intent = Intent(this@AddStory, MainActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                            finish()
                        }is Result.Error -> {
                        val errorMessage = result.error
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                    }
                }

        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }
    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImageL $it")
            binding.previewImageView.setImageURI(it)
        }
    }
}