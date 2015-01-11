package com.sh4dov.google.listeners;

import com.sh4dov.google.model.File;

import java.util.List;

public interface GetFilesListener extends UserRecoverableRequestCodeProvider {
    void getFiles(List<File> files);
}
