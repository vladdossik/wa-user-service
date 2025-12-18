package org.wa.user.service.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.wa.user.service.service.DecryptService;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Service
public class DecryptServiceImpl implements DecryptService {
    @Value("${encrypt.cipher-algorithm}")
    private String cipherAlgorithm;

    @Value("${encrypt.secret-key-algorithm}")
    private String secretKeyAlgorithm;

    private final SecretKey secretKey;

    public DecryptServiceImpl(@Value("${encrypt.secret}") String secretKey) {
        byte[] key = Arrays.copyOf(secretKey.getBytes(StandardCharsets.UTF_8), 32);
        this.secretKey = new SecretKeySpec(key, secretKeyAlgorithm);
    }

    public String decrypt(String encryptedText) {
        if (encryptedText == null) return null;
        try {
            String[] parts = encryptedText.split(":");
            byte[] decryptVector = Base64.getDecoder().decode(parts[0]);
            byte[] cipherText = Base64.getDecoder().decode(parts[1]);

            Cipher cipher = Cipher.getInstance(cipherAlgorithm);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(128, decryptVector));

            return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new IllegalStateException("Decryption failed", e);
        }
    }
}
