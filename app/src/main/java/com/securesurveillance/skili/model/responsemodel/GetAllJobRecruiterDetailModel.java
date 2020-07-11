package com.securesurveillance.skili.model.responsemodel;

import com.securesurveillance.skili.model.SkillModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by adarsh on 11/18/2018.
 */

public class GetAllJobRecruiterDetailModel implements Serializable {

    String message;
    String statusCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public class RecruiterDetail implements Serializable {
        String name;
        String profilePic;
        String rating;
        String profileId;
        String mobileNumber;
        String favRecruiter;
        String favRecruiterId;

        public String getFavRecruiter() {
            return favRecruiter;
        }

        public void setFavRecruiter(String favRecruiter) {
            this.favRecruiter = favRecruiter;
        }

        public String getFavRecruiterId() {
            return favRecruiterId;
        }

        public void setFavRecruiterId(String favRecruiterId) {
            this.favRecruiterId = favRecruiterId;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getProfileId() {
            return profileId;
        }

        public void setProfileId(String profileId) {
            this.profileId = profileId;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public class ApplicantDetail implements Serializable {
        String name;
        String profilePic;
        String rating;
        String profileId;

        public String getProfileId() {
            return profileId;
        }

        public void setProfileId(String profileId) {
            this.profileId = profileId;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    RecruiterDetail postedBy;
    String jobId;
    String favoriteId;// used for favourite job list only dont use it anywhere else
    String jobLocation;
    String latitude;
    String longitude;
    String budget;
    String budgetType;
    String purposeOfJob;
    RecruiterJobSkillModel skills;
    String jobType;
    String startDate;
    String endDate;
    String startTime;
    String endTime;
    String applicantCount;
    String status;
    String jobTitle;
    String jobDescription;
    ArrayList<ApplicantDetail> acceptedApplicant;
    String applicationStatus;// job search
    boolean savedAsFav;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public boolean isSavedAsFav() {
        return savedAsFav;
    }

    public void setSavedAsFav(boolean savedAsFav) {
        this.savedAsFav = savedAsFav;
    }

    public String getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(String favoriteId) {
        this.favoriteId = favoriteId;
    }

    public ArrayList<ApplicantDetail> getAcceptedApplicant() {
        return acceptedApplicant;
    }

    public void setAcceptedApplicant(ArrayList<ApplicantDetail> acceptedApplicant) {
        this.acceptedApplicant = acceptedApplicant;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public RecruiterDetail getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(RecruiterDetail postedBy) {
        this.postedBy = postedBy;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(String budgetType) {
        this.budgetType = budgetType;
    }

    public String getPurposeOfJob() {
        return purposeOfJob;
    }

    public void setPurposeOfJob(String purposeOfJob) {
        this.purposeOfJob = purposeOfJob;
    }

    public RecruiterJobSkillModel getSkills() {
        return skills;
    }

    public void setSkills(RecruiterJobSkillModel skills) {
        this.skills = skills;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getApplicantCount() {
        return applicantCount;
    }

    public void setApplicantCount(String applicantCount) {
        this.applicantCount = applicantCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public class RecruiterJobSkillModel implements Serializable {
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
