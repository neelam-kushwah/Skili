package com.securesurveillance.skili.apiinterfaces;

import com.securesurveillance.skili.apiHandler.AppConstants;
import com.securesurveillance.skili.model.responsemodel.ConversationOnetoOneModel;
import com.securesurveillance.skili.model.responsemodel.GetAppliedJobListModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by adarsh on 8/19/2018.
 */

public interface GetConversationList_OnetoOneController {

    @GET(AppConstants.Urls.CONVERSATION_LIST_ONETOONE)
    Call<ConversationOnetoOneModel> getResponse(@Header("authorization") String token, @Query("fromId") String fromProfileId,
                                                @Query("toId") String toProfileId);
}
