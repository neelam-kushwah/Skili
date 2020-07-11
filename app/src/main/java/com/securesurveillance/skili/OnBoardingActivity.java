package com.securesurveillance.skili;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.SkinManager;
import com.facebook.accountkit.ui.UIManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.JsonSyntaxException;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.securesurveillance.skili.adapter.MyPagerAdapter;
import com.securesurveillance.skili.adapter.PostsGridViewAdapter;
import com.securesurveillance.skili.apiHandler.API;
import com.securesurveillance.skili.apiHandler.APIResponseListener;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.GetRetrofit;
import com.securesurveillance.skili.apiinterfaces.GetAllDetailsProfileController;
import com.securesurveillance.skili.apiinterfaces.LoginController;
import com.securesurveillance.skili.apiinterfaces.UpdateFCMTokenController;
import com.securesurveillance.skili.fragments.TutorialFiveFragment;
import com.securesurveillance.skili.fragments.TutorialFourthFragment;
import com.securesurveillance.skili.fragments.TutorialOneFragment;
import com.securesurveillance.skili.fragments.TutorialTwoFragment;
import com.securesurveillance.skili.fragments.TutorialthreeFragment;
import com.securesurveillance.skili.model.responsemodel.GetAllProfileDetailsModel;
import com.securesurveillance.skili.model.responsemodel.LoginModel;
import com.securesurveillance.skili.profile.MyProfileActivity;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.MyManifest;
import com.securesurveillance.skili.utility.SharePreferanceWrapperSingleton;
import com.securesurveillance.skili.utility.SweetAlertController;
import com.securesurveillance.skili.utility.ZoomoutTransformation;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class OnBoardingActivity extends FragmentActivity {
    ArrayList<String> arrayList;
    public static int screenHeight;
    public static int screenWidth;
    private static final int APP_REQUEST_CODE = 99;
    Context ctx;
    SharePreferanceWrapperSingleton objSPS;
    MyPagerAdapter pagerAdapter;
    private int dotsCount;
    private ViewPager onboard_pager;
    private Button btn_get_started;
    int previous_pos = 0;
    LoginModel loginResponse;
    public static String fcmToken = "";
private void signout(){
    try {
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        if (mAuth != null) {
            mAuth.signOut();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(OnBoardingActivity.this);
        ctx = this;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        setContentView(R.layout.layout_onboarding);
        signout();
        FirebaseApp.initializeApp(this);

        // [START retrieve_current_token]
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        fcmToken = token;
                        if (!TextUtils.isEmpty(objSPS.getValueFromShared_Pref(Constants.ID))) {
                            // call update fcm token api.
                            updateFcmtoken();
                        }

                    }
                });
        // [END retrieve_current_token]


        arrayList = new ArrayList<>();
        arrayList.add("To hire someone");
        arrayList.add("To get hired");

        btn_get_started = (Button) findViewById(R.id.btn_get_started);
        onboard_pager = (ViewPager) findViewById(R.id.pager_introduction);
        ZoomoutTransformation zoomoutTransformation = new ZoomoutTransformation();
        onboard_pager.setPageTransformer(true, zoomoutTransformation);
        dotsCount = 5;


        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        addingFragmentsTOpagerAdapter();
        onboard_pager.setAdapter(pagerAdapter);

        onboard_pager.setCurrentItem(0);
        onboard_pager.setOffscreenPageLimit(5);
        onboard_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                int pos = position + 1;

                if (pos == dotsCount && previous_pos == (dotsCount - 1))
                    show_animation();
                else if (pos == (dotsCount - 1) && previous_pos == dotsCount)
                    hide_animation();

                previous_pos = pos;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btn_get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signout();
                Dexter.withActivity(OnBoardingActivity.this)
                        .withPermissions(
                                MyManifest.permission.READ_EXTERNAL_STORAGE,
                                MyManifest.permission.WRITE_EXTERNAL_STORAGE,
                                MyManifest.permission.ACCESS_FINE_LOCATION,
                                MyManifest.permission.ACCESS_NETWORK_STATE

//                                MyManifest.permission.READ_PHONE_STATE,
//                                MyManifest.permission.READ_SMS,
//                                MyManifest.permission.RECEIVE_SMS
                        )
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                // check if all permissions are granted
                                if (report.areAllPermissionsGranted()) {
                                    // do you work now
                                    loginWithSMS();

                                }

                                // check for permanent denial of any permission
                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    // permission is denied permenantly, navigate user to app settings
                                    Toast.makeText(OnBoardingActivity.this, "Please provide all permissions", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        })
                        .onSameThread()
                        .check();
            }
        });


    }

    private void updateFcmtoken() {
        UpdateFCMTokenController anInterface = GetRetrofit.getInstance().create(UpdateFCMTokenController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));

        Call<LoginModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN), objSPS.getValueFromShared_Pref(Constants.ID), fcmToken);

        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    CommonMethod.cancelDialog(dialog, ctx);

                }
            }

            @Override
            public void onFailure(Call call, final Throwable t) {
                if (!isFinishing()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (t instanceof UnknownHostException) {
                                //  CommonMethod.showToastMessage(ctx, getString(R.string.UNKNOWN_HOST));
                            } else if (t instanceof ConnectException) {
                                // CommonMethod.showToastMessage(ctx, getString(R.string.UNKNOWN_HOST));
                            } else if (t instanceof SocketTimeoutException) {
                                //  CommonMethod.showToastMessage(ctx, getString(R.string.SOCKET_TIME_OUT));
                            } else if (t instanceof SocketException) {
                                // CommonMethod.showToastMessage(ctx, getString(R.string.UNABLE_TO_CONNECT));
                            } else if (t instanceof JsonSyntaxException) {
                                // CommonMethod.showToastMessage(ctx, getString(R.string.JSON_SYNTAX));
                            } else {
                                // CommonMethod.showToastMessage(ctx, getString(R.string.UNKNOWN_ERROR));
                            }
                            t.printStackTrace();
                        }
                    });
                }
            }
        });

    }


    // Button bottomUp animation

    public void show_animation() {
        Animation show = AnimationUtils.loadAnimation(this, R.anim.slide_up_anim);

        btn_get_started.startAnimation(show);

        show.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                btn_get_started.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                btn_get_started.clearAnimation();

            }

        });


    }

    // Button Topdown animation

    public void hide_animation() {
        Animation hide = AnimationUtils.loadAnimation(this, R.anim.slide_down_anim);

        btn_get_started.startAnimation(hide);

        hide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                btn_get_started.clearAnimation();
                btn_get_started.setVisibility(View.GONE);

            }

        });


    }


    private void loginWithSMS() {
        //   phoneLogin();
        Intent i = new Intent(ctx, PhoneAuthActivity.class);
        startActivityForResult(i, APP_REQUEST_CODE);
    }

