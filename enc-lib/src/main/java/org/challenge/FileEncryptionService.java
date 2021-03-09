package org.challenge;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;

import static org.challenge.KeyFactory.generateCipher;

public class FileEncryptionService {

    public static void encrypt(SecretKeySpec secretKeySpec, File input, File output) throws IOException,
            NoSuchProviderException,
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidAlgorithmParameterException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException {

        byte[] fileAsBytes = FileUtils.readFileToByteArray(input);
        byte[] iv = KeyFactory.generateInitializationVector();

        Cipher cipher = generateCipher(Cipher.ENCRYPT_MODE, iv, secretKeySpec);
        byte[] encryptedFileBytes = cipher.doFinal(fileAsBytes);

        FileUtils.writeByteArrayToFile(output, ArrayUtils.addAll(iv, encryptedFileBytes));

    }

    public static void decrypt(SecretKeySpec secretKeySpec, File input, File output) throws IOException,
            NoSuchProviderException,
            InvalidAlgorithmParameterException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            NoSuchPaddingException,
            BadPaddingException,
            IllegalBlockSizeException {

        byte[] fileBytes = FileUtils.readFileToByteArray(input);
        byte[] iv = Arrays.copyOfRange(fileBytes, 0, 16);
        Cipher cipher = generateCipher(Cipher.DECRYPT_MODE, iv, secretKeySpec);
        byte[] decryptedFileBytes = cipher.doFinal(fileBytes, KeyFactory.IV_SIZE, fileBytes.length - KeyFactory.IV_SIZE);
        FileUtils.writeByteArrayToFile(output, decryptedFileBytes);

    }

}
