package com.sh4dov.google.listeners;

import com.google.api.services.drive.model.File;

import java.util.List;

public interface GetFilesListener {
    void getFiles(List<File> files);
}
