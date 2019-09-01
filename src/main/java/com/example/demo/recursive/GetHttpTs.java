package com.example.demo.recursive;

import com.example.demo.config.BizConstant;
import com.example.demo.data.Video;
import com.example.demo.file.util.RecordLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetHttpTs {
    private static Logger logger = LoggerFactory.getLogger(GetHttpTs.class);

    private static String CONTENT = "https://21maokk.com/";

    private static String DOWN_LOGS = "F:\\ts\\log";

    private static String DOWN_URL = "F:\\ts\\data\\";
    static {
        DOWN_URL = DOWN_URL + new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    public static void main(String[] args) {
        GetHttpTs getHttpTs = new GetHttpTs();

        try {
            logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<start>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            logger.info(">>>getHome url: " + "https://21maokk.com/vodtypehtml/13-34.html");
            List<Video> homeLst = getHome("https://21maokk.com/vodtypehtml/13-34.html");

            logger.info(">>>getHome home list size: " + homeLst.size());

            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
            logger.info(">>>process video list by executor start video count: " + homeLst.size());
            for (int i = 0; i < homeLst.size(); i++) {
                Video video = homeLst.get(i);
                logger.info("video name: " + video.getName());
                executor.execute(() -> {
                    try {
                        getM3u8Url(video);
                        getTsUrl(video);
                        getTs(video);
                        downByTask(getHttpTs, video);
                        mergeVideo(video);
                        logger.info(">>>>>>>>>>>>>>>>>download video success video file name: " + video.getFileName() + "video name: " + video.getName() + "<<<<<<<<<<<");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            executor.shutdown();
            while (!executor.isShutdown()) {
                Thread.sleep(1000 * 10);
                logger.info(">>>>>>>>>>>>>>>>>>>>>>application is running...");
            }
            logger.info(">>>>>>>>>>>>>>>all download has been successfully<<<<<<<<<<<<<<<<<<<<<<<<<");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Video> getHome(String startUrl) throws Exception {
//        System.out.println("getHome start startUrl: " + startUrl);
        int tryTime = 0;
        for (; tryTime < 10; ) {
            try {
                List<Video> result = new ArrayList<>();
                URL url = new URL(startUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setDoOutput(true); // 设置该连接是可以输出的
                connection.setRequestMethod("GET"); // 设置请求方式
                connection.setRequestProperty("accept", "*/*");
                connection.setRequestProperty("connection", "Keep-Alive");
                connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                connection.setReadTimeout(BizConstant.ONE_MINUTE);
                connection.setReadTimeout(BizConstant.ONE_MINUTE);

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                String line;

                while ((line = br.readLine()) != null) { // 读取数据
                    String regex2 = "<a href=\"/vodhtml/([a-zA-z0-9]*).html";
                    Pattern r = Pattern.compile(regex2);
                    Matcher m = r.matcher(line);
                    if (m.find()) {
                        Video video = new Video();
                        int startIndex = line.indexOf("href") + 7;
                        int lastIndex = line.indexOf("\"", startIndex);
                        String entrance = line.substring(startIndex, lastIndex);
                        int nameStart = line.indexOf("title") + 7;
                        int nameEnd = line.indexOf("\"", nameStart);
                        String name = line.substring(nameStart, nameEnd);
                        video.setEntrance(entrance.replace("vodhtml", "vodplayhtml") + "?road=1");
                        video.setName(name);
                        String img = br.readLine();
                        String imgUrl = img.substring(img.indexOf("https"), img.indexOf(".jpg") + 4);
                        video.setImg(imgUrl);
                        result.add(video);
                    }
                }
                connection.disconnect();
                return result;
            } catch (Exception e) {
//                System.out.println("getHome timeout try time: " + tryTime);
                tryTime++;
            }
        }
        throw new Exception("getHome失败！!!" + startUrl);
    }

    private static void getM3u8Url(Video video) throws Exception {
        logger.info("getM3u8Url start connection url is: " + CONTENT + video.getEntrance());
        int tryTime = 0;
        while (tryTime < 10) {
            try {
                URL url = new URL(CONTENT + video.getEntrance());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setDoOutput(true); // 设置该连接是可以输出的
                connection.setRequestMethod("GET"); // 设置请求方式
                connection.setRequestProperty("accept", "*/*");
                connection.setRequestProperty("connection", "Keep-Alive");
                connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                connection.setConnectTimeout(BizConstant.ONE_MINUTE);
                connection.setReadTimeout(BizConstant.ONE_MINUTE);

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) { // 读取数据
                    String regex2 = "src=\"https://(.*?)/index.m3u8\"";
                    Pattern r = Pattern.compile(regex2);
                    Matcher m = r.matcher(line);
                    if (m.find()) {
                        int startIndex = line.indexOf("http");
                        int lastIndex = line.indexOf("index.m3u8");
                        video.setM3u8(line.substring(startIndex, lastIndex + 10));
                        video.setFileName(line.substring(lastIndex - 8, lastIndex));
                        connection.disconnect();
                        return;
                    }
                }
                connection.disconnect();
                return;
            } catch (ConnectException e) {
                logger.warn("getM3u8Url timeOut try again " + tryTime + "times url: " + CONTENT + video.getEntrance());
            } catch (Exception e) {
                logger.error("getM3u8Url err try again " + tryTime + "times url: " + CONTENT + video.getEntrance(), e);
            } finally {
                tryTime++;
            }
        }
        logger.error("getM3u8Url failed url: " + CONTENT + video.getEntrance());
        throw new Exception("getM3u8Url failed");
    }

    private static void getTsUrl(Video video) throws Exception {
        String m3u8 = video.getM3u8();
        logger.info("getTsUrl start connection url is: " + m3u8);
        int tryTime = 0;
        while (tryTime < 10) {
            try {
                URL url = new URL(m3u8);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setDoOutput(true); // 设置该连接是可以输出的
                connection.setRequestMethod("GET"); // 设置请求方式
                connection.setRequestProperty("Content-Type", "application/x-mpegURL");

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) { // 读取数据
                    if (line.endsWith(".m3u8")) {
                        video.setTsUrl(m3u8.replace("index.m3u8", line));
                    }
                }
                connection.disconnect();
                return;
            } catch (ConnectException e) {
                logger.warn("getTsUrl timeOut try again " + tryTime + "times fileName: " + video.getFileName() + " |url: " + m3u8);
            } catch (Exception e) {
                logger.error("getTsUrl err try again " + tryTime + "times fileName: " + video.getFileName() + " |url: " + m3u8, e);
            } finally {
                tryTime++;
            }
        }
        logger.error("getTsUrl failed fileName: " + video.getFileName() + " |url: " + m3u8);
        throw new Exception("getTsUrl failed");
    }

    private static void getTs(Video video) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("getTs start video fileName: " + video.getFileName());
        }
        int tryTime = 0;
        while (tryTime < 10) {
            try {
                String urlHead = video.getTsUrl().substring(0, video.getTsUrl().lastIndexOf("/"));
                URL url = new URL(video.getTsUrl());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setDoOutput(true); // 设置该连接是可以输出的
                connection.setRequestMethod("GET"); // 设置请求方式
                connection.setRequestProperty("accept", "*/*");
                connection.setRequestProperty("connection", "Keep-Alive");
                connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                connection.setConnectTimeout(BizConstant.ONE_MINUTE);
                connection.setReadTimeout(BizConstant.ONE_MINUTE);

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                String line;
                List<String> result = new ArrayList<>();
                while ((line = br.readLine()) != null) { // 读取数据
                    if (line.endsWith(".ts")) {
                        result.add(urlHead + "/" + line);
                    }
                }
                video.setTs(result);
                connection.disconnect();
                return;
            } catch (ConnectException e) {
                logger.warn("getTs timeOut try again " + tryTime + "times fileName: " + video.getFileName());
            } catch (Exception e) {
                logger.error("getTs err try again " + tryTime + "times fileName: " + video.getFileName(), e);
            } finally {
                tryTime++;
            }
        }
        logger.error("getTs failed fileName: " + video.getFileName() + " |url: " + video.getTsUrl());
        throw new Exception("getTs failed");
    }

    private static void downByTask(GetHttpTs getHttpTs, Video video) {
        if (logger.isDebugEnabled()) {
            logger.debug("downByTask start video fileName: " + video.getFileName());
        }
        try {
            String finalFile = DOWN_URL + "\\" + video.getFileName() + "\\";
            if (!new File(finalFile).exists()) {
                new File(finalFile).mkdirs();
            }
            //下载封面
            try {
                String imgUrl = video.getImg();
                downVideo(imgUrl, finalFile, video.getName(), "jpg");
            } catch (Exception e) {
                logger.error("down img err url: " + video.getImg(), e);
            }

            //线程池开启多线程下载
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(200);
            for (int i = 0; i < video.getTs().size(); i++) {
                final int taskNum = i;
                executor.execute(()->{
                    String tsUrl = video.getTs().get(taskNum);
                    try {
                        String taskName = taskNum / 1000 > 0 ? "d" + taskNum : taskNum / 100 > 0 ? "c" + taskNum :
                                taskNum / 10 > 0 ? "b" + taskNum : "a" + taskNum;
                        downVideo(tsUrl, finalFile, taskName, "ts");
                        logger.info("down " + video.getFileName() + " ts success task: " + taskNum + "/" + video.getTs().size());
                    } catch (Exception e) {
                        RecordLog recordLog = new RecordLog(DOWN_LOGS + "\\err.log", true);
                        recordLog.write(">>>>TS<<<<< " + tsUrl + "\r\n");
                        logger.error("down " + video.getFileName() + " ts error task: " + taskNum, e);
                    }
                });
            }
            executor.shutdown();
            while (!executor.isShutdown()) {
                Thread.sleep(10*1000);
            }
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
    private static void downVideo(String videoUrl, String downloadPath, String fileName, String SuffixName) throws Exception {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        RandomAccessFile randomAccessFile = null;
        int tryTime = 0;

        //路径名加上文件名加上后缀名 = 整个文件下载路径
        String fullPathName = downloadPath + fileName + "." + SuffixName;

        while (tryTime < 10) {
            try {
                // 1.获取连接对象
                URL url = new URL(videoUrl);
                // 获取链接对象，就是靠这个对象来获取流
                connection = (HttpURLConnection) url.openConnection();
                // Range代表读取的范围，bytes=0-代表从0字节到最大字节，意味着读取所有资源
                connection.setRequestProperty("Range", "bytes=0-");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.3");
                connection.setConnectTimeout(BizConstant.ONE_MINUTE);
                connection.setReadTimeout(BizConstant.ONE_MINUTE);
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
                }
                return;
            } catch (ConnectException e) {// 具体的异常放到前面
                System.out.println("downVideo try again|videoUrl: " + videoUrl + " |exception: " + e.getClass());
            } catch (SocketTimeoutException e) {// 具体的异常放到前面
                System.out.println("downVideo try again|videoUrl: " + videoUrl + " |exception: " + e.getClass());
            } catch (Exception e) {
                logger.error("downVideo err url: " + videoUrl, e);
            } finally {
                tryTime++;
                try {
                    if (connection != null) connection.disconnect();
                    if (inputStream != null) inputStream.close();
                    if (randomAccessFile != null) randomAccessFile.close();
                } catch (IOException e) {
                    logger.error("downVideo close resource err url: " + videoUrl, e);
                }
            }
        }
        logger.error("downVideo err url: " + videoUrl);
        throw new Exception("downVideo err|videoUrl: " + videoUrl);
    }

    private static void mergeVideo(Video video) throws Exception {
        logger.info("merge video start file name: " + video.getFileName() + "ts count: " + video.getTs().size());
        File sourceFile = new File(DOWN_URL + video.getFileName());
        if (sourceFile.exists()) {
            File[] sourceVideos = sourceFile.listFiles((dir, name) -> Pattern.compile("/*.ts").matcher(name).matches());
            if (sourceVideos.length > 0) {
                String targetFilePath = DOWN_URL + video.getFileName() + "/result/" + video.getName() + ".ts";
                BufferedInputStream in = null;
                try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(targetFilePath)))) {
                    for (File sourceVideo : sourceVideos) {
                        in = new BufferedInputStream(new FileInputStream(sourceVideo));
                        byte[] buffer = new byte[2048];
                        int readSize = -1;
                        do {
                            readSize = in.read(buffer);
                            if (readSize != -1) {
                                out.write(buffer, 0, readSize);
                                out.flush();
                            }
                        } while (readSize != -1);
                    }
                } catch (Exception e) {
                    logger.error("merge video err file name: " + video.getFileName(), e);
                    throw new Exception("merge video err");
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Exception e) {
                            logger.error("merge video err(close source) file name: " + video.getFileName(), e);
                        }
                    }
                }
            }
        }
    }

}
