package com.demuha.submission01.model

import androidx.paging.PagingData
import com.google.gson.annotations.SerializedName

abstract class BaseResponse<T> {
    @field:SerializedName("error")
    var error: Boolean = false

    @field:SerializedName("message")
    lateinit var message: String

    abstract val data: T?
}

data class LoginResponse(
    @field:SerializedName("loginResult")
    override val data: LoginDto?
) : BaseResponse<LoginDto>()

data class StoryResponse(
    @field:SerializedName("listStory")
    override val data: List<StoryDto>?
) : BaseResponse<List<StoryDto>>()

data class MessageResponse(
    override val data: Nothing? = null
) : BaseResponse<Nothing>()