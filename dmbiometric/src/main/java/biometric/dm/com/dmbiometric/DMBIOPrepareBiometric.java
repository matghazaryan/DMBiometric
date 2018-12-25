package biometric.dm.com.dmbiometric;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;


@RequiresApi(api = Build.VERSION_CODES.M)
abstract class DMBIOPrepareBiometric extends DMBIOBaseBiometric {

    private KeyStore keyStore;

    private void generateKey(final DMBIOIPrepareListener listener) {
        try {

            keyStore = KeyStore.getInstance(DMBIOIConstants.DefaultValue.ANDROID_KEY_STORE);
            keyStore.load(null);

            if (!keyStore.containsAlias(DMBIOIConstants.DefaultValue.KEY_ALIAS)) {
                final KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, DMBIOIConstants.DefaultValue.ANDROID_KEY_STORE);
                keyGenerator.init(new
                        KeyGenParameterSpec.Builder(DMBIOIConstants.DefaultValue.KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build());

                keyGenerator.generateKey();
            }
            listener.onSuccess();
        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            listener.onFailed();
            exc.printStackTrace();
        }
    }

    private void initCipher(final DMBIOConfigs configs, final DMBIOIPrepareListener listener) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);

        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            e.printStackTrace();
            listener.onFailed();
            return;
        }

        try {
            keyStore.load(null);
            final SecretKey key = (SecretKey) keyStore.getKey(DMBIOIConstants.DefaultValue.KEY_ALIAS, null);

            switch (configs.getEncryptionMode()) {
                case ENCRYPT:
                    cipher.init(Cipher.ENCRYPT_MODE, key);
                    DMBIOBasePrefUtils.setIV(Base64.encodeToString(cipher.getIV(), Base64.NO_WRAP));
                    break;
                case DECRYPT:
                    final byte[] iv = Base64.decode(DMBIOBasePrefUtils.getIV(), Base64.NO_WRAP);
                    final IvParameterSpec ivSpec = new IvParameterSpec(iv);

                    try {
                        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
                    } catch (InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                        listener.onFailed();
                        return;
                    }
                    break;
            }

            configs.setCipher(cipher);

            listener.onSuccess();

        } catch (KeyStoreException
                | CertificateException
                | UnrecoverableKeyException
                | IOException
                | NoSuchAlgorithmException
                | InvalidKeyException e) {
            e.printStackTrace();
            listener.onFailed();
        }
    }

    void prepare(final DMBIOConfigs configs, final DMBIOIPrepareListener listener) {
        generateKey(new DMBIOIPrepareListener() {
            @Override
            public void onSuccess() {
                initCipher(configs, listener);
            }

            @Override
            public void onFailed() {
                listener.onFailed();
            }
        });
    }
}
