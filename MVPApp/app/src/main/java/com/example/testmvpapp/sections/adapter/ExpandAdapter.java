package com.example.testmvpapp.sections.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.testmvpapp.Model.Levelone;
import com.example.testmvpapp.Model.Levelzero;
import com.example.testmvpapp.R;

import java.util.List;

/**
 * 类似qq好友的折叠效果
 */
public class ExpandAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ExpandAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.levelzero);
        addItemType(TYPE_LEVEL_1, R.layout.levelone);
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case 0:
                final Levelzero lv0 = (Levelzero) item;
                helper.setText(R.id.Lv0_tv, lv0.friendGroup);
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getAdapterPosition();
                        if (lv0.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });
                break;
            case 1:
                final Levelone lv1 = (Levelone) item;
                // helper.setImageResource(R.id.Lv1_iv, lv1.friendSculpture);
                helper.setText(R.id.title_one, lv1.friendName);
                break;
        }
    }
}
