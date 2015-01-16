package com.sh4dov.google.listeners;


import com.google.api.services.drive.model.File;

public interface FileProgressListener {
    void onProgress(File file, double progress);
}
