package com.sh4dov.google;

import android.app.Activity;

import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListener;
import com.google.api.client.http.GenericUrl;
import com.sh4dov.google.listeners.DownloadFileListener;
import com.sh4dov.google.listeners.OnFailedListener;
import com.sh4dov.google.model.File;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DownloadFileOperation extends OperationBase<DriveService> implements MediaHttpDownloaderProgressListener {
    private final File file;
    private final ByteArrayOutputStream outputStream;
    private Activity activity;
    private DownloadFileListener listener;

    public DownloadFileOperation(Activity activity, File file, DownloadFileListener listener, OnFailedListener onFailedListener) {
        super(onFailedListener);
        this.activity = activity;
        this.file = file;
        this.listener = listener;
        outputStream = new ByteArrayOutputStream();
    }

    @Override
    public void execute(DriveService scope) throws IOException {
        scope.getDrive()
                .files()
                .get(file.getId())
                .getMediaHttpDownloader()
                .setDirectDownloadEnabled(false)
                .setProgressListener(this)
                .download(new GenericUrl(file.getDownloadUrl()), outputStream);
    }

    @Override
    public void progressChanged(final MediaHttpDownloader downloader) throws IOException {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listener.onProgress(file, downloader.getProgress());

                if (downloader.getDownloadState() == MediaHttpDownloader.DownloadState.MEDIA_COMPLETE) {
                    try {
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    listener.onDownloadedFile(file, outputStream.toByteArray());
                }
            }
        });
    }
}
