package com.securesurveillance.skili.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.melnykov.fab.FloatingActionButton;
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.SliderActivity;
import com.securesurveillance.skili.adapter.HomeJobsAdapter;
import com.securesurveillance.skili.adapter.SliderJobsAdapter;
import com.securesurveillance.skili.apiHandler.API;
import com.securesurveillance.skili.apiHandler.APIResponseListener;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.GetRetrofit;
import com.securesurveillance.skili.apiinterfaces.ApplyJobController;
import com.securesurveillance.skili.apiinterfaces.FilterJobController;
import com.securesurveillance.skili.apiinterfaces.FilterProfileController;
import com.securesurveillance.skili.apiinterfaces.PostJobController;
import com.securesurveillance.skili.apiinterfaces.SearchJobController;
import com.securesurveillance.skili.model.HomeJobsModel;
import com.securesurveillance.skili.model.requestmodel.SearchJobsDefaultRequestModel;
import com.securesurveillance.skili.model.responsemodel.FilterModel;
import com.securesurveillance.skili.model.responsemodel.GetAllJobRecruiterDetailModel;
import com.securesurveillance.skili.model.responsemodel.LoginModel;
import com.securesurveillance.skili.model.responsemodel.SearchCandidateResponse;
import com.securesurveillance.skili.model.responsemodel.SearchJobsResponse;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.DialogUtil;
import com.securesurveillance.skili.utility.SharePreferanceWrapperSingleton;
import com.securesurveillance.skili.utility.SweetAlertController;

