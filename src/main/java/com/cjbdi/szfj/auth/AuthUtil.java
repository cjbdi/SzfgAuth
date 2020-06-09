package com.cjbdi.szfj.auth;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author Boning Liang
 */
public class AuthUtil {

    private static byte[] encryptAesBytes(byte[] data, byte[] pass) {
        AES aes = new AES(Mode.CBC, Padding.NoPadding,
                new SecretKeySpec(pass, "AES"),
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

}
