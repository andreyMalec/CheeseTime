package com.malec.presentation

import android.content.Context

class Resources(private val context: Context) {
    private val res = context.resources

    fun recipes() = res.getStringArray(R.array.recipes).toList()

    fun stringCheeseTypeAny() = context.getString(R.string.filter_cheese_type_any)

//    fun stringCheeseDeleted() = context.getString(R.string.cheese_deleted)
//
//    fun stringCheeseCreated() = context.getString(R.string.cheese_created)
//
//    fun stringCheeseUpdated() = context.getString(R.string.cheese_updated)
//
//    fun stringTaskDeleted() = context.getString(R.string.task_deleted)
//
//    fun stringTaskCreated() = context.getString(R.string.task_created)
//
//    fun stringTaskUpdated() = context.getString(R.string.task_updated)

    fun stringPhotoDownloadedSuccessful() = context.getString(R.string.photo_downloaded_successful)

    fun stringEmptyFieldsError() = context.getString(R.string.empty_fields_error)
}