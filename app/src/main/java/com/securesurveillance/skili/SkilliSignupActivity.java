package com.securesurveillance.skili;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.securesurveillance.skili.utility.ExpandableRecyclerView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.securesurveillance.skili.adapter.CloudAdapter;
import com.securesurveillance.skili.apiHandler.API;
import com.securesurveillance.skili.apiHandler.APIResponseListener;
import com.securesurveillance.skili.apiHandler.AppConstants;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.GetRetrofit;
import com.securesurveillance.skili.apiinterfaces.GetCategoryController;
import com.securesurveillance.skili.apiinterfaces.SkilliSignupController;
import com.securesurveillance.skili.apiinterfaces.UploadProfilePicController;
import com.securesurveillance.skili.compressor.Compressor;
import com.securesurveillance.skili.compressor.FileUtil;
import com.securesurveillance.skili.materialspinner.MaterialSpinner;
import com.securesurveillance.skili.materialspinner.MaterialSpinnerMultiple;
import com.securesurveillance.skili.model.responsemodel.GetCategoryModel;
import com.securesurveillance.skili.model.responsemodel.LoginModel;
import com.securesurveillance.skili.model.SkillModel;
import com.securesurveillance.skili.model.requestmodel.SkilliSignupRequestModel;
import com.securesurveillance.skili.model.responsemodel.ProfilePicResponseModel;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.MarshMPermission;
import com.securesurveillance.skili.utility.SharePreferanceWrapperSingleton;
import com.securesurveillance.skili.utility.SweetAlertController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

/**
 * Created by adarsh on 8/18/2018.
 */

