package com.example.scansaga.Model;

public class Upload {
    private String ImageUrl;

    public Upload(){

    }
    public Upload(String imageUrl){
        ImageUrl = imageUrl;
    }
    public String getImageUrl(){
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
