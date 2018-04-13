package com.supreme.abc.supremechat_client;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

//client side password encryption
public class AdvancedCrypto {
    private static String algorithm = "AES";

    // Performs Encryption
    public static String encrypt(String plainText) throws Exception {
        Key key = new SecretKeySpec(new byte[] { 'A', 'S', 'e', 'c', 'u', 'r', 'e',
                'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' }, algorithm);
        Cipher chiper = Cipher.getInstance(algorithm);
        chiper.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = chiper.doFinal(plainText.getBytes());
        String encryptedValue = encode(encVal);
        return encryptedValue;
    }

    private static String encode(byte[] byteArray) {
        StringBuilder buf = new StringBuilder();
        int intVal = 0;
        String frag = "";
        for (byte b : byteArray) {
            intVal = (int) (0xff & b);
            frag = Integer.toHexString(intVal);
            if (1 == frag.length()) {
                frag = "0" + frag;
            }
            buf.append(frag);
        }
        return buf.toString();
    }
}