package biometric.dm.com.dmbiometric;


final class DMBIOBasePrefUtils {

    public static String getData() {
        return DMBIOBiometricPrefsCacheManager.getInstance().getStringFromCache(DMBIOIConstants.PrefKey.DATA, "");
    }

    public static void setData(final String data) {
        DMBIOBiometricPrefsCacheManager.getInstance().putInCache(DMBIOIConstants.PrefKey.DATA, data);
    }

    static String getIV() {
        return DMBIOBiometricPrefsCacheManager.getInstance().getStringFromCache(DMBIOIConstants.PrefKey.KEY_IV, "");
    }

    static void setIV(final String iv) {
        DMBIOBiometricPrefsCacheManager.getInstance().putInCache(DMBIOIConstants.PrefKey.KEY_IV, iv);
    }
}
