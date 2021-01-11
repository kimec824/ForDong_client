package com.example.madcamp_proj2;

//class가 이제 폴더 처럼 작동을 하는 거지.
import android.graphics.drawable.Drawable;

public class PhotoGridItem {
    private String thumbnail_path ; //폴더 대표 사진 path
    private String photo_group_name ; // 그룹의 이름(분류기준)

    public String getThumbnail_path() {
        return thumbnail_path;
    }

    public void setThumbnail_path(String thumbnail_path) {
        this.thumbnail_path = thumbnail_path;
    }

    public String getPhoto_group_name() {
        return photo_group_name;
    }

    public void setPhoto_group_name(String photo_group_name) {
        this.photo_group_name = photo_group_name;
    }
}
