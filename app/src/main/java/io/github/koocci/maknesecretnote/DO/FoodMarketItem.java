package io.github.koocci.maknesecretnote.DO;

/**
 * Created by gujinhyeon on 2018. 8. 24..
 *
 * 음식점 리스트 아이템
 *
 */

public class FoodMarketItem {

    private int id;
    private String name;
    private String phone;
    private String address;
    private String officeHours;
    private int visitCount;
    private int category;
    private String imagePath;
    private int pref;
    private String comment;


    public FoodMarketItem(int id, String name, String phone, String address, String officeHours, int visitCount, int category, String imagePath, int pref, String comment) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.officeHours = officeHours;
        this.visitCount = visitCount;
        this.category = category;
        this.imagePath = imagePath;
        this.pref = pref;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOfficeHours() {
        return officeHours;
    }

    public void setOfficeHours(String officeHours) {
        this.officeHours = officeHours;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getPref() {
        return pref;
    }

    public void setPref(int pref) {
        this.pref = pref;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
