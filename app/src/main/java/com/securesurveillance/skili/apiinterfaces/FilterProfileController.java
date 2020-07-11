package com.securesurveillance.skili.apiinterfaces;

import com.securesurveillance.skili.apiHandler.AppConstants;
import com.securesurveillance.skili.model.responsemodel.FilterModel;
import com.securesurveillance.skili.model.responsemodel.GetAllJobRecruiterModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by adarsh on 8/19/2018.
 */

public interface FilterProfileController {

    @GET(AppConstants.Urls.FILTER_PROFILE)
    Call<FilterModel> getResponse(@Header("authorization") String token);
}
