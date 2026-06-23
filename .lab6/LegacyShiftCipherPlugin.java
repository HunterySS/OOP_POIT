package processing.plugins;

import processing.LegacyTextPlugin;

/**
 * Legacy plugin emulates external lab plugin from another student.
 */
public class LegacyShiftCipherPlugin implements LegacyTextPlugin {
    private int shift = 3;

    @Override
    public String getName() {
        return "Friend Caesar cipher";
    }

    @Override
    public String encode(String input) {
        return shift(input, shift);
    }

    @Override
    public String decode(String input) {
        return shift(input, -shift);
    }

    @Override
    public void setShift(int shift) {
        this.shift = shift;
    }

    @Override
    public int getShift() {
        return shift;
    }

    private String shift(String text, int delta) {
        StringBuilder builder = new StringBuilder(text.length());
        for (char ch : text.toCharArray()) {
            builder.append((char) (ch + delta));
        }
        return builder.toString();
    }
}
