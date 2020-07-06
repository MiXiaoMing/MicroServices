package com.microservices.common.utils;

public final class StringUtil {

    public static boolean isEmpty(CharSequence s) {
        if (s == null) {
            return true;
        } else {
            return s.length() == 0;
        }
    }

    /**
     * 字符长度，一个汉字顶两个字符
     */
    public static int characterLength(String source) {
        if (isEmpty(source)) {
            return 0;
        } else {
            int length = 0;
            for (int i = 0; i < source.length(); i++) {
                int ascii = Character.codePointAt(source, i);
                if (ascii >= 0 && ascii <= 255) {
                    length++;

                } else {
                    length += 2;
                }
            }
            return length;
        }
    }
}