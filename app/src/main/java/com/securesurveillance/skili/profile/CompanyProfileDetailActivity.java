package com.securesurveillance.skili.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import com.google.gson.JsonSyntaxException;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.securesurveillance.skili.BuildConfig;
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.adapter.IndustriesAdaptor;
import com.securesurveillance.skili.adapter.SpinnerAdaptor;
import com.securesurveillance.skili.apiHandler.API;
import com.securesurveillance.skili.apiHandler.APIResponseListener;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.GetRetrofit;
import com.securesurveillance.skili.apiinterfaces.GetCategoryController;
import com.securesurveillance.skili.apiinterfaces.RecruiterUpdateController;
import com.securesurveillance.skili.model.responsemodel.GetCategoryModel;
import com.securesurveillance.skili.model.responsemodel.LoginModel;
import com.securesurveillance.skili.model.SkillModel;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.SharePreferanceWrapperSingleton;
import com.securesurveillance.skili.utility.SweetAlertController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by adarsh on 7/23/2018.
 */

public class CompanyProfileDetailActivity extends AppCompatActivity implements IndustriesAdaptor.CbListener {
    ImageView iv_LeftOption;
    TextView tv_Title;
    ArrayList<String> arrTypeOfCompany;
    TextInputLayout tiLCompany, tilWebsite, rlUsername, tiLCategory,rlGSTIN;
    EditText etWebsite, etMobile, etEmail, etCompany, etUsername, etOfficeAddress, etCurrentLocation, etCategory,etGSTIN;
    private SharePreferanceWrapperSingleton objSPS;
    boolean pinclicked = false;
    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    SpinnerAdaptor spinnerAdaptor;
    ListView listView;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;
    Button btnRegister;

    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    Geocoder geocoder;
    List<Address> addresses;
    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;
    ArrayList<SkillModel> arrCategory = new ArrayList<>();
    ArrayList<SkillModel> checkedValue = new ArrayList<>();
    ArrayList<Integer> checkedPosition = new ArrayList<>();
    TextView tvSelectedCategory;
    private ArrayList<String> selectedArrListCategory = new ArrayList<>();
    Dialog dialogCompany;
    ArrayList<String> category;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companyprofiledetail);
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(this);
        iv_LeftOption = (ImageView) findViewById(R.id.iv_LeftOption);
        tv_Title = (TextView) findViewById(R.id.tv_Title);
        tvSelectedCategory = (TextView) findViewById(R.id.tvSelectedCategory);
        tiLCompany = (TextInputLayout) findViewById(R.id.tiLCompany);
        tilWebsite = (TextInputLayout) findViewById(R.id.tilWebsite);
        rlUsername = (TextInputLayout) findViewById(R.id.rlUsername);
        tiLCategory = (TextInputLayout) findViewById(R.id.tiLCategory);
        etWebsite = (EditText) findViewById(R.id.etWebsite);
        etMobile = (EditText) findViewById(R.id.etMobile);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etCompany = (EditText) findViewById(R.id.etCompany);
        etCategory = (EditText) findViewById(R.id.etCategory);
        etGSTIN = (EditText) findViewById(R.id.etGSTIN);
        rlGSTIN = (TextInputLayout) findViewById(R.id.rlGSTIN);

        etCurrentLocation = (EditText) findViewById(R.id.etCurrentLocation);

        callCategoryApi();

        category = getIntent().getStringArrayListExtra("CATEGORY");
        etUsername = (EditText) findViewById(R.id.etUsername);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        Drawable drawable = getResources().getDrawable(R.drawable.pin);
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * 0.6),
                (int) (drawable.getIntrinsicHeight() * 0.6));
        ScaleDrawable sd = new ScaleDrawable(drawable, 0, 40, 40);
        etCurrentLocation.setCompoundDrawables(null, null, sd.getDrawable(), null);
        etCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationIconCliked();
            }
        });
        listView = (ListView) findViewById(R.id.listView);

        Drawable drawable1 = getResources().getDrawable(R.drawable.downarrow_black);
        drawable1.setBounds(0, 0, (int) (drawable1.getIntrinsicWidth() * 0.6),
                (int) (drawable1.getIntrinsicHeight() * 0.6));
        ScaleDrawable sd1 = new ScaleDrawable(drawable1, 0, 40, 40);

        etCategory.setCompoundDrawables(null, null, sd1.getDrawable(), null);
        etCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDialog();

            }
        });

        init();
        etOfficeAddress = (EditText) findViewById(R.id.etOfficeAddress);
        if (objSPS.getValueFromShared_Pref(Constants.ROLE).contains("Company")) {
            tv_Title.setText("Company Details");
            if (!getIntent().getStringExtra("WEBSITE").equals("-")) {
                etWebsite.setText(getIntent().getStringExtra("WEBSITE"));
            }
            if (!getIntent().getStringExtra("EMAIL").equals("-")) {
                etEmail.setText(getIntent().getStringExtra("EMAIL"));
            }
            if (!getIntent().getStringExtra("COMPANY").equals("-")) {
                etCompany.setText(getIntent().getStringExtra("COMPANY"));
            }
            rlUsername.setVisibility(View.GONE);
            rlGSTIN.setVisibility(View.VISIBLE);
            if (!getIntent().getStringExtra("GSTIN").equals("-")) {
                etGSTIN.setText(getIntent().getStringExtra("GSTIN"));
            }
        } else {
            tv_Title.setText("Recruiter Details");
            tilWebsite.setVisibility(View.GONE);
            tiLCompany.setVisibility(View.GONE);
            if (!getIntent().getStringExtra("EMAIL").equals("-")) {
                etEmail.setText(getIntent().getStringExtra("EMAIL"));
            }
            if (!getIntent().getStringExtra("USERNAME").equals("-")) {
                etUsername.setText(getIntent().getStringExtra("USERNAME"));
            }
        }
        etMobile.setText(objSPS.getValueFromShared_Pref(Constants.MOBILE));
        etCurrentLocation.setText(getIntent().getStringExtra("LOCATION"));
        if (!getIntent().getStringExtra("OFFICEADDRESS").equals("-")) {
            etOfficeAddress.setText(getIntent().getStringExtra("OFFICEADDRESS"));
        }

        iv_LeftOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objSPS.getValueFromShared_Pref(Constants.ROLE).contains("Recruiter")) {

                    if (TextUtils.isEmpty(etEmail.getText())) {
                        CommonMethod.showToastMessage(CompanyProfileDetailActivity.this, "Please enter your email address");
                    } else if (!CommonMethod.isValidEmail(etEmail.getText().toString())) {
                        CommonMethod.showToastMessage(CompanyProfileDetailActivity.this, "Please enter your valid email address");
                    } else if (TextUtils.isEmpty(etUsername.getText())) {
                        CommonMethod.showToastMessage(CompanyProfileDetailActivity.this, "Please enter your name");
                    } else if (TextUtils.isEmpty(etCurrentLocation.getText())) {
                        CommonMethod.showToastMessage(CompanyProfileDetailActivity.this, "Please provide your current location");
                    } else {
                        callUpdateApi();
                    }
                } else {
                    if (TextUtils.isEmpty(etEmail.getText())) {
                        CommonMethod.showToastMessage(CompanyProfileDetailActivity.this, "Please enter your email address");
                    } else if (!CommonMethod.isValidEmail(etEmail.getText().toString())) {
                        CommonMethod.showToastMessage(CompanyProfileDetailActivity.this, "Please enter your valid email address");
                    } else if (TextUtils.isEmpty(etCompany.getText())) {
                        CommonMethod.showToastMessage(CompanyProfileDetailActivity.this, "Please enter your company name");
                    } else if (TextUtils.isEmpty(etCurrentLocation.getText())) {
                        CommonMethod.showToastMessage(CompanyProfileDetailActivity.this, "Please provide your current location");
                    } else {
                        callUpdateApi();
                    }
                }
            }
        });

    }

    private void callUpdateApi() {
        RecruiterUpdateController anInterface = GetRetrofit.getInstance().create(RecruiterUpdateController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        JSONObject json = new JSONObject();
        try {


            json.put("_id", objSPS.getValueFromShared_Pref(Constants.ID));
            json.put("role", objSPS.getValueFromShared_Pref(Constants.ROLE));

            if (objSPS.getValueFromShared_Pref(Constants.ROLE).equals("Recruiter")) {
                String[] str = etUsername.getText().toString().trim().split(" ");

                json.put("firstName", CommonMethod.getFirstCapName(str[0]));
                json.put("lastName", CommonMethod.getFirstCapName(str[str.length - 1]));
            } else {
                json.put("companyName", CommonMethod.getFirstCapName(etCompany.getText().toString().trim()));
                if (!TextUtils.isEmpty(etWebsite.getText().toString().trim())) {
                    json.put("website", etWebsite.getText().toString().trim());
                }
                if (!TextUtils.isEmpty(etGSTIN.getText().toString().trim())) {
                    json.put("GSTIN", CommonMethod.getFirstCapName(etGSTIN.getText().toString().trim()));
                }


            }
            if (!TextUtils.isEmpty(etOfficeAddress.getText().toString().trim())) {
                json.put("officeAddress",  CommonMethod.getFirstCapName(etOfficeAddress.getText().toString().trim()));
            }
            json.put("email",  CommonMethod.getFirstCapName(etEmail.getText().toString().trim()));
            if (pinclicked) {
                json.put("locationString",  CommonMethod.getFirstCapName(etCurrentLocation.getText().toString().trim()));
                json.put("latitude", mCurrentLocation.getLatitude());
                json.put("longitude", mCurrentLocation.getLongitude());
                String city;
                city=addresses.get(0).getSubAdminArea();
                if(city==null){
                    addresses.get(0).getLocality();
                }
                if(city==null){
                    city="";
                }
                json.put("city",  CommonMethod.getFirstCapName(city));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
        Call<LoginModel> call = anInterface.getResponse( objSPS.getValueFromShared_Pref(Constants.TOKEN),
                body);
        API.callRun(CompanyProfileDetailActivity.this, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    if (response.code() == 200) {
                        CommonMethod.printAPIResponse(response.body());
                        if (response.body() instanceof LoginModel) {
                            LoginModel model = (LoginModel) response.body();
                            CommonMethod.cancelDialog(dialog, CompanyProfileDetailActivity.this);
                            if (model.isStatus()) {
                                CommonMethod.showToastMessage(CompanyProfileDetailActivity.this, model.getMessage());

                                finish();
                            } else {
                                SweetAlertController.getInstance().showErrorDialog(CompanyProfileDetailActivity.this, SweetAlertDialog.ERROR_TYPE, model.getMessage(), getString(R.string.app_name));
                            }
                        }
                    } else {
                        CommonMethod.cancelDialog(dialog, CompanyProfileDetailActivity.this);
                        CommonMethod.showApiMsgToast(CompanyProfileDetailActivity.this, response.code());
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
                                CommonMethod.showToastMessage(CompanyProfileDetailActivity.this, getString(R.string.UNKNOWN_HOST));
                            } else if (t instanceof ConnectException) {
                                CommonMethod.showToastMessage(CompanyProfileDetailActivity.this, getString(R.string.UNKNOWN_HOST));
                            } else if (t instanceof SocketTimeoutException) {
                                CommonMethod.showToastMessage(CompanyProfileDetailActivity.this, getString(R.string.SOCKET_TIME_OUT));
                            } else if (t instanceof SocketException) {
                                CommonMethod.showToastMessage(CompanyProfileDetailActivity.this, getString(R.string.UNABLE_TO_CONNECT));
                            } else if (t instanceof JsonSyntaxException) {
                                CommonMethod.showToastMessage(CompanyProfileDetailActivity.this, getString(R.string.JSON_SYNTAX));
                            } else {
                                CommonMethod.showToastMessage(CompanyProfileDetailActivity.this, getString(R.string.UNKNOWN_ERROR));
                            }
                            t.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    private void locationIconCliked() {
// Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .

                        withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .

                        withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                mRequestingLocationUpdates = true;
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

    @Override
    public void onResume() {
        super.onResume();

        // Resuming location updates depending on button state and
        // allowed permissions
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        }

        updateLocationUI();
    }


    private void updateLocationUI() {
        if (mCurrentLocation != null) {


            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                etCurrentLocation.setText(address);
                pinclicked = true;
            } catch (IOException e) {
                e.printStackTrace();
            }


            // giving a blink animation on TextView
//            etLocation.setAlpha(0);
//            etLocation.animate().alpha(1).setDuration(300);

            // location last updated time
//            txtUpdatedOn.setText("Last updated on: " + mLastUpdateTime);
        }

    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {


                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(CompanyProfileDetailActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";

                                CommonMethod.showToastMessage(CompanyProfileDetailActivity.this, errorMessage);
                        }

                        updateLocationUI();
                    }
                });
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
        }
    }

    private void callCategoryApi() {
        GetCategoryController anInterface = GetRetrofit.getInstance().create(GetCategoryController.class);
        Map<String, String> data = new HashMap<>();


        Call<GetCategoryModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN));
        API.callRun(CompanyProfileDetailActivity.this, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    if (response.code() == 200) {
                        if (response.body() instanceof GetCategoryModel) {
                            GetCategoryModel model = (GetCategoryModel) response.body();
                            CommonMethod.cancelDialog(dialog, CompanyProfileDetailActivity.this);
                            if (model.isStatus()) {
                                arrCategory = new ArrayList<>();
                                arrCategory = model.getResult();
                                for(int i=0;i<arrCategory.size();i++){
                                    for(int j=0;j<category.size();j++){
                                        if(arrCategory.get(i).getId().equals(category.get(j))){
                                            checkedPosition.add(i);
                                            checkedValue.add(arrCategory.get(i));

                                        }
                                    }
                                }
                                String str = "";

                                for (int i = 0; i < checkedValue.size(); i++) {

                                    if (!selectedArrListCategory.contains(checkedValue.get(i))) {
                                        selectedArrListCategory.add(checkedValue.get(i).getName());
                                    }

                                }
                                tvSelectedCategory.setText(str);

                                if (spinnerAdaptor == null) {
                                    spinnerAdaptor = new SpinnerAdaptor(CompanyProfileDetailActivity.this, selectedArrListCategory);
                                    listView.setAdapter(spinnerAdaptor);
                                } else {
                                    spinnerAdaptor.notifyDataSetChanged();
                                }

                            } else {
                                SweetAlertController.getInstance().showErrorDialogClickListener(CompanyProfileDetailActivity.this, SweetAlertDialog.ERROR_TYPE, "Failed to get Category list", getString(R.string.app_name), "Try again", new SweetAlertController.SweetAlertClickListener() {
                                    @Override
                                    public void onSweetAlertClicked() {
                                        callCategoryApi();
                                    }
                                });

                            }
                        }
                    } else {
                        CommonMethod.cancelDialog(dialog, CompanyProfileDetailActivity.this);
                        CommonMethod.showApiMsgToast(CompanyProfileDetailActivity.this, response.code());
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
                                CommonMethod.showToastMessage(CompanyProfileDetailActivity.this, getString(R.string.UNKNOWN_HOST));
                            } else if (t instanceof ConnectException) {
                                CommonMethod.showToastMessage(CompanyProfileDetailActivity.this, getString(R.string.UNKNOWN_HOST));
                            } else if (t instanceof SocketTimeoutException) {
                                CommonMethod.showToastMessage(CompanyProfileDetailActivity.this, getString(R.string.SOCKET_TIME_OUT));
                            } else if (t instanceof SocketException) {
                                CommonMethod.showToastMessage(CompanyProfileDetailActivity.this, getString(R.string.UNABLE_TO_CONNECT));
                            } else if (t instanceof JsonSyntaxException) {
                                CommonMethod.showToastMessage(CompanyProfileDetailActivity.this, getString(R.string.JSON_SYNTAX));
                            } else {
                                CommonMethod.showToastMessage(CompanyProfileDetailActivity.this, getString(R.string.UNKNOWN_ERROR));
                            }
                            t.printStackTrace();
                        }
                    });
                }
            }
        });

    }


    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }

    private void showCategoryDialog() {
        selectedArrListCategory.clear();
        dialogCompany = new Dialog(CompanyProfileDetailActivity.this, android.R.style.Theme_Material_Light_Dialog_NoActionBar_MinWidth);

        dialogCompany.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialogCompany.setContentView(R.layout.dialog_spinner);
        dialogCompany.setCancelable(false);
        dialogCompany.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.CENTER);
        LinearLayout llDialog = (LinearLayout) dialogCompany.findViewById(R.id.llDialog);
        CardView cvHeader = (CardView) dialogCompany.findViewById(R.id.cvHeader);
        cvHeader.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;
        llDialog.getLayoutParams().width = (int) (screenWidth * .9);
        llDialog.getLayoutParams().height = (int) (screenHeight * .7);
        TextView tvHeaderSpinner = (TextView) dialogCompany.findViewById(R.id.tvHeaderSpinner);
        ListView listView = (ListView) dialogCompany.findViewById(R.id.listView);
        ImageView ivClose = (ImageView) dialogCompany.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCompany.dismiss();
            }
        });
        IndustriesAdaptor spinnerAdaptor = new IndustriesAdaptor(CompanyProfileDetailActivity.this, arrCategory, checkedPosition);
        listView.setAdapter(spinnerAdaptor);
        tvHeaderSpinner.setText("Select Category");

        dialogCompany.show();


    }

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();

                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    @Override
    public void onItemClicked(View v, int pos) {
        CheckBox cb = (CheckBox) v;//.findViewById(R.id.cb);
        // cb.performClick();
        selectedArrListCategory.clear();

        if (cb.isChecked()) {
            if(!checkedValue.contains(arrCategory.get(pos))) {
                checkedValue.add(arrCategory.get(pos));
                checkedPosition.add(pos);
            }

        } else if (!cb.isChecked()) {
            checkedValue.remove(arrCategory.get(pos));
            if (checkedPosition.contains(pos)) {
                for (int i = 0; i < checkedPosition.size(); i++) {
                    if (checkedPosition.get(i) == pos) {
                        checkedPosition.remove(i);
                        selectedArrListCategory.remove(arrCategory.get(pos).getName());

                    }
                }
            }
        }
        String str = "";

        for (int i = 0; i < checkedValue.size(); i++) {

            if (!selectedArrListCategory.contains(checkedValue.get(i))) {
                selectedArrListCategory.add(checkedValue.get(i).getName());
            }

        }
        tvSelectedCategory.setText(str);
        if (checkedValue.size() == arrCategory.size()) {
            dialogCompany.dismiss();
        }
        if (spinnerAdaptor == null) {
            spinnerAdaptor = new SpinnerAdaptor(CompanyProfileDetailActivity.this, selectedArrListCategory);
            listView.setAdapter(spinnerAdaptor);
        } else {
            spinnerAdaptor.notifyDataSetChanged();
        }

    }
}
