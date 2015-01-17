package com.sh4dov.google.builders;

import com.google.api.services.drive.model.File;
import com.sh4dov.google.FileHelper;

public class FileBuilder {
    private File file;

    public FileBuilder(File file) {
        this.file = file;
    }

    public static FileBuilder createNewFile() {
        return new FileBuilder(new File());
    }

    public static FileBuilder createNewFolder() {
        return createNewFile()
                .setMimeType(FileHelper.FOLDER_MIME_TYPE);
    }

    public FileBuilder setTitle(String title) {
        file.setTitle(title);
        return this;
    }

    public FileBuilder setMimeType(String mimeType) {
        file.setMimeType(mimeType);
        return this;
    }

    public File build() {
        return file;
    }
}
