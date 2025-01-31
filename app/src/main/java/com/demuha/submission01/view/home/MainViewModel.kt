package com.demuha.submission01.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.demuha.submission01.data.StoryRepository
import com.demuha.submission01.data.UserRepository
import com.demuha.submission01.model.StoryDto
import com.demuha.submission01.model.User
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
    storyRepository: StoryRepository
) : ViewModel() {

    private val _session = userRepository.getSession().asLiveData()
    val sessions: LiveData<User> = _session


    val stories: LiveData<PagingData<StoryDto>> =
        storyRepository.getStories().cachedIn(viewModelScope)

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

}