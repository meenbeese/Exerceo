package com.health.openworkout.gui.utils;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.health.openworkout.R;

import static android.app.Activity.RESULT_OK;

public class FileDialogHelper {

    private final static int READ_EXTERNAL_STORAGE_PERMISSION_IMPORT = 1;
    private final static int WRITE_EXTERNAL_STORAGE_PERMISSION_EXPORT = 2;
    private final static int READ_EXTERNAL_STORAGE_PERMISSION_OPEN_IMAGE = 3;
    private final static int READ_EXTERNAL_STORAGE_PERMISSION_OPEN_VIDEO = 4;
    public final static int REQUEST_OPEN_IMAGE_DIALOG = 10;
    public final static int REQUEST_OPEN_VIDEO_DIALOG = 20;
    public final static int REQUEST_EXPORT_FILE_DIALOG = 30;
    public final static int REQUEST_IMPORT_FILE_DIALOG = 40;
    public final static int REQUEST_DEBUG_FILE_DIALOG = 50;

    private final Fragment fragment;

    private String defaultFilename;

    private final String TAG = getClass().getSimpleName();

    public FileDialogHelper(Fragment fragment) {
        this.fragment = fragment;
    }

    public void openImportFileDialog() {
        if (checkPermissionForReadExternalStorage()) {
            Intent intent = new Intent()
                    .setType("application/zip")
                    .setAction(Intent.ACTION_OPEN_DOCUMENT);

            fragment.startActivityForResult(Intent.createChooser(intent, fragment.getString(R.string.label_select_import_file)), REQUEST_IMPORT_FILE_DIALOG);
        } else {
            requestPermissionForReadExternalStorage(READ_EXTERNAL_STORAGE_PERMISSION_IMPORT);
        }
    }

    public void openDebugFileDialog(String defaultFilename) {
        if (checkPermissionForWriteExternalStorage()) {
            Intent intent = new Intent()
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_TITLE, defaultFilename)
                    .setAction(Intent.ACTION_CREATE_DOCUMENT);

            fragment.startActivityForResult(Intent.createChooser(intent, fragment.getString(R.string.label_select_debug_file)), REQUEST_DEBUG_FILE_DIALOG);
        } else {
            requestPermissionForWriteExternalStorage(WRITE_EXTERNAL_STORAGE_PERMISSION_EXPORT);
        }
    }

    public void openExportFileDialog() {
        if (checkPermissionForWriteExternalStorage()) {
            Intent intent = new Intent()
                    .setType("application/zip")
                    .putExtra(Intent.EXTRA_TITLE, defaultFilename)
                    .setAction(Intent.ACTION_CREATE_DOCUMENT);

            fragment.startActivityForResult(Intent.createChooser(intent, fragment.getString(R.string.label_select_export_file)), REQUEST_EXPORT_FILE_DIALOG);
        } else {
            requestPermissionForWriteExternalStorage(WRITE_EXTERNAL_STORAGE_PERMISSION_EXPORT);
        }
    }

    public void openExportFileDialog(String defaultFilename) {
        this.defaultFilename = defaultFilename;
        openExportFileDialog();
    }

    public void openImageFileDialog() {
        if (checkPermissionForReadExternalStorage()) {
            Intent intent = new Intent()
                    .setType("image/*")
                    .setAction(Intent.ACTION_OPEN_DOCUMENT);

            fragment.startActivityForResult(Intent.createChooser(intent, fragment.getString(R.string.label_select_image_file)), REQUEST_OPEN_IMAGE_DIALOG);
        } else {
            requestPermissionForReadExternalStorage(READ_EXTERNAL_STORAGE_PERMISSION_OPEN_IMAGE);
        }
    }

    public void openVideoFileDialog() {
        if (checkPermissionForReadExternalStorage()) {
            Intent intent = new Intent()
                    .setType("video/*")
                    .setAction(Intent.ACTION_OPEN_DOCUMENT);

            fragment.startActivityForResult(Intent.createChooser(intent, fragment.getString(R.string.label_select_video_file)), REQUEST_OPEN_VIDEO_DIALOG);
        } else {
            requestPermissionForReadExternalStorage(READ_EXTERNAL_STORAGE_PERMISSION_OPEN_VIDEO);
        }
    }

    private boolean checkPermissionForReadExternalStorage() {
        int result = fragment.getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermissionForReadExternalStorage(int requestCode) {
        try {
            fragment.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    private boolean checkPermissionForWriteExternalStorage() {
        int result = fragment.getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionForWriteExternalStorage(int requestCode) {
        try {
            fragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case READ_EXTERNAL_STORAGE_PERMISSION_IMPORT:
                    openImportFileDialog();
                    break;
                case WRITE_EXTERNAL_STORAGE_PERMISSION_EXPORT:
                    openExportFileDialog();
                    break;
                case READ_EXTERNAL_STORAGE_PERMISSION_OPEN_IMAGE:
                    openImageFileDialog();
                    break;
                case READ_EXTERNAL_STORAGE_PERMISSION_OPEN_VIDEO:
                    openVideoFileDialog();
                    break;
            }
        }
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            return requestCode == REQUEST_IMPORT_FILE_DIALOG ||
                    requestCode == REQUEST_EXPORT_FILE_DIALOG ||
                    requestCode == REQUEST_OPEN_IMAGE_DIALOG ||
                    requestCode == REQUEST_OPEN_VIDEO_DIALOG ||
                    requestCode == REQUEST_DEBUG_FILE_DIALOG;
        }

        return false;
    }
}
