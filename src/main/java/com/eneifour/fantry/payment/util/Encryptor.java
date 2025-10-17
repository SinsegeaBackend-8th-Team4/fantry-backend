package com.eneifour.fantry.payment.util;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class Encryptor {
    private static final String SHA_256 = "SHA-256";

    public static String createOrderId(String username, Integer itemId) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(SHA_256);
        long currentTime = System.currentTimeMillis();
        long nanoTime = System.nanoTime();
        String composed = username + "-" + itemId + "-" + currentTime + "-" + nanoTime;
        byte[] hashBytes = md.digest(composed.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
