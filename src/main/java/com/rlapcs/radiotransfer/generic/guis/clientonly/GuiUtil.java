package com.rlapcs.radiotransfer.generic.guis.clientonly;

import com.rlapcs.radiotransfer.generic.guis.FormattedValue;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                return new FormattedValue(Math.round(100d * value / Math.pow(10, i * 3)) / 100d, orders[i - 1], unit);
        }
        return new FormattedValue(value, "", unit);
    }

    public static void formatLines(List<String> lines, int maxAllowableSize, boolean needsIt) {
        if (needsIt) {
            List<String> linesToIterate = new ArrayList<>();
            linesToIterate.addAll(lines);
            int index = 0;
            for (String line : linesToIterate) {
                //sendDebugMessage("og line: " + line + " : index " + index);
                //sendDebugMessage("oopsies: " + getLineLength(line) + " : " + (maxAllowableSize - 4));

                if (getLineLength(line) > maxAllowableSize - 10) {
                    ArrayList<String> pieces = new ArrayList<>();
                    while (getLineLength(line) > maxAllowableSize - 8) {
                        //sendDebugMessage("Lineeeee " + line);
                        String chunk = getMaxWithinSize(line, maxAllowableSize - 8);
                        //sendDebugMessage("chonk " + chunk);

                        int posOfLastReset = chunk.contains("\u00a7r") ? chunk.lastIndexOf("\u00a7r") + 2 : 0;
                        String lookIn = chunk.substring(posOfLastReset);
                        List<String> allMatches = new ArrayList<>();
                        Matcher m = Pattern.compile("(?i)\u00a7[0-9A-FK-OR]").matcher(lookIn);
                        while (m.find())
                            allMatches.add(m.group());
                        String formatters = String.join("", allMatches);

                        pieces.add(chunk);
                        line = formatters + line.substring(chunk.length());
                        //sendDebugMessage("newlineeee " + line);
                    }
                    pieces.add(line);
                    lines.remove(index);
                    Collections.reverse(pieces);
                    for (String piece : pieces)
                        lines.add(index, piece);
                    index += pieces.size() - 1;
                }
                index++;
            }
        }
    }

    private static String getMaxWithinSize(String text, int maxSize) {
        String[] words = text.split(" ");
        //sendDebugMessage(text);
        int maxIndex = 0;
        while (getLineLength(combineWords(words, maxIndex)) < maxSize) {
            //sendDebugMessage(combineWords(words, maxIndex));
            maxIndex++;
        }
        return combineWords(words, maxIndex - 1);
    }

    private static String combineWords(String[] words, int amount) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            builder.append(words[i]);
            if (i < amount - 1)
                builder.append(" ");
        }
        return builder.toString();
    }
}
