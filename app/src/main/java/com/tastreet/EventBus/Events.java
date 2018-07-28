package com.tastreet.EventBus;

public class Events {

    public static final String SEARCH_METHOD_FRAGMENT = "SEARCH_METHOD_FRAGMENT";
    public static final String DIRECT_MATCHING_FRAGMENT = "DIRECT_MATCHING_FRAGMENT";
    public static final String INQUIRY_MATCHING_FRAGMENT = "INQUIRY_MATCHING_FRAGMENT";
    public static String CURRENT_PAGE = SEARCH_METHOD_FRAGMENT;
    public static final String FINISH_MATCHING = "FINISH_MATCHING";

    public static final String baseUrl = "http://52.78.25.74/";

    public static class Msg{
        private String msg;
        public Msg(String msg){
            this.msg = msg;
        }
        public String getMsg(){
            return msg;
        }
    }
}
