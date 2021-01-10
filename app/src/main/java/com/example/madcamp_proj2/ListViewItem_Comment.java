package com.example.madcamp_proj2;

import android.graphics.Bitmap;

public class ListViewItem_Comment {
    private Bitmap iconDrawable;
    private String descStr=null;
    private String writerStr=null;

    public void setIcon(Bitmap icon) {
        iconDrawable = icon ;
    }
    public void setDesc(String desc) {
        descStr = desc ;
    }
    public void setWriter(String writer) {
        writerStr = writer ;
    }

    public Bitmap getIcon() {
        return this.iconDrawable ;
    }
    public String getDesc() {
        return this.descStr ;
    }
    public String getWriter() {
        return this.writerStr ;
    }
}