public class SkilliSignupActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, CloudAdapter.ItemDelete, View.OnClickListener {
    private String SHOW_PICTURE = "";
    private ImageView ivProfile;
    private EditText etFName, etLName;
    AutoCompleteTextView etLocation;
    public final String IMAGE_DIRECTORY_NAME = "capture";
    private final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private final int GALLERY_IMAGE_REQUEST_CODE = 200;
    private Uri fileUri;
    File profileFile;
    Button btnStart;
    RelativeLayout rlAddSkill;
    int selectedCategoryPosition = 0;
    HashMap<Integer, ArrayList<Integer>> arrSelectedCategory = new HashMap<>();
    HashMap<String, ArrayList<String>> arrayListHashMap = new HashMap<>();
    HashMap<String, Integer> arrayListCBHashMap = new HashMap<>();
    //    GoogleClouldStorageService clouldStorageService;
//    WtBlobId wtBlobId = null;
    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;
    private String role = "";
    Context ctx;
    // bunch of location related apis
    //private FusedLocationProviderClient mFusedLocationClient;
    //private SettingsClient mSettingsClient;
    //private LocationRequest mLocationRequest;
    //private LocationSettingsRequest mLocationSettingsRequest;
    //private LocationCallback mLocationCallback;
    //private Location mCurrentLocation;
//    Geocoder geocoder;
//    List<Address> addresses;
    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;
    SharePreferanceWrapperSingleton objSPS;
    List<SkillModel> arrCategory = new ArrayList<>();
    List<String> arrMainCategoryName = new ArrayList<>();
    ArrayList<String> arrsubCategoryName = new ArrayList<>();
    ExpandableRecyclerView recyclerView;
    MyApplication application;
    long uploadedProfileId = 0;
    TextView tvTerms;
    AppCompatCheckBox cbTerms;
    double latitude = 0.0;
    double longitude = 0.0;
    String address = "";
    String city = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_skilli);
        ctx = this;
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(ctx);
        application = (MyApplication) getApplicationContext();
        tvTerms = (TextView) findViewById(R.id.tvTerms);
        cbTerms = (AppCompatCheckBox) findViewById(R.id.cbTerms);
        btnStart = (Button) findViewById(R.id.btnStart);
        rlAddSkill = (RelativeLayout) findViewById(R.id.rlAddSkill);
        recyclerView = (ExpandableRecyclerView) findViewById(R.id.recyclerView);
        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        etFName = (EditText) findViewById(R.id.etFName);
        etLName = (EditText) findViewById(R.id.etLName);
        etLocation = (AutoCompleteTextView) findViewById(R.id.etLocation);
        role = getIntent().getStringExtra("ROLE");
       // etLocation.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        tvTerms.setOnClickListener(this);
        cbTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SkilliSignupActivity.this, TermsAndConditionActivity.class);
                startActivityForResult(i, 999);
            }
        });
        //init();
        Drawable drawable = getResources().getDrawable(R.drawable.geoicon);
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * 0.6),
                (int) (drawable.getIntrinsicHeight() * 0.6));
        ScaleDrawable sd = new ScaleDrawable(drawable, 0, 40, 40);
        etLocation.setCompoundDrawables(null, null, sd.getDrawable(), null);
        etLocation.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.autocomplete_list_item));
        etLocation.setOnItemClickListener(this);

        rlAddSkill.setOnClickListener(this);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        latitude = 0.0;
        longitude = 0.0;
        address = str;
        getLocationFromAddress(str);
    }
    public void getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                latitude = 0.0;
                longitude = 0.0;
            }
            Address location = address.get(0);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            city = address.get(0).getSubAdminArea();

            if(city==null){
                address.get(0).getLocality();
            }
            if(city==null){
                city="";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String generateNoteOnSD(Context context, String sFileName, InputStream inputStream) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), ".Skilli");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sBody = new StringBuilder();
            for (String line; (line = r.readLine()) != null; ) {
                sBody.append(line).append('\n');
            }

            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
            return gpxfile.getPath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private void callCategoryApi() {
        GetCategoryController anInterface = GetRetrofit.getInstance().create(GetCategoryController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));


        Call<GetCategoryModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN));
        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    if (response.code() == 200) {
                        if (response.body() instanceof GetCategoryModel) {
                            GetCategoryModel model = (GetCategoryModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (model.isStatus()) {
                                arrCategory = new ArrayList<>();
                                arrMainCategoryName= new ArrayList<>();

                                arrCategory = model.getResult();
                                for (int i = 0; i < arrCategory.size(); i++) {
                                    arrMainCategoryName.add(arrCategory.get(i).getName());
                                }
                                if (arrCategory.size() > 0) {
                                    arrsubCategoryName.clear();
                                    for (int i = 0; i < arrCategory.get(0).getSubcategory().size(); i++) {
                                        arrsubCategoryName.add(arrCategory.get(0).getSubcategory().get(i).getName());
                                    }
                                }
                                openSkillDialog();
                            } else {
                                SweetAlertController.getInstance().showErrorDialogClickListener(ctx, SweetAlertDialog.ERROR_TYPE, "Failed to get Category list", getString(R.string.app_name), "Try again", new SweetAlertController.SweetAlertClickListener() {
                                    @Override
                                    public void onSweetAlertClicked() {
                                        callCategoryApi();
                                    }
                                });

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


    private void openSkillDialog() {

        final Dialog dialog = new Dialog(ctx, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.dialog_selectskill);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(SkilliSignupActivity.this, R.color.dialog_transparent)));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.CENTER);
        MaterialSpinner spinner_MainCategory = (MaterialSpinner) dialog.findViewById(R.id.spinner_MainCategory);
        final MaterialSpinnerMultiple spinner_SubCategory = (MaterialSpinnerMultiple) dialog.findViewById(R.id.spinner_SubCategory);
        Button btnDone = (Button) dialog.findViewById(R.id.btnDone);
        final AppCompatCheckBox cbP = (AppCompatCheckBox) dialog.findViewById(R.id.cbP);
        final AppCompatCheckBox cbF = (AppCompatCheckBox) dialog.findViewById(R.id.cbF);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cbF.isChecked() && !cbP.isChecked()) {
                    CommonMethod.showToastMessage(ctx, "Please choose availability");
                } else if (arrSelectedCategory.size() == 0) {
                    CommonMethod.showToastMessage(ctx, "Please choose sub category");
                } else {
                    ArrayList<String> arrSubName = new ArrayList<>();
                    for (int k = 0; k < arrSelectedCategory.get(selectedCategoryPosition).size(); k++) {
                        Log.i("POS", arrSelectedCategory.get(selectedCategoryPosition).get(k).toString());
                        arrSubName.add(arrsubCategoryName.get(arrSelectedCategory.get(selectedCategoryPosition).get(k)));
                    }
                    if (arrayListHashMap.containsKey(arrMainCategoryName.get(selectedCategoryPosition))) {
                        arrayListHashMap.remove(arrMainCategoryName.get(selectedCategoryPosition));
                        arrayListCBHashMap.remove(arrMainCategoryName.get(selectedCategoryPosition));
                    }
                    arrayListHashMap.put(arrMainCategoryName.get(selectedCategoryPosition),
                            arrSubName);
                    int cbValue = 2;
                    if (cbF.isChecked() && cbP.isChecked()) {
                        cbValue = 2; // for both
                    } else if (cbF.isChecked()) {
                        cbValue = 1;// 1 for fulltime
                    } else {
                        cbValue = 0;// 0 for part time
                    }
                    arrayListCBHashMap.put(arrMainCategoryName.get(selectedCategoryPosition), cbValue);
                    CloudAdapter adapter = new CloudAdapter(ctx, arrayListHashMap);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
                    dialog.dismiss();
                }
            }
        });

        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        selectedCategoryPosition = 0;
        spinner_MainCategory.setItems(arrMainCategoryName);
        spinner_SubCategory.setItems(arrsubCategoryName);
        spinner_MainCategory.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                selectedCategoryPosition = position;
                arrsubCategoryName.clear();
                arrSelectedCategory.clear();

                for (int i = 0; i < arrCategory.get(position).getSubcategory().size(); i++) {
                    arrsubCategoryName.add(arrCategory.get(position).getSubcategory().get(i).getName());
                }
                spinner_SubCategory.setItems(arrsubCategoryName);

            }
        });
        spinner_SubCategory.setOnItemSelectedListener(new MaterialSpinnerMultiple.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinnerMultiple view, int position, long id, Object item, ArrayList arrSelectedSubCategory) {
                if (arrSelectedCategory.containsKey(selectedCategoryPosition)) {
                    arrSelectedCategory.remove(selectedCategoryPosition);
                }
                if(arrSelectedSubCategory.size()>0) {
                    arrSelectedCategory.put(selectedCategoryPosition, arrSelectedSubCategory);
                }else{
                    arrSelectedCategory.remove(selectedCategoryPosition);
                }


            }
        });

        dialog.show();

    }

