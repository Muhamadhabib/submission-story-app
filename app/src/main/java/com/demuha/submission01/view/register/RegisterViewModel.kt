package com.demuha.submission01.view.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demuha.submission01.data.UserRepository
import com.demuha.submission01.util.Resource
import kotlinx.coroutines.launch

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _result = MutableLiveData<Resource<String>>()
    val response = _result

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _result.value = Resource.Loading
            userRepository.register(name, email, password).fold(
                onSuccess = { _result.value = Resource.Success(it) },
                onFailure = { _result.value = Resource.Error(it.message ?: "Unknown error") }
            )
        }
    }

}