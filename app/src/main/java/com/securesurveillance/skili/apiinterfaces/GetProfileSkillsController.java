package com.securesurveillance.skili.apiinterfaces;

import com.securesurveillance.skili.apiHandler.AppConstants;
import com.securesurveillance.skili.model.requestmodel.UpdateSkillsRequestModel;
import com.securesurveillance.skili.model.responsemodel.GetApplicantListModel;
import com.securesurveillance.skili.model.responsemodel.GetSkillResponsEModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Header;

/**
 * Created by adarsh on 8/19/2018.
 */

public interface GetProfileSkillsController {

    @GET(AppConstants.Urls.PROFILE_GET_SKILLS)
    Call<GetSkillResponsEModel> getResponse(@Header("authorization") String token ,@Query("mobileNumber") String mobileNumber, @Query("profileId") String profileId);
}
