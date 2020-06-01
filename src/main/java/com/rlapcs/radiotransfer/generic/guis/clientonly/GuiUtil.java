package com.rlapcs.radiotransfer.generic.guis.clientonly;

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
}
