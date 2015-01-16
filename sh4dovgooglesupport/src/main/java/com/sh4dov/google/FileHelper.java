package com.sh4dov.google;

import com.google.api.services.drive.model.File;

public final class FileHelper {
    public static final String FOLDER_MIME_TYPE = "application/vnd.google-apps.folder";

    public static File createFolder() {
        File file = new File();
        file.setMimeType(FOLDER_MIME_TYPE);
        return file;
    }

    public static boolean isFolder(File file) {
        return file.getMimeType().equalsIgnoreCase(FOLDER_MIME_TYPE);
    }
}
