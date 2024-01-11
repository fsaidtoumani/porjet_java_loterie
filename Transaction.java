package org.example;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private static String id=null;


    private static String getlasthash(){
        return id;
    }


    protected static String generateUniqueId() throws NoSuchAlgorithmException {
        if(getlasthash()==null){
            id = Long.toString(System.currentTimeMillis());
        }
        String data = getlasthash();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes());

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
            if (hexString.length() >= 20) { // Limiter à 20 caractères
                break;
            }
        }

        id=hexString.toString();
        return  getlasthash();
    }



}
