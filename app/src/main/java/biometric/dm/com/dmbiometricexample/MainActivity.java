package biometric.dm.com.dmbiometricexample;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

import biometric.dm.com.dmbiometric.constants.IBIOConstants;
import biometric.dm.com.dmbiometric.listeners.IDMBiometricListener;
import biometric.dm.com.dmbiometric.main.DMBiometricManager;
import biometric.dm.com.dmbiometric.prepare.DMBiometricConfigs;

public class MainActivity extends AppCompatActivity implements IDMBiometricListener<User> {

    private DMBiometricManager<User> mBiometricManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            findViewById(R.id.btn_encrypt).setOnClickListener(v -> init(IBIOConstants.EncryptionMode.ENCRYPT));
            findViewById(R.id.btn_decrypt).setOnClickListener(v -> init(IBIOConstants.EncryptionMode.DECRYPT));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init(final IBIOConstants.EncryptionMode mode) {

        @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.biometric_v23_example, null);
        final TextView textView = view.findViewById(R.id.tv_status);


        final User user1 = new User("John@gmail.com", "1");
        final User user2 = new User("John@gmail.com", "2");
        final User user3 = new User("John@gmail.com", "3");

        final User user = new User("John@gmail.com", "1234567899999", Arrays.asList(user1, user2, user3));

        final DMBiometricConfigs<User> configs = new DMBiometricConfigs<User>(this)
                .setTitle("Title")
                .setSubtitle("SubTitle")
                .setDescription("Description")
                .setNegativeButtonText("Button cancel")
                .setClass(User.class)
                .setBiometricListener(this)
                .setObjectForEncrypt(user)
                .setEncrypt(mode)
                .setDialogViewV23(view)
                .setThemeDialogV23(R.style.Theme_Design_Light_BottomSheetDialog)
                .setUpdateStatusV23Listener(textView::setText);


        mBiometricManager = new DMBiometricManager<>(configs);

        mBiometricManager.showBiometric();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBiometricManager != null) {
            mBiometricManager.onStop();
        }
    }

    @Override
    public void onSuccessEncrypted() {

    }

    @Override
    public void onSuccessDecrypted(final User user) {

    }

    @Override
    public void onFailed(final IBIOConstants.FailedType type, final int helpCode, final CharSequence helpString) {
        switch (type) {
            case SDK_VERSION_NOT_SUPPORTED:
                break;
            case BIOMETRIC_AUTHENTICATION_NOT_SUPPORTED:
                break;
            case BIOMETRIC_AUTHENTICATION_NOT_AVAILABLE:
                break;
            case BIOMETRIC_AUTHENTICATION_PERMISSION_NOT_GRANTED:
                break;
            case AUTHENTICATION_HELP:
                break;
            case AUTHENTICATION_CANCELLED:
                break;
            case AUTHENTICATION_FAILED:
                break;
            case AUTHENTICATION_FAILED_EXCEPTION:
                break;
            case BIOMETRIC_SENSOR_DISABLED:
                break;
            case INCORRECT_CONFIGS:
                break;
            case OTHER:
                break;
        }
    }
}
