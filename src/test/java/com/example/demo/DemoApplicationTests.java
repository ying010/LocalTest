package com.example.demo;

import com.example.demo.download.util.AESUtil;
import org.junit.Test;

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
        String content = "密码1993";
        String password = "加密密码";
        System.out.println("需要加密的内容：" + content);
        byte[] encrypt = AESUtil.encrypt(content, password);
        System.out.println("加密后的2进制密文：" + new String(encrypt));
//        String hexStr = ParseSystemUtil.parseByte2HexStr(encrypt);
//        System.out.println("加密后的16进制密文:" + hexStr);
//        byte[] byte2 = ParseSystemUtil.parseHexStr2Byte(hexStr);
//        System.out.println("加密后的2进制密文：" + new String(byte2));
        byte[] decrypt = AESUtil.decrypt(encrypt, password);
        System.out.println("解密后的内容：" + new String(decrypt,"utf-8"));
    }

}
