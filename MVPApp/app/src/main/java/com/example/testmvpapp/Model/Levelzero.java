package com.example.testmvpapp.Model;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.testmvpapp.sections.adapter.ExpandAdapter;

/**
 * 好友组名
 */

public class Levelzero extends AbstractExpandableItem<Levelone> implements MultiItemEntity {

    public String friendGroup;

    public Levelzero(String friendName) {
        this.friendGroup = friendName;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return ExpandAdapter.TYPE_LEVEL_0;
    }
}
