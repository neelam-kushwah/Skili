package com.securesurveillance.skili.model.requestmodel;

import java.util.ArrayList;

/**
 * Created by adarsh on 9/23/2018.
 */

public class UpdateSkillsRequestModel {


    String _id;
    ArrayList<UpdateSkillModel> skills;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public ArrayList<UpdateSkillModel> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<UpdateSkillModel> skills) {
        this.skills = skills;
    }

    public class UpdateSkillModel {
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

