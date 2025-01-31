package com.demuha.submission01.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.demuha.submission01.model.MessageResponse
import com.demuha.submission01.model.StoryDto
import com.demuha.submission01.service.ApiService
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class StoryRepository private constructor(
    private val apiService: ApiService
){
    fun getStories(): LiveData<PagingData<StoryDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = 3
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

    suspend fun uploadPhoto(file: MultipartBody.Part, desc: RequestBody): Result<MessageResponse> {
        try {
            val response = apiService.uploadImage(file, desc)
            return if (!response.error) {
                Result.success(response)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, MessageResponse::class.java)
            val errorMessage = errorBody.message
            return Result.failure(Exception(errorMessage))
        }
    }
    suspend fun getStoriesWithLocation(): Result<List<StoryDto>> {
        try {
            val response = apiService.getStoriesWithLocation()
            return if (!response.error) {
                Result.success(response.data!!)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, MessageResponse::class.java)
            val errorMessage = errorBody.message
            return Result.failure(Exception(errorMessage))
        }
    }
    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(apiService: ApiService): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService).also { instance = it }
            }
    }
}