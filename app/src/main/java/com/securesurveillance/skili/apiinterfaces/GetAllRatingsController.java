package com.securesurveillance.skili.apiinterfaces;

import com.securesurveillance.skili.apiHandler.AppConstants;
import com.securesurveillance.skili.model.responsemodel.GetAllProfileDetailsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by adarsh on 8/19/2018.
 */

public interface GetAllRatingsController {

    @GET(AppConstants.Urls.GET_ALL_RATING)
    Call<GetAllProfileDetailsModel> getResponse(@Header("authorization") String token,
                                         @Query("profileId") String profileId);
}
