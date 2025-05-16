package com.example.keychainapp.logic;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.util.Base64;

public class KeychainService {

    private static final String KEYSTORE_TYPE = "JCEKS";
    private static final String KEYSTORE_PATH = System.getProperty("user.home") + "/keychain.jks";
    private static final String KEYSTORE_PASSWORD = "your_keystore_password";
    private static final String KEY_ALIAS = "keychain_key";

    public KeychainService() {
        try {
            initializeKeyStore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeKeyStore() throws Exception {
        if (!Files.exists(Paths.get(KEYSTORE_PATH))) {
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
            keyStore.load(null, null);
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            SecretKey secretKey = keyGen.generateKey();
            keyStore.setKeyEntry(KEY_ALIAS, secretKey, KEYSTORE_PASSWORD.toCharArray(), null);
            try (FileOutputStream fos = new FileOutputStream(KEYSTORE_PATH)) {
                keyStore.store(fos, KEYSTORE_PASSWORD.toCharArray());
            }
        }
    }

    public void save(String key, String value) throws Exception {
        SecretKey secretKey = getSecretKey();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedValue = cipher.doFinal(value.getBytes());
        String encodedValue = Base64.getEncoder().encodeToString(encryptedValue);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(key + ".txt"))) {
            writer.write(encodedValue);
        }
    }

    public String retrieve(String key) throws Exception {
        SecretKey secretKey = getSecretKey();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        String encodedValue;
        try (BufferedReader reader = new BufferedReader(new FileReader(key + ".txt"))) {
            encodedValue = reader.readLine();
        }
        byte[] decryptedValue = cipher.doFinal(Base64.getDecoder().decode(encodedValue));
        return new String(decryptedValue);
    }

    private SecretKey getSecretKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
        try (FileInputStream fis = new FileInputStream(KEYSTORE_PATH)) {
            keyStore.load(fis, KEYSTORE_PASSWORD.toCharArray());
        }
        return (SecretKey) keyStore.getKey(KEY_ALIAS, KEYSTORE_PASSWORD.toCharArray());
    }
}