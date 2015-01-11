package com.sh4dov.google.model;

import java.util.ArrayList;
import java.util.List;

public final class FileHelper {
    public static List<File> convert(List<com.google.api.services.drive.model.File> files) {
        if (files == null) {
            return null;
        }

        ArrayList<File> result = new ArrayList<File>();
        for (com.google.api.services.drive.model.File file : files) {
            result.add(new File(file));
        }

        return result;
    }

    public static com.google.api.services.drive.model.File convert(File file){
        return file.getSource();
    }

    public static File createFile(){
        return new File(new com.google.api.services.drive.model.File());
    }
}
