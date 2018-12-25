package biometric.dm.com.dmbiometric;


import android.content.Context;
import android.content.SharedPreferences;


final class DMBIOBiometricPrefsCacheManager {

    private static DMBIOBiometricPrefsCacheManager instance;
    private SharedPreferences mSharedPreferences;

    private DMBIOBiometricPrefsCacheManager() {
    }

    public static DMBIOBiometricPrefsCacheManager getInstance() {
        if (instance == null) {
            instance = new DMBIOBiometricPrefsCacheManager();
        }

        return instance;
    }

    void Initialize(final Context context) {
        mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    void putInCache(final String key, final String value) {
        mSharedPreferences.edit().putString(key, value).apply();
    }

    String getStringFromCache(final String key, final String defValue) {
        final String v = mSharedPreferences.getString(key, null);
        return v != null ? v : defValue;
    }
}
