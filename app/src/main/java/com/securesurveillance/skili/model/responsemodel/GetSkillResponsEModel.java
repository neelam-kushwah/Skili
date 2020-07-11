package com.securesurveillance.skili.model.responsemodel;

import java.util.ArrayList;

/**
 * Created by adarsh on 9/23/2018.
 */

public class GetSkillResponsEModel {

    String message;
    boolean status;
    ArrayList<SkillInnerModel> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<SkillInnerModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<SkillInnerModel> result) {
        this.result = result;
    }

    public class SkillInnerModel {
        String category;
        boolean availibilityFullTime;
        boolean availabilityPartTime;
        ArrayList<String> subcategory;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public boolean isAvailibilityFullTime() {
            return availibilityFullTime;
        }

        public void setAvailibilityFullTime(boolean availibilityFullTime) {
            this.availibilityFullTime = availibilityFullTime;
        }

        public boolean isAvailabilityPartTime() {
            return availabilityPartTime;
        }

        public void setAvailabilityPartTime(boolean availabilityPartTime) {
            this.availabilityPartTime = availabilityPartTime;
        }

        public ArrayList<String> getSubcategory() {
            return subcategory;
        }

        public void setSubcategory(ArrayList<String> subcategory) {
            this.subcategory = subcategory;

        }

    }
}

