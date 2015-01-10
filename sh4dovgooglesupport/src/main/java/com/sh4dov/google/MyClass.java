package com.sh4dov.google;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Collections;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
class Task extends AsyncTask<Activity, Integer, Integer>
{
    @Override
    protected Integer doInBackground(Activity... contexts) {
        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(contexts[0], Collections.singleton(DriveScopes.DRIVE));
        Account[] allAccounts = credential.getAllAccounts();
        credential.setSelectedAccountName(allAccounts[0].name);
        Drive service = new Drive.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), credential).build();

        try {
            // Try to perform a Drive API request, for instance:
            FileList list = service.files().list().execute();
            int i=0;
        } catch (UserRecoverableAuthIOException e) {
            contexts[0].startActivityForResult(e.getIntent(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }catch(VerifyError e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }

        return 1;
    }
}

public class MyClass {
}


