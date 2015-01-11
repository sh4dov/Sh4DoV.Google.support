package com.sh4dov.google;

import android.app.Activity;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.drive.Drive;
import com.sh4dov.google.listeners.GetFilesListener;

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

    public DriveService setApplicationName(String name){
        applicationName = name;
        return this;
    }

    public void close(){
        runner.close();
    }

    public boolean isFinished(){
        return runner.isFinished();
    }

    public boolean isConnected(){
        return runner.isConnected();
    }

    public void getFiles(GetFilesListener listener) {
        GetFilesOperation operation = new GetFilesOperation(activity, listener);
        runner.addOperation(operation, listener.getRequestCode());
        runner.run();
    }

    protected Drive getDrive() {
        Drive.Builder builder = new Drive.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), getCredential());
        if(applicationName != null){
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
