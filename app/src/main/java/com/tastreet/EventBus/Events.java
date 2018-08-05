package com.tastreet.EventBus;

import com.tastreet.FoodTruckPage.Festival.FestivalListData;
import com.tastreet.FoodTruckPage.MonthlyFestival.MonthlyFestivalListData;
import com.tastreet.OwnerPage.FoodListData;

public class Events {

    public static final String SEARCH_METHOD_FRAGMENT = "SEARCH_METHOD_FRAGMENT";
    public static final String DIRECT_MATCHING_FRAGMENT = "DIRECT_MATCHING_FRAGMENT";
    public static final String FOODTRUCK_INFO_FRAGMENT = "FOODTRUCK_INFO_FRAGMENT";
    public static final String INQUIRY_MATCHING_FRAGMENT = "INQUIRY_MATCHING_FRAGMENT";

    public static final String FT_MAIN_FRAGMENT = "FT_MAIN_FRAGMENT";
    public static final String FT_MONTHLY_FESTIVAL_DETAIL_FRAGMENT = "FT_MONTHLY_FESTIVAL_DETAIL_FRAGMENT";
    public static final String FT_FESTIVAL_FRAGMENT = "FT_FESTIVAL_FRAGMENT";
    public static final String FT_FESTIVAL_DETAIL_FRAGMENT = "FT_FESTIVAL_DETAIL_FRAGMENT";
    public static final String MYFT_FRAGMENT = "MYFT_FRAGMENT";

    public static final String BACK_BUTTON_PRESS = "BACK_BUTTON_PRESS";

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

    public static class SendMonthlyFestivalData{
        private MonthlyFestivalListData data;
        public SendMonthlyFestivalData(MonthlyFestivalListData data){
            this.data = data;
        }

        public MonthlyFestivalListData getData() {
            return data;
        }
    }

    public static class SendFestivalData{
        private FestivalListData data;
        public SendFestivalData(FestivalListData data){
            this.data = data;
        }
        public FestivalListData getData(){
            return data;
        }
    }

    public static class SendFoodtruckInfoData{
        private FoodListData data;
        public SendFoodtruckInfoData(FoodListData data){
            this.data = data;
        }
        public FoodListData getData() {
            return data;
        }
    }
}
