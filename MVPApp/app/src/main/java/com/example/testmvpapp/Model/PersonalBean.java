package com.example.testmvpapp.Model;

import com.chad.library.adapter.base.entity.SectionEntity;
import java.util.List;

public class PersonalBean  extends SectionEntity<PersonalItemBean> {

    /**
     * heaer 头
     */
    public PersonalBean(boolean isHeader, String header) {
        super(isHeader, header);
    }

    /**
     * item
     */
    public PersonalBean(PersonalItemBean personalItemBean) {
        super(personalItemBean);
    }

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
