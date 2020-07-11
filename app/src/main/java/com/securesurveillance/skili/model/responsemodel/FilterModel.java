package com.securesurveillance.skili.model.responsemodel;

import java.util.ArrayList;

/**
 * Created by adarsh on 8/4/2018.
 */

public class FilterModel {
    String message;
    boolean status;
    FilterResponse result;

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


    public FilterResponse getResult() {
        return result;
    }

    public void setResult(FilterResponse result) {
        this.result = result;
    }

    public class FilterResponse {
        ArrayList<String> skills;
        ArrayList<String> city;

        public ArrayList<String> getSkills() {
            return skills;
        }

        public void setSkills(ArrayList<String> skills) {
            this.skills = skills;
        }

        public ArrayList<String> getCity() {
            return city;
        }

        public void setCity(ArrayList<String> city) {
            this.city = city;
        }
    }

}
