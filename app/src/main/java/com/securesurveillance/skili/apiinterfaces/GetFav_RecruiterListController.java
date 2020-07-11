package com.securesurveillance.skili.apiinterfaces;

import com.securesurveillance.skili.apiHandler.AppConstants;
import com.securesurveillance.skili.model.responsemodel.GetFavApplicantListModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Header;

/**
 * Created by adarsh on 8/19/2018.
 */

public interface GetFav_RecruiterListController {

    @GET(AppConstants.Urls.GET_FAVOURITE_RECRUITER_LIST)
    Call<GetFavApplicantListModel> getResponse(@Header("authorization") String token , @Query("profileId") String profileId);
}
