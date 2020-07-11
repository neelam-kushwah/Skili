package com.securesurveillance.skili.apiinterfaces;

import com.securesurveillance.skili.apiHandler.AppConstants;
import com.securesurveillance.skili.model.responsemodel.LoginModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Header;

/**
 * Created by adarsh on 8/19/2018.
 */

public interface RemoveFavRecruiterController {

    @DELETE(AppConstants.Urls.DELETE_FAVOURITE_APPLICANT)
    Call<LoginModel> getResponse(@Header("authorization") String token , @Query("favoriteId") String favouriteId);
}
