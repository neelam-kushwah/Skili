package com.securesurveillance.skili.apiinterfaces;

import com.securesurveillance.skili.apiHandler.AppConstants;
import com.securesurveillance.skili.model.responsemodel.GetCategoryModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Header;

/**
 * Created by adarsh on 8/19/2018.
 */

public interface GetCategoryController {

    @GET(AppConstants.Urls.GET_CATEGORY_URL)
    Call<GetCategoryModel> getResponse(@Header("authorization") String token );
}
