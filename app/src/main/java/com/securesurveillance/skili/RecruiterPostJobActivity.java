package com.securesurveillance.skili;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.melnykov.fab.FloatingActionButton;
import com.securesurveillance.skili.PostJobStepAActivity;
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.adapter.RecruiterPostedJobsAdapter;
import com.securesurveillance.skili.adapter.SliderJobsAdapter;
import com.securesurveillance.skili.apiHandler.API;
import com.securesurveillance.skili.apiHandler.APIResponseListener;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.GetRetrofit;
import com.securesurveillance.skili.apiinterfaces.GetAllJobRecruiterController;
import com.securesurveillance.skili.model.responsemodel.GetAllJobRecruiterDetailModel;
import com.securesurveillance.skili.model.responsemodel.GetAllJobRecruiterModel;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.SharePreferanceWrapperSingleton;
import com.securesurveillance.skili.utility.SweetAlertController;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecruiterPostJobActivity extends Activity {
    SharePreferanceWrapperSingleton objSPS;
    RecyclerView rvGlobalFeed;
    private Context ctx;
    FloatingActionButton fab;
    TextView tv_Title;
    ImageView iv_LeftOption;

    String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiterpostjob);
        ctx = this;
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(ctx);
        tv_Title = (TextView) findViewById(R.id.tv_Title);
        iv_LeftOption = (ImageView) findViewById(R.id.iv_LeftOption);
        rvGlobalFeed = (RecyclerView) findViewById(R.id.rvGlobalFeed);
        tv_Title.setText("Posted Job");

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        id=getIntent().getStringExtra("PROFILEID");
        iv_LeftOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    private void getAllJobRecruiter() {
        GetAllJobRecruiterController anInterface = GetRetrofit.getInstance().create(GetAllJobRecruiterController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));


        Call<GetAllJobRecruiterModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN), id);
        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    // refresh complete
                    if (response.code() == 200) {
                        if (response.body() instanceof GetAllJobRecruiterModel) {
                            GetAllJobRecruiterModel model = (GetAllJobRecruiterModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (model.isStatus()) {
                                setJobAdapter(model.getResult());
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
    public void onResume() {
        super.onResume();

            getAllJobRecruiter();

    }

    private void setJobAdapter(ArrayList<GetAllJobRecruiterDetailModel> arrList) {

        RecruiterPostedJobsAdapter adapter = new RecruiterPostedJobsAdapter(ctx, arrList, objSPS,0);
        rvGlobalFeed.setAdapter(adapter);
        rvGlobalFeed.setLayoutManager(new LinearLayoutManager(ctx));


    }
}
