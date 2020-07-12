package com.example.lockscreen.fingerprint;

public interface FingerPrintListener {
    void onSuccess();

    void onFailed();

    void onError(CharSequence errorString);

    void onHelp(CharSequence helpString);
}
