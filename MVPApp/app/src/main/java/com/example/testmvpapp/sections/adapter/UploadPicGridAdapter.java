package com.example.testmvpapp.sections.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.testmvpapp.R;

import java.util.ArrayList;
import java.util.List;

public class UploadPicGridAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater; // 视图容器
    // 如果要同时查看本地和远程图片（本地图片：Bitmap 远程图片：链接地址String）
    public static List<Bitmap> bmp = new ArrayList<Bitmap>();
    // 允许添加的图片总数
    public int mMaxImageCount = 0;

    public UploadPicGridAdapter(Context context, List<Bitmap> bmp, int maxImgCount) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        this.bmp = bmp;
        this.mMaxImageCount = maxImgCount;
    }

    @Override
    public int getCount() {
        return (bmp.size() + 1);
    }

    @Override
    public Object getItem(int arg0) {

        return null;
    }

    @Override
    public long getItemId(int arg0) {

        return arg0;
    }

    /**
     * ListView Item设置
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int coord = position;
        UploadPicGridAdapter.ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
            holder = new UploadPicGridAdapter.ViewHolder();
            holder.image = (AppCompatImageView) convertView.findViewById(R.id.iv_item);
            holder.iv_del = (AppCompatImageView) convertView.findViewById(R.id.iiv_del);
            convertView.setTag(holder);
        } else {
            holder = (UploadPicGridAdapter.ViewHolder) convertView.getTag();
        }

        if (position == bmp.size()) {
            // 添加的占位图片
            holder.image.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                    R.drawable.icon_add_photo));
            holder.iv_del.setVisibility(View.GONE);
            if (position == mMaxImageCount) {
                holder.image.setVisibility(View.GONE);
            }
        } else {
            holder.image.setImageBitmap(bmp.get(position));
            holder.iv_del.setVisibility(View.VISIBLE);

            holder.iv_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnDelItemPhotoClickListener != null){
                        mOnDelItemPhotoClickListener.onDelItemPhotoClick(position);
                    }
                }
            });
        }
        return convertView;
    }

    /* 删除回调 */
    public  interface  OnDelItemPhotoClickListener{
        void onDelItemPhotoClick(int position);
    }

    public OnDelItemPhotoClickListener mOnDelItemPhotoClickListener;

    public void setOnDelItemPhotoClickListener(OnDelItemPhotoClickListener mOnDelItemPhotoClickListener){
        this.mOnDelItemPhotoClickListener = mOnDelItemPhotoClickListener;
    }

    public class ViewHolder {
        public AppCompatImageView image;
        public AppCompatImageView iv_del;
    }
}
