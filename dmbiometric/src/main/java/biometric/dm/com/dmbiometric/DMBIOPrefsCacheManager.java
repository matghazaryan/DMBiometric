package biometric.dm.com.dmbiometric;


import android.content.Context;
import android.content.SharedPreferences;


final class DMBIOPrefsCacheManager {

    private static DMBIOPrefsCacheManager instance;
    private SharedPreferences mSharedPreferences;

    private DMBIOPrefsCacheManager() {
    }

    public static DMBIOPrefsCacheManager getInstance() {
        if (instance == null) {
            instance = new DMBIOPrefsCacheManager();
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
