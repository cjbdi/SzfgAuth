package com.github.cjbdi.szfg.auth;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cjbdi.szfg.core.vo.FtpVO;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

/**
 * @author Boning Liang
 */
public class AuthUtil {

    private static final String SIGNATURE_ALGORITHM="MD5withRSA";
    private static final String AES = "AES";
    private static final String RSA = "RSA";
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static byte[] encryptAesBytes(byte[] data, byte[] pass) {
        AES aes = new AES(Mode.CBC, Padding.NoPadding,
                new SecretKeySpec(pass, AES),
                new IvParameterSpec(pass));
        int len = data.length % 16 == 0 ? data.length : (data.length / 16 + 1) * 16;
        byte[] paddingdata = new byte[len];
        System.arraycopy(data, 0, paddingdata, 0, data.length);
        return aes.encrypt(paddingdata);
    }

    public static String encryptAES(String data, String pass) {
        byte[] result = encryptAesBytes(data.getBytes(StandardCharsets.UTF_8), pass.getBytes());
        return Base64.encodeUrlSafe(result, StandardCharsets.UTF_8);
    }

    private static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePublic(keySpec);
    }

    private static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePrivate(keySpec);
    }

    public static byte[] sign(byte[] data, String str_priK) throws Exception {
        PrivateKey priK = getPrivateKey(str_priK);
        Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
        sig.initSign(priK);
        sig.update(data);
        return sig.sign();
    }

    public static String encryptRSA(String content, String publicKeyStr) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKeyStr));
        byte[] cache = {};
        byte[] resultBytes = {};
        byte[] input = content.getBytes();
        int inputLength = input.length;
        int offset = 0;
        while (inputLength - offset > 0) {
            if (inputLength - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(input, offset, MAX_ENCRYPT_BLOCK);
                offset += MAX_ENCRYPT_BLOCK;
            } else {
                cache = cipher.doFinal(input, offset, inputLength - offset);
                offset = inputLength;
            }
            resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
            System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
        }
        return Base64.encode(resultBytes);
    }

//    public static String encryptRSA(String content, String publicKeyStr) throws Exception {
//        Cipher cipher = Cipher.getInstance(RSA);
//        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKeyStr));
//        return Base64.encode(cipher.doFinal(content.getBytes()));
//    }

    public static String encryptFtp(FtpVO ftpVO, String publicKeyStr) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(ftpVO);
        return encryptRSA(content, publicKeyStr);
    }

}
