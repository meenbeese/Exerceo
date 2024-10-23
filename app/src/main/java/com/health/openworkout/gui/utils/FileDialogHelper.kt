package com.health.openworkout.gui.utils

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log

import androidx.fragment.app.Fragment

import com.health.openworkout.R

class FileDialogHelper(private val fragment: Fragment) {

    private var defaultFilename: String? = null
    private val TAG = FileDialogHelper::class.java.simpleName

    fun openImportFileDialog() {
        handleFileDialog(
            action = Intent.ACTION_OPEN_DOCUMENT,
            mimeType = "application/zip",
            dialogLabel = R.string.label_select_import_file,
            requestCode = REQUEST_IMPORT_FILE_DIALOG,
            permission = Manifest.permission.READ_EXTERNAL_STORAGE,
            permissionRequestCode = READ_EXTERNAL_STORAGE_PERMISSION_IMPORT
        )
    }

    fun openDebugFileDialog(defaultFilename: String?) {
        handleFileDialog(
            action = Intent.ACTION_CREATE_DOCUMENT,
            mimeType = "text/plain",
            dialogLabel = R.string.label_select_debug_file,
            requestCode = REQUEST_DEBUG_FILE_DIALOG,
            permission = Manifest.permission.WRITE_EXTERNAL_STORAGE,
            permissionRequestCode = WRITE_EXTERNAL_STORAGE_PERMISSION_EXPORT,
            defaultFilename = defaultFilename
        )
    }

    fun openExportFileDialog() {
        handleFileDialog(
            action = Intent.ACTION_CREATE_DOCUMENT,
            mimeType = "application/zip",
            dialogLabel = R.string.label_select_export_file,
            requestCode = REQUEST_EXPORT_FILE_DIALOG,
            permission = Manifest.permission.WRITE_EXTERNAL_STORAGE,
            permissionRequestCode = WRITE_EXTERNAL_STORAGE_PERMISSION_EXPORT,
            defaultFilename = defaultFilename
        )
    }

    fun openExportFileDialog(defaultFilename: String?) {
        this.defaultFilename = defaultFilename
        openExportFileDialog()
    }

    fun openImageFileDialog() {
        handleFileDialog(
            action = Intent.ACTION_OPEN_DOCUMENT,
            mimeType = "image/*",
            dialogLabel = R.string.label_select_image_file,
            requestCode = REQUEST_OPEN_IMAGE_DIALOG,
            permission = Manifest.permission.READ_EXTERNAL_STORAGE,
            permissionRequestCode = READ_EXTERNAL_STORAGE_PERMISSION_OPEN_IMAGE
        )
    }

    fun openVideoFileDialog() {
        handleFileDialog(
            action = Intent.ACTION_OPEN_DOCUMENT,
            mimeType = "video/*",
            dialogLabel = R.string.label_select_video_file,
            requestCode = REQUEST_OPEN_VIDEO_DIALOG,
            permission = Manifest.permission.READ_EXTERNAL_STORAGE,
            permissionRequestCode = READ_EXTERNAL_STORAGE_PERMISSION_OPEN_VIDEO
        )
    }

    private fun handleFileDialog(
        action: String,
        mimeType: String,
        dialogLabel: Int,
        requestCode: Int,
        permission: String,
        permissionRequestCode: Int,
        defaultFilename: String? = null
    ) {
        if (checkPermission(permission)) {
            val intent = Intent().apply {
                type = mimeType
                defaultFilename?.let { putExtra(Intent.EXTRA_TITLE, it) }
                setAction(action)
            }
            fragment.startActivityForResult(
                Intent.createChooser(intent, fragment.getString(dialogLabel)), requestCode
            )
        } else {
            requestPermission(permission, permissionRequestCode)
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return fragment.requireContext().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(permission: String, requestCode: Int) {
        try {
            fragment.requestPermissions(arrayOf(permission), requestCode)
        } catch (ex: Exception) {
            Log.e(TAG, "Error requesting permission: $permission", ex)
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                READ_EXTERNAL_STORAGE_PERMISSION_IMPORT -> openImportFileDialog()
                WRITE_EXTERNAL_STORAGE_PERMISSION_EXPORT -> openExportFileDialog()
                READ_EXTERNAL_STORAGE_PERMISSION_OPEN_IMAGE -> openImageFileDialog()
                READ_EXTERNAL_STORAGE_PERMISSION_OPEN_VIDEO -> openVideoFileDialog()
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMPORT_FILE_DIALOG,
                REQUEST_EXPORT_FILE_DIALOG,
                REQUEST_OPEN_IMAGE_DIALOG,
                REQUEST_OPEN_VIDEO_DIALOG,
                REQUEST_DEBUG_FILE_DIALOG -> true
                else -> false
            }
        } else {
            false
        }
    }

    companion object {
        private const val READ_EXTERNAL_STORAGE_PERMISSION_IMPORT = 1
        private const val WRITE_EXTERNAL_STORAGE_PERMISSION_EXPORT = 2
        private const val READ_EXTERNAL_STORAGE_PERMISSION_OPEN_IMAGE = 3
        private const val READ_EXTERNAL_STORAGE_PERMISSION_OPEN_VIDEO = 4
        const val REQUEST_OPEN_IMAGE_DIALOG = 10
        const val REQUEST_OPEN_VIDEO_DIALOG = 20
        const val REQUEST_EXPORT_FILE_DIALOG = 30
        const val REQUEST_IMPORT_FILE_DIALOG = 40
        const val REQUEST_DEBUG_FILE_DIALOG = 50
    }
}
