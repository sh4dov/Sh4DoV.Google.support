package com.sh4dov.google;

import com.sh4dov.google.listeners.OnFailedListener;

public abstract class OperationBase<T extends Service> implements Operation<T> {
    private OnFailedListener onFailedListener;

    public OperationBase(OnFailedListener onFailedListener) {
        this.onFailedListener = onFailedListener != null ? onFailedListener : OnFailedListenerNullObject.getInstance();
    }

    public void onFinished() {
    }

    public void onFailed(Exception e) {
        onFailedListener.onFailed(e);
    }
}
