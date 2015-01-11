package com.sh4dov.google;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
class ConnectTask<T extends Service> extends AsyncTask<Void, Void, Void> {
    private ExecuteOperation<T> operation;
    private T service;

    public ConnectTask(ExecuteOperation<T> operation, T service) {
        this.operation = operation;
        this.service = service;
    }

    Operation<T> getOperation(){return operation.getOperation();}

    @Override
    protected Void doInBackground(Void... voids) {
        operation.execute(service);
        Log.d(PackageInfo.TAG, "Executed operation " + operation.getOperation().getClass().getName());
        return null;
    }
}
