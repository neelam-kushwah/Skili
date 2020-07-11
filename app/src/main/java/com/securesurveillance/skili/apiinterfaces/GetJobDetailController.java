package com.securesurveillance.skili.apiinterfaces;

import com.securesurveillance.skili.apiHandler.AppConstants;
import com.securesurveillance.skili.model.responsemodel.GetAllJobRecruiterDetailModel;
import com.securesurveillance.skili.model.responsemodel.GetAllProfileDetailsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by adarsh on 8/19/2018.
 */

public interface GetJobDetailController {

    @GET(AppConstants.Urls.GET_JOB_DETAIL)
    Call<GetAllJobRecruiterDetailModel> getResponse(@Header("authorization") String token,
                                                   @Query("jid") String jobId);
}
