package com.demuha.submission01.model

data class User(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)