package com.sh4dov.google.model;

public class File {
    private com.google.api.services.drive.model.File file;

    File(com.google.api.services.drive.model.File file) {
        this.file = file;
    }

    public String getTitle() {
        return file.getTitle();
    }

    public File setTitle(String title) {
        file.setTitle(title);
        return this;
    }

    public String getId() {
        return file.getId();
    }

    public long getFileSize() {
        return file.getFileSize();
    }

    public String getDownloadUrl() {
        return file.getDownloadUrl();
    }

    com.google.api.services.drive.model.File getSource() {
        return file;
    }
}
