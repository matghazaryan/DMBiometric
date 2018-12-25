package biometric.dm.com.dmbiometric;

import javax.crypto.Cipher;

abstract class DMBIOBase {

    protected abstract void onSdkVersionNotSupported();

    protected abstract void onBiometricAuthenticationNotSupported();

    protected abstract void onBiometricAuthenticationNotAvailable();


    protected abstract void onAuthenticationFailed();

    protected abstract void onAuthenticationCancelled();

    protected abstract void onAuthenticationSuccessful(Cipher result);

    protected abstract void onAuthenticationHelp(int helpCode, CharSequence helpString);

    protected abstract void onAuthenticationError(int errorCode, CharSequence errString);
}
