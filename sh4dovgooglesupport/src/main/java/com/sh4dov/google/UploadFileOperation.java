package com.sh4dov.google;

import android.app.Activity;

import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.DriveRequest;
import com.google.api.services.drive.model.File;
import com.sh4dov.google.listeners.OnFailedListener;
import com.sh4dov.google.listeners.UploadFileListener;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class UploadFileOperation extends OperationBase<DriveService> implements MediaHttpUploaderProgressListener {
    public static final boolean DIRECT_UPLOAD_ENABLED = false;
    private Activity activity;
    private byte[] content;
    private File file;
    private UploadFileListener listener;

    public UploadFileOperation(Activity activity, File file, byte[] content, UploadFileListener listener, OnFailedListener onFailedListener) {
        super(onFailedListener);
        this.activity = activity;
        this.file = file;
        this.content = content;
        this.listener = listener;
    }

    @Override
    public void execute(DriveService scope) throws IOException {
        InputStreamContent inputStreamContent = new InputStreamContent("application/octet-stream", new BufferedInputStream(new ByteArrayInputStream(content)));
        inputStreamContent.setLength(content.length);
        String id = file.getId();
        DriveRequest<File> request;

        if (id == null || id.isEmpty()) {
            request = scope.getDrive().files().insert(file, inputStreamContent);
        } else {
            request = scope.getDrive().files().update(id, file, inputStreamContent);
        }

        request.getMediaHttpUploader()
                .setDirectUploadEnabled(DIRECT_UPLOAD_ENABLED)
                .setProgressListener(this);
        final File newFile = request.execute();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listener.onUploaded(newFile);
            }
        });
    }

    @Override
    public void progressChanged(final MediaHttpUploader uploader) throws IOException {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                double progress = 0;
                try {
                    progress = uploader.getProgress();
                } catch (IOException e) {
                    e.printStackTrace();
                    onFailed(e);
                }
                listener.onProgress(file, progress);
            }
        });
    }
}
