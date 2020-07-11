package com.securesurveillance.skili.apiinterfaces;

import com.securesurveillance.skili.apiHandler.AppConstants;
import com.securesurveillance.skili.model.responsemodel.ProfilePicResponseModel;
import com.securesurveillance.skili.model.responsemodel.UploadPostResponseModel;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Header;


/**
 * Created by adarsh on 8/4/2018.
 */

public interface UploadPostController {


    @Multipart
    @POST(AppConstants.Urls.UPLOAD_POST)
    Call<UploadPostResponseModel> getResponse(@Header("authorization") String token , @Query("descriptions") String descriptions,

                                              @Query("fileType") String fileType,
                                              @Query("profileId") String profileId,
                                              @Part MultipartBody.Part file
    );


}
