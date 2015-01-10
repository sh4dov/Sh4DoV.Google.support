package com.sh4dov.google;

import android.app.Activity;

class ExecuteOperation<T> implements Operation<T> {
    private Runner connector;
    private Activity activity;
    private Operation<T> operation;

    public ExecuteOperation(Runner connector, Activity activity, Operation<T> operation) {
        this.connector = connector;
        this.activity = activity;
        this.operation = operation;
    }

    @Override
    public void execute(T scope) {
        operation.execute(scope);
    }

    @Override
    public void onFinished() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connector.run();
                operation.onFinished();
            }
        });
    }

    @Override
    public void onFailed() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                operation.onFailed();
            }
        });
    }
}
