package com.securesurveillance.skili.model.responsemodel;

import java.util.ArrayList;

/**
 * Created by adarsh on 9/23/2018.
 */

public class GetApplicantListModel {
    boolean status;
    String message;
    Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Result {
        ArrayList<Applicant> applicants;

        public ArrayList<Applicant> getApplicants() {
            return applicants;
        }

        public void setApplicants(ArrayList<Applicant> applicants) {
            this.applicants = applicants;
        }

        public class Applicant {

            String name;
            String profilePic;
            String rating;
            String profileId;
            String favouriteApplicant;
            String mobileNumber;

            public String getFavouriteApplicant() {
                return favouriteApplicant;
            }

            public void setFavouriteApplicant(String favouriteApplicant) {
                this.favouriteApplicant = favouriteApplicant;
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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getProfilePic() {
                return profilePic;
            }

            public void setProfilePic(String profilePic) {
                this.profilePic = profilePic;
            }

            public String getRating() {
                return rating;
            }

            public void setRating(String rating) {
                this.rating = rating;
            }
        }
    }
}
