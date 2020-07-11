package com.securesurveillance.skili.apiinterfaces;

import com.securesurveillance.skili.apiHandler.AppConstants;
import com.securesurveillance.skili.model.responsemodel.LoginModel;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by adarsh on 8/4/2018.
 */

public interface EditJobController {


    @POST(AppConstants.Urls.JOB_EDIT)
    Call<LoginModel> getResponse(@Header("authorization") String token , @Body RequestBody json);
}
