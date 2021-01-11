package com.example.madcamp_proj2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProfileGridAdapter extends BaseAdapter {
    ArrayList<String> photos;

    public ProfileGridAdapter(ArrayList<String> photos) {
        this.photos = photos;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.profile_photos, parent, false);
        }
        ImageView imageView = convertView.findViewById(R.id.imageView2);

        String url = "http://"+context.getString(R.string.ip)+":8080/photos/uploads/";
        Glide.with(context).load(url+photos.get(pos)).into(imageView);
        return convertView;
    }
}
