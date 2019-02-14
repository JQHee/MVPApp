package com.example.testmvpapp.component.net.entity;

import java.util.ArrayList;

public class FileParentBody {

    // 上传的key
    private String key = "file";
    // 文件类型
    private String mediaType = "image/png";
    // 文件数组
    private ArrayList<FileBody> bodys = new ArrayList<>();

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setBodys(ArrayList<FileBody> bodys) {
        this.bodys = bodys;
    }

    public ArrayList<FileBody> getBodys() {
        return bodys;
    }
}
