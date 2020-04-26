package com.mohtaref.clinics.model;

public class MenuModel {
    private String image, titleName,CategoryId,lat,log;

    public MenuModel(String image, String titleName, String CategoryId, String lat, String log)
    {
        this.image=image;
        this.titleName=titleName;
        this.CategoryId=CategoryId;
        this.lat=lat;
        this.log=log;

    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
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
