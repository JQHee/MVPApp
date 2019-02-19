package com.example.testmvpapp.sections.main.personal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.testmvpapp.Model.PersonalBean;
import com.example.testmvpapp.Model.PersonalItemBean;
import com.example.testmvpapp.R;
import com.example.testmvpapp.base.BasePresenter;
import com.example.testmvpapp.base.SimpleFragment;
import com.example.testmvpapp.sections.adapter.PersonalAdapter;
import com.example.testmvpapp.util.base.ToastUtils;
import com.example.testmvpapp.util.log.LatteLogger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PersonalFragment extends SimpleFragment {

    @BindView(R.id.rv_personal)
    RecyclerView mPersonalRV;

    private PersonalAdapter mPersonalAdapter;


    @Override
    protected Object getLayout() {
        return R.layout.fragment_personal;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        setupRecyclerView();
    }

    private void setupRecyclerView() {

        mPersonalRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        final PersonalItemBean itemBean = new PersonalItemBean();
        itemBean.setId(0);
        itemBean.setTitle("item");

        // header 0 有两条
        final PersonalBean section = new PersonalBean(true, "header");
        section.setTitle("section");
        final PersonalBean bean1 = new PersonalBean(itemBean);
        final PersonalBean bean2 = new PersonalBean(itemBean);

        // header 1 有一条
        final PersonalBean section1 = new PersonalBean(true, "header");
        section1.setTitle("section1");

        final List<PersonalBean> datas = new ArrayList<>();
        datas.add(section);
        datas.add(bean1);
        datas.add(bean2);
        datas.add(section1);
        datas.add(bean1);

        mPersonalAdapter = new PersonalAdapter(R.layout.item_personal, R.layout.recycler_view_personal_section, datas);
        // 添加header头
        mPersonalAdapter.addHeaderView(getHeaderView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast("header 点击");
                LatteLogger.d("点击");
            }
        }));
        mPersonalAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                PersonalBean item = (PersonalBean) adapter.getData().get(position);
                LatteLogger.d("点击");
                switch (view.getId()) {
                    case R.id.tv_title:
                        ToastUtils.showToast("点击");
                        break;

                        default:
                            break;
                }
            }
        });
        mPersonalRV.setAdapter(mPersonalAdapter);
    }

    /**
     * 设置recycle的header头
     */
    private View getHeaderView(View.OnClickListener listener) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.recycler_view_header_personal, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        view.setOnClickListener(listener);
        return view;
    }

}