import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.securesurveillance.skili.fragments.HomeFragment.pager;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeJobFragment extends Fragment implements HomeJobsAdapter.ApplyListener, View.OnClickListener, DialogUtil.FilterCandidate {
    SharePreferanceWrapperSingleton objSPS;
    ArrayList<GetAllJobRecruiterDetailModel> arrRows;
    RecyclerView rvGlobalFeed;
    MaterialRefreshLayout refresh;
    private RelativeLayout rlFilter;
    public static ArrayList<String> arrLastSelectedSkills = new ArrayList<>();
    public static String lastSelectedCity = "";
    public static String lastStartDate = "", lastEndDate = "";
    public static String lastSelectedMAxPay = "", lastSelectedDisMaxValue = "", lastSelectedMinPay = "", lastSelectedDisMinValue = "";
    public static String lastBudgetType = "";
    public static String lastJobType="";
    public static boolean lastIsBothTimeEnable=false;



    public HomeJobFragment() {
        // Required empty public constructor
    }

    private void callFilterApi() {
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        Call<FilterModel> call;

        String role = SharePreferanceWrapperSingleton.getSingletonInstance().getValueFromShared_Pref(Constants.ROLE);
        if (role.contains("Individual")) {
            //jobs
            FilterJobController anInterface = GetRetrofit.getInstance().create(FilterJobController.class);
            call = anInterface.getResponse(
                    objSPS.getValueFromShared_Pref(Constants.TOKEN));
        } else {
            //profile
            FilterProfileController anInterface = GetRetrofit.getInstance().create(FilterProfileController.class);
            call = anInterface.getResponse(
                    objSPS.getValueFromShared_Pref(Constants.TOKEN));
        }


        API.callRun(getActivity(), true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (getActivity()!=null&&!getActivity().isFinishing()) {
                    // refresh complete
                    if (response.code() == 200) {
                        if (response.body() instanceof FilterModel) {
                            FilterModel model = (FilterModel) response.body();
                            CommonMethod.cancelDialog(dialog, getActivity());
                            if (model.isStatus()) {
                                DialogUtil.openFilterDialog(getActivity(), getActivity(), model.getResult().getCity(),
                                        model.getResult().getSkills(), HomeJobFragment.this, HomeJobFragment.this);


                            } else {
                                SweetAlertController.getInstance().showErrorDialog(getActivity(), SweetAlertDialog.ERROR_TYPE,
                                        model.getMessage(), getString(R.string.app_name));

                            }
                        }
                    } else {
                        CommonMethod.cancelDialog(dialog, getActivity());
                        CommonMethod.showApiMsgToast(getActivity(), response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call call, final Throwable t) {
                if (getActivity()!=null&&!getActivity().isFinishing()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (t instanceof UnknownHostException) {
                                CommonMethod.showToastMessage(getActivity(), getString(R.string.UNKNOWN_HOST));
                            } else if (t instanceof ConnectException) {
                                CommonMethod.showToastMessage(getActivity(), getString(R.string.UNKNOWN_HOST));
                            } else if (t instanceof SocketTimeoutException) {
                                CommonMethod.showToastMessage(getActivity(), getString(R.string.SOCKET_TIME_OUT));
                            } else if (t instanceof SocketException) {
                                CommonMethod.showToastMessage(getActivity(), getString(R.string.UNABLE_TO_CONNECT));
                            } else if (t instanceof JsonSyntaxException) {
                                CommonMethod.showToastMessage(getActivity(), getString(R.string.JSON_SYNTAX));
                            } else {
                                CommonMethod.showToastMessage(getActivity(), getString(R.string.UNKNOWN_ERROR));
                            }
                            t.printStackTrace();
                        }
                    });

                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlFilter:
                callFilterApi();

                break;
        }
    }

    @Override
    public void onSearch(ArrayList<String> arrSkills, String selectedCity, String startDate, String endDate,
                         String selectedMaxValue,
                         String selectedDistanceValue, String selectedMinValue, String selectedDistanceMinValue,
                         String budgetType,String jobType,boolean isbothEnable) {
        lastEndDate = endDate;
        lastStartDate = startDate;
        lastSelectedCity = selectedCity;
        lastSelectedMAxPay = selectedMaxValue;
        arrLastSelectedSkills = arrSkills;
        lastSelectedDisMaxValue = selectedDistanceValue;
        lastSelectedMinPay = selectedMinValue;
        lastSelectedDisMinValue = selectedDistanceMinValue;
        lastBudgetType = budgetType;
        lastJobType=jobType;
        lastIsBothTimeEnable=isbothEnable;
        callJobSearchAPI(true, false);
    }

    @Override
    public void onClear() {
        lastEndDate = "";
        lastStartDate = "";
        lastSelectedCity = "";
        lastSelectedMAxPay = "";
        arrLastSelectedSkills = new ArrayList<>();
        lastSelectedDisMaxValue = "";
        lastSelectedMinPay = "";
        lastSelectedDisMinValue = "";
        lastBudgetType="";
        lastJobType="";
        lastIsBothTimeEnable=false;
        callJobSearchAPI(false, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homejobs, container, false);
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(getActivity());
        rvGlobalFeed = (RecyclerView) view.findViewById(R.id.rvGlobalFeed);
        rlFilter = (RelativeLayout) view.findViewById(R.id.rlFilter);
        refresh = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
        rlFilter.setOnClickListener(this);
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                //refreshing...
                if (arrLastSelectedSkills.size() > 0) {
                    callJobSearchAPI(true, false);
                } else {
                    callJobSearchAPI(false, false);

                }
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //load more refreshing...
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (arrLastSelectedSkills.size() > 0) {
            callJobSearchAPI(true, false);
        } else {
            callJobSearchAPI(false, true);
        }

    }

    private void callJobSearchAPI(boolean isfilterEnable, boolean isNotFromRefresh) {
        SearchJobController anInterface = GetRetrofit.getInstance().create(SearchJobController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        SearchJobsDefaultRequestModel model = new SearchJobsDefaultRequestModel();
        model.setFilterEnable("" + isfilterEnable);
        model.setPagenumber("" + 1);
        model.setPageSize("10000");
        SearchJobsDefaultRequestModel.SkillRequestModel skillRequestModel = model.new SkillRequestModel();
        String skills = objSPS.getValueFromShared_Pref(Constants.SKILLS);
        String[] skillArray = skills.split(",");
        skillRequestModel.setMobileNumber(objSPS.getValueFromShared_Pref(Constants.MOBILE));
        if (arrLastSelectedSkills.size() > 0) {
            skillRequestModel.setSkills(arrLastSelectedSkills);
            skillRequestModel.setDistanceFrom(lastSelectedDisMinValue);
            skillRequestModel.setDistanceTo(lastSelectedDisMaxValue);
            skillRequestModel.setPayFrom(lastSelectedMinPay);
            skillRequestModel.setPayTo(lastSelectedMAxPay);
            skillRequestModel.setBudgetType(lastBudgetType);
            skillRequestModel.setJobType(lastJobType);
            skillRequestModel.setBothTimeEnable(lastIsBothTimeEnable);

        } else {
            skillRequestModel.setSkills(new ArrayList<String>(Arrays.asList(skillArray)));
        }
        model.setSearchCriteria(skillRequestModel);
        Gson gson = new Gson();
        String json = gson.toJson(model);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
        Call<SearchJobsResponse> call = anInterface.getResponse(objSPS.getValueFromShared_Pref(Constants.TOKEN),
                body);
        API.callRun
                (getActivity(), isNotFromRefresh, call, new APIResponseListener() {
                    @Override
                    public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                        if (getActivity()!=null&&!getActivity().isFinishing()) {
                            // refresh complete
                            refresh.finishRefresh();
                            if (response.code() == 200) {
                                CommonMethod.printAPIResponse(response.body());
                                if (response.body() instanceof SearchJobsResponse) {
                                    SearchJobsResponse model = (SearchJobsResponse) response.body();
                                    CommonMethod.cancelDialog(dialog, getActivity());
                                    if (model.isStatus()) {
                                        if(model.getResult().getUnreadNotificationCount()!=0){
                                            SliderActivity.hotlist_hot.setText(""+model.getResult().getUnreadNotificationCount());
                                            SliderActivity.hotlist_hot.setVisibility(View.VISIBLE);
                                        }
                                        CommonMethod.showToastMessage(getActivity(), model.getMessage());
                                        arrRows = model.getResult().getRows();

                                        HomeJobsAdapter adapter = new HomeJobsAdapter(getActivity(), arrRows, HomeJobFragment.this);
                                        rvGlobalFeed.setAdapter(adapter);
                                        rvGlobalFeed.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    } else {
                                        SweetAlertController.getInstance().showErrorDialog(getActivity(), SweetAlertDialog.ERROR_TYPE, model.getMessage(), getString(R.string.app_name));
                                    }
                                }
                            } else {
                                CommonMethod.cancelDialog(dialog, getActivity());
                                CommonMethod.showApiMsgToast(getActivity(), response.code());
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
                                        CommonMethod.showToastMessage(getActivity(), getString(R.string.UNKNOWN_HOST));
                                    } else if (t instanceof ConnectException) {
                                        CommonMethod.showToastMessage(getActivity(), getString(R.string.UNKNOWN_HOST));
                                    } else if (t instanceof SocketTimeoutException) {
                                        CommonMethod.showToastMessage(getActivity(), getString(R.string.SOCKET_TIME_OUT));
                                    } else if (t instanceof SocketException) {
                                        CommonMethod.showToastMessage(getActivity(), getString(R.string.UNABLE_TO_CONNECT));
                                    } else if (t instanceof JsonSyntaxException) {
                                        CommonMethod.showToastMessage(getActivity(), getString(R.string.JSON_SYNTAX));
                                    } else {
                                        CommonMethod.showToastMessage(getActivity(), getString(R.string.UNKNOWN_ERROR));
                                    }
                                    t.printStackTrace();
                                }
                            });
                        }
                    }
                });
    }

    private void callApplyJobAPI(final int pos) {
        ApplyJobController anInterface = GetRetrofit.getInstance().create(ApplyJobController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        JSONObject json = new JSONObject();
        try {

            json.put("jobID", arrRows.get(pos).getJobId());

            json.put("appliedBy", objSPS.getValueFromShared_Pref(Constants.ID));

        } catch (Exception e) {


        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
        Call<LoginModel> call = anInterface.getResponse(objSPS.getValueFromShared_Pref(Constants.TOKEN),
                body);
        API.callRun
                (getActivity(), true, call, new APIResponseListener() {
                    @Override
                    public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                        if (getActivity()!=null&&!getActivity().isFinishing()) {
                            if (response.code() == 200) {
                                CommonMethod.printAPIResponse(response.body());
                                if (response.body() instanceof LoginModel) {
                                    LoginModel model = (LoginModel) response.body();
                                    CommonMethod.cancelDialog(dialog, getActivity());
                                    if (model.isStatus()) {
                                        CommonMethod.showToastMessage(getActivity(), model.getMessage());
                                        arrRows.get(pos).setApplicationStatus("APPLIED");

                                        HomeJobsAdapter adapter = new HomeJobsAdapter(getActivity(), arrRows, HomeJobFragment.this);
                                        rvGlobalFeed.setAdapter(adapter);
                                        rvGlobalFeed.setLayoutManager(new LinearLayoutManager(getActivity()));

                                    } else {
                                        SweetAlertController.getInstance().showErrorDialog(getActivity(), SweetAlertDialog.ERROR_TYPE, model.getMessage(), getString(R.string.app_name));
                                    }
                                }
                            } else {
                                CommonMethod.cancelDialog(dialog, getActivity());
                                CommonMethod.showApiMsgToast(getActivity(), response.code());
                            }
                        }
                    }


                    @Override
                    public void onFailure(Call call, final Throwable t) {
                        if (getActivity()!=null&&!getActivity().isFinishing()) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (t instanceof UnknownHostException) {
                                        CommonMethod.showToastMessage(getActivity(), getString(R.string.UNKNOWN_HOST));
                                    } else if (t instanceof ConnectException) {
                                        CommonMethod.showToastMessage(getActivity(), getString(R.string.UNKNOWN_HOST));
                                    } else if (t instanceof SocketTimeoutException) {
                                        CommonMethod.showToastMessage(getActivity(), getString(R.string.SOCKET_TIME_OUT));
                                    } else if (t instanceof SocketException) {
                                        CommonMethod.showToastMessage(getActivity(), getString(R.string.UNABLE_TO_CONNECT));
                                    } else if (t instanceof JsonSyntaxException) {
                                        CommonMethod.showToastMessage(getActivity(), getString(R.string.JSON_SYNTAX));
                                    } else {
                                        CommonMethod.showToastMessage(getActivity(), getString(R.string.UNKNOWN_ERROR));
                                    }
                                    t.printStackTrace();
                                }
                            });
                        }
                    }
                });
    }

    @Override
    public void onApplyClicked(int pos) {
        callApplyJobAPI(pos);
    }
}
