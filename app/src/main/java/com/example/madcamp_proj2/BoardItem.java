package com.example.madcamp_proj2;

import java.io.Serializable;

public class BoardItem implements Serializable {
    private String titleStr, contentStr;
    public BoardItem(){}
    //TODO: 글 작성자, 댓글 추가
    public String gettitle(){return titleStr;}
    public void settitle(String title){this.titleStr = title;}

    public String getcontent(){return contentStr;}
    public void setcontent(String content){this.contentStr = content;}

}
