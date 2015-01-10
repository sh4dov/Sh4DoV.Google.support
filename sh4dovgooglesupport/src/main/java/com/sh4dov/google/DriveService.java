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
    private int reconnectRequestCode;
    private String scope;
    private Runner<DriveService> runner;

    public DriveService(Activity activity, int reconnectRequestCode, String scope) {
        this.activity = activity;
        this.reconnectRequestCode = reconnectRequestCode;
        this.scope = scope;
        runner = new Runner<DriveService>(activity, this);
    }

    public DriveService setAccountName(String accountName) {
        this.accountName = accountName;
        return this;
    }

    public void getFiles(GetFilesListener listener) {
        GetFilesOperation operation = new GetFilesOperation(listener);
        runner.addOperation(operation);
        runner.run();
    }

    protected Drive getDrive() {
        return new Drive.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), getCredential()).build();
    }

    private GoogleAccountCredential getCredential() {
        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(activity, Collections.singleton(scope));
        if (accountName != null) {
            credential.setSelectedAccountName(accountName);
        }
        return credential;
    }
}
