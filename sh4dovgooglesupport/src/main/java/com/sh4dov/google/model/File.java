package com.sh4dov.google.model;

public class File {
    private com.google.api.services.drive.model.File file;

    File(com.google.api.services.drive.model.File file) {
        this.file = file;
    }

    public String getTitle(){return file.getTitle();}
}
