package com.sh4dov.google.builders;

import com.google.api.services.drive.model.File;

public class FileBuilder {
    private File file;

    public FileBuilder(File file) {
        this.file = file;
    }

    public static FileBuilder createNewFile() {
        return new FileBuilder(new File());
    }

    public FileBuilder setTitle(String title) {
        file.setTitle(title);
        return this;
    }

    public File build(){
        return file;
    }
}
