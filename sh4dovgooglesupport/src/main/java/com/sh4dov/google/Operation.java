package com.sh4dov.google;

import java.io.IOException;

interface Operation<T> {

    void execute(T scope) throws IOException;

    void onFinished();

    void onFailed(Exception e);
}
