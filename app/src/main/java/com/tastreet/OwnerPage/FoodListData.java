package com.tastreet.OwnerPage;

import java.io.Serializable;

public class FoodListData implements Serializable{
    private String ft_main_img;
    private String ft_name;
    private String origin;
    private String ft_num;
    private String ft_intro;
    private String ft_menu_img;
    private String ft_sns_f;
    private String ft_sns_i;
    private String category;

    public FoodListData(String ft_main_img, String ft_name, String origin, String ft_num, String ft_intro, String ft_menu_img, String ft_sns_f, String ft_sns_i, String category) {
        this.ft_main_img = ft_main_img;
        this.ft_name = ft_name;
        this.origin = origin;
        this.ft_num = ft_num;
        this.ft_intro = ft_intro;
        this.ft_menu_img = ft_menu_img;
        this.ft_sns_f = ft_sns_f;
        this.ft_sns_i = ft_sns_i;
        this.category = category;
    }

    public FoodListData() {
    }

    public String getFt_main_img() {
        return ft_main_img;
    }

    public void setFt_main_img(String ft_main_img) {
        this.ft_main_img = ft_main_img;
    }

    public String getFt_name() {
        return ft_name;
    }

    public void setFt_name(String ft_name) {
        this.ft_name = ft_name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getFt_num() {
        return ft_num;
    }

    public void setFt_num(String ft_num) {
        this.ft_num = ft_num;
    }

    public String getFt_intro() {
        return ft_intro;
    }

    public void setFt_intro(String ft_intro) {
        this.ft_intro = ft_intro;
    }

    public String getFt_menu_img() {
        return ft_menu_img;
    }

    public void setFt_menu_img(String ft_menu_img) {
        this.ft_menu_img = ft_menu_img;
    }

    public String getFt_sns_f() {
        return ft_sns_f;
    }

    public void setFt_sns_f(String ft_sns_f) {
        this.ft_sns_f = ft_sns_f;
    }

    public String getFt_sns_i() {
        return ft_sns_i;
    }

    public void setFt_sns_i(String ft_sns_i) {
        this.ft_sns_i = ft_sns_i;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
