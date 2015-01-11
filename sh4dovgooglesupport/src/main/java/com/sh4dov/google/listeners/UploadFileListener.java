package com.sh4dov.google.listeners;

import com.sh4dov.google.model.File;

public interface UploadFileListener extends UserRecoverableRequestCodeProvider, FileProgressListener{
    void onUploaded(File file);
}
