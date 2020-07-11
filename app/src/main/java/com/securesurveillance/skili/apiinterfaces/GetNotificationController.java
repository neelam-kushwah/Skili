package com.securesurveillance.skili.apiinterfaces;

import com.securesurveillance.skili.apiHandler.AppConstants;
import com.securesurveillance.skili.model.responsemodel.GetAllJobRecruiterModel;
import com.securesurveillance.skili.model.responsemodel.NotificationModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Header;

/**
 * Created by adarsh on 8/19/2018.
 */

public interface GetNotificationController {

    @GET(AppConstants.Urls.FETCH_NOTIFICATION)
    Call<NotificationModel> getResponse(@Header("authorization") String token , @Query("pid") String profileId);
}
