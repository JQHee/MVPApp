package com.example.testmvpapp.sections.adapter;

import android.support.v7.widget.AppCompatTextView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.testmvpapp.Model.CountryCodeListBean;
import com.example.testmvpapp.Model.PersonalBean;
import com.example.testmvpapp.Model.PersonalItemBean;
import com.example.testmvpapp.R;

import java.util.List;


/**
 * 实现添加header头和section
 */

public class PersonalAdapter extends BaseSectionQuickAdapter<PersonalBean, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public PersonalAdapter(int layoutResId, int sectionHeadResId, List<PersonalBean> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, PersonalBean item) {
        final AppCompatTextView textView =  helper.getView(R.id.tv_title);
        textView.setText(item.getTitle());
    }

    @Override
    protected void convert(BaseViewHolder helper, PersonalBean item) {
        PersonalItemBean bean = item.t;
        final AppCompatTextView textView =  helper.getView(R.id.tv_title);
        textView.setText(bean.getTitle());
    }
}
