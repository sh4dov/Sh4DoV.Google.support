package com.sh4dov.google;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.sh4dov.google.listeners.GetFilesListener;

import java.io.IOException;
import java.util.List;

class GetFilesOperation implements Operation<DriveService> {
    private GetFilesListener getFilesListener;

    public GetFilesOperation(GetFilesListener getFilesListener) {
        this.getFilesListener = getFilesListener;
    }

    @Override
    public void execute(DriveService scope) {
        Drive drive = scope.getDrive();
        List<File> files = null;
        try {
            files = drive.files().list().execute().getItems();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        // TODO: Add resolution if exist, handling fail.
        getFilesListener.getFiles(files);
    }

    @Override
    public void onFinished() {

    }

    @Override
    public void onFailed() {

    }
}
