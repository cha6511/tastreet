package com.tastreet.OwnerPage;

public class FoodListData {
    private String img_url;
    private String description;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FoodListData(String img_url, String description) {

        this.img_url = img_url;
        this.description = description;
    }
}
