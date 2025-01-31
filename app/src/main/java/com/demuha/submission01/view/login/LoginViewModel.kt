package com.demuha.submission01.view.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demuha.submission01.data.UserRepository
import com.demuha.submission01.model.LoginDto
import com.demuha.submission01.model.User
import com.demuha.submission01.util.Resource
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _result = MutableLiveData<Resource<LoginDto>>()
    val response = _result

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _result.value = Resource.Loading
            repository.login(email,password)
                .onSuccess {
                    repository.saveSession(User(email, it.token))
                    _result.value = Resource.Success(it)
                }
                .onFailure {
                    _result.value = Resource.Error(it.message ?: "Unknown error")
                }
        }
    }
}