package com.securesurveillance.skili.model.requestmodel;

import java.util.ArrayList;

/**
 * Created by adarsh on 12/11/2018.
 */

public class UpdateExperienceRequestModel {
    private String _id;
    private ArrayList<Experience> experience;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public ArrayList<Experience> getExperience() {
        return experience;
    }

    public void setExperience(ArrayList<Experience> experience) {
        this.experience = experience;
    }

    public class Experience{

        String employerName;
        String jobDescription;
        String jobStartDate;
        String jobEndDate;
        String profileName;
        String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
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
