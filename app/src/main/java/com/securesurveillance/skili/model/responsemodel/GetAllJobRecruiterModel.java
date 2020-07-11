package com.securesurveillance.skili.model.responsemodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.securesurveillance.skili.model.Jobdetail;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by adarsh on 9/23/2018.
 */

public class GetAllJobRecruiterModel {
    boolean status;
    String message;
    ArrayList<GetAllJobRecruiterDetailModel> result;


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

    public ArrayList<GetAllJobRecruiterDetailModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<GetAllJobRecruiterDetailModel> result) {
        this.result = result;
    }
}
