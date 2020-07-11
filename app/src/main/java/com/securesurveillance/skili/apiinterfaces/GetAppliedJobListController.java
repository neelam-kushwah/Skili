package com.securesurveillance.skili.apiinterfaces;

import com.securesurveillance.skili.apiHandler.AppConstants;
import com.securesurveillance.skili.model.responsemodel.GetAppliedJobListModel;
import com.securesurveillance.skili.model.responsemodel.GetFavApplicantListModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by adarsh on 8/19/2018.
 */

public interface GetAppliedJobListController {

    @GET(AppConstants.Urls.GET_APPLIED_JOB_LIST)
    Call<GetAppliedJobListModel> getResponse(@Header("authorization") String token , @Query("pid") String profileId);
}
