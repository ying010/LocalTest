package com.example.demo.download.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * AES加密算法
 *
 * @author W.Z.King
 */
public class AESUtil {

    /**
     * 解密
     * @param content
     * @param password
     * @return
     */
    public static byte[] decrypt(byte[] content, String password) {
        try {
            Key secretKey = new SecretKeySpec(password.getBytes("utf-8"), "AES");
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            // 初始化为解密模式的密码器
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    /**
     * 加密
     * @param content
     * @param password
     * @return
     */
    public static byte[] encrypt(String content, String password) {
        try {
            //创建密码器
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            //初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(password));
            //加密
            int len = content.getBytes("UTF-8").length;
            int m = len % 16;
            if (m != 0) {
                for (int i = 0; i < 16 - m; i++) {
                    content += " ";
                }
            }
            byte[] byteContent = content.getBytes("utf-8");
            byte[] result = cipher.doFinal(byteContent);
            return result;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    /**
     * 获取AES密钥
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static SecretKeySpec getSecretKeySpec(String password) throws NoSuchAlgorithmException {
        //转换为AES专用密钥
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
        return key;
    }

}
