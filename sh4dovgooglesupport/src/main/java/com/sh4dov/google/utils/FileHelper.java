package com.sh4dov.google.utils;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;

import java.util.List;

public final class FileHelper {
    public static final String FOLDER_MIME_TYPE = "application/vnd.google-apps.folder";
    public static final String ROOT_ID = "root";

    public static File firstOrDefault(List<File> list, final String title) {
        return firstOrDefault(list, title, null);
    }

    public static File firstOrDefault(List<File> list, final String title, final String parentId) {
        final boolean isParentRoot = parentId != null && parentId.equalsIgnoreCase(ROOT_ID);
        return ListHelper.firstOrDefault(list, new ListHelper.Predicate<File>() {
            @Override
            public boolean match(File item) {
                boolean result = item.getTitle().equalsIgnoreCase(title);
                if (!result || parentId == null || parentId.isEmpty()) {
                    return result;
                }

                return ListHelper.any(item.getParents(), new ListHelper.Predicate<ParentReference>() {
                    @Override
                    public boolean match(ParentReference item) {
                        if(isParentRoot){
                            return item.getIsRoot();
                        }
                        return item.getId().equalsIgnoreCase(parentId);
                    }
                });
            }
        });
    }

    public static boolean isFolder(File file) {
        return file.getMimeType().equalsIgnoreCase(FOLDER_MIME_TYPE);
    }
}
