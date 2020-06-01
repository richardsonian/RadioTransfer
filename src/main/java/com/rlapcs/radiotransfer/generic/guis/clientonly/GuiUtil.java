package com.rlapcs.radiotransfer.generic.guis.clientonly;

public class GuiUtil {
    public static int getLineLength(String line) {
        char[] letters = line.toCharArray();
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
        }
        return length;
    }
}
