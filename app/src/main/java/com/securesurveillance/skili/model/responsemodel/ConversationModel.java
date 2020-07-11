package com.securesurveillance.skili.model.responsemodel;

import java.util.ArrayList;

/**
 * Created by adarsh on 8/4/2018.
 */

public class ConversationModel {
    String message;
    boolean status;
    ArrayList<ConversationResponse> result;


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

    public ArrayList<ConversationResponse> getResult() {
        return result;
    }

    public void setResult(ArrayList<ConversationResponse> result) {
        this.result = result;
    }

    public class ConversationResponse {

        String id;
        String message;
        String toProfileId;
        String toProfilePic;
        String toProfileName;
        String msgDate;
        boolean favouriteProfile;


        public boolean isFavouriteProfile() {
            return favouriteProfile;
        }

        public void setFavouriteProfile(boolean favouriteProfile) {
            this.favouriteProfile = favouriteProfile;
        }

        public String getMsgDate() {
            return msgDate;
        }

        public void setMsgDate(String msgDate) {
            this.msgDate = msgDate;
        }

        public String getToProfileId() {
            return toProfileId;
        }

        public void setToProfileId(String toProfileId) {
            this.toProfileId = toProfileId;
        }

        public String getToProfilePic() {
            return toProfilePic;
        }

        public void setToProfilePic(String toProfilePic) {
            this.toProfilePic = toProfilePic;
        }

        public String getToProfileName() {
            return toProfileName;
        }

        public void setToProfileName(String toProfileName) {
            this.toProfileName = toProfileName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
