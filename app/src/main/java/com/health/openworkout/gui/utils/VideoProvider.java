package com.health.openworkout.gui.utils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.CancellationSignal;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

public class VideoProvider extends ContentProvider {

    private final String TAG = getClass().getSimpleName();

    @Override
    public AssetFileDescriptor openAssetFile(Uri uri, @NonNull String mode) {
        AssetManager am = getContext().getAssets();
        String file_name = uri.getPath().substring(1);

        AssetFileDescriptor afd = null;

        try {
            afd = am.openFd(file_name);
        } catch (IOException ex) {
            Log.e(TAG, ex.toString());
        }

        return afd;
    }

    @Override
    public String getType(@NonNull Uri p1) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri p1, String p2, String[] p3) {
        return 0;
    }

    @Override
    public Cursor query(@NonNull Uri p1, String[] p2, String p3, String[] p4, String p5) {
        return null;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, CancellationSignal cancellationSignal) {
        return super.query( uri, projection, selection, selectionArgs, sortOrder, cancellationSignal );
    }

    @Override
    public Uri insert(@NonNull Uri p1, ContentValues p2) {
        return null;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public int update(@NonNull Uri p1, ContentValues p2, String p3, String[] p4) {
        return 0;
    }
}
