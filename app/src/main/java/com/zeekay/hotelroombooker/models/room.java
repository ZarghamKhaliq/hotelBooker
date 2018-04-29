package com.zeekay.hotelroombooker.models;

public class room {

    boolean tv;
    boolean wifi;
    boolean ac;

    boolean isBooked;
    String booker;

    String beds;
    String price;
    String num;

    String im1;
    String im2;
    String im3;



    room(){

    }

    public room( boolean tv, boolean wifi, boolean ac, String num,String beds, String price, String im1, String im2, String im3) {
        this.beds = beds;
        this.tv = tv;
        this.wifi = wifi;
        this.ac = ac;
        this.price = price;
        this.num = num;
        this.im1 = im1;
        this.im2 = im2;
        this.im3 = im3;
    }


    public boolean isTv() {
        return tv;
    }

    public void setTv(boolean tv) {
        this.tv = tv;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isAc() {
        return ac;
    }

    public void setAc(boolean ac) {
        this.ac = ac;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean isBooked) {
        this.isBooked = isBooked;
    }

    public String getBooker() {
        return booker;
    }

    public void setBooker(String booker) {
        this.booker = booker;
    }

    public String getBeds() {
        return beds;
    }

    public void setBeds(String beds) {
        this.beds = beds;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getIm1() {
        return im1;
    }

    public void setIm1(String im1) {
        this.im1 = im1;
    }

    public String getIm2() {
        return im2;
    }

    public void setIm2(String im2) {
        this.im2 = im2;
    }

    public String getIm3() {
        return im3;
    }

    public void setIm3(String im3) {
        this.im3 = im3;
    }
}
