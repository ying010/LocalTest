package com.example.demo.file.util;

import java.io.File;
import java.io.FileWriter;

public class RecordLog {
    private String file;
    private boolean append;

    public RecordLog(String file, boolean append) {
        this.file = file;
        this.append = append;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public boolean isAppend() {
        return append;
    }

    public void setAppend(boolean append) {
        this.append = append;
    }

    public void write(String log) {
        try(FileWriter fileWriter = new FileWriter(file, append)) {
            fileWriter.write(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
