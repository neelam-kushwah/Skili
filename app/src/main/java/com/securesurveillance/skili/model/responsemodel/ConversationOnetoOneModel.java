package com.securesurveillance.skili.model.responsemodel;

import java.util.ArrayList;

/**
 * Created by adarsh on 8/4/2018.
 */

public class ConversationOnetoOneModel {
    String message;
    boolean status;
    Result result;

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

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {
        String favouriteProfile;
        ArrayList<ConversationResponse> conversation;

        public String getFavouriteProfile() {
            return favouriteProfile;
        }

        public void setFavouriteProfile(String favouriteProfile) {
            this.favouriteProfile = favouriteProfile;
        }

        public ArrayList<ConversationResponse> getConversation() {
            return conversation;
        }

        public void setConversation(ArrayList<ConversationResponse> conversation) {
            this.conversation = conversation;
        }
    }


    public class ConversationResponse {

        String id;
        String message;
        String fromProfileId;
        String msgTimeInMillisecond;

        public String getMsgTimeInMillisecond() {
            return msgTimeInMillisecond;
        }

        public void setMsgTimeInMillisecond(String msgTimeInMillisecond) {
            this.msgTimeInMillisecond = msgTimeInMillisecond;
        }

        public String getFromProfileId() {
            return fromProfileId;
        }

        public void setFromProfileId(String fromProfileId) {
            this.fromProfileId = fromProfileId;
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
