package com.sh4dov.google;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
class ConnectTask extends AsyncTask<Void, Void, Void> {
    private Operation<Service> operation;
    private Service service;

    public ConnectTask(Operation<Service> operation, Service service) {
        this.operation = operation;
        this.service = service;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        operation.execute(service);
        return null;
    }
}
