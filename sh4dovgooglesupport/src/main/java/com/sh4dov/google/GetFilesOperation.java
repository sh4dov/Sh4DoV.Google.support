package com.sh4dov.google;

import android.app.Activity;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.sh4dov.google.listeners.GetFilesListener;
import com.sh4dov.google.listeners.OnFailedListener;

import java.io.IOException;
import java.util.List;

class GetFilesOperation extends OperationBase<DriveService> {
    private Activity activity;
    private GetFilesListener getFilesListener;
    private String q;

    public GetFilesOperation(String q, Activity activity, GetFilesListener getFilesListener, OnFailedListener onFailedListener) {
        super(onFailedListener);
        this.q = q;
        this.activity = activity;
        this.getFilesListener = getFilesListener;
    }

    @Override
    public void execute(DriveService scope) throws IOException {
        Drive drive = scope.getDrive();
        Drive.Files.List list = drive.files().list();
        if (q != null && !q.isEmpty()) {
            list.setQ(q);
        }
        final List<File> files = list.execute().getItems();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getFilesListener.onGetFiles(files);
            }
        });
    }

}
