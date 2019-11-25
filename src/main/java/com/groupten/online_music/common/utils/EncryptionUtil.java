package com.groupten.online_music.common.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

public class EncryptionUtil {
    public static final String KEY_SHA = "SHA";

    public static String encryption(String inputStr) {
        BigInteger sha = null;
        //System.out.println("=======加密前的数据:" + inputStr);
        byte[] inputData = inputStr.getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(KEY_SHA);
            messageDigest.update(inputData);
            sha = new BigInteger(messageDigest.digest());
            return sha.toString(32);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
