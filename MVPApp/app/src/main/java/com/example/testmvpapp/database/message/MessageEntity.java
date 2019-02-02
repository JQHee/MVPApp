package com.example.testmvpapp.database.message;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by HJQ on 2017/5/11 0011 上午 9:24.
 * 描述：存放极光推送的消息
 */
@Entity
public class MessageEntity {

    // 主键自增
    @Id(autoincrement = true)
    private Long id;
    private String title;
    private String content;

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }


    @Generated(hash = 1041633038)
    public MessageEntity(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }


    @Generated(hash = 1797882234)
    public MessageEntity() {
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getTitle() {
        return this.title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getContent() {
        return this.content;
    }


    public void setContent(String content) {
        this.content = content;
    }
}
