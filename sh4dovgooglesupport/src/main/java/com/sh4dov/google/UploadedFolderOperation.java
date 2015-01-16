package com.sh4dov.google;

import android.app.Activity;

import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.sh4dov.google.listeners.OnFailedListener;
import com.sh4dov.google.listeners.UploadFileListener;

import java.io.IOException;

public class UploadedFolderOperation extends OperationBase<DriveService> implements MediaHttpUploaderProgressListener {
    private static final boolean DIRECT_UPLOAD_ENABLED = false;
    private final Activity activity;
    private final File file;
    private final UploadFileListener listener;

    public UploadedFolderOperation(Activity activity, File file, UploadFileListener listener, OnFailedListener onFailedListener) {
        super(onFailedListener);
        this.activity = activity;
        this.file = file;
        this.listener = listener;
    }

    @Override
    public void execute(DriveService scope) throws IOException {
        String id = file.getId();

        if(id == null || id.isEmpty()){
            Drive.Files.Insert insert = scope.getDrive().files().insert(file);
            insert.getMediaHttpUploader()
                    .setDirectUploadEnabled(DIRECT_UPLOAD_ENABLED)
                    .setProgressListener(this);
            insert.execute();
        } else{
            Drive.Files.Update update = scope.getDrive().files().update(id, file);
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

                if (uploader.getUploadState() == MediaHttpUploader.UploadState.MEDIA_COMPLETE) {
                    listener.onUploaded(file);
                }
            }
        });
    }
}
