package com.example.demo.download.data;

import java.util.List;

public class Video {
    private String name;
    private String fileName;
    private String img;
    private String entrance;
    private String m3u8;
    private String tsUrl;
    private List<String> ts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getEntrance() {
        return entrance;
    }

    public void setEntrance(String entrance) {
        this.entrance = entrance;
    }

    public String getM3u8() {
        return m3u8;
    }

    public void setM3u8(String m3u8) {
        this.m3u8 = m3u8;
    }

    public String getTsUrl() {
        return tsUrl;
    }

    public void setTsUrl(String tsUrl) {
        this.tsUrl = tsUrl;
    }

    public List<String> getTs() {
        return ts;
    }

    public void setTs(List<String> ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Video|name :" + name);
        sb.append("|img :" + img);
        sb.append("|entrance: " + entrance);
        sb.append("|m3u8: " + m3u8);
        sb.append("|tsUrl: " + tsUrl);
        sb.append("\r\n|ts: " + ts);
        return sb.toString();
    }
}
