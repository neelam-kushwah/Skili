package com.securesurveillance.skili.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.melnykov.fab.FloatingActionButton;
import com.securesurveillance.skili.BuildConfig;
import com.securesurveillance.skili.PostJobStepAActivity;
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.SkilliSignupActivity;
import com.securesurveillance.skili.SliderActivity;
import com.securesurveillance.skili.adapter.GlobalFeedAdapter;
import com.securesurveillance.skili.apiHandler.API;
import com.securesurveillance.skili.apiHandler.APIResponseListener;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.GetRetrofit;
import com.securesurveillance.skili.apiinterfaces.FilterJobController;
import com.securesurveillance.skili.apiinterfaces.FilterProfileController;
import com.securesurveillance.skili.apiinterfaces.SearchCandidateController;
import com.securesurveillance.skili.apiinterfaces.SearchJobController;
import com.securesurveillance.skili.model.GlobalFeedModel;
import com.securesurveillance.skili.model.requestmodel.SearchJobsDefaultRequestModel;
import com.securesurveillance.skili.model.responsemodel.FilterModel;
import com.securesurveillance.skili.model.responsemodel.SearchCandidateResponse;
import com.securesurveillance.skili.model.responsemodel.SearchJobsResponse;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.DialogUtil;
import com.securesurveillance.skili.utility.SharePreferanceWrapperSingleton;
import com.securesurveillance.skili.utility.SweetAlertController;

import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class GlobalFeedFragment extends Fragment implements View.OnClickListener, DialogUtil.FilterCandidate {

    RecyclerView rvGlobalFeed;
    SharePreferanceWrapperSingleton objSPS;

    public GlobalFeedFragment() {
        // Required empty public constructor
    }

//Locatuon code

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;
    private String role = "";
    Context ctx;
    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    Geocoder geocoder;
    List<Address> addresses;
    MaterialRefreshLayout refresh;
    private RelativeLayout rlFilter;
    FloatingActionButton fab;

    public static ArrayList<String> arrLastSelectedSkills = new ArrayList<>();
    public static String lastSelectedCity = "";
    public static String lastStartDate = "", lastEndDate = "";
    public static String lastBudgetType="";
    public static String lastJobType="";
    public static boolean lastIsBothTimeEnable=false;
    public static String lastSelectedMAxPay = "", lastSelectedDisMaxValue = "", lastSelectedMinPay = "", lastSelectedDisMinValue = "";

    //end
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_globalfeed, container, false);
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(getActivity());
        rvGlobalFeed = (RecyclerView) view.findViewById(R.id.rvGlobalFeed);
        refresh = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
        rlFilter = (RelativeLayout) view.findViewById(R.id.rlFilter);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        rlFilter.setOnClickListener(this);
