package com.example.testmvpapp.sections.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    public static List<Bitmap> bmp = new ArrayList<Bitmap>();

    public UploadPicGridAdapter(Context context,List<Bitmap> bmp) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        this.bmp = bmp;
    }


    public int getCount() {
        return (bmp.size() + 1);
    }

    public Object getItem(int arg0) {

        return null;
    }

    public long getItemId(int arg0) {

        return 0;
    }

    public void update(){

    }


    /**
     * ListView Item设置
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int coord = position;
        UploadPicGridAdapter.ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
            holder = new UploadPicGridAdapter.ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.iv_item);
            holder.iv_del = (ImageView) convertView.findViewById(R.id.iiv_del);
            convertView.setTag(holder);
        } else {
            holder = (UploadPicGridAdapter.ViewHolder) convertView.getTag();
        }

        if (position == bmp.size()) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                    R.mipmap.ic_launcher));
            holder.iv_del.setVisibility(View.GONE);
            if (position == 3)
            {
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

    public  interface  OnDelItemPhotoClickListener{
        void onDelItemPhotoClick(int position);
    }

    public OnDelItemPhotoClickListener mOnDelItemPhotoClickListener;

    public void setOnDelItemPhotoClickListener(OnDelItemPhotoClickListener mOnDelItemPhotoClickListener){
        this.mOnDelItemPhotoClickListener = mOnDelItemPhotoClickListener;
    }

    public class ViewHolder {
        public ImageView image;
        public ImageView iv_del;
    }
}
