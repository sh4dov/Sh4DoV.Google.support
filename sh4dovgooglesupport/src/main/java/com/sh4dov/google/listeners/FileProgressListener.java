package com.sh4dov.google.listeners;

import com.sh4dov.google.model.File;

public interface FileProgressListener {
    void onProgress(File file, double progress);
}
