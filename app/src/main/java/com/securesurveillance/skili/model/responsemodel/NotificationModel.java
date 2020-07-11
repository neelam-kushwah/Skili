package com.securesurveillance.skili.model.responsemodel;

import java.util.ArrayList;

/**
 * Created by adarsh on 8/4/2018.
 */

public class NotificationModel {
    String message;
    boolean status;
    ArrayList<NotificationResponse> result;

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

    public ArrayList<NotificationResponse> getResult() {
        return result;
    }

    public void setResult(ArrayList<NotificationResponse> result) {
        this.result = result;
    }

    public class NotificationResponse {

        String date;
        String notificationId;
        String title;
        String descriptions;
        String action;//VIEW_PROFILE ,VIEW_JOB,VIEW_ALL_JOB,NO_ACTION
        String  tag1;
        String tag2;
        String tag3;
        String tag4;
        boolean read;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTag3() {
            return tag3;
        }

        public String getTag4() {
            return tag4;
        }

        public String getTag2() {
            return tag2;
        }


        public String getNotificationId() {
            return notificationId;
        }

        public void setNotificationId(String notificationId) {
            this.notificationId = notificationId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescriptions() {
            return descriptions;
        }


        public String getAction() {
            return action;
        }


        public String getTag1() {
            return tag1;
        }


        public boolean isRead() {
            return read;
        }

    }

}
