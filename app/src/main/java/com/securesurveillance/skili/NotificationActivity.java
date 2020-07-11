package com.securesurveillance.skili;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.securesurveillance.skili.adapter.NotificationAdapter;
import com.securesurveillance.skili.apiHandler.API;
import com.securesurveillance.skili.apiHandler.APIResponseListener;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.GetRetrofit;
import com.securesurveillance.skili.apiinterfaces.GetAllRatingsController;
import com.securesurveillance.skili.apiinterfaces.GetJobDetailController;
import com.securesurveillance.skili.apiinterfaces.GetNotificationController;
import com.securesurveillance.skili.model.responsemodel.GetAllJobRecruiterDetailModel;
import com.securesurveillance.skili.model.responsemodel.GetAllProfileDetailsModel;
import com.securesurveillance.skili.model.responsemodel.NotificationModel;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.SharePreferanceWrapperSingleton;
import com.securesurveillance.skili.utility.SweetAlertController;

import java.io.Serializable;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Response;

import static com.securesurveillance.skili.SliderActivity.hotlist_hot;

/**
 * Created by adarsh on 1/20/2019.
 */

public class NotificationActivity extends Activity implements NotificationAdapter.OnViewJobClicked {
    private TextView tv_Title;
    private ImageView iv_LeftOption;
    private RecyclerView rvNotification;
    private Context ctx;
    SharePreferanceWrapperSingleton objSPS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ctx = this;
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(ctx);

        tv_Title = (TextView) findViewById(R.id.tv_Title);
        iv_LeftOption = (ImageView) findViewById(R.id.iv_LeftOption);
        rvNotification = (RecyclerView) findViewById(R.id.rvNotification);
        iv_LeftOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_Title.setText("Notifications");

