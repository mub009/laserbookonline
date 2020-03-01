package com.mohtaref.clinics.model;

public class MenuModel {
    private String image, titleName,CategoryId;
    public MenuModel(String image, String titleName, String CategoryId)
     {
        this.image=image;
        this.titleName=titleName;
        this.CategoryId=CategoryId;
     }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
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
