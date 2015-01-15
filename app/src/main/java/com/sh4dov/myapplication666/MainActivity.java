package com.sh4dov.myapplication666;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.AccountPicker;
import com.google.api.services.drive.DriveScopes;
import com.sh4dov.google.DriveService;
import com.sh4dov.google.listeners.DownloadFileListener;
import com.sh4dov.google.listeners.GetFilesListener;
import com.sh4dov.google.listeners.UploadFileListener;
import com.sh4dov.google.model.File;
import com.sh4dov.google.model.FileHelper;

import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {

    private DriveService driveService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        choseAccount();

        // new Task().execute(this);

    }

    private void testGDrive(String accountName) {
        driveService = new DriveService(this)
                .addScope(DriveScopes.DRIVE)
                .setAccountName(accountName)
                .setApplicationName("Sample app by Sh4DoV");

        final DownloadFileListener downloadFileListener = new DownloadFileListener() {
            @Override
            public void onDownloadedFile(File file, byte[] content) {
                TextView tv = (TextView) findViewById(R.id.dwnload);
                tv.setText("download file " + file.getTitle() + " completed size: " + content.length);
            }

            @Override
            public void onProgress(File file, double progress) {
                TextView tv = (TextView) findViewById(R.id.dwnload);
                tv.setText("downloading file " + file.getTitle() + ": " + progress);
            }

            @Override
            public int getRequestCode() {
                return 1;
            }
        };

        driveService.getFiles(new GetFilesListener() {
            @Override
            public void onGetFiles(List<File> files) {
                TextView tv = (TextView) findViewById(R.id.txt);
                StringBuilder sb = new StringBuilder();
                File uploadFile = null;
                for (File file : files) {
                    sb.append(file.getTitle() + " size: " + file.getFileSize() + " " + file.getModifiedDate() + "\r\n");

                    if(file.getTitle().equalsIgnoreCase("gp.apk")){
                        driveService.downloadFile(file, downloadFileListener, null);
                    }

                    if(file.getTitle().equalsIgnoreCase("test.txt")){
                        uploadFile = file;
                    }
                }
                tv.setText(sb.toString());

                if(uploadFile == null){
                    uploadFile = FileHelper.createFile();
                    uploadFile.setTitle("test.txt");
                }

                String content = "content " + new Date().toString();
                driveService.uploadFile(uploadFile, content.getBytes(), new UploadFileListener() {
                    @Override
                    public void onUploaded(File file) {

                    }

                    @Override
                    public void onProgress(File file, double progress) {

                    }

                    @Override
                    public int getRequestCode() {
                        return 0;
                    }
                }, null);
            }

            @Override
            public int getRequestCode() {
                return 1;
            }
        }, null);
    }

    protected void onStop() {
        super.onStop();
        if(driveService != null && driveService.isConnected() && !driveService.isFinished()){
            driveService.close();
        }
    }

    private void choseAccount() {
        Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"}, true, null, null, null, null);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    int i = 0;
                    // App is authorized, you can go back to sending the API request
                } else {
                    // User denied access, show him the account chooser again
                }
                break;

            case 2:
                if (resultCode == RESULT_OK) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    testGDrive(accountName);
                }
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
        if (id == R.id.action_exit) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
