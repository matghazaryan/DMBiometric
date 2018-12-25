package biometric.dm.com.dmbiometric;

interface DMBIOIDecryptListener {

    void onSuccess(String decryptedText);

    void onFailed();
}
