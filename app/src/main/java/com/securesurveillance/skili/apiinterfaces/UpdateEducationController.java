package com.securesurveillance.skili.apiinterfaces;

import com.securesurveillance.skili.apiHandler.AppConstants;
import com.securesurveillance.skili.model.responsemodel.LoginModel;
import com.securesurveillance.skili.model.responsemodel.UpdateEducationResponseModel;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Header;

/**
 * Created by adarsh on 8/4/2018.
 */

public interface UpdateEducationController {


    @POST(AppConstants.Urls.UPDATE_EDUCATION)
    Call<UpdateEducationResponseModel> getResponse(@Header("authorization") String token , @Body RequestBody json);
}
