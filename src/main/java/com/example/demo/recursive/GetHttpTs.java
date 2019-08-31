package com.example.demo.recursive;

import net.sf.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MyTask implements Runnable {
    private int taskNum;
    private String tsUrl;
    private String finalFile;
    private GetHttpTs getHttpTs;

    public MyTask(int taskNum, String tsUrl, String finalFile, GetHttpTs getHttpTs) {
        this.taskNum = taskNum;
        this.tsUrl = tsUrl;
        this.finalFile = finalFile;
        this.getHttpTs = getHttpTs;
    }

    @Override
    public void run() {
        System.out.println("正在执行task " + taskNum);
        try {
            System.out.println(tsUrl);
            String taskName = taskNum / 1000 > 0 ? "d" + taskNum : taskNum / 100 > 0 ? "c" + taskNum :
                    taskNum / 10 > 0 ? "b" + taskNum : "a" + taskNum;
            GetHttpTs.downVideo(tsUrl, finalFile, taskName, "ts");
        } catch (Exception e) {
            getHttpTs.getERROR_TS().add(tsUrl);
            e.printStackTrace();
        }
        System.out.println("task " + taskNum + "执行完毕");
    }
}


public class GetHttpTs {
    private static String CONTENT = "https://21maokk.com";
    public static void main(String[] args) {
        GetHttpTs getHttpTs = new GetHttpTs();
        String downUrl = "F:\\ts";
        try {
            List<String> homeLst = getHome("https://21maokk.com/vodtypehtml/13-25.html");

            System.out.println("<<<<<<<<<<<<<<<<" + homeLst);
            for (String homeUrl : homeLst) {
                String m3u8Url = getM3u8Url(CONTENT + "/" + homeUrl.replace("vodhtml", "vodplayhtml") + "?road=1");
                downByTask(downUrl, getHttpTs, m3u8Url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<String> getTs(String tsUrl) throws Exception {
        try {
            String urlHead = tsUrl.substring(0, tsUrl.lastIndexOf("/"));
            URL url = new URL(tsUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true); // 设置该连接是可以输出的
            connection.setRequestMethod("GET"); // 设置请求方式
            connection.setRequestProperty("Content-Type", "application/x-mpegURL");

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line;
            List<String> result = new ArrayList<String>();
            while ((line = br.readLine()) != null) { // 读取数据
                if (line.endsWith(".ts")) {
                    result.add(urlHead + "/" + line);
                }
            }
            connection.disconnect();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("获取ts连接失败！");
        }
    }

    private static String getM3u8(String startUrl) throws Exception {
        try {
            String m3u8Url = "";
            URL url = new URL(startUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true); // 设置该连接是可以输出的
            connection.setRequestMethod("GET"); // 设置请求方式
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");


            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line;
            List<String> result = new ArrayList<String>();
            while ((line = br.readLine()) != null) { // 读取数据
                if (line.endsWith(".m3u8")) {
                    m3u8Url = startUrl.replace("index.m3u8", line);
                }
            }
            connection.disconnect();
            return m3u8Url;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("获取m3u8失败！");
        }
    }

    private static List<String> getHome(String startUrl) throws Exception {
        try {
            URL url = new URL(startUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true); // 设置该连接是可以输出的
            connection.setRequestMethod("GET"); // 设置请求方式
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line;
            List<String> result = new ArrayList<String>();
            while ((line = br.readLine()) != null) { // 读取数据
                String regex2 = "<a href=\"/vodhtml/([a-zA-z0-9]*).html";
                Pattern r = Pattern.compile(regex2);
                Matcher m = r.matcher(line);
                if (m.find()) {
                    int startIndex = line.indexOf("href") + 7;
                    int lastIndex = line.indexOf("\"", startIndex);
                    result.add(line.substring(startIndex, lastIndex));
                }
            }
            connection.disconnect();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("获取信息失败！");
        }
    }

    private static List<String> getVideoUrl(String startUrl) throws Exception {
        List<String> result = new ArrayList<>();
        try {
            URL url = new URL(startUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true); // 设置该连接是可以输出的
            connection.setRequestMethod("GET"); // 设置请求方式
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line;

            while ((line = br.readLine()) != null) { // 读取数据
                String regex2 = "href=\"/vodplayhtml/([a-zA-z0-9]*).html";
                Pattern r = Pattern.compile(regex2);
                Matcher m = r.matcher(line);
                if (m.find()) {
                    int startIndex = line.indexOf("href") + 7;
                    int lastIndex = line.indexOf("\"", startIndex);
                    result.add(line.substring(startIndex, lastIndex));
                }
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String getM3u8Url(String startUrl) throws Exception {
        String m3u8Url = "";
        try {
            URL url = new URL(startUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true); // 设置该连接是可以输出的
            connection.setRequestMethod("GET"); // 设置请求方式
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line;
            while ((line = br.readLine()) != null) { // 读取数据
                String regex2 = "src=\"https://(.*?)/index.m3u8\"";
                Pattern r = Pattern.compile(regex2);
                Matcher m = r.matcher(line);
                if (m.find()) {
                    int startIndex = line.indexOf("http");
                    int lastIndex = line.indexOf("index.m3u8");
                    m3u8Url = line.substring(startIndex, lastIndex);
                }
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return m3u8Url;
    }

    private static void downByTask(String downUrl, GetHttpTs getHttpTs, String url) {
        try {
//            String url = "https://cdn2.lajiao-bo.com/20190811/KIYtWmBz/index.m3u8";
            String currentFile = url.substring(url.lastIndexOf("/") - 8, url.lastIndexOf("/"));
            String finalFile = downUrl + "\\" + currentFile + "\\";
            if (!new File(finalFile).exists()) {
                new File(finalFile).mkdirs();
            }
            String m3u8Url = getM3u8(url + "index.m3u8");
            List<String> tsLst = getTs(m3u8Url);
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(200);

            for (int i = 0; i < tsLst.size(); i++) {
                MyTask myTask = new MyTask(i, tsLst.get(i), finalFile, getHttpTs);
                executor.execute(myTask);
                System.out.println("线程池中线程数目：" + executor.getPoolSize() + "，队列中等待执行的任务数目：" +
                        executor.getQueue().size() + "，已执行玩别的任务数目：" + executor.getCompletedTaskCount());
            }
            executor.shutdown();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 下载视频
     *
     * @param videoUrl     实际视频地址
     * @param downloadPath 文件下载地址
     * @param fileName     文件名
     * @param SuffixName   后缀名
     */
    public static void downVideo(String videoUrl, String downloadPath, String fileName, String SuffixName) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        RandomAccessFile randomAccessFile = null;

        //路径名加上文件名加上后缀名 = 整个文件下载路径
        String fullPathName = downloadPath + fileName + "." + SuffixName;

        try {
            // 1.获取连接对象
            URL url = new URL(videoUrl);
            // 获取链接对象，就是靠这个对象来获取流
            connection = (HttpURLConnection) url.openConnection();
            // Range代表读取的范围，bytes=0-代表从0字节到最大字节，意味着读取所有资源
            connection.setRequestProperty("Range", "bytes=0-");
            // 与网页建立链接，链接成功后就可以获得流；
            connection.connect();
            // 2.获取连接对象的流
            inputStream = connection.getInputStream();
            // 已下载的大小 下载进度
            int downloaded = 0;
            // 总文件的大小
            int fileSize = connection.getContentLength();
            // getFile获取此URL的文件名。返回的文件部分将与getPath（）相同,具体视频链接的文件名字视情况而定
            // String fileName = url.getFile();
            // fileName = fileName.substring(fileName.lastIndexOf("/") + 1);//特殊需要截取文件名字
            // 3.把资源写入文件
            randomAccessFile = new RandomAccessFile(fullPathName, "rw");
            while (downloaded < fileSize) {
                // 3.1设置缓存流的大小
                //判断当前剩余的下载大小是否大于缓存之，如果不大于就把缓存的大小设为剩余的。
                byte[] buffer = null;
                if (fileSize - downloaded >= 1024 * 1024 * 100) {
                    buffer = new byte[1024 * 1024 * 100];
                } else {
                    buffer = new byte[fileSize - downloaded];
                }
                // 3.2把每一次缓存的数据写入文件
                int read = -1;
                int currentDownload = 0;
                long startTime = System.currentTimeMillis();
                // 这段代码是按照缓存的大小，读写该大小的字节。然后循环依次写入缓存的大小，直至结束。
                // 这样的优势在于，不用让硬件频繁的写入，可以提高效率和保护硬盘吧
                while (currentDownload < buffer.length) {
                    read = inputStream.read();
                    buffer[currentDownload++] = (byte) read;
                }
                long endTime = System.currentTimeMillis();
                double speed = 0.0; //下载速度
                if (endTime - startTime > 0) {
                    speed = currentDownload / 1024.0 / ((double) (endTime - startTime) / 1000);
                }
                randomAccessFile.write(buffer);
                downloaded += currentDownload;
                randomAccessFile.seek(downloaded);
                System.out.printf(fullPathName + "下载了进度:%.2f%%,下载速度：%.1fkb/s(%.1fM/s)%n", downloaded * 1.0 / fileSize * 10000 / 100,
                        speed, speed / 1000);
            }

        } catch (Exception e) {// 具体的异常放到前面
            e.printStackTrace();
        } finally {
            try {
                //关闭资源、连接
                connection.disconnect();
                inputStream.close();
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public JSONObject getHttpJson(String url, int comefrom) throws Exception {
        try {
            URL realUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            //请求成功
            if (connection.getResponseCode() == 200) {
                InputStream is = connection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //10MB的缓存
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                String jsonString = baos.toString();
                baos.close();
                is.close();
                //转换成json数据处理
                // getHttpJson函数的后面的参数1，表示返回的是json数据，2表示http接口的数据在一个（）中的数据
                JSONObject jsonArray = getJsonString(jsonString, comefrom);
                return jsonArray;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public JSONObject getJsonString(String str, int comefrom) throws Exception {
        JSONObject jo = null;
        if (comefrom == 1) {
            return JSONObject.fromObject(str);
        } else if (comefrom == 2) {
            int indexStart = 0;
            //字符处理
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == '(') {
                    indexStart = i;
                    break;
                }
            }
            String strNew = "";
            //分割字符串
            for (int i = indexStart + 1; i < str.length() - 1; i++) {
                strNew += str.charAt(i);
            }
            return JSONObject.fromObject(strNew);
        }
        return jo;
    }

    private Set<String> ERROR_TS = new HashSet<>();

    public Set<String> getERROR_TS() {
        return ERROR_TS;
    }

    public void setERROR_TS(Set<String> ERROR_TS) {
        this.ERROR_TS = ERROR_TS;
    }
}
