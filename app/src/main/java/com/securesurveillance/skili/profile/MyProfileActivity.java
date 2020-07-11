package com.securesurveillance.skili.profile;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.melnykov.fab.FloatingActionButton;
import com.securesurveillance.skili.RatingCommentsActivity;
import com.securesurveillance.skili.RecruiterPostJobActivity;
import com.securesurveillance.skili.adapter.FavouriteAdapter;
import com.securesurveillance.skili.adapter.RecruiterPostedJobsAdapter;
import com.securesurveillance.skili.apiinterfaces.RemoveFavApplicantController;
import com.securesurveillance.skili.fragments.FavouriteFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.adapter.PostsGridViewAdapter;
import com.securesurveillance.skili.apiHandler.API;
import com.securesurveillance.skili.apiHandler.APIResponseListener;
import com.securesurveillance.skili.apiHandler.AppConstants;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.GetRetrofit;
import com.securesurveillance.skili.apiinterfaces.AddFavApplicantController;
import com.securesurveillance.skili.apiinterfaces.AddFavRecruiter;
import com.securesurveillance.skili.apiinterfaces.GetAllDetailsProfileController;
import com.securesurveillance.skili.apiinterfaces.UpdateProfilePicController;
import com.securesurveillance.skili.apiinterfaces.UploadPostController;
import com.securesurveillance.skili.compressor.Compressor;
import com.securesurveillance.skili.compressor.FileUtil;
import com.securesurveillance.skili.fragments.FragmentDrawer;
import com.securesurveillance.skili.model.responsemodel.GetAllProfileDetailsModel;
import com.securesurveillance.skili.model.responsemodel.LoginModel;
import com.securesurveillance.skili.model.responsemodel.ProfilePicResponseModel;
import com.securesurveillance.skili.model.responsemodel.UploadPostResponseModel;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.ExpandableGridView;
import com.securesurveillance.skili.utility.FileExtenion;
import com.securesurveillance.skili.utility.MarshMPermission;
import com.securesurveillance.skili.utility.SharePreferanceWrapperSingleton;
import com.securesurveillance.skili.utility.SweetAlertController;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by adarsh on 10/21/2018.
 */

