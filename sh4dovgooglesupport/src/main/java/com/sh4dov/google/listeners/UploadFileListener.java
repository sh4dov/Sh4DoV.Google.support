package com.sh4dov.google.listeners;


import com.google.api.services.drive.model.File;

public interface UploadFileListener extends UserRecoverableRequestCodeProvider, FileProgressListener {
    void onUploaded(File file);
}
