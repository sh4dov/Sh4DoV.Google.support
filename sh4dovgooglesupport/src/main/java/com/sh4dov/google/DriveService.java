package com.sh4dov.google;

import android.app.Activity;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.sh4dov.google.listeners.DownloadFileListener;
import com.sh4dov.google.listeners.GetFilesListener;
import com.sh4dov.google.listeners.OnFailedListener;
import com.sh4dov.google.listeners.UploadFileListener;

import java.util.ArrayList;
import java.util.Collection;

public class DriveService implements Service {
    public final static String DRIVE = DriveScopes.DRIVE;
    public final static String DRIVE_APPDATA = DriveScopes.DRIVE_APPDATA;
    public final static String USER_INFO_PROFILE = "https://www.googleapis.com/auth/userinfo.profile";
    private String accountName;
    private Activity activity;
    private Runner<DriveService> runner;
    private String applicationName;
    private Collection<String> scopes = new ArrayList<String>();

    public DriveService(Activity activity) {
        this.activity = activity;
        runner = new Runner<DriveService>(activity, this);
    }

    public DriveService setAccountName(String accountName) {
        this.accountName = accountName;
        return this;
    }

    public DriveService setApplicationName(String name) {
        applicationName = name;
        return this;
    }

    public DriveService addScope(String scope) {
        if (!scopes.contains(scope)) {
            scopes.add(scope);
        }

        return this;
    }

    public void close() {
        runner.close();
    }

    public boolean isFinished() {
        return runner.isFinished();
    }

    public boolean isConnected() {
        return runner.isConnected();
    }

    public void getFiles(GetFilesListener getFilesListener, OnFailedListener onFailedListener) {
        GetFilesOperation operation = new GetFilesOperation(activity, getFilesListener, onFailedListener);
        runner.addOperation(operation, getFilesListener.getRequestCode());
        runner.run();
    }

    public void downloadFile(File file, DownloadFileListener listener, OnFailedListener onFailedListener) {
        DownloadFileOperation operation = new DownloadFileOperation(activity, file, listener, onFailedListener);
        runner.addOperation(operation, listener.getRequestCode());
        runner.run();
    }

    public void uploadFile(File file, byte[] content, UploadFileListener listener, OnFailedListener onFailedListener) {
        UploadFileOperation operation = new UploadFileOperation(activity, file, content, listener, onFailedListener);
        runner.addOperation(operation, listener.getRequestCode());
        runner.run();
    }

    public void uploadFolder(File file, UploadFileListener listener, OnFailedListener onFailedListener){
        UploadedFolderOperation operation = new UploadedFolderOperation(activity, file, listener, onFailedListener);
        runner.addOperation(operation, listener.getRequestCode());
        runner.run();
    }

    protected Drive getDrive() {
        Drive.Builder builder = new Drive.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), getCredential());
        if (applicationName != null) {
            builder.setApplicationName(applicationName);
        }

        return builder.build();
    }

    private GoogleAccountCredential getCredential() {
        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(activity, scopes);
        if (accountName != null) {
            credential.setSelectedAccountName(accountName);
        }
        return credential;
    }
}
