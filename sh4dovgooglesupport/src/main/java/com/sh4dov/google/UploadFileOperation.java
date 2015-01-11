package com.sh4dov.google;

import android.app.Activity;

import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.sh4dov.google.listeners.OnFailedListener;
import com.sh4dov.google.listeners.UploadFileListener;
import com.sh4dov.google.model.File;
import com.sh4dov.google.model.FileHelper;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class UploadFileOperation extends OperationBase<DriveService> implements MediaHttpUploaderProgressListener {
    public static final boolean DIRECT_UPLOAD_ENABLED = false;
    private Activity activity;
    private File file;
    private byte[] content;
    private UploadFileListener listener;

    public UploadFileOperation(Activity activity,  File file, byte[] content, UploadFileListener listener, OnFailedListener onFailedListener) {
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

        if (id == null || id.isEmpty()) {
            Drive.Files.Insert insert = scope.getDrive().files().insert(FileHelper.convert(file), inputStreamContent);
            insert.getMediaHttpUploader()
                    .setDirectUploadEnabled(DIRECT_UPLOAD_ENABLED)
                    .setProgressListener(this);
            insert.execute();
        } else {
            Drive.Files.Update update = scope.getDrive().files().update(id, FileHelper.convert(file), inputStreamContent);
            update.getMediaHttpUploader()
                    .setDirectUploadEnabled(DIRECT_UPLOAD_ENABLED)
                    .setProgressListener(this);
            update.execute();
        }
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
                }
                listener.onProgress(file, progress);

                if(uploader.getUploadState() == MediaHttpUploader.UploadState.MEDIA_COMPLETE){
                    listener.onUploaded(file);
                }
            }
        });
    }
}
