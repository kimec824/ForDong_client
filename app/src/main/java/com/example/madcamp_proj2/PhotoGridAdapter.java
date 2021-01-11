package com.example.madcamp_proj2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.example.madcamp_proj2.Fragment2.gridView;
import static com.example.madcamp_proj2.Fragment2.recyclerView;

public class PhotoGridAdapter extends BaseAdapter {

    public ArrayList<PhotoGridItem> photoGroupList = new ArrayList<PhotoGridItem>();
    public PhotoGridAdapter(){}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.photogroup, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView thumbnailImageView = (ImageView) convertView.findViewById(R.id.thumbnail_imageview);
        TextView groupTextView = (TextView) convertView.findViewById(R.id.group_name);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        PhotoGridItem photoGroup = photoGroupList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        String url = "http://"+context.getString(R.string.ip)+":8080/photos/uploads/";
        Glide.with(gridView).load(url+photoGroup.getThumbnail_path()).into(thumbnailImageView);
        groupTextView.setText(photoGroup.getPhoto_group_name());



        return convertView;
    }

    @Override
    public int getCount() { return photoGroupList.size(); }

    @Override
    public Object getItem(int position) { return photoGroupList.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String image_path, String group_name) {
        PhotoGridItem item = new PhotoGridItem();
        item.setThumbnail_path(image_path);
        item.setPhoto_group_name(group_name);
        photoGroupList.add(item);
    }
}
