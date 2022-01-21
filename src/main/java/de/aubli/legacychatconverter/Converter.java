package de.aubli.legacychatconverter;


import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author AlexMl Created on 30.10.21 for LegacyChatConverter
 */
public class Converter {

    private static final Map<Character, String> CHAR_CHATCOLOR_MAP = new HashMap<>();
    private static final Map<Character, String> CHAR_NAMEDTEXTCOLOR_MAP = new HashMap<>();
    private static final Map<String, String> CHATCOLOR_NAMEDTEXTCOLOR_MAP = new HashMap<>();
    private static final Map<Character, String> DECORATIONS_CHAR_MAP = new HashMap<>();
    private static final Map<String, String> DECORATIONS_NAME_MAP = new HashMap<>();

    static {
        for (ChatColor color : ChatColor.values()) {
            if (color.ordinal() <= ChatColor.WHITE.ordinal()) {
                CHAR_NAMEDTEXTCOLOR_MAP.put(color.getChar(), "NamedTextColor." + color.name());
            }
            CHAR_NAMEDTEXTCOLOR_MAP.put('r', "NamedTextColor.WHITE");
            CHAR_CHATCOLOR_MAP.put(color.getChar(), "ChatColor." + color.name());
        }
        DECORATIONS_CHAR_MAP.put('k', "TextDecoration.OBFUSCATED");
        DECORATIONS_CHAR_MAP.put('l', "TextDecoration.BOLD");
        DECORATIONS_CHAR_MAP.put('m', "TextDecoration.STRIKETHROUGH");
        DECORATIONS_CHAR_MAP.put('n', "TextDecoration.UNDERLINED");
        DECORATIONS_CHAR_MAP.put('o', "TextDecoration.ITALIC");

        for (ChatColor color : ChatColor.values()) {
            if (CHAR_NAMEDTEXTCOLOR_MAP.containsKey(color.getChar())) {
                CHATCOLOR_NAMEDTEXTCOLOR_MAP.put("ChatColor." + color.name(), CHAR_NAMEDTEXTCOLOR_MAP.get(color.getChar()));
            } else if (DECORATIONS_CHAR_MAP.containsKey(color.getChar())) {
                DECORATIONS_NAME_MAP.put("ChatColor." + color.name(), DECORATIONS_CHAR_MAP.get(color.getChar()));
            }
        }
    }