//    private void updateLocationUI() {
//        if (mCurrentLocation != null) {
//
//
//            geocoder = new Geocoder(this, Locale.getDefault());
//
//            try {
//                addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                city = addresses.get(0).getSubAdminArea();
//                if(city==null){
//                    city=addresses.get(0).getLocality();
//                }
//                if(city==null){
//                    city="";
//                }
//
//                etLocation.setText(address);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//            // giving a blink animation on TextView
////            etLocation.setAlpha(0);
////            etLocation.animate().alpha(1).setDuration(300);
//
//            // location last updated time
////            txtUpdatedOn.setText("Last updated on: " + mLastUpdateTime);
//        }
//
//    }

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

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        // Resuming location updates depending on button state and
//        // allowed permissions
//        if (mRequestingLocationUpdates && checkPermissions()) {
//            startLocationUpdates();
//        }
//
//        updateLocationUI();
//    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
//    private void startLocationUpdates() {
//        mSettingsClient
//                .checkLocationSettings(mLocationSettingsRequest)
//                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
//                    @SuppressLint("MissingPermission")
//                    @Override
//                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
//
//
//                        //noinspection MissingPermission
//                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
//                                mLocationCallback, Looper.myLooper());
//
//                        updateLocationUI();
//                    }
//                })
//                .addOnFailureListener(this, new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        int statusCode = ((ApiException) e).getStatusCode();
//                        switch (statusCode) {
//                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//
//                                try {
//                                    // Show the dialog by calling startResolutionForResult(), and check the
//                                    // result in onActivityResult().
//                                    ResolvableApiException rae = (ResolvableApiException) e;
//                                    rae.startResolutionForResult(SkilliSignupActivity.this, REQUEST_CHECK_SETTINGS);
//                                } catch (IntentSender.SendIntentException sie) {
//                                }
//                                break;
//                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                                String errorMessage = "Location settings are inadequate, and cannot be " +
//                                        "fixed here. Fix in Settings.";
//
//                                CommonMethod.showToastMessage(SkilliSignupActivity.this, errorMessage);
//                        }
//
//                        updateLocationUI();
//                    }
//                });
//    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        if (mRequestingLocationUpdates) {
//            // pausing location updates
//            stopLocationUpdates();
//        }
//    }
//
//    public void stopLocationUpdates() {
//        // Removing location updates
//        mFusedLocationClient
//                .removeLocationUpdates(mLocationCallback)
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                    }
//                });
//    }

//    private void init() {
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        mSettingsClient = LocationServices.getSettingsClient(this);
//
//        mLocationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                super.onLocationResult(locationResult);
//                // location is received
//                mCurrentLocation = locationResult.getLastLocation();
//
//                updateLocationUI();
//            }
//        };
//
//        mRequestingLocationUpdates = false;
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
//        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
//        builder.addLocationRequest(mLocationRequest);
//        mLocationSettingsRequest = builder.build();
//    }

