package com.example.madcamp_proj2;

import android.graphics.drawable.Drawable;

public class GridViewItem {
    private String name;
    private Drawable image ; //gridview item
    private String context;
    //private String dep_str ; // 추가하고싶으면 다른 정보들

    public void setIcon(Drawable image) { this.image = image ; }
    public void setName(String name){this.name = name;}
    public void setphotoContext(String context){this.context = context;}
    //public void setStr(String title) { this.dep_str = title ;    }

    public Drawable getIcon() { return this.image ; }
    public String getName(){return this.name;}
    public String getphotoConText(){return this.context;}
    //public String getStr() { return this.dep_str ;}

}
