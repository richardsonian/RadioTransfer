package com.rlapcs.radiotransfer.generic.guis.clientonly;

import com.rlapcs.radiotransfer.generic.guis.FormattedValue;
import net.minecraft.util.text.TextFormatting;

import static com.rlapcs.radiotransfer.util.Debug.sendDebugMessage;

public class GuiUtil {
    public static int getLineLength(String line) {
        char[] letters = TextFormatting.getTextWithoutFormattingCodes(line).toCharArray();
        int length = 1;
        for (char letter : letters) {
            switch (letter) {
                case 't':
                case ' ':
                case 'I':
                    length += 3; break;
                case 'k':
                case 'f':
                case '(':
                case ')':
                    length += 4; break;
                case 'i':
                case '.':
                case ':':
                    length += 1; break;
                case 'l':
                    length += 2; break;
                default:
                    length += 5;
            }
            length += 1;
            //sendDebugMessage("char: " + letter + " : currentLength: " + length);
        }
        //sendDebugMessage("string: " + line + " : totalLength " + length);
        return length;
    }

    public static FormattedValue formatUnit(double value, String unit) {
        String[] orders = { "k", "M", "G", "T", "P" };
        for (int i = 5; i > 0; i--) {
            if (value / Math.pow(10, i * 3) >= 1)
                return new FormattedValue(Math.round(100 * value / Math.pow(10, i * 3)) / 100, orders[i - 1], unit);
        }
        return new FormattedValue(value, "", unit);
    }
}