//    private void locationIconCliked() {
//// Requesting ACCESS_FINE_LOCATION using Dexter library
//        Dexter.withActivity(this)
//                .
//
//                        withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                .
//
//                        withListener(new PermissionListener() {
//                            @Override
//                            public void onPermissionGranted(PermissionGrantedResponse response) {
//                                mRequestingLocationUpdates = true;
//                                startLocationUpdates();
//                            }
//
//                            @Override
//                            public void onPermissionDenied(PermissionDeniedResponse response) {
//                                if (response.isPermanentlyDenied()) {
//                                    // open device settings when the permission is
//                                    // denied permanently
//                                    openSettings();
//                                }
//                            }
//
//                            @Override
//                            public void onPermissionRationaleShouldBeShown(PermissionRequest
//                                                                                   permission, PermissionToken token) {
//                                token.continuePermissionRequest();
//                            }
//                        }).
//
//                check();
//
//    }

    private void selectImage() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_IMAGE_REQUEST_CODE);
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    private File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("TAG", "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
//            case AppConstants.MPermission.M_CAMERA:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    checkCameraAndStoragePermission();
//                } else {
//                    CommonMethod.showToastMessage(SkilliSignupActivity.this, getString(R.string.permission_denied_you_cannot_access_camera));
//                }
//                break;
            case AppConstants.MPermission.M_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkCameraAndStoragePermission();
                } else {
                    CommonMethod.showToastMessage(SkilliSignupActivity.this, getString(R.string.permission_denied_you_cannot_read_external_storage));
                }
                break;
            default:
                break;
        }
    }

    private void checkCameraAndStoragePermission() {
        if (CommonMethod.isMarshMallow()) {
//            if (!MarshMPermission.checkPermission(SkilliSignupActivity.this, AppConstants.MPermission.M_CAMERA)) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, AppConstants.MPermission.M_CAMERA);
//            } else
            if (!MarshMPermission.checkPermission(SkilliSignupActivity.this, AppConstants.MPermission.M_READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstants.MPermission.M_READ_EXTERNAL_STORAGE);
            } else {
                selectImage();
            }
        } else {
            selectImage();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            fileUri = savedInstanceState.getParcelable("file_uri");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 999) {
                if (resultCode == RESULT_OK) {
                    cbTerms.setChecked(true);
                }else{
                    cbTerms.setChecked(false);
                }
            } else if (requestCode == GALLERY_IMAGE_REQUEST_CODE) {
                onAction(resultCode, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onAction(int resultCode, Intent data) throws Exception {
        if (resultCode == RESULT_OK) {
            if (data != null) {
                findImageTag(SHOW_PICTURE, FileUtil.from(this, data.getData()));
            } else {
                CommonMethod.showToastMessage(SkilliSignupActivity.this, getString(R.string.unable_to_get_image_from_gallery));
            }

        } else if (resultCode == RESULT_CANCELED) {
            CommonMethod.showToastMessage(SkilliSignupActivity.this, getString(R.string.image_loading_cancelled_by_user));
        } else {
            CommonMethod.showToastMessage(SkilliSignupActivity.this, getString(R.string.sorry_failed_to_capture_image));
        }
    }

    private void findImageTag(String SHOW_PICTURE, File fileUri) {
        switch (SHOW_PICTURE) {
            case "PROFILE":
                imageLoading(ivProfile, "PROFILE", fileUri);
                break;

            default:
                break;
        }
    }

    private void imageLoading(ImageView imageView, final String SHOW_PICTURE, final File fileUri) {
        if (imageView != null) {
            final ProgressDialog imgProgressBar = new ProgressDialog(SkilliSignupActivity.this);
            imgProgressBar.setMessage(getString(R.string.getting_image));
            imgProgressBar.show();
            imageView.setBackground(null);
            Picasso.get().load(fileUri).
                    placeholder(R.drawable.ic_add_photo)
                    .error(R.drawable.ic_add_photo)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            CommonMethod.cancelDialog(imgProgressBar, SkilliSignupActivity.this);
                            customCompressImage(fileUri, SHOW_PICTURE, true, fileUri);
                        }

                        @Override
                        public void onError(Exception e) {
                            CommonMethod.cancelDialog(imgProgressBar, SkilliSignupActivity.this);
                            CommonMethod.showToastMessage(SkilliSignupActivity.this, getString(R.string.image_loading_failed_please_try_again));
                            setCompressedImage(SHOW_PICTURE, false, null, fileUri);

                        }


                    });
        }
    }

    public void customCompressImage(final File fileUri, final String SHOW_PICTURE, final boolean isSuccess, final File originalFile) {
        try {
            // Compress image using RxJava in background thread
            Compressor.getDefault(this)
                    .compressToFileAsObservable(fileUri)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<File>() {
                        @Override
                        public void call(File file) {
                            setCompressedImage(SHOW_PICTURE, isSuccess, file, originalFile);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            CommonMethod.showToastMessage(SkilliSignupActivity.this, throwable.getMessage());
                            setCompressedImage(SHOW_PICTURE, false, null, originalFile);
                        }
                    });
        } catch (Exception e) {
            String error = e.getMessage();
            if (!error.isEmpty())
                CommonMethod.showToastMessage(SkilliSignupActivity.this, error);
            e.printStackTrace();
        }

    }

    private void setCompressedImage(String SHOW_PICTURE, boolean isSuccess, File file, File originalFile) {
        if (SHOW_PICTURE.equalsIgnoreCase("PROFILE")) {
            profileFile = file;


            uploadProfilePic();
        }
    }

    private void uploadProfilePic() {
        UploadProfilePicController anInterface = GetRetrofit.getInstance().create(UploadProfilePicController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        String json = new String();

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), profileFile);

// MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", profileFile.getName(), requestFile);


        Call<ProfilePicResponseModel> call = anInterface.getResponse(objSPS.getValueFromShared_Pref(Constants.TOKEN),
                body);
        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    if (response.code() == 200) {
                        CommonMethod.printAPIResponse(response.body());
                        if (response.body() instanceof ProfilePicResponseModel) {
                            ProfilePicResponseModel model = (ProfilePicResponseModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (model.isStatus()) {

                                uploadedProfileId = model.getResult().getId();
                            } else {
                                SweetAlertController.getInstance().showErrorDialog(ctx, SweetAlertDialog.ERROR_TYPE, model.getMessage(), getString(R.string.app_name));
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
    public void onItemDeleted(int positionParent, int childPosition, boolean isParent) {
        List<String> keys = new ArrayList<String>(arrayListHashMap.keySet()); // <== Set to List

        if (isParent) {
            arrayListHashMap.remove(keys.get(positionParent));
        } else {
            ArrayList<String> arrChild = arrayListHashMap.get(keys.get(positionParent));
            arrChild.remove(childPosition);
            arrayListHashMap.remove(keys.get(positionParent));
            if (arrChild.size() > 0) {
                arrayListHashMap.put(keys.get(positionParent), arrChild);
            }
        }

        CloudAdapter adapter = new CloudAdapter(ctx, arrayListHashMap);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));


    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void callSkilliSignupApi() {
        SkilliSignupController anInterface = GetRetrofit.getInstance().create(SkilliSignupController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        String json = new String();
        try {
            Gson gson = new Gson();
            SkilliSignupRequestModel requestModel = new SkilliSignupRequestModel();
            requestModel.setFirstName(CommonMethod.getFirstCapName(etFName.getText().toString().trim()));
            requestModel.setLastName(CommonMethod.getFirstCapName(etLName.getText().toString().trim()));
            requestModel.setLatitude("" + latitude);
            requestModel.setLongitude("" + longitude);
            requestModel.setDeviceId(OnBoardingActivity.fcmToken);
            requestModel.setCity(CommonMethod.getFirstCapName(city));

            requestModel.setLocationString(CommonMethod.getFirstCapName(etLocation.getText().toString().trim()));
            requestModel.setMobileNumber(objSPS.getValueFromShared_Pref(Constants.MOBILE)
            );
            requestModel.setRole(role);

            ArrayList<SkilliSignupRequestModel.SkillRequestModel> arrSkillRequestModel = new ArrayList<>();
            List<String> keys = new ArrayList<String>(arrayListHashMap.keySet()); // <== Set to List
            SkilliSignupRequestModel.SkillRequestModel model;
            ArrayList<String> strSubCategory;
            for (int i = 0; i < keys.size(); i++) {
                model = new SkilliSignupRequestModel().new SkillRequestModel();
                model.setCategory(keys.get(i));
                strSubCategory = new ArrayList<>();
                for (int j = 0; j < arrayListHashMap.get(keys.get(i)).size(); j++) {
                    strSubCategory.add(arrayListHashMap.get(keys.get(i)).get(j));

                }
                model.setSubcategory(strSubCategory);
                if (arrayListCBHashMap.get(keys.get(i)) == 0) {
                    model.setAvailabilityPartTime(true);

                }
                if (arrayListCBHashMap.get(keys.get(i)) == 1) {
                    model.setAvailibilityFullTime(true);

                }
                if (arrayListCBHashMap.get(keys.get(i)) == 2) {
                    model.setAvailibilityFullTime(true);
                    model.setAvailabilityPartTime(true);

                }
                arrSkillRequestModel.add(model);
            }
            requestModel.setSkills(arrSkillRequestModel);
            SkilliSignupRequestModel.ProfilePicRequestModel profilePicRequestModel = new SkilliSignupRequestModel().new ProfilePicRequestModel();
            profilePicRequestModel.setId(uploadedProfileId);
            requestModel.setProfilePic(profilePicRequestModel);
            json = gson.toJson(requestModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Call<LoginModel> call = anInterface.getResponse(objSPS.getValueFromShared_Pref(Constants.TOKEN),
                body);
        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    if (response.code() == 200) {
                        CommonMethod.printAPIResponse(response.body());
                        if (response.body() instanceof LoginModel) {
                            LoginModel model = (LoginModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (model.isStatus()) {

                                objSPS.setValueToSharedPref(Constants.FIRSTNAME, model.getResult().getFirstName());
                                objSPS.setValueToSharedPref(Constants.LASTNAME, model.getResult().getLastName());

                                objSPS.setValueToSharedPref(Constants.TOKEN, model.getResult().getToken());
                                objSPS.setValueToSharedPref(Constants.MOBILE, model.getResult().getMobileNumber());
                                objSPS.setValueToSharedPref(Constants.ID, model.getResult().get_id());
                                objSPS.setValueToSharedPref(Constants.ROLE, model.getResult().getRole());
                                objSPS.setValueToSharedPref(Constants.LOGGED_IN, true);
                                if (model.getResult().getSkills() != null) {

                                    String skills = "";
                                    for (int j = 0; j < model.getResult().getSkills().size(); j++) {
                                        if (skills.equals("")) {
                                            skills = model.getResult().getSkills().get(j).getCategory();
                                        } else {
                                            skills = skills + "," + model.getResult().getSkills().get(j).getCategory();

                                        }
                                    }
                                    objSPS.setValueToSharedPref(Constants.SKILLS, skills);
                                }
                                if (model.getResult().getProfilePicThumbnail() != null) {
                                    objSPS.setValueToSharedPref(Constants.IMAGE, model.getResult().getProfilePicThumbnail());

                                }

                                Intent i = new Intent(ctx, SliderActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            } else {
                                SweetAlertController.getInstance().showErrorDialog(ctx, SweetAlertDialog.ERROR_TYPE, model.getMessage(), getString(R.string.app_name));
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tvTerms:
                Intent i = new Intent(SkilliSignupActivity.this, TermsAndConditionActivity.class);
                startActivityForResult(i, 999);
                break;
            case R.id.etLocation:
                //locationIconCliked();
                break;
            case R.id.ivProfile:
                SHOW_PICTURE = "PROFILE";
                checkCameraAndStoragePermission();
                break;
            case R.id.btnStart:
                if (TextUtils.isEmpty(etFName.getText().toString().trim())) {
                    CommonMethod.showToastMessage(ctx, "Please enter your first name.");
                } else if (TextUtils.isEmpty(etLName.getText().toString().trim())) {
                    CommonMethod.showToastMessage(ctx, "Please enter your last name.");
                } else if (TextUtils.isEmpty(etLocation.getText().toString().trim())) {
                    CommonMethod.showToastMessage(ctx, "Please provide your current location.");
                } else if (arrayListHashMap.size() == 0) {
                    CommonMethod.showToastMessage(ctx, "Please add skills.");
                } else if (uploadedProfileId == 0) {
                    CommonMethod.showToastMessage(ctx, "Please upload profile pic.");
                } else if (!cbTerms.isChecked()) {
                    CommonMethod.showToastMessage(ctx, "Please accept Terms and conditions");
                } else {
                    callSkilliSignupApi();
                }
                break;

            case R.id.rlAddSkill:
                callCategoryApi();
                break;


        }
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }
    private static final String LOG_TAG = "EmpuApp";
    private static final String API_KEY = "AIzaSyDPFjRVRPEL0Mhqwd5NrJzi5FzmjtFOONg";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&types=geocode");
            sb.append("&components=country:in");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

}
