package com.sh4dov.google;

interface Operation<T> {
    void execute(T scope);

    void onFinished();

    void onFailed();
}
