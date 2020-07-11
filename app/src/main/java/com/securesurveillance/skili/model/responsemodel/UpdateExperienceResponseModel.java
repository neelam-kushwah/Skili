package com.securesurveillance.skili.model.responsemodel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by adarsh on 12/11/2018.
 */

public class UpdateExperienceResponseModel implements Serializable {

    String message;
    boolean status;
    private ArrayList<ExperienceResponse> result;


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

    public ArrayList<ExperienceResponse> getResult() {
        return result;
    }

    public void setResult(ArrayList<ExperienceResponse> result) {
        this.result = result;
    }

    public class ExperienceResponse {
        int id;
        String employerName;
        String jobDescription;
        String jobStartDate;
        String jobEndDate;
        String profileName;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEmployerName() {
            return employerName;
        }

        public void setEmployerName(String employerName) {
            this.employerName = employerName;
        }

        public String getJobDescription() {
            return jobDescription;
        }

        public void setJobDescription(String jobDescription) {
            this.jobDescription = jobDescription;
        }

        public String getJobStartDate() {
            return jobStartDate;
        }

        public void setJobStartDate(String jobStartDate) {
            this.jobStartDate = jobStartDate;
        }

        public String getJobEndDate() {
            return jobEndDate;
        }

        public void setJobEndDate(String jobEndDate) {
            this.jobEndDate = jobEndDate;
        }

        public String getProfileName() {
            return profileName;
        }

        public void setProfileName(String profileName) {
            this.profileName = profileName;
        }
    }


}
