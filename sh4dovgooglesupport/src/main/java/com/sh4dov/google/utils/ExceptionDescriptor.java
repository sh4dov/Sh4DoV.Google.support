package com.sh4dov.google.utils;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

public class ExceptionDescriptor {
    public String getDescription(UserRecoverableAuthIOException e) {
        if (e == null) {
            return "null";
        }

        StringBuilder sb = new StringBuilder();

        if (e.getMessage() != null) {
            appendLine(sb, e.getMessage());
        }

        if (e.getCause() != null) {
            appendLine(sb, e.getCause().toString());
        }

        return sb.toString();
    }

    private void appendLine(StringBuilder sb, String line) {
        if (sb.length() > 0) {
            sb.append("\r\n");
        }

        sb.append(line);
    }
}
