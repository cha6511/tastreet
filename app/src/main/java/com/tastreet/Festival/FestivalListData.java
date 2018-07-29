package com.tastreet.Festival;

import java.io.Serializable;

public class FestivalListData implements Serializable{
    private String festival_name;
    private String recruit_amt;
    private String recruit_status;
    private String festival_img;
    private String festival_loc;
    private String festival_hp;
    private String festival_phone;
    private String festival_desc;
    private String festival_date;


    public String getFestival_name() {
        return festival_name;
    }

    public void setFestival_name(String festival_name) {
        this.festival_name = festival_name;
    }

    public String getRecruit_amt() {
        return recruit_amt;
    }

    public void setRecruit_amt(String recruit_amt) {
        this.recruit_amt = recruit_amt;
    }

    public String getRecruit_status() {
        return recruit_status;
    }

    public void setRecruit_status(String recruit_status) {
        this.recruit_status = recruit_status;
    }

    public String getFestival_img() {
        return festival_img;
    }

    public void setFestival_img(String festival_img) {
        this.festival_img = festival_img;
    }

    public String getFestival_loc() {
        return festival_loc;
    }

    public void setFestival_loc(String festival_loc) {
        this.festival_loc = festival_loc;
    }

    public String getFestival_hp() {
        return festival_hp;
    }

    public void setFestival_hp(String festival_hp) {
        this.festival_hp = festival_hp;
    }

    public String getFestival_phone() {
        return festival_phone;
    }

    public void setFestival_phone(String festival_phone) {
        this.festival_phone = festival_phone;
    }

    public String getFestival_desc() {
        return festival_desc;
    }

    public void setFestival_desc(String festival_desc) {
        this.festival_desc = festival_desc;
    }

    public String getFestival_date() {
        return festival_date;
    }

    public void setFestival_date(String festival_date) {
        this.festival_date = festival_date;
    }

    public FestivalListData(String festival_name, String recruit_amt, String recruit_status, String festival_img, String festival_loc, String festival_hp, String festival_phone, String festival_desc, String festival_date) {

        this.festival_name = festival_name;
        this.recruit_amt = recruit_amt;
        this.recruit_status = recruit_status;
        this.festival_img = festival_img;
        this.festival_loc = festival_loc;
        this.festival_hp = festival_hp;
        this.festival_phone = festival_phone;
        this.festival_desc = festival_desc;
        this.festival_date = festival_date;
    }
}
