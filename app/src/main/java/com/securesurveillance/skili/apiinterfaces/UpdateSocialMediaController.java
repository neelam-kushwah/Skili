package com.securesurveillance.skili.apiinterfaces;

import com.securesurveillance.skili.apiHandler.AppConstants;
import com.securesurveillance.skili.model.responsemodel.LoginModel;
import com.securesurveillance.skili.model.responsemodel.UpdateExperienceResponseModel;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by adarsh on 8/4/2018.
 */

public interface UpdateSocialMediaController {


    @POST(AppConstants.Urls.UPDATE_SOCIAL_MEDIA_URL)
    Call<LoginModel> getResponse(@Header("authorization") String token, @Body RequestBody json);
}
