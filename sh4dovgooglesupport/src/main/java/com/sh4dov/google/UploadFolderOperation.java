package com.sh4dov.google;

import android.app.Activity;

import com.google.api.services.drive.DriveRequest;
import com.google.api.services.drive.model.File;
import com.sh4dov.google.listeners.FolderListener;
import com.sh4dov.google.listeners.OnFailedListener;

import java.io.IOException;

public class UploadFolderOperation extends OperationBase<DriveService> {
    private final Activity activity;
    private final File file;
    private final FolderListener listener;

    public UploadFolderOperation(Activity activity, File file, FolderListener listener, OnFailedListener onFailedListener) {
        super(onFailedListener);
        this.activity = activity;
        this.file = file;
        this.listener = listener;
    }

    @Override
    public void execute(DriveService scope) throws IOException {
        String id = file.getId();
        DriveRequest<File> request;

        if (id == null || id.isEmpty()) {
            request = scope.getDrive().files().insert(file);
        } else {
            request = scope.getDrive().files().update(id, file);
        }

        final File newFolder = request.execute();

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listener.onUpdatedFolder(newFolder);
            }
        });
    }
}
