package biometric.dm.com.dmbiometric;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

@SuppressLint({"MissingPermission"})
final class DMBIOUseConditionUtils {

    static boolean isBiometricPromptEnabled() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P);
    }

    static boolean isSdkVersionSupported() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    static boolean isHardwareSupported(final Context context) {
        return FingerprintManagerCompat.from(context).isHardwareDetected();
    }

    static boolean isFingerprintAvailable(final Context context) {
        return FingerprintManagerCompat.from(context).hasEnrolledFingerprints();
    }
}
