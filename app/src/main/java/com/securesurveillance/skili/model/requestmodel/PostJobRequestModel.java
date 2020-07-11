package com.securesurveillance.skili.model.requestmodel;

import com.securesurveillance.skili.utility.Constants;

import java.util.ArrayList;

/**
 * Created by adarsh on 11/24/2018.
 */

public class PostJobRequestModel {
    String profileId;
    String jobTitle;
    String jobDescription;
    String purposeOfJob;
    String jobType;
    String startDate;
    String endDate;
    String startTime;
    String endtime;
    String jobLocation;
    String latitude;
    String longitude;
    String budget;
    String budgetType;
    SkillRequestModel skills;
    String status;
    String city;
    String existingJobId;

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getExistingJobId() {
        return existingJobId;
    }

    public void setExistingJobId(String existingJobId) {
        this.existingJobId = existingJobId;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSkills(SkillRequestModel skills) {
        this.skills = skills;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public void setPurposeOfJob(String purposeOfJob) {
        this.purposeOfJob = purposeOfJob;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public void setBudgetType(String budgetType) {
        this.budgetType = budgetType;
    }

    public class SkillRequestModel {
        String category;

        ArrayList<String> subcategory;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }


        public ArrayList<String> getSubcategory() {
            return subcategory;
        }

        public void setSubcategory(ArrayList<String> subcategory) {
            this.subcategory = subcategory;
        }
    }

}