    //Standalone
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            while (in.hasNext()) {
                String line = in.nextLine();
                System.out.println(">> " + convert(line) + "\n");
            }
        }
    }

    @NotNull
    public static String convert(@Nullable String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        if (input.startsWith("ChatColor.translateAlternateColorCodes") || (input.contains("&") && !input.contains("ChatColor"))) {
            input = input.replace("ChatColor.translateAlternateColorCodes('&', ", "").replace("\"", "");
            return convertAmpersand(input.substring(0, input.length() - 1));//last ')'
        } else {
            return convertSection(input);
        }
    }

    public static String convertSection(String input) {
        StringBuilder result = new StringBuilder("Component.text(");

        String[] parts = input.split("\\+");
        int length = parts.length;
        String nextDecoration = null;
        boolean appended = false;
        boolean noColorAppend = false;
        for (int i = 0; i < length; i++) {
            String part = parts[i].trim();
            String next = parts.length > i + 1 ? parts[i + 1].trim() : null;
            if (part.startsWith("ChatColor.")) {
                part = part.replace(".toString()", "");
                if (nextDecoration != null && !noColorAppend) {
                    result.append(", ").append(nextDecoration).append(")");

                    if (appended) {
                        result.append(")");
                    }

                    nextDecoration = null;
                    result.append(".append(Component.text(");
                    appended = true;
                }

                if (next != null && next.startsWith("ChatColor.")) {
                    next = next.replace(".toString()", "");
                    String color = CHATCOLOR_NAMEDTEXTCOLOR_MAP.containsKey(part) ? CHATCOLOR_NAMEDTEXTCOLOR_MAP.get(part) : CHATCOLOR_NAMEDTEXTCOLOR_MAP.get(next);
                    String style = DECORATIONS_NAME_MAP.containsKey(part) ? DECORATIONS_NAME_MAP.get(part) : DECORATIONS_NAME_MAP.get(next);

                    if (color != null && style != null) {
                        nextDecoration = "Style.style(" + color + ").decorate(" + style + ")";
                    }
                    i++;
                } else {
                    String color = CHATCOLOR_NAMEDTEXTCOLOR_MAP.get(part);
                    String style = DECORATIONS_NAME_MAP.get(part);

                    if (color != null) {
                        nextDecoration = color;
                    } else if (style != null) {
                        nextDecoration = "Style.style(" + style + ")";
                    }
                }
                continue;
            }
            if (part.contains("\\n")) {
                int index = part.indexOf("\\n");
                while (index != -1) {
                    String subPart = part.substring(0, index);
                    result.append(subPart);
                    if (!subPart.isEmpty()) {
                        if (nextDecoration != null) {
                            result.append("\", ").append(nextDecoration);
                        } else {
                            result.append("\"");
                        }
                        result.append(")");
                        if (appended) {
                            result.append(")");
                            appended = false;
                        }
                    }
                    result.append(".append(Component.newline())");
                    part = part.substring(index + 2);//after \n
                    index = part.indexOf("\\n");
                }
                if (!part.isEmpty()) {
                    result.append(".append(Component.text(\"").append(part);
                    appended = true;
                }
            } else {
                result.append(part);
            }
            if (i + 1 < length) {
                if (nextDecoration != null) {
                    result.append(", ").append(nextDecoration).append(")");

                    if (appended) {
                        result.append(")");
                    }
                    result.append(".append(Component.text(");
                    appended = true;
                    noColorAppend = true;
                }
            }
        }
        if (nextDecoration != null) {
            result.append(", ").append(nextDecoration).append(")");
            if (appended) {
                result.append(")");
            }
        } else {
            result.append(")");
        }
        return result.toString();
    }

    public static String convertAmpersand(String input) {
        StringBuilder result = new StringBuilder("Component.text(\"");

        char[] charArray = input.toCharArray();
        int length = charArray.length;
        String nextDecoration = null;
        boolean appended = false;
        for (int i = 0; i < length; i++) {
            char c = charArray[i];
            if (c == '&') {
                if (nextDecoration != null) {
                    result.append("\", ").append(nextDecoration).append(")");

                    if (appended) {
                        result.append(")");
                    }

                    nextDecoration = null;
                    result.append(".append(Component.text(\"");
                    appended = true;
                }

                if (length > i + 3 && charArray[i + 2] == '&') {
                    String color = CHAR_NAMEDTEXTCOLOR_MAP.containsKey(charArray[i + 1]) ? CHAR_NAMEDTEXTCOLOR_MAP.get(charArray[i + 1]) : CHAR_NAMEDTEXTCOLOR_MAP.get(charArray[i + 3]);
                    String style = DECORATIONS_CHAR_MAP.containsKey(charArray[i + 1]) ? DECORATIONS_CHAR_MAP.get(charArray[i + 1]) : DECORATIONS_CHAR_MAP.get(charArray[i + 3]);

                    if (color != null && style != null) {
                        nextDecoration = "Style.style(" + color + ").decorate(" + style + ")";
                    }
                    i += 3;
                    continue;
                } else if (length > i + 1) {
                    String color = CHAR_NAMEDTEXTCOLOR_MAP.get(charArray[i + 1]);
                    String style = DECORATIONS_CHAR_MAP.get(charArray[i + 1]);

                    if (color != null) {
                        nextDecoration = color;
                    } else if (style != null) {
                        nextDecoration = "Style.style(" + style + ")";
                    }
                    i++;
                    continue;
                }
            }
            result.append(c);
        }
        if (nextDecoration != null) {
            result.append("\", ").append(nextDecoration).append(")");
            if (appended) {
                result.append(")");
            }
        } else {
            result.append("\")");
        }
        return result.toString();
    }

    //ChatColor.translateAlternateColorCodes('&', "&a&l(!) &7You have &asuccessfully &7applied a " + cursor.getItemMeta().getDisplayName())
    @NotNull
    public static String convertAmpersandToChatColor(@Nullable String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        if (input.startsWith("ChatColor.translateAlternateColorCodes")) {
            input = input.replace("ChatColor.translateAlternateColorCodes('&', \"", "");
            input = input.substring(0, input.length() - (input.endsWith("\"") ? 2 : 1));//last ')'
        }
        StringBuilder result = new StringBuilder();

        char[] charArray = input.toCharArray();
        int length = charArray.length;
        boolean hasNextColor = false;
        for (int i = 0; i < length; i++) {
            char c = charArray[i];
            if (c == '&') {
                if (length > i + 1) {
                    String color = CHAR_CHATCOLOR_MAP.get(charArray[i + 1]);
                    if (color == null) {
                        result.append("&");
                        continue;
                    }
                    if (result.length() > 0) {
                        long count = result.chars().filter(q -> q == '"').count();
                        if (count % 2 != 0) {
                            result.append("\" ");
                        }
                        result.append("+ ");
                    }
                    result.append(color);
                    if (hasNextColor && (length <= i + 3 || charArray[i + 2] != '&')) {
                        result.append(".toString()");
                    }

                    if (length > i + 3 && charArray[i + 2] == '&') {
                        if (CHAR_CHATCOLOR_MAP.containsKey(charArray[i + 3])) {
                            hasNextColor = true;
                            result.append(" ");
                        } else {
                            result.append(" + \"");
                        }
                    } else {
                        hasNextColor = false;
                        result.append(" + \"");
                    }

                    i++;
                    continue;
                }
            }
            result.append(c);
        }
        long count = result.chars().filter(q -> q == '"').count();
        if (count % 2 != 0) {
            result.append('"');
        }
        return result.toString();
    }

}