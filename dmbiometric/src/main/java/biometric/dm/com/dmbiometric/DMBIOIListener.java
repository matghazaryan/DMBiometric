package biometric.dm.com.dmbiometric;


public interface DMBIOIListener<T> {

    default void onSuccessEncrypted() {
    }

    default void onSuccessDecrypted(final T t) {
    }

    void onFailed(final DMBIOIConstants.FailedType type, final int helpCode, final CharSequence helpString);
}
