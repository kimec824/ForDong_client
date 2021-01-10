package com.example.madcamp_proj2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {

    public ArrayList<GridViewItem> gridViewItemList = new ArrayList<GridViewItem>();
    public GridViewAdapter(){}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.gridview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.image_gridview);
        TextView tv_name = (TextView) convertView.findViewById(R.id.textView3);
        TextView tv_path = (TextView) convertView.findViewById(R.id.textView4);
        TextView tv_context = (TextView) convertView.findViewById(R.id.textView5);
        //TextView strTextView = (TextView) convertView.findViewById(R.id.text_gridview);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        GridViewItem gridViewItem = gridViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        iconImageView.setImageBitmap(gridViewItem.getIcon());
        tv_name.setText(gridViewItem.getName());
        tv_path.setText(gridViewItem.getImagePath());
        tv_context.setText(gridViewItem.getPhotoConText());
        //strTextView.setText(gridViewItemKaist.getStr());

        return convertView;
    }

    @Override
    public int getCount() { return gridViewItemList.size(); }

    @Override
    public Object getItem(int position) { return gridViewItemList.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Bitmap icon, String name, String context, String image_path) {
        GridViewItem item = new GridViewItem();

        item.setIcon(icon);
        item.setName(name);
        item.setPhotoContext(context);
        item.setImagePath(image_path);
        //item.setStr(str);

        gridViewItemList.add(item);
    }
}