        getNotificationList();
    }
    private void getAllRatings() {
        GetAllRatingsController anInterface = GetRetrofit.getInstance().create(GetAllRatingsController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));

        Call<GetAllProfileDetailsModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN), objSPS.getValueFromShared_Pref(Constants.ID));
        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {

                    if (response.code() == 200) {
                        if (response.body() instanceof GetAllProfileDetailsModel) {
                            GetAllProfileDetailsModel model = (GetAllProfileDetailsModel) response.body();
                            CommonMethod.cancelDialog(dialog,ctx);
                            if (model.isStatus()) {
                                Intent i = new Intent(ctx, RatingCommentsActivity.class);
                                i.putExtra("RATING", model.getResult().getRating());
                                if (TextUtils.isEmpty(model.getResult().getLastName())) {
                                    model.getResult().setLastName("");
                                }
                                i.putExtra("NAME", model.getResult().getFirstName() + " " + model.getResult().getLastName());
                                i.putExtra("PIC", objSPS.getValueFromShared_Pref(Constants.IMAGE));
                                if (model.getResult().getRatingFeedbacks().size() > 0) {

                                    i.putExtra("RATINGDATA", (Serializable) model.getResult().getRatingFeedbacks());
                                }
                                startActivity(i);

                            } else{
                                SweetAlertController.getInstance().showErrorDialog(ctx, SweetAlertDialog.ERROR_TYPE,
                                        model.getMessage(), getString(R.string.app_name));

                            }
                        }
                    } else {
                        CommonMethod.cancelDialog(dialog,ctx);
                        CommonMethod.showApiMsgToast(ctx, response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call call, final Throwable t) {
                if (!isFinishing()) {
                  runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (t instanceof UnknownHostException) {
                                CommonMethod.showToastMessage(ctx, getString(R.string.UNKNOWN_HOST));
                            } else if (t instanceof ConnectException) {
                                CommonMethod.showToastMessage(ctx, getString(R.string.UNKNOWN_HOST));
                            } else if (t instanceof SocketTimeoutException) {
                                CommonMethod.showToastMessage(ctx, getString(R.string.SOCKET_TIME_OUT));
                            } else if (t instanceof SocketException) {
                                CommonMethod.showToastMessage(ctx, getString(R.string.UNABLE_TO_CONNECT));
                            } else if (t instanceof JsonSyntaxException) {
                                CommonMethod.showToastMessage(ctx, getString(R.string.JSON_SYNTAX));
                            } else {
                                CommonMethod.showToastMessage(ctx, getString(R.string.UNKNOWN_ERROR));
                            }
                            t.printStackTrace();
                        }
                    });
                }
            }
        });

    }

    private void getJobDetail(String jobID) {
        GetJobDetailController anInterface = GetRetrofit.getInstance().create(GetJobDetailController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));


        Call<GetAllJobRecruiterDetailModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN), jobID);
        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {

                    if (response.code() == 200) {
                        if (response.body() instanceof GetAllJobRecruiterDetailModel) {
                            GetAllJobRecruiterDetailModel model = (GetAllJobRecruiterDetailModel) response.body();
                            if (model.getStatusCode().equals("200")) {
                                Intent i = new Intent(ctx, JobDetailRecruiterActivity.class);
                                i.putExtra("DATA", model);
                                if (model.isSavedAsFav()) {
                                    i.putExtra("ISFAV", 1);
                                } else {
                                    i.putExtra("ISFAV", 0);
                                }
                                startActivity(i);
                                CommonMethod.cancelDialog(dialog, ctx);

                            } else {
                                SweetAlertController.getInstance().showErrorDialog(ctx, SweetAlertDialog.ERROR_TYPE,
                                        model.getMessage(), getString(R.string.app_name));

                            }
                        }
                    } else {
                        CommonMethod.cancelDialog(dialog, ctx);
                        CommonMethod.showApiMsgToast(ctx, response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call call, final Throwable t) {
                if (!isFinishing()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (t instanceof UnknownHostException) {
                                CommonMethod.showToastMessage(ctx, getString(R.string.UNKNOWN_HOST));
                            } else if (t instanceof ConnectException) {
                                CommonMethod.showToastMessage(ctx, getString(R.string.UNKNOWN_HOST));
                            } else if (t instanceof SocketTimeoutException) {
                                CommonMethod.showToastMessage(ctx, getString(R.string.SOCKET_TIME_OUT));
                            } else if (t instanceof SocketException) {
                                CommonMethod.showToastMessage(ctx, getString(R.string.UNABLE_TO_CONNECT));
                            } else if (t instanceof JsonSyntaxException) {
                                CommonMethod.showToastMessage(ctx, getString(R.string.JSON_SYNTAX));
                            } else {
                                CommonMethod.showToastMessage(ctx, getString(R.string.UNKNOWN_ERROR));
                            }
                            t.printStackTrace();
                        }
                    });

                }
            }
        });

    }

    private void getNotificationList() {
        GetNotificationController anInterface = GetRetrofit.getInstance().create(GetNotificationController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));


        Call<NotificationModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN), objSPS.getValueFromShared_Pref(Constants.ID));
        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    // refresh complete
                    if (response.code() == 200) {
                        if (response.body() instanceof NotificationModel) {
                            NotificationModel model = (NotificationModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (model.isStatus()) {
                                hotlist_hot.setVisibility(View.GONE);

                                NotificationAdapter adapter = new NotificationAdapter(ctx, model.getResult());
                                rvNotification.setAdapter(adapter);
                                rvNotification.setLayoutManager(new LinearLayoutManager(ctx));


                            } else {
                                SweetAlertController.getInstance().showErrorDialog(ctx, SweetAlertDialog.ERROR_TYPE,
                                        model.getMessage(), getString(R.string.app_name));

                            }
                        }
                    } else {
                        CommonMethod.cancelDialog(dialog, ctx);
                        CommonMethod.showApiMsgToast(ctx, response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call call, final Throwable t) {
                if (!isFinishing()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (t instanceof UnknownHostException) {
                                CommonMethod.showToastMessage(ctx, getString(R.string.UNKNOWN_HOST));
                            } else if (t instanceof ConnectException) {
                                CommonMethod.showToastMessage(ctx, getString(R.string.UNKNOWN_HOST));
                            } else if (t instanceof SocketTimeoutException) {
                                CommonMethod.showToastMessage(ctx, getString(R.string.SOCKET_TIME_OUT));
                            } else if (t instanceof SocketException) {
                                CommonMethod.showToastMessage(ctx, getString(R.string.UNABLE_TO_CONNECT));
                            } else if (t instanceof JsonSyntaxException) {
                                CommonMethod.showToastMessage(ctx, getString(R.string.JSON_SYNTAX));
                            } else {
                                CommonMethod.showToastMessage(ctx, getString(R.string.UNKNOWN_ERROR));
                            }
                            t.printStackTrace();
                        }
                    });

                }
            }
        });

    }

    @Override
    public void onJobclicked(String jobId) {
        getJobDetail(jobId);
    }

    @Override
    public void onSeeRatingClicked() {
        getAllRatings();
    }
}
