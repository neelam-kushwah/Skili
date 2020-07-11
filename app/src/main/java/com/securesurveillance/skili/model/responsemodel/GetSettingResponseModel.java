package com.securesurveillance.skili.model.responsemodel;

import java.util.ArrayList;

/**
 * Created by adarsh on 8/4/2018.
 */

public class GetSettingResponseModel {
    String message;
    boolean status;
    SettingResponse result;

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


    public SettingResponse getResult() {
        return result;
    }

    public void setResult(SettingResponse result) {
        this.result = result;
    }

    public class SettingResponse {
        String whoCanSeePost;
        boolean recevieSms;
        boolean recevieEmail;
        boolean activeAsFreelancerRecruiter;
        String whoCanSeeMobile;
        String whoCanSeeEmail;
        String whoCanSeeAddress;

        public String getWhoCanSeeMobile() {
            return whoCanSeeMobile;
        }

        public void setWhoCanSeeMobile(String whoCanSeeMobile) {
            this.whoCanSeeMobile = whoCanSeeMobile;
        }

        public String getWhoCanSeeEmail() {
            return whoCanSeeEmail;
        }

        public void setWhoCanSeeEmail(String whoCanSeeEmail) {
            this.whoCanSeeEmail = whoCanSeeEmail;
        }

        public String getWhoCanSeeAddress() {
            return whoCanSeeAddress;
        }

        public void setWhoCanSeeAddress(String whoCanSeeAddress) {
            this.whoCanSeeAddress = whoCanSeeAddress;
        }

        public String getWhoCanSeePost() {
            return whoCanSeePost;
        }

        public void setWhoCanSeePost(String whoCanSeePost) {
            this.whoCanSeePost = whoCanSeePost;
        }

        public boolean isRecevieSms() {
            return recevieSms;
        }

        public void setRecevieSms(boolean recevieSms) {
            this.recevieSms = recevieSms;
        }

        public boolean isRecevieEmail() {
            return recevieEmail;
        }

        public void setRecevieEmail(boolean recevieEmail) {
            this.recevieEmail = recevieEmail;
        }

        public boolean isActiveAsFreelancerRecruiter() {
            return activeAsFreelancerRecruiter;
        }

        public void setActiveAsFreelancerRecruiter(boolean activeAsFreelancerRecruiter) {
            this.activeAsFreelancerRecruiter = activeAsFreelancerRecruiter;
        }
    }

}
