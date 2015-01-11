package com.sh4dov.google;

import com.sh4dov.google.listeners.OnFailedListener;

class OnFailedListenerNullObject implements OnFailedListener {
    private static final OnFailedListenerNullObject instance = new OnFailedListenerNullObject();

    private OnFailedListenerNullObject() {
    }

    public static OnFailedListenerNullObject getInstance() {
        return instance;
    }

    @Override
    public void onFailed(Exception e) {
    }
}
