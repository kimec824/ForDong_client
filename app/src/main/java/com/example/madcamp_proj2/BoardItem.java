package com.example.madcamp_proj2;

import java.io.Serializable;

public class BoardItem implements Serializable {
    private String titleStr, contentStr, writerStr, timeStr;
    private int type;
    public BoardItem(){}
    //TODO: 글 작성자, 댓글 추가
    public String gettitle(){return titleStr;}
    public void settitle(String title){this.titleStr = title;}

    public String getcontent(){return contentStr;}
    public void setcontent(String content){this.contentStr = content;}

    public String getwriter(){return writerStr;}
    public void setwriter(String writer){this.writerStr = writer;}

    public String gettime(){return timeStr;}
    public void settime(String time){this.timeStr = time;}

    public int gettype(){return type;}
    public void settype(int type){this.type = type;}

    }

