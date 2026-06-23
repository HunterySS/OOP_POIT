package processing;

/**
 * Legacy plugin API received from another student.
 */
public interface LegacyTextPlugin {
    /**
     * @return legacy plugin name for UI.
     */
    String getName();

    /**
     * Encodes text before writing to file.
     */
    String encode(String input);

    /**
     * Decodes text after reading from file.
     */
    String decode(String input);

    /**
     * Configures shift amount for encoding algorithm.
     */
    void setShift(int shift);

    /**
     * @return currently configured shift value.
     */
    int getShift();
}
