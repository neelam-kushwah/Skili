package com.securesurveillance.skili.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.JsonSyntaxException;
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.adapter.HomePagerAdapter;
import com.securesurveillance.skili.apiHandler.API;
import com.securesurveillance.skili.apiHandler.APIResponseListener;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.GetRetrofit;
import com.securesurveillance.skili.apiinterfaces.FilterJobController;
import com.securesurveillance.skili.apiinterfaces.FilterProfileController;
import com.securesurveillance.skili.apiinterfaces.GetAllJobRecruiterController;
import com.securesurveillance.skili.model.responsemodel.FilterModel;
import com.securesurveillance.skili.model.responsemodel.GetAllJobRecruiterModel;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.DialogUtil;
import com.securesurveillance.skili.utility.PagerSlidingTabStrip;
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
public class SkilliHomeFragment extends Fragment {

    private PagerSlidingTabStrip tabs;
    public static ViewPager pager;
    private HomePagerAdapter adapter;
//    private RelativeLayout rlFilter;

    public SkilliHomeFragment() {
        // Required empty public constructor
    }

    SharePreferanceWrapperSingleton objSPS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_skilli_home, container, false);
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(getActivity());
        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        pager = (ViewPager) view.findViewById(R.id.pager);
//        rlFilter = (RelativeLayout) view.findViewById(R.id.rlFilter);
//        rlFilter.setOnClickListener(this);
        adapter = new HomePagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
        pager.setOffscreenPageLimit(0);
        pager.setCurrentItem(0);
        return view;
    }

//    private void callFilterApi() {
//        Map<String, String> data = new HashMap<>();
//        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
//        Call<FilterModel> call;
//
//        String role = SharePreferanceWrapperSingleton.getSingletonInstance().getValueFromShared_Pref(Constants.ROLE);
//        if (role.contains("Individual")) {
//            //jobs
//            FilterJobController anInterface = GetRetrofit.getInstance().create(FilterJobController.class);
//           call = anInterface.getResponse(
//                    objSPS.getValueFromShared_Pref(Constants.TOKEN));
//        } else {
//            //profile
//            FilterProfileController anInterface = GetRetrofit.getInstance().create(FilterProfileController.class);
//           call = anInterface.getResponse(
//                    objSPS.getValueFromShared_Pref(Constants.TOKEN));
//        }
//
//
//        API.callRun(getActivity(), true, call, new APIResponseListener() {
//            @Override
//            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
//                if (!getActivity().isFinishing()) {
//                    // refresh complete
//                    if (response.code() == 200) {
//                        if (response.body() instanceof FilterModel) {
//                            FilterModel model = (FilterModel) response.body();
//                            CommonMethod.cancelDialog(dialog, getActivity());
//                            if (model.isStatus()) {
//                                DialogUtil.openFilterDialog(getActivity(), getActivity(),model.getResult().getCity(),
//                                        model.getResult().getSkills(),SkilliHomeFragment.this);
//
//                            } else {
//                                SweetAlertController.getInstance().showErrorDialog(getActivity(), SweetAlertDialog.ERROR_TYPE,
//                                        model.getMessage(), getString(R.string.app_name));
//
//                            }
//                        }
//                    } else {
//                        CommonMethod.cancelDialog(dialog, getActivity());
//                        CommonMethod.showApiMsgToast(getActivity(), response.code());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call call, final Throwable t) {
//                if (!getActivity().isFinishing()) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (t instanceof UnknownHostException) {
//                                CommonMethod.showToastMessage(getActivity(), getString(R.string.UNKNOWN_HOST));
//                            } else if (t instanceof ConnectException) {
//                                CommonMethod.showToastMessage(getActivity(), getString(R.string.UNKNOWN_HOST));
//                            } else if (t instanceof SocketTimeoutException) {
//                                CommonMethod.showToastMessage(getActivity(), getString(R.string.SOCKET_TIME_OUT));
//                            } else if (t instanceof SocketException) {
//                                CommonMethod.showToastMessage(getActivity(), getString(R.string.UNABLE_TO_CONNECT));
//                            } else if (t instanceof JsonSyntaxException) {
//                                CommonMethod.showToastMessage(getActivity(), getString(R.string.JSON_SYNTAX));
//                            } else {
//                                CommonMethod.showToastMessage(getActivity(), getString(R.string.UNKNOWN_ERROR));
//                            }
//                            t.printStackTrace();
//                        }
//                    });
//
//                }
//            }
//        });
//
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.rlFilter:
//                callFilterApi();
//
//                break;
//        }
//    }
//
//    @Override
//    public void onSearch(ArrayList<String> arrSkills, String selectedCity, String s, String toString, Number selectedMaxValue) {
//
//    }
}
