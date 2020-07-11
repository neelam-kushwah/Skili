package com.securesurveillance.skili.apiinterfaces;

import com.securesurveillance.skili.apiHandler.AppConstants;
import com.securesurveillance.skili.model.responsemodel.GetAllJobRecruiterModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by adarsh on 8/19/2018.
 */

public interface GetAllJobRecruiterController {

    @GET(AppConstants.Urls.GET_ALL_JOB_RECRUITER)
    Call<GetAllJobRecruiterModel> getResponse(@Header("authorization") String token , @Query("pid") String recruiterProfileId);
}
