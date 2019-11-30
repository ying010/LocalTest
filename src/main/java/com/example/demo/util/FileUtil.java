package com.example.demo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
    public static byte[] read(File file) throws IOException {
        int length = (int) file.length();
        byte[] bytes = new byte[length];
        FileInputStream inputStream = new FileInputStream(file);
        inputStream.read(bytes);
        inputStream.close();
        return bytes;
    }

    public static void write(byte[] content, String path) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(new File(path));
        outputStream.write(content);
        outputStream.close();
    }
}
