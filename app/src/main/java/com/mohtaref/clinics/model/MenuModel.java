package com.mohtaref.clinics.model;

public class MenuModel {
    private String image, titleName;

 public MenuModel(String image,String titleName)
 {
        this.image=image;
        this.titleName=titleName;
 }

    public String getImage() {
        return image;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitleName(String TitleName) {
        this.titleName = TitleName;
    }
}
