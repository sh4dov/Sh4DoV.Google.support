package com.sh4dov.google;

import android.app.Activity;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.sh4dov.google.listeners.GetFilesListener;
import com.sh4dov.google.model.FileHelper;

import java.io.IOException;
import java.util.List;

class GetFilesOperation implements Operation<DriveService> {
    private Activity activity;
    private GetFilesListener getFilesListener;

    public GetFilesOperation(Activity activity, GetFilesListener getFilesListener) {
        this.activity = activity;
        this.getFilesListener = getFilesListener;
    }

    @Override
    public void execute(DriveService scope) throws IOException {
        Drive drive = scope.getDrive();
        final List<File> files = drive.files().list().execute().getItems();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getFilesListener.getFiles(FileHelper.convert(files));
            }
        });
    }

    @Override
    public void onFinished() {

    }

    @Override
    public void onFailed(Exception e) {

    }
}
