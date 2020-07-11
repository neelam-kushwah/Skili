package com.securesurveillance.skili.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.JsonSyntaxException;
import com.melnykov.fab.FloatingActionButton;
import com.securesurveillance.skili.PostJobStepAActivity;
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.adapter.SliderJobsAdapter;
import com.securesurveillance.skili.apiHandler.API;
import com.securesurveillance.skili.apiHandler.APIResponseListener;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.GetRetrofit;
import com.securesurveillance.skili.apiinterfaces.GetAllJobRecruiterController;
import com.securesurveillance.skili.apiinterfaces.GetAppliedJobListController;
import com.securesurveillance.skili.model.responsemodel.GetAllJobRecruiterDetailModel;
import com.securesurveillance.skili.model.responsemodel.GetAllJobRecruiterModel;
import com.securesurveillance.skili.model.responsemodel.GetAppliedJobListModel;
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
public class AppliedJobFragment extends Fragment {
    SharePreferanceWrapperSingleton objSPS;
    RecyclerView rvGlobalFeed;
    private Context ctx;
    MaterialRefreshLayout refresh;

    public AppliedJobFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_individual_jobs, container, false);
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(getActivity());
        rvGlobalFeed = (RecyclerView) view.findViewById(R.id.rvGlobalFeed);
        refresh = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
        ctx = getActivity();
        getAppliedJobList(true);
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                //refreshing...
                getAppliedJobList(false);

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //load more refreshing...
            }
        });
        return view;
    }

    private void getAppliedJobList(boolean isNotRefresh) {
        GetAppliedJobListController anInterface = GetRetrofit.getInstance().create(GetAppliedJobListController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));


        Call<GetAppliedJobListModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN), objSPS.getValueFromShared_Pref(Constants.ID));
        API.callRun(ctx, isNotRefresh, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (getActivity()!=null&&!getActivity().isFinishing()) {
                    // refresh complete
                    refresh.finishRefresh();
                    if (response.code() == 200) {
                        if (response.body() instanceof GetAppliedJobListModel) {
                            GetAppliedJobListModel model = (GetAppliedJobListModel) response.body();
                            if (model.isStatus()) {
                                setJobAdapter(model.getResult());
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
                if (getActivity()!=null&&!getActivity().isFinishing()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // refresh complete
                            refresh.finishRefresh();
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


    private void setJobAdapter(ArrayList<GetAllJobRecruiterDetailModel> arrList) {
        if (arrList.size() > 0) {
            SliderJobsAdapter adapter = new SliderJobsAdapter(getActivity(), arrList, objSPS, 0);
            rvGlobalFeed.setAdapter(adapter);
            rvGlobalFeed.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

    }
}
