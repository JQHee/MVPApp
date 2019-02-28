package com.example.testmvpapp.sections.sign;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.testmvpapp.Model.Levelone;
import com.example.testmvpapp.Model.Levelzero;
import com.example.testmvpapp.R;
import com.example.testmvpapp.base.SimpleActivity;
import com.example.testmvpapp.sections.adapter.ExpandAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 类似qq好友的折叠列表
 */
public class ExpandActivity extends SimpleActivity {

    @BindView(R.id.rv)
    RecyclerView mrecycleView;

    private ArrayList<MultiItemEntity> mRes;
    private List<Levelzero> lv0 = new ArrayList<>();

    @Override
    protected Object getLayout() {
        return R.layout.activity_expand;
    }

    @Override
    protected void init() {
        initData();
        setupRecyclerView();
    }

    private  void setupRecyclerView() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        mrecycleView.setLayoutManager(manager);
        mrecycleView.setAdapter(new ExpandAdapter(mRes));
    }

    private void initData() {
        mRes = new ArrayList<>();
        lv0.clear();
        lv0.add(new Levelzero("我的好友"));
        lv0.add(new Levelzero("家人"));
        for(int i = 0;i<5;i++){
            Levelone lv1 = new Levelone("好友名称", 0);
            lv0.get(0).addSubItem(lv1);
        }
        for(int i = 0;i<5;i++){
            Levelone lv1 = new Levelone("家人名称", 0);
            lv0.get(1).addSubItem(lv1);
        }
        for (int j = 0; j < lv0.size(); j++) {
            mRes.add(lv0.get(j));
        }
        return ;
    }
}
