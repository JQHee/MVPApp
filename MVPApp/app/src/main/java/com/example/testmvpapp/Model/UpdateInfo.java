package com.example.testmvpapp.Model;

public class UpdateInfo {

    // 线上的最新版本
    private String Version;
    // 下载地址
    private String UpadeUrl;
    // 应用更新描述
    private String Desc;
    private String VersionCode;

    public void setVersion(String version) {
        this.Version = version;
    }

    public String getVersion() {
        return Version;
    }

    public void setUpadeUrl(String url) {
        this.UpadeUrl = url;
    }

    public String getUpadeUrl() {
        return UpadeUrl;
    }

    public void setDesc(String description) {
        this.Desc = description;
    }

    public String getDesc() {
        return Desc;
    }

    public void setVersionCode(String versionCode) {
        VersionCode = versionCode;
    }

    public String getVersionCode() {
        return VersionCode;
    }

}
