package com.tastreet.MonthlyFestival;

import java.io.Serializable;

public class MonthlyFestivalListData implements Serializable{
    private String mfs_name;
    private String mfs_img;
    private String mfs_addr;
    private String mfs_term;
    private String mfs_homeaddr;
    private String mfs_num;
    private String mfs_etc;

    public String getMfs_img() {
        return mfs_img;
    }

    public void setMfs_img(String mfs_img) {
        this.mfs_img = mfs_img;
    }

    public String getMfs_addr() {
        return mfs_addr;
    }

    public void setMfs_addr(String mfs_addr) {
        this.mfs_addr = mfs_addr;
    }

    public String getMfs_homeaddr() {
        return mfs_homeaddr;
    }

    public void setMfs_homeaddr(String mfs_homeaddr) {
        this.mfs_homeaddr = mfs_homeaddr;
    }

    public String getMfs_num() {
        return mfs_num;
    }

    public void setMfs_num(String mfs_num) {
        this.mfs_num = mfs_num;
    }

    public String getMfs_etc() {
        return mfs_etc;
    }

    public void setMfs_etc(String mfs_etc) {
        this.mfs_etc = mfs_etc;
    }

    public String getMfs_name() {
        return mfs_name;
    }

    public void setMfs_name(String mfs_name) {
        this.mfs_name = mfs_name;
    }

    public String getMfs_term() {
        return mfs_term;
    }

    public void setMfs_term(String mfs_term) {
        this.mfs_term = mfs_term;
    }

    public MonthlyFestivalListData(String mfs_name, String mfs_img, String mfs_addr, String mfs_term, String mfs_homeaddr, String mfs_num, String mfs_etc) {

        this.mfs_name = mfs_name;
        this.mfs_img = mfs_img;
        this.mfs_addr = mfs_addr;
        this.mfs_term = mfs_term;
        this.mfs_homeaddr = mfs_homeaddr;
        this.mfs_num = mfs_num;
        this.mfs_etc = mfs_etc;
    }
}
