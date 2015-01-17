package com.sh4dov.google.listeners;

public class NullUserRecoverableRequestCodeProvider implements UserRecoverableRequestCodeProvider {
    private static final UserRecoverableRequestCodeProvider instance = new NullUserRecoverableRequestCodeProvider();

    private NullUserRecoverableRequestCodeProvider() {
    }

    public static UserRecoverableRequestCodeProvider getInstance() {
        return instance;
    }

    @Override
    public int getRequestCode() {
        return 0;
    }
}
