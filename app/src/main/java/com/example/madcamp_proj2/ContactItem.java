package com.example.madcamp_proj2;

import android.graphics.Bitmap;

import java.io.Serializable;

public class ContactItem implements Serializable {
    private Bitmap user_profile;
    private String user_phNumber, user_name;
    private String mail;

    private String ID;
    private String photo;

    public ContactItem(){}

    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getID() {return ID; }
    public void setID(String ID) {
        this.ID = ID;
    }

    public Bitmap getUser_profile(){return user_profile;}
    public void setUser_profile(Bitmap user_profile){this.user_profile = user_profile;}

    public String getMail(){ return this.mail; }
    public void setMail(String mail){this.mail = mail;}


    public String getUser_phNumber(){return user_phNumber;}
    public String getUser_name(){return user_name;}

    public void setUser_phNumber(String string){this.user_phNumber = string;}
    public void setUser_name(String string){this.user_name = string; }

    public String toString(){
        return this.user_phNumber;
    }
    public int hashCode(){
        return getPhNumberChanged().hashCode();
    }
    public String getPhNumberChanged(){
        return user_phNumber.replace("-","");
    }
    public boolean equals(Object o){
        if(o instanceof ContactItem) return getPhNumberChanged().equals(((ContactItem) o).getPhNumberChanged());
        return false;
    }
}