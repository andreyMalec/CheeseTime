package com.malec.domain.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

abstract class UriSharer(private val context: Context) {
    protected abstract fun writeToStream(content: Any, stream: FileOutputStream)

    protected fun shareUri(uri: Uri) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(uri, context.contentResolver.getType(uri))
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        val chooserIntent = Intent.createChooser(
            shareIntent,
            "Choose an app"
        )
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)
    }

    protected fun makeUri(fileName: String, content: Any): Uri =
        FileProvider.getUriForFile(context, context.packageName, writeToFile(fileName, content))

    private fun writeToFile(fileName: String, content: Any): File {
        val file = makeFile(fileName)
        FileOutputStream(file).also {
            writeToStream(content, it)
            it.flush()
            it.close()
        }
        return file
    }

    private fun makeFile(fileName: String): File {
        val cachePath = File(context.cacheDir, "docs")
        cachePath.mkdirs()

        return File(cachePath, fileName)
    }
}