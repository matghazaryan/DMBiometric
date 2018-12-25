package biometric.dm.com.dmbiometric;

import android.annotation.SuppressLint;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;

import java.util.concurrent.Executor;


@RequiresApi(api = Build.VERSION_CODES.P)
@SuppressLint("MissingPermission")
final class DMBIOV28 implements DMBIOIManager {

    private final DMBIOConfigs configs;
    private final DMBIOBase biometric;

    private CancellationSignal cancellationSignal;

    DMBIOV28(final DMBIOConfigs configs, final DMBIOBase biometric) {
        this.configs = configs;
        this.biometric = biometric;
    }

    @Override
    public void showBiometricDialog() {
        if (configs != null) {
            initBiometric();
        }
    }

    private void initBiometric() {
        cancellationSignal = new CancellationSignal();
        final Executor executor = configs.getContext().getMainExecutor();
        final BiometricPrompt build = new BiometricPrompt.Builder(configs.getContext())
                .setTitle(configs.getTitle())
                .setSubtitle(configs.getSubtitle())
                .setDescription(configs.getDescription())
                .setNegativeButton(configs.getNegativeButtonText(), executor, (dialogInterface, i) -> biometric.onAuthenticationCancelled())
                .build();

        build.authenticate(new BiometricPrompt.CryptoObject(configs.getCipher()), cancellationSignal, executor, new DMBIOIListenerConverterV23ToV28(biometric));
    }

    @Override
    public void cancel() {
        if (cancellationSignal != null && !cancellationSignal.isCanceled()) {
            cancellationSignal.cancel();
        }
    }

    @Override
    public void dismiss() {
        cancel();
    }
}
