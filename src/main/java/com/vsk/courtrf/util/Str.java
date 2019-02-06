package com.vsk.courtrf.util;

public class Str {

    public static boolean stNotEmpty( final String s ) {
        // Null-safe, short-circuit evaluation.
        return !(s == null || s.trim().isEmpty());
    }
    public static boolean stEmpty( final String s ) {
        // Null-safe, short-circuit evaluation.
        return (s == null || s.trim().isEmpty());
    }

    public static String stClear(String p0) {
        return p0.replace("<b>", "")
                .replace("</b>","");
    }

    public static String fromUnicode(String unicode) {
        String str = unicode.replace("\\", "");
        String[] arr = str.split("u");
        StringBuffer text = new StringBuffer();
        for (int i = 1; i < arr.length; i++) {
            try {
                int hexVal = Integer.parseInt(arr[i], 16);
                text.append(Character.toChars(hexVal));
            } catch ( NumberFormatException e) {
                text.append(arr[i]);
            }
        }
        return text.toString();
    }

    public static String toUnicode(String text) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < text.length(); i++) {
            int codePoint = text.codePointAt(i);
            // Skip over the second char in a surrogate pair
            if (codePoint > 0xffff) {
                i++;
            }
            String hex = Integer.toHexString(codePoint);
            sb.append("\\u");
            for (int j = 0; j < 4 - hex.length(); j++) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }

}
