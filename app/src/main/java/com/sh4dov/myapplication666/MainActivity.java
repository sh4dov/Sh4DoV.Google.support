package com.sh4dov.myapplication666;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;
import com.sh4dov.google.MyClass;

import java.io.IOException;
import java.util.Collections;

class Task extends AsyncTask<Activity, Integer, Integer>
{
    @Override
    protected Integer doInBackground(Activity... contexts) {
        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(contexts[0], Collections.singleton(DriveScopes.DRIVE));
        Account[] allAccounts = credential.getAllAccounts();
        credential.setSelectedAccountName(allAccounts[0].name);
        Drive service = new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential).build();

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
            e.printStackTrace();new MyClass();
        }

        return 1;
    }
}

public class MainActivity extends Activity {

    private Drive service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Task().execute(this);

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    int i=0;
                    // App is authorized, you can go back to sending the API request
                } else {
                    // User denied access, show him the account chooser again
                }
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
