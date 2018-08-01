package com.tastreet;

public interface AsyncDone { //서버 응답을 받아와서 Main Thread로 값을 넘겨줄 인터페이스
    void getResult(String result);
}
