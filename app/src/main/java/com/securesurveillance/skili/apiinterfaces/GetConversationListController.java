package com.securesurveillance.skili.apiinterfaces;

import com.securesurveillance.skili.apiHandler.AppConstants;
import com.securesurveillance.skili.model.responsemodel.ConversationModel;
import com.securesurveillance.skili.model.responsemodel.ConversationOnetoOneModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by adarsh on 8/19/2018.
 */

public interface GetConversationListController {

    @GET(AppConstants.Urls.CONVERSATION_LIST)
    Call<ConversationModel> getResponse(@Header("authorization") String token, @Query("fromId") String fromProfileId);
}