//    private void phoneLogin() {
//
//        final Intent intent = new Intent(this, AccountKitActivity.class);
//        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
//                new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE, AccountKitActivity.ResponseType.CODE); // or .ResponseType.TOKEN
//// constructor with a background image imageAccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder;
//        UIManager uiManager;
//
//// Skin is CLASSIC, CONTEMPORARY, or TRANSLUCENT
//// Tint is WHITE or BLACK
//// TintIntensity is a value between 55-85%
//
//        uiManager = new SkinManager(
//                SkinManager.Skin.CONTEMPORARY,
//                ContextCompat.getColor(this, R.color.yellow),
//                R.drawable.onboarding,
//                SkinManager.Tint.WHITE, 0.00);
//
//        configurationBuilder.setUIManager(uiManager);
//        configurationBuilder.setReadPhoneStateEnabled(true);
//        configurationBuilder.setReceiveSMS(true);
//
//        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());
//        startActivityForResult(intent, APP_REQUEST_CODE);
//    }

    private void addingFragmentsTOpagerAdapter() {
        pagerAdapter.addFragments(new TutorialOneFragment());
        pagerAdapter.addFragments(new TutorialTwoFragment());
        pagerAdapter.addFragments(new TutorialthreeFragment());
        pagerAdapter.addFragments(new TutorialFourthFragment());
        pagerAdapter.addFragments(new TutorialFiveFragment());

    }

    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request


