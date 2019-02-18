package com.example.testmvpapp.sections.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.testmvpapp.Model.CountryCodeListBean;
import com.example.testmvpapp.R;

import java.util.List;

public class CountryCodeAdapter extends BaseMultiItemQuickAdapter<CountryCodeListBean, BaseViewHolder> {

    private static final int ITEM_NORMAL = 0;

    public CountryCodeAdapter(List<CountryCodeListBean> data) {
        super(data);
        addItemType(ITEM_NORMAL, R.layout.item_country_code);
    }

    @Override
    protected void convert(BaseViewHolder helper, CountryCodeListBean item) {
        switch (helper.getItemViewType()) {
            case ITEM_NORMAL:
                helper.setText(R.id.tv_title, item.getmText());
                break;
            default:break;
        }
    }
}
