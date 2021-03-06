package org.challenge;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

public class KeyFactory {

    public static final int AES_KEY_SIZE = 256;
    public static final int IV_SIZE = 16;

    public static SecretKeySpec generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(AES_KEY_SIZE);
        SecretKey key = keyGenerator.generateKey();
        return new SecretKeySpec(key.getEncoded(), "AES");
    }

    public static Cipher generateCipher(int mode, byte[] iv, SecretKeySpec secretKeySpec) throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            NoSuchProviderException,
            InvalidAlgorithmParameterException,
            InvalidKeyException {

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(mode, secretKeySpec, spec);
        return cipher;
    }

    static byte[] generateInitializationVector() {
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

}
