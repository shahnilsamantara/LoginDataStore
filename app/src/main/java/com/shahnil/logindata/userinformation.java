package com.shahnil.logindata;

public class userinformation {

    private String mName;
    private String mId;
    private String mImagename;

    public userinformation(){
        //needed for firebase

    }

    public userinformation(String imagename) {
        if(imagename.trim().equals("")){
            imagename = "no name";
        }
        mImagename = imagename;
    }


    public String getmImagename() {
        return mImagename;
    }

    public void setmImagename(String mImagename) {
        this.mImagename = mImagename;
    }
}
