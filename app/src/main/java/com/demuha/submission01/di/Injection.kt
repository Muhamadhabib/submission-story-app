package com.demuha.submission01.di

import android.content.Context
import com.demuha.submission01.config.ApiConfig
import com.demuha.submission01.data.StoryRepository
import com.demuha.submission01.data.UserRepository
import com.demuha.submission01.data.pref.UserPreference
import com.demuha.submission01.data.pref.dataStore

object Injection {
    fun provideStoryRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(pref)
        return StoryRepository.getInstance(apiService)
    }
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(pref)
        return UserRepository.getInstance(pref, apiService)
    }
}