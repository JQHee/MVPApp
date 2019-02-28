package com.example.testmvpapp.sections.sign;

import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.example.testmvpapp.Model.CountryCodeListBean;
import com.example.testmvpapp.R;
import com.example.testmvpapp.base.SimpleActivity;
import com.example.testmvpapp.sections.adapter.CountryCodeAdapter;
import com.example.testmvpapp.ui.recycler.decoration.BaseDecoration;
import com.example.testmvpapp.util.bus.LiveBus;
import com.example.testmvpapp.util.log.LatteLogger;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CountryCodeActivity extends SimpleActivity {


    @BindView(R.id.rv_country_code_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;

    @Override
    protected Object getLayout() {
        return R.layout.activity_country_code;
    }

    @Override
    protected void init() {
        setTitle("国家码");
        addToolBar(R.mipmap.menu_back);
        setRecycler();
        setupRefresh();
    }

    private void setupRefresh() {

        // 自定义刷新的样式
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(this));
        // mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
    }

    private void setRecycler() {

        final CountryCodeListBean beanOne = new CountryCodeListBean.Builder()
                .setId(0)
                .setText("+86")
                .build();
        final CountryCodeListBean beanTwo = new CountryCodeListBean.Builder()
                .setId(1)
                .setText("+87")
                .build();
        final List<CountryCodeListBean> datas = new ArrayList<>();
        datas.add(beanOne);
        datas.add(beanTwo);

        //设置RecyclerView
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        // 设置分割线
        mRecyclerView.addItemDecoration
                (BaseDecoration.create(ContextCompat.getColor(getContext(), R.color.app_background), 1));
        final CountryCodeAdapter adapter = new CountryCodeAdapter(datas);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LatteLogger.d(baseQuickAdapter.getData().get(position));
                LiveBus.getDefault().postEvent("COUNTRY_CODE", baseQuickAdapter.getData().get(position));
                finish();
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
    }

}
