package com.securesurveillance.skili.model.responsemodel;

import com.securesurveillance.skili.model.SkillModel;

import java.util.ArrayList;

/**
 * Created by adarsh on 8/19/2018.
 */

public class GetCategoryModel {
    private boolean status;
    private ArrayList<SkillModel> result;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<SkillModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<SkillModel> result) {
        this.result = result;
    }
}
