package biometric.dm.com.dmbiometric;

final class DMBIOPrepareUtils {

    static String beforeEncryption(final String json) {
        return DMBIOIConstants.DefaultValue.PART_1 +
                json +
                DMBIOIConstants.DefaultValue.PART_2;
    }

    static String afterDecryption(final String decryptedText) {
        return decryptedText.replace(DMBIOIConstants.DefaultValue.PART_1, "").replace(DMBIOIConstants.DefaultValue.PART_2, "");
    }
}
