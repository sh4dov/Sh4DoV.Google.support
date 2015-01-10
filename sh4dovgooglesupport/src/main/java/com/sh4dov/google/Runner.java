package com.sh4dov.google;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.ConcurrentLinkedQueue;

class Runner<T> {
    private final Activity activity;
    private final Service service;
    private final Object sync = new Object();
    private ConnectTask task = null;
    private ConcurrentLinkedQueue<Operation<T>> operations = new ConcurrentLinkedQueue<Operation<T>>();


    public Runner(Activity activity, Service service) {
        this.activity = activity;
        this.service = service;
    }

    public Runner<T> addOperation(Operation<T> operation) {
        operations.add(new ExecuteOperation<T>(this, activity, operation));
        return this;
    }

    public void run() {
        synchronized (sync) {
            if (!isConnected()) {
                Operation operation = operations.poll();
                if (operation == null) {
                    return;
                }

                Log.d(Runner.class.getName(), "Executing new operation");
                task = new ConnectTask(operation, service);
                task.execute();
            } else {
                Log.e(Runner.class.getName(), "Cannot run, task is not finished yet.");
            }
        }
    }

    public boolean isConnected() {
        synchronized (sync) {
            return task != null && task.getStatus() == AsyncTask.Status.RUNNING;
        }
    }

    public boolean isFinished() {
        synchronized (sync) {
            return task != null && task.getStatus() == AsyncTask.Status.FINISHED;
        }
    }

    public void close() {
        operations.clear();
        synchronized (sync) {
            if (isConnected()) {
                task.cancel(true);
            }
        }
    }
}
