package com.securesurveillance.skili.apiinterfaces;

import com.securesurveillance.skili.apiHandler.AppConstants;
import com.securesurveillance.skili.model.responsemodel.LoginModel;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Header;

/**
 * Created by adarsh on 8/4/2018.
 */

public interface LoginController {


    @POST(AppConstants.Urls.LOGIN_URL)
    Call<LoginModel> getResponse(@Body RequestBody json);
}
