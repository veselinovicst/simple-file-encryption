package org.challenge;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.security.*;

public class App {

    public static void main(String[] args) throws NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            IOException,
            BadPaddingException,
            IllegalBlockSizeException,
            NoSuchProviderException,
            InvalidAlgorithmParameterException {

        Security.addProvider(new BouncyCastleProvider());

        File inputFile = new File(App.class.getResource("/testfile.pdf").getFile());
        File outputFile = new File("enc-test.pdf");
        File decryptedOutputFile = new File("dec-test.pdf");

        SecretKeySpec secretKeySpec = KeyFactory.generateAESKey();

        System.out.println("File encrypting ...");
        FileEncryptionService.encrypt(secretKeySpec, inputFile, outputFile);

        System.out.println("File decryption ...");
        FileEncryptionService.decrypt(secretKeySpec, outputFile, decryptedOutputFile);
    }

}
