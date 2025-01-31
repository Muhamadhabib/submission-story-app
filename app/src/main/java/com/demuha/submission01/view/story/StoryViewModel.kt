package com.demuha.submission01.view.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demuha.submission01.data.StoryRepository
import com.demuha.submission01.model.StoryDto
import com.demuha.submission01.util.Resource
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _result = MutableLiveData<Resource<String>>()
    val response = _result
    private val _stories = MediatorLiveData<Resource<List<StoryDto>>>()
    val stories: LiveData<Resource<List<StoryDto>>> = _stories

    fun addStory(file: MultipartBody.Part, desc: RequestBody) {
        viewModelScope.launch {
            _result.value = Resource.Loading
            storyRepository.uploadPhoto(file, desc).fold(
                onSuccess = { _result.value = Resource.Success(it.message) },
                onFailure = { _result.value = Resource.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun getStoriesWithLocation() {
        viewModelScope.launch {
            _stories.value = Resource.Loading
            storyRepository.getStoriesWithLocation().fold(
                onSuccess = {
                    if (it.isNotEmpty()) {
                        _stories.value = Resource.Success(it)
                    } else {
                        _stories.value = Resource.Error("Not Found")
                    }
                },
                onFailure = { _stories.value = Resource.Error(it.message ?: "Unknown error") }
            )
        }
    }
}