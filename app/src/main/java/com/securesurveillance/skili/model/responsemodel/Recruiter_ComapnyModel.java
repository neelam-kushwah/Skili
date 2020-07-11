package com.securesurveillance.skili.model.responsemodel;

import com.securesurveillance.skili.model.SkillModel;

import java.util.ArrayList;

/**
 * Created by adarsh on 8/4/2018.
 */

public class Recruiter_ComapnyModel {
    String message;
    boolean status;
    DetailModel result;

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


    public DetailModel getResult() {
        return result;
    }

    public void setResult(DetailModel result) {
        this.result = result;
    }



    public class DetailModel {
        ArrayList<SkillModel.InnerSkillModel> categories;
        String mobileNumber;
        String lastName;
        String _id;
        String firstName;
        String role;
        String locationString;
        String city;
        String email;
        String website;
        String companyName;
        String GSTIN;
        String officeAddress;


        public String getOfficeAddress() {
            return officeAddress;
        }

        public void setOfficeAddress(String officeAddress) {
            this.officeAddress = officeAddress;
        }

        public String getGSTIN() {
            return GSTIN;
        }

        public void setGSTIN(String GSTIN) {
            this.GSTIN = GSTIN;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
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

        public String getLocationString() {
            return locationString;
        }

        public void setLocationString(String locationString) {
            this.locationString = locationString;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public ArrayList<SkillModel.InnerSkillModel> getCategories() {
            return categories;
        }

        public void setCategories(ArrayList<SkillModel.InnerSkillModel> categories) {
            this.categories = categories;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

      }



}
