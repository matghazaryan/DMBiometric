package biometric.dm.com.dmbiometric;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import javax.crypto.Cipher;


public final class DMBIOManager<T> extends DMBIOPrepareBiometric {

    private final DMBIOConfigs<T> configs;
    private DMBIOIBiometric biometric;

    public DMBIOManager(final DMBIOConfigs<T> configs) {
        this.configs = configs;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isConfigsCorrect(final DMBIOConfigs<T> configs) {

        boolean isSuccess = true;

        try {
            if (configs == null) {
                throw new Exception("DMBiometricConfigs configs can't be null");
            } else if (configs.getBiometricListener() == null) {
                throw new Exception("DMBiometricConfigs biometricListener can't be null");
            } else if (configs.getTClass() == null) {
                throw new Exception("DMBiometricConfigs tClass (for encrypt and decrypt) can't be null");
            } else if (configs.getEncryptionMode() == DMBIOIConstants.EncryptionMode.ENCRYPT && configs.getObjectForEncrypt() == null) {
                throw new Exception("DMBiometricConfigs object for encrypt can't be null");
            } else if (configs.getDialogViewV23() == null) {
                if (configs.getTitle() == null) {
                    throw new Exception("DMBiometricConfigs Dialog title can't be null");
                } else if (configs.getDescription() == null) {
                    throw new Exception("DMBiometricConfigs Dialog description can't be null");
                } else if (configs.getNegativeButtonText() == null) {
                    throw new Exception("DMBiometricConfigs Dialog negative button text can't be null");
                }
            } else {
                if (configs.getUpdateStatusV23Listener() == null) {
                    throw new Exception("DMBiometricConfigs updateStatusV23Listener can't be null");
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            if (configs != null) {
                configs.getBiometricListener().onFailed(DMBIOIConstants.FailedType.INCORRECT_CONFIGS, 0, null);
            }
            isSuccess = false;
        }

        if (isSuccess) {

            final Context context = configs.getContext();

            if (!DMBIOUseConditionUtils.isSdkVersionSupported()) {
                onSdkVersionNotSupported();
                isSuccess = false;
            } else if (!DMBIOUseConditionUtils.isHardwareSupported(context)) {
                onBiometricAuthenticationNotSupported();
                isSuccess = false;
            } else if (!DMBIOUseConditionUtils.isFingerprintAvailable(context)) {
                onBiometricAuthenticationNotAvailable();
                isSuccess = false;
            }
        }

        return isSuccess;
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showBiometric() {
        if (isConfigsCorrect(configs)) {
            DMBIOBiometricPrefsCacheManager.getInstance().Initialize(configs.getContext());
            prepare(configs, new DMBIOIPrepareListener() {
                @Override
                public void onSuccess() {
                    if (DMBIOUseConditionUtils.isBiometricPromptEnabled()) {
                        biometric = new DMBIOBiometricV28(configs, DMBIOManager.this);
                    } else {
                        biometric = new DMBIOBiometricV23(configs, DMBIOManager.this);
                    }

                    biometric.showBiometricDialog();
                }

                @Override
                public void onFailed() {
                    configs.getBiometricListener().onFailed(DMBIOIConstants.FailedType.AUTHENTICATION_FAILED_EXCEPTION, 0, null);
                }
            });
        }
    }

    public void cancel() {
        if (biometric != null) {
            biometric.cancel();
        }
    }

    private void dismiss() {
        if (biometric != null) {
            biometric.dismiss();
        }
    }

    /**
     * Call in onStop()
     */
    public void onStop() {
        cancel();
    }

    private void doEncryptingOrDecrypting(final Cipher cipher, final DMBIOIFinishListener listener) {
        switch (configs.getEncryptionMode()) {
            case ENCRYPT:
                final String json = DMBIOJsonUtils.objectToJson(configs.getObjectForEncrypt());
                final String textForEncrypt = DMBIOPrepareUtils.beforeEncryption(json);

                DMBIOEncryptionAndDecryptionUtils.encryptString(cipher, textForEncrypt, new DMBIOIEncryptListener() {
                    @Override
                    public void onSuccess(final String encryptedText) {
                        DMBIOBasePrefUtils.setData(encryptedText);
                        configs.getBiometricListener().onSuccessEncrypted();

                        listener.onFinish();
                    }

                    @Override
                    public void onFailed() {
                        configs.getBiometricListener().onFailed(DMBIOIConstants.FailedType.AUTHENTICATION_FAILED_EXCEPTION, 0, null);

                        listener.onFinish();
                    }
                });
                break;
            case DECRYPT:
                final String textForDecrypt = DMBIOBasePrefUtils.getData();
                DMBIOEncryptionAndDecryptionUtils.decryptString(cipher, textForDecrypt, new DMBIOIDecryptListener() {
                    @Override
                    public void onSuccess(final String decryptedText) {
                        final String json = DMBIOPrepareUtils.afterDecryption(decryptedText);
                        final T t = DMBIOJsonUtils.jsonToObject(json, configs.getTClass());
                        configs.getBiometricListener().onSuccessDecrypted(t);

                        listener.onFinish();
                    }

                    @Override
                    public void onFailed() {
                        configs.getBiometricListener().onFailed(DMBIOIConstants.FailedType.AUTHENTICATION_FAILED_EXCEPTION, 0, null);

                        listener.onFinish();
                    }
                });
                break;
            default:
                listener.onFinish();
                configs.getBiometricListener().onFailed(DMBIOIConstants.FailedType.AUTHENTICATION_FAILED, 0, null);
        }
    }

    @Override
    protected void onSdkVersionNotSupported() {
        configs.getBiometricListener().onFailed(DMBIOIConstants.FailedType.SDK_VERSION_NOT_SUPPORTED, 0, null);
    }

    @Override
    protected void onBiometricAuthenticationNotSupported() {
        configs.getBiometricListener().onFailed(DMBIOIConstants.FailedType.BIOMETRIC_AUTHENTICATION_NOT_SUPPORTED, 0, null);
    }

    @Override
    protected void onBiometricAuthenticationNotAvailable() {
        configs.getBiometricListener().onFailed(DMBIOIConstants.FailedType.BIOMETRIC_AUTHENTICATION_NOT_AVAILABLE, 0, null);
    }

    @Override
    protected void onAuthenticationFailed() {
        configs.getBiometricListener().onFailed(DMBIOIConstants.FailedType.AUTHENTICATION_FAILED, 0, null);
    }

    @Override
    protected void onAuthenticationCancelled() {
        configs.getBiometricListener().onFailed(DMBIOIConstants.FailedType.AUTHENTICATION_CANCELLED, 0, null);
    }

    @Override
    protected void onAuthenticationSuccessful(final Cipher cipher) {
        doEncryptingOrDecrypting(cipher, this::dismiss);
    }

    @Override
    protected void onAuthenticationHelp(final int helpCode, final CharSequence helpString) {
        configs.getBiometricListener().onFailed(DMBIOIConstants.FailedType.AUTHENTICATION_HELP, helpCode, helpString);
    }

    @Override
    protected void onAuthenticationError(final int errorCode, final CharSequence errString) {
        switch (errorCode) {
            case 10:
                configs.getBiometricListener().onFailed(DMBIOIConstants.FailedType.AUTHENTICATION_CANCELLED, errorCode, errString);
                break;
            case 9:
                configs.getBiometricListener().onFailed(DMBIOIConstants.FailedType.BIOMETRIC_SENSOR_DISABLED, errorCode, errString);
                break;
//            case 7:
//                configs.getBiometricListener().onFailed(DMConstants.FailedType.OTHER, errorCode, errString);
//                break;
            default:
                configs.getBiometricListener().onFailed(DMBIOIConstants.FailedType.OTHER, errorCode, errString);
        }
    }
}
