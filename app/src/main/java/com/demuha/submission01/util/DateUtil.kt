package com.demuha.submission01.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
fun parseDateRelative(date: String): String {
    val dateTime = Instant.parse(date)
    val relative = dateTime.toRelativeTimeString()
    return relative
}

@RequiresApi(Build.VERSION_CODES.O)
private fun Instant.toRelativeTimeString(): String {
    val now = Instant.now()
    val duration = Duration.between(this, now)

    return when {
        duration.toMinutes() < 1 -> "just now"
        duration.toHours() < 1 -> "${duration.toMinutes()} minutes ago"
        duration.toDays() < 1 -> "${duration.toHours()} hours ago"
        duration.toDays() < 30 -> "${duration.toDays()} days ago"
        else -> {
            val months = duration.toDays() / 30
            if (months < 12) "$months months ago"
            else "${months / 12} years ago"
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(dateString: String): String {
    val instant = Instant.parse(dateString)
    val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    return localDate.format(formatter)
}