public class MyProfileActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView ivBack, ivMenu, ivFav;
    ExpandableGridView gridView;
    String isFavouriteAdded="false";
    FloatingActionButton fab;
    SharePreferanceWrapperSingleton objSPS;
    Context ctx;
    ImageView ivProfile;
    TextView tvName, tvJobcounter, tvCounterLabel;
    GetAllProfileDetailsModel model;
    int GALLERY_IMAGE_REQUEST_CODE = 1;
    public static final int BUFFER = 2048;
    private TextView tvPrice, tvPriceType, tvRating, tvReviews;
    String pid = "";
    private String SHOW_PICTURE = "";
    File profileFile;
    String PROFILEID = "";
    LinearLayout tvRatingView,llJobCounter;
    boolean isItMe=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);
        ctx = this;
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(ctx);
        if (getIntent().hasExtra("PID")) {
            pid = getIntent().getStringExtra("PID");
        }
        if(getIntent().hasExtra("ISFAV")) {
           isFavouriteAdded= getIntent().getStringExtra("ISFAV");
        }

        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivMenu = (ImageView) findViewById(R.id.ivMenu);
        ivFav = (ImageView) findViewById(R.id.ivFav);
        gridView = (ExpandableGridView) findViewById(R.id.gridView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        ivProfile.setOnClickListener(this);
        tvName = (TextView) findViewById(R.id.tvName);
        tvJobcounter = (TextView) findViewById(R.id.tvJobcounter);
        tvCounterLabel = (TextView) findViewById(R.id.tvCounterLabel);
        tvRatingView = (LinearLayout) findViewById(R.id.tvRatingView);
        llJobCounter=(LinearLayout)findViewById(R.id.llJobCounter);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvRating = (TextView) findViewById(R.id.tvRating);
        tvPriceType = (TextView) findViewById(R.id.tvPriceType);
        tvReviews = (TextView) findViewById(R.id.tvReviews);
        ivBack.setOnClickListener(this);
        ivMenu.setOnClickListener(this);
        fab.setOnClickListener(this);
        ivFav.setOnClickListener(this);
        llJobCounter.setOnClickListener(this);
        if (!objSPS.getValueFromShared_Pref(Constants.ROLE).equals("Individual")) {
            tvCounterLabel.setText("Job Posted");

        }
        if (!pid.isEmpty()) {
            if (!pid.equalsIgnoreCase(objSPS.getValueFromShared_Pref(Constants.MOBILE))) {

                if (!objSPS.getValueFromShared_Pref(Constants.ROLE).equals("Individual")) {
                    tvCounterLabel.setText("Job Counter");

                }else{
                    tvCounterLabel.setText("Job Posted");

                }
            }
            fab.setVisibility(View.GONE);
            Picasso.get().load(R.drawable.info).into(ivMenu);

        }

        if (getIntent().hasExtra("PROFILEID")) {
            PROFILEID = getIntent().getStringExtra("PROFILEID");
            ivFav.setVisibility(View.VISIBLE);
            if(!PROFILEID.equals(objSPS.getValueFromShared_Pref(Constants.ID))) {
                    if (isFavouriteAdded.equals("true")) {
                        ivFav.setBackground(null);
                        ivFav.setBackground(getDrawable(R.drawable.heart_fill));
                    } else {
                        ivFav.setBackground(null);
                        ivFav.setBackground(getDrawable(R.drawable.heart));
                    }

            }else{
                isItMe=true;
                ivFav.setVisibility(View.GONE);
            }

        }
        tvRatingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ctx, RatingCommentsActivity.class);
                i.putExtra("RATING", model.getResult().getRating());
                if (TextUtils.isEmpty(model.getResult().getLastName())) {
                    model.getResult().setLastName("");
                }
                i.putExtra("NAME", model.getResult().getFirstName() + " " + model.getResult().getLastName());
                i.putExtra("PIC", model.getResult().getProfilePic().getMediaLink());
                if (model.getResult().getRatingFeedbacks().size() > 0) {

                    i.putExtra("RATINGDATA", (Serializable) model.getResult().getRatingFeedbacks());
                }
                startActivity(i);


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllDetails();

    }

    private void getAllDetails() {
        GetAllDetailsProfileController anInterface = GetRetrofit.getInstance().create(GetAllDetailsProfileController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        String num = objSPS.getValueFromShared_Pref(Constants.MOBILE);
        if (!pid.isEmpty()) {
            num = pid;
        }
        Call<GetAllProfileDetailsModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN), num,PROFILEID);

        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    if (response.code() == 200) {
                        if (response.body() instanceof GetAllProfileDetailsModel) {
                            model = (GetAllProfileDetailsModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (model.isStatus()) {
                                tvName.setText(model.getResult().getFirstName() + " " + model.getResult().getLastName());
                                tvJobcounter.setText(model.getResult().getAppliedJobs());
                                tvRating.setText(model.getResult().getRating());
                                if (!TextUtils.isEmpty(model.getResult().getReviewCounts())) {
                                    tvReviews.setText("( " + model.getResult().getReviewCounts() + " Reviews )");

                                }
                                if (!TextUtils.isEmpty(model.getResult().getCharge())) {

                                    tvPrice.setText(model.getResult().getCharge());
                                    String chargeTypeString = "";
                                    if (!TextUtils.isEmpty(model.getResult().getChargeType())) {
                                        if (model.getResult().getChargeType().contains("MONTH")) {
                                            chargeTypeString = "INR/month";
                                        } else if (model.getResult().getChargeType().contains("HOUR")) {
                                            chargeTypeString = "INR/hour";

                                        } else if (model.getResult().getChargeType().contains("YEAR")) {
                                            chargeTypeString = "INR/year";

                                        }
                                        tvPriceType.setText(chargeTypeString);

                                    }
                                }


                                Picasso.get().load(model.getResult().getProfilePic().getMediaLink()).into(ivProfile, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        ivProfile.setBackground(getDrawable(R.drawable.user_profile));
                                    }
                                });
                              //  if (model.getResult().getPost().size() > 0) {
                                    PostsGridViewAdapter adapter = new PostsGridViewAdapter(MyProfileActivity.this,
                                            model.getResult().getPost(),isItMe);
                                    gridView.setAdapter(adapter);
                                    gridView.setNumColumns(3);
                                    gridView.setExpanded(true);
                               // }
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

    private void addFavApplicant() {
        AddFavApplicantController anInterface = GetRetrofit.getInstance().create(AddFavApplicantController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        Call<LoginModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN), objSPS.getValueFromShared_Pref(Constants.ID), PROFILEID);
        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    if (response.code() == 200) {
                        if (response.body() instanceof LoginModel) {
                            LoginModel model = (LoginModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (model.isStatus()) {
                                isFavouriteAdded="true";
                                Toast.makeText(ctx, model.getMessage(), Toast.LENGTH_LONG).show();
                                ivFav.setBackground(null);
                                ivFav.setBackground(getDrawable(R.drawable.heart_fill));
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
    private void deleteFavRecruiter() {
        RemoveFavApplicantController anInterface = GetRetrofit.getInstance().create(RemoveFavApplicantController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        Call<LoginModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN),PROFILEID);

        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (ctx!=null&&!isFinishing()) {
                    if (response.code() == 200) {
                        if (response.body() instanceof LoginModel) {
                            LoginModel model = (LoginModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (model.isStatus()) {
                                isFavouriteAdded="false";

                                Toast.makeText(ctx, model.getMessage(), Toast.LENGTH_LONG).show();
                                    ivFav.setBackground(null);
                                    ivFav.setBackground(getDrawable(R.drawable.heart));
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
                if (ctx!=null&&!isFinishing()) {
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
    private void deleteFavApplicant() {
        RemoveFavApplicantController anInterface = GetRetrofit.getInstance().create(RemoveFavApplicantController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        Call<LoginModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN), PROFILEID);

        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (ctx!=null&&!isFinishing()) {
                    if (response.code() == 200) {
                        if (response.body() instanceof LoginModel) {
                            LoginModel model = (LoginModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (model.isStatus()) {
                                isFavouriteAdded="false";

                                Toast.makeText(ctx, model.getMessage(), Toast.LENGTH_LONG).show();
                                ivFav.setBackground(null);
                                ivFav.setBackground(getDrawable(R.drawable.heart));
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
                if (ctx!=null&&!isFinishing()) {
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

    private void addFavRecruiter() {
        AddFavRecruiter anInterface = GetRetrofit.getInstance().create(AddFavRecruiter.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        Call<LoginModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN), objSPS.getValueFromShared_Pref(Constants.ID), PROFILEID);
        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    if (response.code() == 200) {
                        if (response.body() instanceof LoginModel) {
                            LoginModel model = (LoginModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (model.isStatus()) {
                                isFavouriteAdded="true";

                                Toast.makeText(ctx, model.getMessage(), Toast.LENGTH_LONG).show();
                                ivFav.setBackground(null);
                                ivFav.setBackground(getDrawable(R.drawable.heart_fill));
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivProfile:
                openImageDialog();
                break;

            case R.id.ivFav:
                if (objSPS.getValueFromShared_Pref(Constants.ROLE).contains("Individual")) {
                    if (isFavouriteAdded.equals("true")) {
                        deleteFavRecruiter();
//                        Toast.makeText(ctx, "Already added as favourite recruiter", Toast.LENGTH_LONG).show();
                    } else {
                        addFavRecruiter();
                    }

                } else {
                    if (isFavouriteAdded.equals("true")) {
                        deleteFavApplicant();
//                        Toast.makeText(ctx, "Already added as favourite candidate", Toast.LENGTH_LONG).show();
                    } else {
                        addFavApplicant();
                    }
                }

                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.llJobCounter:
                if(tvCounterLabel.getText().toString().contains("Posted")&&!TextUtils.isEmpty(PROFILEID)){
                    Intent i = new Intent(MyProfileActivity.this, RecruiterPostJobActivity.class);
                    i.putExtra("PROFILEID",PROFILEID);
                    startActivity(i);

                }
                break;
            case R.id.ivMenu:
                if (model != null) {
                    Intent i = new Intent(MyProfileActivity.this, PersonalDetailsActivity.class);
                    i.putExtra(Constants.BundleData.Profile.PF_FULL_NAME, tvName.getText());
                    i.putExtra(Constants.BundleData.Profile.PF_CITY, model.getResult().getCity());
                    i.putExtra(Constants.BundleData.Profile.PF_DOB, model.getResult().getDob());
                    i.putExtra(Constants.BundleData.Profile.PF_EMAIL, model.getResult().getEmail());
                    i.putExtra(Constants.BundleData.Profile.PF_MOBILE, model.getResult().getMobileNumber());
                    i.putExtra(Constants.BundleData.Profile.PF_WebURL, model.getResult().getWebsite());
                    i.putExtra(Constants.BundleData.Profile.PF_EDUCATION, (Serializable) model.getResult().getEducation());
                    i.putExtra(Constants.BundleData.Profile.PF_EXPERIENCE, (Serializable) model.getResult().getExperience());
                    i.putExtra(Constants.BundleData.Profile.PF_FIRST_NAME, model.getResult().getFirstName());
                    i.putExtra(Constants.BundleData.Profile.PF_LAST_NAME, model.getResult().getLastName());
                    i.putExtra(Constants.BundleData.Profile.PF_ADDRESS, model.getResult().getLocationString());
                    i.putExtra(Constants.BundleData.Profile.PF_LATITUDE, model.getResult().getLatitude());
                    i.putExtra(Constants.BundleData.Profile.PF_LONGITUDe, model.getResult().getLongitude());
                    i.putExtra(Constants.BundleData.Profile.PF_CHARGE, model.getResult().getCharge());
                    i.putExtra(Constants.BundleData.Profile.PF_CHARGETYPE, model.getResult().getChargeType());
                    i.putExtra(Constants.BundleData.Profile.PF_SHOWMOBILE, model.getResult().isDisplayMobile());
                    i.putExtra(Constants.BundleData.Profile.PF_COMPANY, model.getResult().getCompanyName());
                    i.putExtra(Constants.BundleData.Profile.PF_GSTN, model.getResult().getGstin());
                    i.putExtra(Constants.BundleData.Profile.PF_FBURL, model.getResult().getFacebookLink());
                    i.putExtra(Constants.BundleData.Profile.PF_INSTAURL, model.getResult().getInstagramLink());
                    i.putExtra(Constants.BundleData.Profile.PF_TWITTERURL, model.getResult().getLinkedInLink());
                    i.putExtra("PID",pid);

                    if (!pid.isEmpty()) {
                        i.putExtra("ISNOTME", true);
                    }
                    startActivity(i);
                }
                break;
            case R.id.fab:
                SHOW_PICTURE = "";
                checkCameraAndStoragePermission();

                break;

        }
    }

    private void selectImage() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_IMAGE_REQUEST_CODE);
    }

    private void checkCameraAndStoragePermission() {
        if (CommonMethod.isMarshMallow()) {
//            if (!MarshMPermission.checkPermission(MyProfileActivity.this, AppConstants.MPermission.M_CAMERA)) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, AppConstants.MPermission.M_CAMERA);
//            } else
            if (!MarshMPermission.checkPermission(MyProfileActivity.this, AppConstants.MPermission.M_READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstants.MPermission.M_READ_EXTERNAL_STORAGE);
            } else {
                if (SHOW_PICTURE.equals("")) {
                    selectMedia();

                } else {
                    selectImage();

                }
            }
        } else {
            if (SHOW_PICTURE.equals("")) {
                selectMedia();

            } else {
                selectImage();

            }
        }
    }

    public FileExtenion getFileExtenion(String contentType) {
        switch (contentType) {
            case "mp3":
                return FileExtenion.AUDIO_MP3;
            case "jpeg":
                return FileExtenion.IMG_JPEG;
            case "png":
                return FileExtenion.IMG_PNG;
            case "gif":
                return FileExtenion.IMG_GIF;
            case "vob":
                return FileExtenion.VIDEO_VOB;
            case "jpg":
                return FileExtenion.IMG_JPG;
            case "mp4":
                return FileExtenion.VIDEO_MP4;
            default:
                break;
        }
        return null;


    }

    private void selectMedia() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/* video/*");
        startActivityForResult(intent, GALLERY_IMAGE_REQUEST_CODE);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == GALLERY_IMAGE_REQUEST_CODE) {
                onAction(resultCode, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCompressedImage(String SHOW_PICTURE, boolean isSuccess, File file, File originalFile) {
        if (SHOW_PICTURE.equalsIgnoreCase("PROFILE")) {
            profileFile = file;
            uploadProfilePic();
        }
    }

    Dialog imageDialog;

    private void openImageDialog() {
        imageDialog = new Dialog(ctx, android.R.style.Theme_Translucent_NoTitleBar);
        imageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        imageDialog.setContentView(R.layout.dialog_profileimage);
        imageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(ctx, R.color.dialog_transparent)));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.CENTER);
        final ImageView ivImage = (ImageView) imageDialog.findViewById(R.id.ivImage);
        TextView tvPicHeading=(TextView)imageDialog.findViewById(R.id.tvPicHeading);
        Button btnDone = (Button) imageDialog.findViewById(R.id.btnDone);
        if (!pid.isEmpty()) {
            btnDone.setVisibility(View.GONE);
            tvPicHeading.setText("Profile Pic");
        }
        ImageView ivClose = (ImageView) imageDialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageDialog.dismiss();
            }
        });
        Picasso.get().load(model.getResult().getProfilePic().getMediaLink()).into(ivImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                ivImage.setBackground(getDrawable(R.drawable.user_profile));
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SHOW_PICTURE = "PROFILE";
                checkCameraAndStoragePermission();
            }
        });
        imageDialog.show();
    }

    private void uploadProfilePic() {
        UpdateProfilePicController anInterface = GetRetrofit.getInstance().create(UpdateProfilePicController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), profileFile);
        RequestBody requestProfileID =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), objSPS.getValueFromShared_Pref(Constants.ID));
// MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", profileFile.getName(), requestFile);


        Call<ProfilePicResponseModel> call = anInterface.getResponse(objSPS.getValueFromShared_Pref(Constants.TOKEN),
                body, requestProfileID);
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
                                if (imageDialog != null && imageDialog.isShowing())
                                    imageDialog.dismiss();
                                objSPS.setValueToSharedPref(Constants.IMAGE, model.getResult().getMediaLink());

                                Picasso.get().load(objSPS.getValueFromShared_Pref(Constants.IMAGE)).into(FragmentDrawer.imgProfile, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        FragmentDrawer.imgProfile.setBackground((getDrawable(R.drawable.user_profile)));

                                    }
                                });
                                Toast.makeText(ctx, model.getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                SweetAlertController.getInstance().showErrorDialog(ctx, SweetAlertDialog.ERROR_TYPE, model.getMessage(), getString(R.string.app_name));
                                ivProfile.setBackground(null);
                                Picasso.get().load(R.drawable.ic_add_photo).into(ivProfile);
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
                            CommonMethod.showToastMessage(MyProfileActivity.this, throwable.getMessage());
                        }
                    });
        } catch (Exception e) {
            String error = e.getMessage();
            if (!error.isEmpty())
                CommonMethod.showToastMessage(MyProfileActivity.this, error);
            e.printStackTrace();
        }

    }

    private void imageLoading(final File fileUri) {
        final ProgressDialog imgProgressBar = new ProgressDialog(MyProfileActivity.this);
        imgProgressBar.setMessage(getString(R.string.getting_image));
        imgProgressBar.show();
        ivProfile.setBackground(null);
        Picasso.get().load(fileUri).
                placeholder(R.drawable.ic_add_photo)
                .error(R.drawable.ic_add_photo)
                .into(ivProfile, new Callback() {
                    @Override
                    public void onSuccess() {
                        CommonMethod.cancelDialog(imgProgressBar, MyProfileActivity.this);
                        customCompressImage(fileUri, SHOW_PICTURE, true, fileUri);
                    }

                    @Override
                    public void onError(Exception e) {
                        CommonMethod.cancelDialog(imgProgressBar, MyProfileActivity.this);
                        CommonMethod.showToastMessage(MyProfileActivity.this, getString(R.string.image_loading_failed_please_try_again));

                    }


                });

    }

    private void onAction(int resultCode, Intent data) throws Exception {
        if (resultCode == RESULT_OK) {
            if (data != null) {
                if (SHOW_PICTURE.equals("")) {
                    CommonMethod.cancelDialog(progressDialog, MyProfileActivity.this);
                    progressDialog = new ProgressDialog(MyProfileActivity.this);
                    CommonMethod.loadingDialog(progressDialog, MyProfileActivity.this);
                    File file = FileUtil.from(this, data.getData());

                    uploadMediaApi(file);
                } else {
                    File file = FileUtil.from(this, data.getData());

                    imageLoading(file);
                }
            } else {
                CommonMethod.showToastMessage(MyProfileActivity.this, getString(R.string.unable_to_get_image_from_gallery));
            }

        } else if (resultCode == RESULT_CANCELED) {
            CommonMethod.showToastMessage(MyProfileActivity.this, getString(R.string.image_loading_cancelled_by_user));
        } else {
            CommonMethod.showToastMessage(MyProfileActivity.this, getString(R.string.sorry_failed_to_capture_image));
        }
    }
      ProgressDialog progressDialog;

    private void uploadMediaApi(File file) {

        UploadPostController anInterface = GetRetrofit.getInstance().create(UploadPostController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        String json = new String();

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        String extension = FilenameUtils.getExtension(file.getAbsolutePath()); // returns "exe"
        getFileExtenion(extension);
// MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        CommonMethod.cancelDialog(progressDialog, MyProfileActivity.this);


        Call<UploadPostResponseModel> call = anInterface.getResponse(objSPS.getValueFromShared_Pref(Constants.TOKEN), "description", getFileExtenion(extension).getContentType(),
                objSPS.getValueFromShared_Pref(Constants.ID),
                body);
        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    if (response.code() == 200) {
                        CommonMethod.printAPIResponse(response.body());
                        if (response.body() instanceof UploadPostResponseModel) {
                            UploadPostResponseModel model = (UploadPostResponseModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (model.isStatus()) {

                                CommonMethod.showToastMessage(MyProfileActivity.this, model.getMessage());
                                getAllDetails();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
//            case AppConstants.MPermission.M_CAMERA:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    checkCameraAndStoragePermission();
//                } else {
//                    CommonMethod.showToastMessage(MyProfileActivity.this, getString(R.string.permission_denied_you_cannot_access_camera));
//                }
//                break;
            case AppConstants.MPermission.M_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkCameraAndStoragePermission();
                } else {
                    CommonMethod.showToastMessage(MyProfileActivity.this, getString(R.string.permission_denied_you_cannot_read_external_storage));
                }
                break;
            default:
                break;
        }
    }


}
