package com.securesurveillance.skili.apiinterfaces;

import com.securesurveillance.skili.apiHandler.AppConstants;
import com.securesurveillance.skili.model.responsemodel.FilterModel;
import com.securesurveillance.skili.model.responsemodel.GetAllJobRecruiterModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by adarsh on 8/19/2018.
 */

public interface FilterJobController {

    @GET(AppConstants.Urls.FILTER_JOB)
    Call<FilterModel> getResponse(@Header("authorization") String token);
}
