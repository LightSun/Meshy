package com.heaven7.java.meshy.signature;

import com.heaven7.java.meshy.MessageSignature;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * @author heaven7
 * @since 1.0.2
 */
public final class MD5SaltSignature implements MessageSignature {

    @Override
    public String signature(byte[] data, String key) {
        return MD5Util.encode(new String(data, StandardCharsets.UTF_8) + key);
    }
}

/**
 * @author heaven7
 * @since 1.0.2
 */
class MD5Util {

    private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F' };

    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    /**
     * encode the message by md5
     * @param message the message to encrypt
     *                @return the result
     */
    public static String encode(String message){
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
        byte[] result = md5.digest(message.getBytes(StandardCharsets.UTF_8));
        return toHexString(result).toLowerCase(Locale.CHINA);
    }
}