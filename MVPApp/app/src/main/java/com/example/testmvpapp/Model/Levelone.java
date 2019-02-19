package com.example.testmvpapp.Model;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.testmvpapp.sections.adapter.ExpandAdapter;

/**
 * 好友信息
 */
public class Levelone implements MultiItemEntity {

    public String friendName;
    public int friendSculpture;

    public Levelone(String friendName, int friendSculpture) {
        this.friendName = friendName;
        this.friendSculpture = friendSculpture;
    }

    @Override
    public int getItemType() {
        return ExpandAdapter.TYPE_LEVEL_1;
    }
}
