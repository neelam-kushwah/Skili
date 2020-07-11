package com.securesurveillance.skili.apiHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;


import com.securesurveillance.skili.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class API {
    private static ProgressDialog progressDialog;

    public static <ResModel> void callRun(final Context context, final boolean idDialog, final Call<ResModel> call, final int retryCount,
                                          final APIResponseListener listener) {
        if (ConnectivityUtils.isNetworkEnabled(context)) {
            new AsyncTask<Object, Object, String>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    if (idDialog) {
                        CommonMethod.cancelDialog(progressDialog, context);
                        progressDialog = new ProgressDialog(context);
                        CommonMethod.loadingDialog(progressDialog, context);
                    }
                }

                @Override
                protected String doInBackground(Object... params) {
                    call.enqueue(new RetryAbleCallBack<ResModel>(call, retryCount) {
                        @Override
                        public void onFinalResponse(final Call<ResModel> call, final Response<ResModel> response) {

                                final Activity activity = (Activity) context;
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (response != null) {
                                            //  Headers headers = response.headers();
                                            if (response.code() == 401) {
                                                try {
                                                    JSONObject json = new JSONObject(response.errorBody().string());
                                                    String str = json.getJSONObject("data").getString("msg");
                                                    CommonMethod.showToastMessage(context, str);
                                                } catch (JSONException | IOException e) {
                                                    e.printStackTrace();
                                                }

                                            } else if (response.code() == 500) {
                                                CommonMethod.showToastMessage(context, context.getString(R.string.something_went_wrong_on_server) + " 500 error code");
                                                CommonMethod.cancelDialog(progressDialog, context);
                                            } else {
                                                listener.onSuccess(call, response, progressDialog);
                                            }
                                        } else
                                            CommonMethod.cancelDialog(progressDialog, context);
                                    }
                                });



                        }

                        @Override
                        public void onFinalFailure(final Call<ResModel> call, final Throwable t) {
                            try {
                                Activity activity = (Activity) context;
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        CommonMethod.cancelDialog(progressDialog, context);
                                        if (t != null)
                                            listener.onFailure(call, t);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    return null;
                }
            }.execute();

        } else {
            CommonMethod.showToastMessage(context, context.getString(R.string.err_msg_network_not_found));
        }
    }

    public static <ResModel> void callRun(final Context context, final boolean idDialog, Call<ResModel> call, final APIResponseListener listener) {
        callRun(context, idDialog, call, 0, listener);
    }

    static boolean isCallSuccess(Response response) {
        int code = response.code();
        return (code >= 200 && code < 400);
    }

}
