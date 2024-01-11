package org.example;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicLong;

public class SerialNumberGenerator {
    private AtomicLong counter = new AtomicLong(0);

    public String generateSerialNumber() {
        long currentTime = System.currentTimeMillis();
        long count = counter.getAndIncrement();
        String input = currentTime + ":" + count;

        return hash(input).substring(0, 20); // Retourne les 20 premiers caractères du hash
    }

    private String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur lors de la génération du hash", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
