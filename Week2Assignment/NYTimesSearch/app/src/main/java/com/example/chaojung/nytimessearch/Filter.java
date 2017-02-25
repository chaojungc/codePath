package com.example.chaojung.nytimessearch;

import org.parceler.Parcel;

/**
 * Created by ChaoJung on 2017/2/25.
 */

@Parcel
public class Filter {

    String beginDate;
    String sortOrder;
    Boolean checkArt;
    Boolean checkFashion;
    Boolean checkSport;

    public Filter() {

    }

    public String getNewsDesk(){
        String newsType = getCheckArt() ? "\"Arts\" " : "";
        newsType += getCheckFashion() ? "\"Fashion\" " : "";
        newsType += getCheckSport() ? "\"Sports\"" : "";
        return newsType.trim();
    }


    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getCheckArt() {
        return checkArt;
    }

    public void setCheckArt(Boolean checkArt) {
        this.checkArt = checkArt;
    }

    public Boolean getCheckFashion() {
        return checkFashion;
    }

    public void setCheckFashion(Boolean checkFashion) {
        this.checkFashion = checkFashion;
    }

    public Boolean getCheckSport() {
        return checkSport;
    }

    public void setCheckSport(Boolean checkSport) {
        this.checkSport = checkSport;
    }
}
