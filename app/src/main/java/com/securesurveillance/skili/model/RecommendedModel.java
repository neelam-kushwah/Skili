package com.securesurveillance.skili.model;

/**
 * Created by adarsh on 7/8/2018.
 */

public class RecommendedModel {

    private String designationHeader;
    private String experience;
    private String companyName;
    private String postedOn;
    private String place;

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDesignationHeader() {
        return designationHeader;
    }

    public void setDesignationHeader(String designationHeader) {
        this.designationHeader = designationHeader;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(String postedOn) {
        this.postedOn = postedOn;
    }
}
