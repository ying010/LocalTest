package com.example.demo;

import com.example.demo.download.util.AESUtil;
import com.example.demo.util.FileUtil;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class DemoApplicationTests {

    @Test
    public void aesTest() throws UnsupportedEncodingException {
        String content = "密密码1993";
        String password = "050eaff5ac9b8cf8";
        System.out.println("需要加密的内容：" + content + "|长度：" + content.length());
        byte[] encrypt = AESUtil.encrypt(content, password);
        System.out.println("加密后的2进制密文：" + bytesToHexString(encrypt) + "|字节长度：" + encrypt.length);
        byte[] decrypt = AESUtil.decrypt(encrypt, password);
        String decryptContent = new String(decrypt, "utf-8");
        System.out.println("解密后的内容：" + decryptContent + "|长度：" + decryptContent.length() + "|字节长度： " + decrypt.length);
    }

    @Test
    public void tsTest() throws IOException {
        byte[] fileByte = FileUtil.read(new File("F:/ts/response.ts"));
        String password = "050eaff5ac9b8cf8";
        byte[] decrypt = AESUtil.decrypt(fileByte, password);
        int count = 0;
        for (int i = 0; i < 1000; i++) {
            System.out.println(decrypt[i]);
        }
        System.out.println("------------------------------");
        for (int i = decrypt.length - 1; i > decrypt.length - 1000; i--) {
            System.out.println(decrypt[i]);
        }
//        FileUtil.write(decrypt, "F:/ts/new.ts");
    }

    @Test
    public void fileTest() throws IOException {
        byte[] fileByte = FileUtil.read(new File("F:/ts/response.ts"));
        FileUtil.write(fileByte, "F:/ts/new.ts");
        FileUtil.deleteFile(new File("F:/ts/response.ts"));
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);

            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

}
