package com.securesurveillance.skili.model.responsemodel;

import java.util.ArrayList;

/**
 * Created by adarsh on 9/23/2018.
 */

public class GetFavJobListModel {
    boolean status;
    String message;
    ArrayList<GetAllJobRecruiterDetailModel> result;

    public ArrayList<GetAllJobRecruiterDetailModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<GetAllJobRecruiterDetailModel> result) {
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


}