//            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
//            String toastMessage;
//            if (loginResult.getError() != null) {
//                toastMessage = loginResult.getError().getErrorType().getMessage();
//                CommonMethod.showToastMessage(ctx, toastMessage);
//
//            } else if (loginResult.wasCancelled()) {
//                toastMessage = "Login Cancelled";
//                CommonMethod.showToastMessage(ctx, toastMessage);
//            } else {
//
//                callLoginAPi(loginResult.getAuthorizationCode());
//
//            }
            if (resultCode == Activity.RESULT_OK) {
                callLoginAPi(data.getStringExtra("Data"));
            }


        }
    }


    private void callLoginAPi(String code) {
        LoginController anInterface = GetRetrofit.getInstance().create(LoginController.class);
        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
            json.put("deviceId", fcmToken);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        Call<LoginModel> call = anInterface.getResponse(
                body);
        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    if (response.code() == 200) {
                        CommonMethod.printAPIResponse(response.body());
                        if (response.body() instanceof LoginModel) {
                            loginResponse = (LoginModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (loginResponse.isStatus()) {
                                objSPS.setValueToSharedPref(Constants.FIRSTNAME, loginResponse.getResult().getFirstName());
                                objSPS.setValueToSharedPref(Constants.LASTNAME, loginResponse.getResult().getLastName());
                                objSPS.setValueToSharedPref(Constants.TOKEN, loginResponse.getResult().getToken());
                                objSPS.setValueToSharedPref(Constants.MOBILE, loginResponse.getResult().getMobileNumber());
                                objSPS.setValueToSharedPref(Constants.ID, loginResponse.getResult().get_id());
                                objSPS.setValueToSharedPref(Constants.ROLE, loginResponse.getResult().getRole());
                                if (loginResponse.getResult().getFcmTokenReceived() != null && loginResponse.getResult().getFcmTokenReceived().equalsIgnoreCase("false")) {
                                    updateFcmtoken();
                                }
                                if (loginResponse.getResult().getSkills() != null) {
                                    String skills = "";
                                    for (int j = 0; j < loginResponse.getResult().getSkills().size(); j++) {
                                        if (skills.equals("")) {
                                            skills = loginResponse.getResult().getSkills().get(j).getCategory();
                                        } else {
                                            skills = skills + "," + loginResponse.getResult().getSkills().get(j).getCategory();

                                        }
                                    }
                                    objSPS.setValueToSharedPref(Constants.SKILLS, skills);
                                }
                                Intent i;
                                if (loginResponse.getResult().getRole().equals("GUEST")) {

                                    // showPreferenceDialog();
                                    i = new Intent(OnBoardingActivity.this, ChooseCategoryActivity.class);
                                    startActivity(i);
                                } else {
                                    if (loginResponse.getResult().getProfilePic() != null) {
                                        objSPS.setValueToSharedPref(Constants.IMAGE, loginResponse.getResult().getProfilePic());

                                    }
                                    objSPS.setValueToSharedPref(Constants.LOGGED_IN, true);
                                    i = new Intent(ctx, SliderActivity.class);


                                    startActivity(i);

                                    finish();

                                }
                            } else {
                                SweetAlertController.getInstance().showErrorDialog(ctx, SweetAlertDialog.ERROR_TYPE, loginResponse.getMessage(), getString(R.string.app_name));
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
//                                CommonMethod.showToastMessage(ctx, getString(R.string.UNKNOWN_ERROR));
                                CommonMethod.showToastMessage(ctx, t.getMessage().toString());

                            }
                            t.printStackTrace();
                        }
                    });
                }
            }
        });
    }


}
