package biometric.dm.com.dmbiometric;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;

import javax.crypto.Cipher;


public final class DMBIOConfigs<T> {

    private final Context context;

    private String title;
    private String subtitle;
    private String description;
    private String negativeButtonText;

    private DMBIOIConstants.EncryptionMode encryptionMode;

    private Cipher cipher;

    private DMBIOIListener<T> biometricListener;

    private View dialogViewV23;

    private DMBIOIUpdateStatusListener updateStatusV23Listener;

    private int themeDialogV23 = R.style.BottomSheetDialogTheme;

    private T objectForEncrypt;

    private Class<T> tClass;

    public DMBIOConfigs(final Context context) {
        this.context = context;
    }

    public DMBIOConfigs<T> setTitle(@NonNull final String title) {
        this.title = title;
        return this;
    }

    public DMBIOConfigs<T> setSubtitle(@NonNull final String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public DMBIOConfigs<T> setDescription(@NonNull final String description) {
        this.description = description;
        return this;
    }

    public DMBIOConfigs<T> setNegativeButtonText(@NonNull final String negativeButtonText) {
        this.negativeButtonText = negativeButtonText;
        return this;
    }

    public DMBIOConfigs<T> setEncrypt(final DMBIOIConstants.EncryptionMode encryptionMode) {
        this.encryptionMode = encryptionMode;
        return this;
    }

    void setCipher(final Cipher cipher) {
        this.cipher = cipher;
    }

    public DMBIOConfigs<T> setBiometricListener(final DMBIOIListener<T> biometricListener) {
        this.biometricListener = biometricListener;
        return this;
    }

    public DMBIOConfigs<T> setDialogViewV23(final View dialogViewV23) {
        this.dialogViewV23 = dialogViewV23;
        return this;
    }

    public DMBIOConfigs<T> setUpdateStatusV23Listener(final DMBIOIUpdateStatusListener updateStatusV23Listener) {
        this.updateStatusV23Listener = updateStatusV23Listener;
        return this;
    }

    public DMBIOConfigs<T> setThemeDialogV23(@StyleRes int themeDialogV23) {
        this.themeDialogV23 = themeDialogV23;
        return this;
    }

    public DMBIOConfigs<T> setObjectForEncrypt(final T objectForEncrypt) {
        this.objectForEncrypt = objectForEncrypt;
        return this;
    }

    public DMBIOConfigs<T> setClass(final Class<T> tClass) {
        this.tClass = tClass;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return description;
    }

    public String getNegativeButtonText() {
        return negativeButtonText;
    }

    public DMBIOIConstants.EncryptionMode getEncryptionMode() {
        return encryptionMode;
    }

    Cipher getCipher() {
        return cipher;
    }

    public Context getContext() {
        return context;
    }

    public DMBIOIListener<T> getBiometricListener() {
        return biometricListener;
    }

    public View getDialogViewV23() {
        return dialogViewV23;
    }

    public DMBIOIUpdateStatusListener getUpdateStatusV23Listener() {
        return updateStatusV23Listener;
    }

    public int getThemeDialogV23() {
        return themeDialogV23;
    }

    public T getObjectForEncrypt() {
        return objectForEncrypt;
    }

    public Class<T> getTClass() {
        return tClass;
    }
}
