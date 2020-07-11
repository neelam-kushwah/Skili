package com.securesurveillance.skili.model.responsemodel;

import com.securesurveillance.skili.model.SkillModel;

import java.util.ArrayList;

/**
 * Created by adarsh on 8/4/2018.
 */

public class LoginModel {
    String message;
    boolean status;
    LoginResponse result;


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


    public LoginResponse getResult() {
        return result;
    }

    public void setResult(LoginResponse result) {
        this.result = result;
    }

    public class LoginResponse {
        String mobileNumber;
        String role;
        String token;
        String _id;
        String firstName;
        String lastName;
        String profilePic;
        String profilePicThumbnail;
        String reference_id;// only for add fav
        String fcmTokenReceived;

        public String getFcmTokenReceived() {
            return fcmTokenReceived;
        }

        public void setFcmTokenReceived(String fcmTokenReceived) {
            this.fcmTokenReceived = fcmTokenReceived;
        }

        public String getProfilePicThumbnail() {
            return profilePicThumbnail;
        }

        public void setProfilePicThumbnail(String profilePicThumbnail) {
            this.profilePicThumbnail = profilePicThumbnail;
        }

        public String getReference_id() {
            return reference_id;
        }

        public void setReference_id(String reference_id) {
            this.reference_id = reference_id;
        }

        ArrayList<Skills> skills;

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        public ArrayList<Skills> getSkills() {
            return skills;
        }

        public void setSkills(ArrayList<Skills> skills) {
            this.skills = skills;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public class Skills {
            private String category;

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }
        }
    }

}
