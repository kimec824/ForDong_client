package com.example.madcamp_proj2;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class FeedItem {

    private String Id;
    private String image_path;
    private String context;
    private String photo_group;

    public String getId() { return Id;  }

    public void setId(String id) { this.Id = id;  }

    public String getPhoto_group() { return photo_group;  }

    public void setPhoto_group(String photo_group) {this.photo_group = photo_group; }

    //private String dep_str ; // 추가하고싶으면 다른 정보들

    public void setPhotoContext(String context){this.context = context;}
    public void setImagePath(String file_path){this.image_path = file_path;}
    //public void setStr(String title) { this.dep_str = title ;    }

    public String getPhotoConText(){return this.context;}
    public String getImagePath(){return this.image_path;}
    //public String getStr() { return this.dep_str ;}

}
