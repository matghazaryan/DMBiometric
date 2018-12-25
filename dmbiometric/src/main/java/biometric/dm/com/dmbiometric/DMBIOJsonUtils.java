package biometric.dm.com.dmbiometric;

import com.google.gson.Gson;

final class DMBIOJsonUtils {

    static <T> String objectToJson(final T t) {

        if (t == null) {
            try {
                throw new Exception("Object for encryption can't be null");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new Gson().toJson(t);
    }

    static <T> T jsonToObject(final String json, final Class<T> aClass) {
        if (json == null) {
            try {
                throw new Exception("Object for decryption can't be null");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new Gson().fromJson(json, aClass);
    }
}
