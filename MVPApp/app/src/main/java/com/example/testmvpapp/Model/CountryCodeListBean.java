package com.example.testmvpapp.Model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class CountryCodeListBean implements MultiItemEntity {

    private int mItemType = 0;
    private String mText = null;
    private int mId = 0;

    public CountryCodeListBean(int itemType, String text, int id) {
        this.mItemType = itemType;
        this.mText = text;
        this.mId = id;
    }

    public void setmItemType(int mItemType) {
        this.mItemType = mItemType;
    }

    @Override
    public int getItemType() {
        return mItemType;
    }

    public String getmText() {
        if (mText == null) {
            return "";
        }
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    /**
     * 简单的构造器
     */
    public static final class Builder {
        private int id = 0;
        private int itemType = 0;
        private String text = null;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setItemType(int itemType) {
            this.itemType = itemType;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public CountryCodeListBean build() {
            return new CountryCodeListBean(itemType, text, id);
        }
    }
}
