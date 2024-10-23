package com.health.openworkout.gui.utils

import android.content.ContentProvider
import android.content.ContentValues
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.net.Uri
import android.os.CancellationSignal
import android.util.Log

import java.io.IOException

class VideoProvider : ContentProvider() {

    private val TAG = VideoProvider::class.java.simpleName

    override fun openAssetFile(uri: Uri, mode: String): AssetFileDescriptor? {
        val context = context ?: run {
            Log.e(TAG, "Context is null, cannot open file.")
            return null
        }

        val fileName = uri.path?.substring(1) ?: run {
            Log.e(TAG, "File name is null for URI: $uri")
            return null
        }

        return try {
            context.assets.openFd(fileName)
        } catch (ex: IOException) {
            Log.e(TAG, "Error opening asset file: $fileName", ex)
            null
        }
    }

    override fun getType(uri: Uri): String? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? = null

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?,
        cancellationSignal: CancellationSignal?
    ): Cursor? = super.query(uri, projection, selection, selectionArgs, sortOrder, cancellationSignal)

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun onCreate(): Boolean = false

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0
}
