package com.shawn.cyber.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author Shawn Pan
 * Date  2024/12/11 21:37
 */
public class CyberWdaUtils {
    public static String Base64Decoder(String base64String){
        return new String(Base64.getDecoder().decode(base64String), StandardCharsets.UTF_8);
    }

    public static String toUnicode(String s){
        String reg = "[\u4e00-\u9fa5]";
        Pattern p = Pattern.compile(reg);
        char[] unicodeChars = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(char c:unicodeChars) {
            Matcher m = p.matcher(String.valueOf(c));
            if (m.find()) {
                sb.append("\\u" + Integer.toHexString(c | 0x10000).substring(1));
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
