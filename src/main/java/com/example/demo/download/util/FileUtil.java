package com.example.demo.download.util;

import java.io.*;

/**
 * 文件工具
 *
 * @author wzhiy
 */
public class FileUtil {
    public static byte[] read(File file) throws Exception {
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file))) {
            byte[] buffer = new byte[(int)file.length()];
            in.read(buffer);
//            int readSize = -1;
//            do {
//                readSize = in.read(buffer);
//                if (readSize != -1) {
//
//                }
//            } while (readSize != -1);
            return buffer;
        } catch (Exception e) {
            throw new Exception("read file err!");
        }
    }

    public static void write(String path, byte[] file) throws Exception {
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(path)))) {
            out.write(file, 0, file.length);
            out.flush();
        } catch (Exception e) {
            throw new Exception("write file err!");
        }
    }
}
