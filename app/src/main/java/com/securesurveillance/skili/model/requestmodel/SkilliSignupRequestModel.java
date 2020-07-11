package com.securesurveillance.skili.model.requestmodel;

import com.securesurveillance.skili.model.Jobdetail;

import java.util.ArrayList;

/**
 * Created by adarsh on 9/23/2018.
 */

public class SkilliSignupRequestModel {

    String mobileNumber;
    String firstName;
    String lastName;
    String locationString;
    String latitude;
    String longitude;
    String role;
    String city;
    ArrayList<SkillRequestModel> skills;
    ProfilePicRequestModel profilePic;
    String deviceId;


    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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

    public String getLocationString() {
        return locationString;
    }

    public void setLocationString(String locationString) {
        this.locationString = locationString;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ArrayList<SkillRequestModel> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<SkillRequestModel> skills) {
        this.skills = skills;
    }

    public ProfilePicRequestModel getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(ProfilePicRequestModel profilePic) {
        this.profilePic = profilePic;
    }

    public class SkillRequestModel{
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

    public class ProfilePicRequestModel{
       long id;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }
}

