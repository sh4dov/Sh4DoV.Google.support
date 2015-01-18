package com.sh4dov.google;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.sh4dov.google.utils.ExceptionDescriptor;

import java.io.IOException;

class ExecuteOperation<T> implements Operation<T> {
    private Activity activity;
    private Operation<T> operation;
    private int requestCode;
    private Runner runner;

    public ExecuteOperation(Runner runner, Activity activity, Operation<T> operation, int requestCode) {
        this.runner = runner;
        this.activity = activity;
        this.operation = operation;
        this.requestCode = requestCode;
    }

    @Override
    public void execute(final T scope) {
        try {
            operation.execute(scope);
        } catch (UserRecoverableAuthIOException e) {
            Log.d(PackageInfo.TAG, operation.getClass().getName() + ": " + new ExceptionDescriptor().getDescription(e));
            final Intent intent = e.getIntent();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.startActivityForResult(intent, requestCode);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            onFailed(e);
        }
    }

    @Override
    public void onFailed(final Exception e) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                operation.onFailed(e);
            }
        });
    }

    @Override
    public void onFinished() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                runner.run();
                operation.onFinished();
            }
        });
    }

    Operation<T> getOperation() {
        return operation;
    }
}
