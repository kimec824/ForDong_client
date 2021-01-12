package com.example.madcamp_proj2;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class FeedItem {
    private String Id;
    private Bitmap image = null; //gridview item
    private String image_path;
    private String context;
    //private String dep_str ; // 추가하고싶으면 다른 정보들


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setIcon(Bitmap image) { this.image = image ; }
    public void setPhotoContext(String context){this.context = context;}
    public void setImagePath(String file_path){this.image_path = file_path;}
    //public void setStr(String title) { this.dep_str = title ;    }

    public Bitmap getIcon() { return this.image ; }
    public String getPhotoConText(){return this.context;}
    public String getImagePath(){return this.image_path;}
    //public String getStr() { return this.dep_str ;}

}
