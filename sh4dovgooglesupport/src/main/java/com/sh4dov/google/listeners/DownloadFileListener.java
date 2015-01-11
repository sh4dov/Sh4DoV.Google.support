package com.sh4dov.google.listeners;

import com.sh4dov.google.model.File;

public interface DownloadFileListener extends UserRecoverableRequestCodeProvider, FileProgressListener {
    void onDownloadedFile(File file, byte[] content);
}

