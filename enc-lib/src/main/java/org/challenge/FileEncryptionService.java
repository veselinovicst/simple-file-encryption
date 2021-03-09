package org.challenge;

import org.bouncycastle.jcajce.io.CipherInputStream;
import org.bouncycastle.jcajce.io.CipherOutputStream;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import static org.challenge.KeyFactory.generateCipher;

public class FileEncryptionService {

    public static void encrypt(SecretKeySpec secretKeySpec, FileInputStream input, FileOutputStream output) throws IOException,
            NoSuchProviderException,
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidAlgorithmParameterException,
            InvalidKeyException {

        byte[] iv = KeyFactory.generateInitializationVector();
        Cipher cipher = generateCipher(Cipher.ENCRYPT_MODE, iv, secretKeySpec);

        try (CipherOutputStream cipherOut = new CipherOutputStream(output, cipher)) {
            output.write(iv);

            byte[] buf = new byte[4096];
            int length;
            while ((length = input.read(buf)) > 0) {
                cipherOut.write(buf, 0, length);
            }
        }

    }

    public static void decrypt(SecretKeySpec secretKeySpec, FileInputStream input, FileOutputStream output) throws IOException,
            NoSuchProviderException,
            InvalidAlgorithmParameterException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            NoSuchPaddingException {

        byte[] iv = new byte[KeyFactory.IV_SIZE];
        input.read(iv);
        Cipher cipher = generateCipher(Cipher.DECRYPT_MODE, iv, secretKeySpec);

        try (CipherInputStream cipherIn = new CipherInputStream(input, cipher)) {
            byte[] buf = new byte[4096];
            int length;
            while ((length = cipherIn.read(buf)) > 0) {
                output.write(buf, 0, length);
            }
        }
    }

}
