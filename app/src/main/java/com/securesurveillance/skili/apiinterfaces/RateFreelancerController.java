package com.securesurveillance.skili.apiinterfaces;

import com.securesurveillance.skili.apiHandler.AppConstants;
import com.securesurveillance.skili.model.responsemodel.LoginModel;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by adarsh on 8/4/2018.
 */

public interface RateFreelancerController {


    @POST(AppConstants.Urls.RATING_INDI)
    Call<LoginModel> getResponse(@Header("authorization") String token, @Body RequestBody json);
}
