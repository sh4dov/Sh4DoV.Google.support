package com.sh4dov.google.listeners;


import com.google.api.services.drive.model.File;

public interface DownloadFileListener extends FileProgressListener {
    void onDownloadedFile(File file, byte[] content);
}

