package com.securesurveillance.skili.model.responsemodel;

import java.util.ArrayList;

/**
 * Created by adarsh on 9/23/2018.
 */

public class GetFavApplicantListModel {
    boolean status;
    String message;
    ArrayList<Applicant> result;

    public ArrayList<Applicant> getResult() {
        return result;
    }

    public void setResult(ArrayList<Applicant> result) {
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


    public class Applicant {

        String favoriteId;
        String profileId;
        String name;
        String profilePic;
        String distance;
        String mobileNumber;
        String charge;
        String chargeType;
        String rating;

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getCharge() {
            return charge;
        }

        public void setCharge(String charge) {
            this.charge = charge;
        }

        public String getChargeType() {
            return chargeType;
        }

        public void setChargeType(String chargeType) {
            this.chargeType = chargeType;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
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

        public String getFavoriteId() {
            return favoriteId;
        }

        public void setFavoriteId(String favoriteId) {
            this.favoriteId = favoriteId;
        }

        public String getProfileId() {
            return profileId;
        }

        public void setProfileId(String profileId) {
            this.profileId = profileId;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }
    }

}
