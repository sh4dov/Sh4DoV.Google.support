package com.sh4dov.google;

import android.app.Activity;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.drive.Drive;
import com.sh4dov.google.listeners.DownloadFileListener;
import com.sh4dov.google.listeners.GetFilesListener;
import com.sh4dov.google.listeners.OnFailedListener;
import com.sh4dov.google.listeners.UploadFileListener;
import com.sh4dov.google.model.File;

import java.util.Collections;

public class DriveService implements Service {
    private String accountName;
    private Activity activity;
    private String scope;
    private Runner<DriveService> runner;
    private String applicationName;

    public DriveService(Activity activity, String scope) {
        this.activity = activity;
        this.scope = scope;
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

    public void uploadFile(File file, byte[] content, UploadFileListener listener, OnFailedListener onFailedListener){
        UploadFileOperation operation = new UploadFileOperation(activity, file, content, listener, onFailedListener);
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
        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(activity, Collections.singleton(scope));
        if (accountName != null) {
            credential.setSelectedAccountName(accountName);
        }
        return credential;
    }
}
