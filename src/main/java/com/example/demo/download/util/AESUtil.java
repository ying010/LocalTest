package com.example.demo.download.util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

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
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES");
            // 初始化为解密模式的密码器
            cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(password, 128));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
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
            Cipher cipher = Cipher.getInstance("AES");
            //初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(password, 128));
            //加密
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
    private static SecretKeySpec getSecretKeySpec(String password, int type) throws NoSuchAlgorithmException {
        //创建AES的Key生产者
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        //利用用户密码作为随机数初始化出
        //加密没关系，SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，
        //所以解密只要有password就行
        kgen.init(type, new SecureRandom(password.getBytes()));
        // 根据用户密码，生成一个密钥
        SecretKey secretKey = kgen.generateKey();
        //返回基本编码格式的密钥，如果此密钥不支持编码，则返回
        byte[] enCodeFormat = secretKey.getEncoded();
        //转换为AES专用密钥
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        return key;
    }
}
