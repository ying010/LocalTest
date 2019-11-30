package com.example.demo;

import com.example.demo.download.util.AESUtil;
import com.example.demo.download.util.FileUtil;
import org.junit.Test;

import java.io.File;

//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class DemoApplicationTests {

    @Test
    public void contextLoads() throws Exception {
        File file = new File("D:\\ts\\response.ts");
        byte[] fileByte = FileUtil.read(file);

        byte[] targetFile = AESUtil.decrypt(fileByte, "050eaff5ac9b8cf8");
        FileUtil.write("D:\\ts\\new.ts", targetFile);

    }

}
