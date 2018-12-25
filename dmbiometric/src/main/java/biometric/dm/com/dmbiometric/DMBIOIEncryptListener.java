package biometric.dm.com.dmbiometric;

interface DMBIOIEncryptListener {

    void onSuccess(String encryptedText);

    void onFailed();
}
