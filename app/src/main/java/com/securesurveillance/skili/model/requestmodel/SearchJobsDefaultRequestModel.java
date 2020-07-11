package com.securesurveillance.skili.model.requestmodel;

import java.util.ArrayList;

/**
 * Created by adarsh on 11/24/2018.
 */

public class SearchJobsDefaultRequestModel {
    String filterEnable;
    String pagenumber;
    String pageSize;


    SkillRequestModel searchCriteria;

    public String getFilterEnable() {
        return filterEnable;
    }

    public void setFilterEnable(String filterEnable) {
        this.filterEnable = filterEnable;
    }

    public String getPagenumber() {
        return pagenumber;
    }

    public void setPagenumber(String pagenumber) {
        this.pagenumber = pagenumber;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public SkillRequestModel getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(SkillRequestModel searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public class SkillRequestModel {
        String mobileNumber;
        String city;
        String payFrom;
        String payTo;
        String distanceFrom;
        String distanceTo;
        String budgetType;
        String jobType;
        boolean IsBothTimeEnable=false;

        public boolean isBothTimeEnable() {
            return IsBothTimeEnable;
        }

        public void setBothTimeEnable(boolean bothTimeEnable) {
            IsBothTimeEnable = bothTimeEnable;
        }

        public String getJobType() {
            return jobType;
        }

        public void setJobType(String jobType) {
            this.jobType = jobType;
        }

        public String getBudgetType() {
            return budgetType;
        }

        public void setBudgetType(String budgetType) {
            this.budgetType = budgetType;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPayFrom() {
            return payFrom;
        }

        public void setPayFrom(String payFrom) {
            this.payFrom = payFrom;
        }

        public String getPayTo() {
            return payTo;
        }

        public void setPayTo(String payTo) {
            this.payTo = payTo;
        }

        public String getDistanceFrom() {
            return distanceFrom;
        }

        public void setDistanceFrom(String distanceFrom) {
            this.distanceFrom = distanceFrom;
        }

        public String getDistanceTo() {
            return distanceTo;
        }

        public void setDistanceTo(String distanceTo) {
            this.distanceTo = distanceTo;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        ArrayList<String> skills;

        public ArrayList<String> getSkills() {
            return skills;
        }

        public void setSkills(ArrayList<String> skills) {
            this.skills = skills;
        }
    }

}

