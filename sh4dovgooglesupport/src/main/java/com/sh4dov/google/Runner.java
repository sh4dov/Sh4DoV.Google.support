package com.sh4dov.google;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.util.concurrent.ConcurrentLinkedQueue;

class Runner<T extends Service> {
    private final Activity activity;
    private final T service;
    private final Object sync = new Object();
    private ConnectTask<T> task = null;
    private ConcurrentLinkedQueue<ExecuteOperation<T>> operations = new ConcurrentLinkedQueue<ExecuteOperation<T>>();


    public Runner(Activity activity, T service) {
        this.activity = activity;
        this.service = service;
    }

    public Runner<T> addOperation(Operation<T> operation, int requestCode) {
        operations.add(new ExecuteOperation<T>(this, activity, operation, requestCode));
        return this;
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public void run() {
        synchronized (sync) {
            if (!isConnected()) {
                ExecuteOperation<T> operation = operations.poll();
                if (operation == null) {
                    return;
                }

                Log.d(PackageInfo.TAG, "Executing operation " + operation.getOperation().getClass().getName());
                task = new ConnectTask<T>(operation, service);
                task.execute();
            } else {
                Log.e(PackageInfo.TAG, "Cannot run, task is not finished yet.");
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public boolean isConnected() {
        synchronized (sync) {
            return task != null && task.getStatus() == AsyncTask.Status.RUNNING;
        }
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public boolean isFinished() {
        synchronized (sync) {
            return task != null && task.getStatus() == AsyncTask.Status.FINISHED;
        }
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public void close() {
        Log.d(PackageInfo.TAG, "Pending operations to close: " + operations.size());
        operations.clear();
        synchronized (sync) {
            if (isConnected()) {
                Log.d(PackageInfo.TAG, "Closing running operation: " + task.getOperation().getClass().getName());
                task.cancel(true);
            }
        }

        Log.d(PackageInfo.TAG, "Closed operations");
    }
}
