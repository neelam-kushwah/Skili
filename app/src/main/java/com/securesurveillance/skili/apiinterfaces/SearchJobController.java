package com.securesurveillance.skili.apiinterfaces;

import com.securesurveillance.skili.apiHandler.AppConstants;
import com.securesurveillance.skili.model.responsemodel.SearchJobsResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Header;

/**
 * Created by adarsh on 8/19/2018.
 */

public interface SearchJobController {

    @POST(AppConstants.Urls.GLOBALFEED_DEFAULT)
    Call<SearchJobsResponse> getResponse(@Header("authorization") String token , @Body RequestBody json);
}
