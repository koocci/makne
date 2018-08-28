package io.github.koocci.maknesecretnote.DO;

/**
 * Created by gujinhyeon on 2018. 8. 24..
 *
 * 음식점 리스트 아이템
 *
 * thumbnail : 썸네일
 * name : 이름
 * loc : 위치
 * pref : 평균 선호도
 * count : 방문 횟수
 */

public class FoodMarketItem {
    private int thumbnail;
    private String name;
    private String loc;
    private int pref;
    private int count;
    private int type;

    public FoodMarketItem(int thumbnail, String name, String loc, int pref, int count, int type) {
        this.thumbnail = thumbnail;
        this.name = name;
        this.loc = loc;
        this.pref = pref;
        this.count = count;
        this.type = type;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public int getPref() {
        return pref;
    }

    public void setPref(int pref) {
        this.pref = pref;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