//        init();
//        locationIconCliked();
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                //refreshing...
                //if(mCurrentLocation!=null) {
                if (arrLastSelectedSkills.size() > 0) {
                    callCandidateSearchAPI(true, false);
                } else {
                    callCandidateSearchAPI(false, false);

                }
                //}

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //load more refreshing...
            }
        });

        fab.attachToRecyclerView(rvGlobalFeed);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PostJobStepAActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (arrLastSelectedSkills.size() > 0) {
            callCandidateSearchAPI(true, false);
        } else {
            callCandidateSearchAPI(false, true);
        }

    }

    private void callFilterApi() {
//        Map<String, String> data = new HashMap<>();
//        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
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
                                        model.getResult().getSkills(), GlobalFeedFragment.this,GlobalFeedFragment.this);


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
        if(selectedDistanceValue.equals("0")){
            selectedDistanceValue="";
        }

        if(selectedMaxValue.equals("0")){
            selectedMaxValue="";
        }

            lastEndDate = endDate;
        lastStartDate = startDate;
        lastSelectedCity = selectedCity;
        lastSelectedMAxPay = selectedMaxValue;
        arrLastSelectedSkills = arrSkills;
        lastSelectedDisMaxValue = selectedDistanceValue;
        lastSelectedMinPay = selectedMinValue;
        lastSelectedDisMinValue = selectedDistanceMinValue;
        lastBudgetType=budgetType;
        lastJobType=jobType;
        lastIsBothTimeEnable=isbothEnable;


        callCandidateSearchAPI(true, true);
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
        callCandidateSearchAPI(false, true);
    }

    private void locationIconCliked() {
// Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(getActivity())
                .

                        withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .

                        withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                startLocationUpdates();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                if (response.isPermanentlyDenied()) {
                                    // open device settings when the permission is
                                    // denied permanently
                                    openSettings();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest
                                                                                   permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).

                check();

    }

    private void updateLocationUI() {
        if (mCurrentLocation != null) {


            stopLocationUpdates();

            callCandidateSearchAPI(false, true);
        }

    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback);
    }

    @SuppressLint("RestrictedApi")
    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mSettingsClient = LocationServices.getSettingsClient(getActivity());

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();

                updateLocationUI();
            }
        };

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {


                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";

                                CommonMethod.showToastMessage(getActivity(), errorMessage);
                        }

                        updateLocationUI();
                    }
                });
    }


    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void callCandidateSearchAPI(boolean isfilterEnable, boolean isNotFromRefresh) {
        SearchCandidateController anInterface = GetRetrofit.getInstance().create(SearchCandidateController.class);
//        Map<String, String> data = new HashMap<>();
//        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        SearchJobsDefaultRequestModel model = new SearchJobsDefaultRequestModel();
        model.setFilterEnable("" + isfilterEnable);
        model.setPagenumber("" + 1);
        model.setPageSize("10000");
        SearchJobsDefaultRequestModel.SkillRequestModel skillRequestModel = model.new SkillRequestModel();
        String skills = objSPS.getValueFromShared_Pref(Constants.SKILLS);
        String[] skillArray = skills.split(",");
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
        skillRequestModel.setMobileNumber(objSPS.getValueFromShared_Pref(Constants.MOBILE));

        model.setSearchCriteria(skillRequestModel);

        Gson gson = new Gson();
        String json = gson.toJson(model);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Call<SearchCandidateResponse> call = anInterface.getResponse(objSPS.getValueFromShared_Pref(Constants.TOKEN),
                body);
        API.callRun
                (getActivity(), isNotFromRefresh, call, new APIResponseListener() {
                    @Override
                    public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                        if (getActivity()!=null&&!getActivity().isFinishing()) {
                            refresh.finishRefresh();

                            if (response.code() == 200) {
                                CommonMethod.printAPIResponse(response.body());
                                if (response.body() instanceof SearchCandidateResponse) {
                                    SearchCandidateResponse model = (SearchCandidateResponse) response.body();
                                    CommonMethod.cancelDialog(dialog, getActivity());
                                    if (model.isStatus()) {
                                        if(model.getResult().getUnreadNotificationCount()!=0){
                                            SliderActivity.hotlist_hot.setText(""+model.getResult().getUnreadNotificationCount());
                                            SliderActivity.hotlist_hot.setVisibility(View.VISIBLE);
                                        }
                                        ArrayList<SearchCandidateResponse.Result.Candidate> arrCandidate = model.getResult().getRows();
                                        setAdapter(arrCandidate);
                                        CommonMethod.showToastMessage(getActivity(), model.getMessage());
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

    private void setAdapter(ArrayList<SearchCandidateResponse.Result.Candidate> arrCandidate) {
//        ArrayList<GlobalFeedModel> arrList = new ArrayList<GlobalFeedModel>();
//        GlobalFeedModel model;
//        for (int i = 0; i < 10; i++) {
//            model = new GlobalFeedModel();
//                model.setName("Vijay Jadeja");
//                model.setDistance("2");
//                model.setPrice("700");
//                model.setRating("4.5");
//                model.setViews("435");
//
//            arrList.add(model);
//        }
        GlobalFeedAdapter adapter = new GlobalFeedAdapter(getActivity(), arrCandidate);
        rvGlobalFeed.setAdapter(adapter);
        rvGlobalFeed.setLayoutManager(new LinearLayoutManager(getActivity()));


    }
}
