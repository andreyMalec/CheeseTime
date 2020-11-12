package com.malec.cheesetime.service

import android.content.Context
import com.malec.cheesetime.R

class Resources(private val context: Context) {
    private val res = context.resources

    fun recipes() = res.getStringArray(R.array.recipes).toList()

    fun stringCheeseDeleted() = context.getString(R.string.cheese_deleted)

    fun stringCheeseCreated() = context.getString(R.string.cheese_created)

    fun stringCheeseUpdated() = context.getString(R.string.cheese_updated)

    fun stringPhotoDownloadedSuccessful() = context.getString(R.string.photo_downloaded_successful)
}