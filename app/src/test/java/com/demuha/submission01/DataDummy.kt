package com.demuha.submission01

import com.demuha.submission01.model.StoryDto

object DataDummy {

    fun generateDummyStoryResponse(): List<StoryDto> {
        val items: MutableList<StoryDto> = arrayListOf()
        for (i in 0..100) {
            val quote = StoryDto(
                "id $i",
                "name + $i",
                "desctiption  $i",
                i.toString(),
                "createdAt $i",
                i.toDouble(),
                i.toDouble()
            )
            items.add(quote)
        }
        return items
    }
}