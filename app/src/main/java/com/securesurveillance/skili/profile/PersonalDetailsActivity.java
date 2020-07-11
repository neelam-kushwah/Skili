package com.securesurveillance.skili.profile;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.CardView;
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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.securesurveillance.skili.apiinterfaces.UpdateSocialMediaController;
import com.securesurveillance.skili.utility.ExpandableRecyclerView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.securesurveillance.skili.MyApplication;
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.SkilliSignupActivity;
import com.securesurveillance.skili.adapter.CloudAdapter;
import com.securesurveillance.skili.adapter.PostsGridViewAdapter;
import com.securesurveillance.skili.apiHandler.API;
import com.securesurveillance.skili.apiHandler.APIResponseListener;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.GetRetrofit;
import com.securesurveillance.skili.apiinterfaces.DeleteEducationController;
import com.securesurveillance.skili.apiinterfaces.DeleteExperienceController;
import com.securesurveillance.skili.apiinterfaces.EducationUpdateController;
import com.securesurveillance.skili.apiinterfaces.GetAllDetailsProfileController;
import com.securesurveillance.skili.apiinterfaces.GetCategoryController;
import com.securesurveillance.skili.apiinterfaces.GetProfileSkillsController;
import com.securesurveillance.skili.apiinterfaces.SkillsUpdateController;
import com.securesurveillance.skili.apiinterfaces.UpdateEducationController;
import com.securesurveillance.skili.apiinterfaces.UpdateExperienceController;
import com.securesurveillance.skili.apiinterfaces.UpdatePersonalDetailsController;
import com.securesurveillance.skili.materialspinner.MaterialSpinner;
import com.securesurveillance.skili.materialspinner.MaterialSpinnerMultiple;
import com.securesurveillance.skili.model.SkillModel;
import com.securesurveillance.skili.model.requestmodel.PersonalInfoUpdateModel;
import com.securesurveillance.skili.model.requestmodel.SkilliSignupRequestModel;
import com.securesurveillance.skili.model.requestmodel.UpdateEducationRequestModel;
import com.securesurveillance.skili.model.requestmodel.UpdateExperienceRequestModel;
import com.securesurveillance.skili.model.requestmodel.UpdateSkillsRequestModel;
import com.securesurveillance.skili.model.responsemodel.GetAllProfileDetailsModel;
import com.securesurveillance.skili.model.responsemodel.GetCategoryModel;
import com.securesurveillance.skili.model.responsemodel.GetSkillResponsEModel;
import com.securesurveillance.skili.model.responsemodel.LoginModel;
import com.securesurveillance.skili.model.responsemodel.UpdateEducationResponseModel;
import com.securesurveillance.skili.model.responsemodel.UpdateExperienceResponseModel;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.SharePreferanceWrapperSingleton;
import com.securesurveillance.skili.utility.SweetAlertController;
import com.securesurveillance.skili.utility.SwipeDetector;

import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

public class PersonalDetailsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, CloudAdapter.ItemDelete {
    ImageView iv_LeftOption, ivUpdateBasicDetails;
    TextView tv_Title, ivUpdateEducation, ivUpdateExperience, ivUpdateSkills, ivSocialMedia;
    Context ctx;
    MyApplication application;
    LinearLayout llCardViewEducation, llCardViewExperience;
    private SharePreferanceWrapperSingleton objSPS;
    String PF_FULL_NAME, PF_FIRST_NAME, PF_LAST_NAME, PF_CITY, PF_DOB, PF_EMAIL, PF_MOBILE, PF_WebURL,
            PF_CHARGE, PF_CHARGETYPE, PF_COMPANY, PF_GSTN, PF_FBURL, PF_INSTAURL, PF_TWITTERURL, PID;
    Boolean PF_SHOWMOBILE;
    ArrayList<GetAllProfileDetailsModel.Education> PF_EDUCATION;
    ArrayList<GetAllProfileDetailsModel.Experience> PF_EXPERIENCE;
    TextView tvFullName, tvEmail, tvMobile, tvdob, tvdobLabel, tvWebsite, tvAddress, tvCity, tvGSTN, tvGSTNLabel, tvOrg, tvOrgLabel;
    int index = 0;
    int clickedDateIndex = 0;
    ScrollView scrollView;
    Button btnSubmit;
    double latitude = 0.0;
    double longitude = 0.0;
    String address = "";
    String city = "";
    List<SkillModel> arrCategory = new ArrayList<>();
    List<String> arrMainCategoryName = new ArrayList<>();
    ArrayList<String> arrsubCategoryName = new ArrayList<>();
    HashMap<Integer, ArrayList<Integer>> arrSelectedCategory = new HashMap<>();
    HashMap<String, ArrayList<String>> arrayListHashMap = new HashMap<>();
    HashMap<String, Integer> arrayListCBHashMap = new HashMap<>();
    int selectedCategoryPosition = 0;
    boolean isSkillUpdated = false;
    private static final String LOG_TAG = "EmpuApp";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    //------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyDPFjRVRPEL0Mhqwd5NrJzi5FzmjtFOONg";
    ArrayList<UpdateEducationRequestModel.Education> arrEducation = new ArrayList<>();
    UpdateEducationRequestModel.Education educationModel;
    ArrayList<UpdateExperienceRequestModel.Experience> arrExperience = new ArrayList<>();
    UpdateExperienceRequestModel.Experience experienceModel;
    ExpandableRecyclerView recyclerView;
    TextView tvEmailLabel, tvWebsiteLabel, tvPayLabel, tvPay, tvFbUrl, tvInstaUrl, tvTwitterUrl, tvTwitterUrlLabel, tvInstaUrlLabel,
            tvFbUrlLabel;
    CardView cvSocial;

    RelativeLayout rlEducation, rlExperience;
    TextView tvMobileLabel, tvAddressLabel;
    LinearLayout llDob;

    @Override
    public void onBackPressed() {
        //   super.onBackPressed();
        if (!getIntent().hasExtra("ISNOTME") && isSkillUpdated) {
            callSkillsUpdateApi();
        } else {
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skilli_profile_details);
        ctx = this;
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(ctx);
        application = (MyApplication) getApplicationContext();
        iv_LeftOption = (ImageView) findViewById(R.id.iv_LeftOption);
        ivUpdateBasicDetails = (ImageView) findViewById(R.id.ivUpdateBasicDetails);
        ivUpdateEducation = (TextView) findViewById(R.id.ivUpdateEducation);
        ivUpdateExperience = (TextView) findViewById(R.id.ivUpdateExperience);
        ivUpdateSkills = (TextView) findViewById(R.id.ivUpdateSkills);
        ivSocialMedia = (TextView) findViewById(R.id.ivSocialMedia);
        llDob = (LinearLayout) findViewById(R.id.llDob);
        llCardViewEducation = (LinearLayout) findViewById(R.id.llCardViewEducation);
        rlEducation = (RelativeLayout) findViewById(R.id.rlEducation);
        rlExperience = (RelativeLayout) findViewById(R.id.rlExperience);
        llCardViewExperience = (LinearLayout) findViewById(R.id.llCardViewExperience);
        recyclerView = (ExpandableRecyclerView) findViewById(R.id.recyclerView);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        cvSocial = (CardView) findViewById(R.id.cvSocial);
        tvInstaUrl = (TextView) findViewById(R.id.tvInstaUrl);
        tvInstaUrlLabel = (TextView) findViewById(R.id.tvInstaUrlLabel);
        tvFbUrl = (TextView) findViewById(R.id.tvFbUrl);
        tvFbUrlLabel = (TextView) findViewById(R.id.tvFbUrlLabel);
        tvTwitterUrl = (TextView) findViewById(R.id.tvTwitterUrl);
        tvTwitterUrlLabel = (TextView) findViewById(R.id.tvTwitterUrlLabel);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getIntent().hasExtra("ISNOTME") && isSkillUpdated) {
                    callSkillsUpdateApi();
                } else {
                    finish();
                }
            }
        });
        if (getIntent().hasExtra("ISNOTME")) {
            btnSubmit.setVisibility(View.GONE);
        }
        tv_Title = (TextView) findViewById(R.id.tv_Title);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        tvFullName = (TextView) findViewById(R.id.tvFullName);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvEmailLabel = (TextView) findViewById(R.id.tvEmailLabel);
        tvMobile = (TextView) findViewById(R.id.tvMobile);
        tvMobileLabel = (TextView) findViewById(R.id.tvMobileLabel);
        tvdob = (TextView) findViewById(R.id.tvdob);
        tvdobLabel = (TextView) findViewById(R.id.tvdobLabel);
        tvWebsite = (TextView) findViewById(R.id.tvWebsite);
        tvWebsiteLabel = (TextView) findViewById(R.id.tvWebsiteLabel);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvAddressLabel = (TextView) findViewById(R.id.tvAddressLabel);


        tvCity = (TextView) findViewById(R.id.tvCity);
        tvOrg = (TextView) findViewById(R.id.tvOrg);
        tvOrgLabel = (TextView) findViewById(R.id.tvOrgLabel);
        tvGSTN = (TextView) findViewById(R.id.tvGSTN);
        tvGSTNLabel = (TextView) findViewById(R.id.tvGSTNLabel);

        tvPayLabel = (TextView) findViewById(R.id.tvPayLabel);
        tvPay = (TextView) findViewById(R.id.tvPay);
        if (getIntent().hasExtra("ISNOTME")) {
            ivUpdateBasicDetails.setVisibility(View.GONE);
            ivUpdateEducation.setVisibility(View.GONE);
            ivUpdateExperience.setVisibility(View.GONE);
            ivUpdateSkills.setVisibility(View.GONE);
            ivSocialMedia.setVisibility(View.GONE);

        }
        tv_Title.setText("Basic Details");
        iv_LeftOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  finish();
                if (!getIntent().hasExtra("ISNOTME") && isSkillUpdated) {
                    callSkillsUpdateApi();
                } else {
                    finish();
                }
            }
        });
        if (!getIntent().hasExtra("ISNOTME")
                && !objSPS.getValueFromShared_Pref(Constants.ROLE).equalsIgnoreCase("Individual")) {

            // recruiter and me
            rlEducation.setVisibility(View.GONE);
            llCardViewEducation.setVisibility(View.GONE);
            llCardViewExperience.setVisibility(View.GONE);
            rlExperience.setVisibility(View.GONE);
            tvPay.setVisibility(View.GONE);
            tvPayLabel.setVisibility(View.GONE);
            llDob.setVisibility(View.GONE);

        } else if (getIntent().hasExtra("ISNOTME") &&
                objSPS.getValueFromShared_Pref(Constants.ROLE).equalsIgnoreCase("Individual")) {


            // individual and someoneelse
            rlEducation.setVisibility(View.GONE);
            llCardViewEducation.setVisibility(View.GONE);
            llCardViewExperience.setVisibility(View.GONE);
            rlExperience.setVisibility(View.GONE);
            tvPay.setVisibility(View.GONE);
            tvPayLabel.setVisibility(View.GONE);
            llDob.setVisibility(View.GONE);

        }

        getBundleData();
        ivUpdateEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEducation();
            }
        });
        ivUpdateBasicDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPersonalDetailsDialog();
            }
        });
        ivUpdateExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openExperience();
            }
        });
        ivUpdateSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callCategoryApi();

            }
        });

        ivSocialMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSocialMediaUrlDialog();

            }
        });

        getskills();
    }


    private void getskills() {
        GetProfileSkillsController anInterface = GetRetrofit.getInstance().create(GetProfileSkillsController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        String num = objSPS.getValueFromShared_Pref(Constants.MOBILE);
        if (!PID.isEmpty()) {
            num = PID;
        }
        Call<GetSkillResponsEModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN), num, objSPS.getValueFromShared_Pref(Constants.ID));

        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    if (response.code() == 200) {
                        if (response.body() instanceof GetSkillResponsEModel) {
                            GetSkillResponsEModel model = (GetSkillResponsEModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (model.isStatus()) {
                                ArrayList<String> arrSubName = new ArrayList<>();
                                for (int i = 0; i < model.getResult().size(); i++) {
                                    arrSubName = new ArrayList<>();
                                    for (int j = 0; j < model.getResult().get(i).getSubcategory().size(); j++) {
                                        arrSubName.add(model.getResult().get(i).getSubcategory().get(j));
                                    }
                                    int cbValue = 2;
                                    if (model.getResult().get(i).isAvailabilityPartTime() && model.getResult().get(i).isAvailibilityFullTime()) {
                                        cbValue = 2; // for both
                                    } else if (model.getResult().get(i).isAvailibilityFullTime()) {
                                        cbValue = 1;// 1 for fulltime
                                    } else {
                                        cbValue = 0;// 0 for part time
                                    }
                                    arrayListHashMap.put(model.getResult().get(i).getCategory(), arrSubName);
                                    arrayListCBHashMap.put(model.getResult().get(i).getCategory(), cbValue);

                                }
                                CloudAdapter adapter = new CloudAdapter(ctx, arrayListHashMap, getIntent().hasExtra("ISNOTME"));
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
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
                                arrMainCategoryName = new ArrayList<>();
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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(PersonalDetailsActivity.this, R.color.dialog_transparent)));
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
                    isSkillUpdated = true;
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
                    CloudAdapter adapter = new CloudAdapter(ctx, arrayListHashMap, getIntent().hasExtra("ISNOTME"));
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
                if (arrSelectedSubCategory.size() > 0) {
                    arrSelectedCategory.put(selectedCategoryPosition, arrSelectedSubCategory);
                } else {
                    arrSelectedCategory.remove(selectedCategoryPosition);
                }


            }
        });

        dialog.show();

    }

    public void openSocialMediaUrlDialog() {
        final AppCompatDialog dialog = new AppCompatDialog
                (ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.dialog_socialmedia);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(ctx, R.color.dialog_transparent)));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.CENTER);
        final EditText etFbUrl = (EditText) dialog.findViewById(R.id.etFbUrl);
        final EditText etInstagramUrl = (EditText) dialog.findViewById(R.id.etInstagramUrl);
        final EditText etTwitterUrl = (EditText) dialog.findViewById(R.id.etTwitterUrl);

        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button btnDone = (Button) dialog.findViewById(R.id.btnDone);

        etFbUrl.setText(PF_FBURL);
        etInstagramUrl.setText(PF_INSTAURL);
        etTwitterUrl.setText(PF_TWITTERURL);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(etFbUrl.getText().toString().trim()) &&
                        TextUtils.isEmpty(etTwitterUrl.getText().toString().trim()) &&
                        TextUtils.isEmpty(etInstagramUrl.getText().toString().trim()) && TextUtils.isEmpty(PF_INSTAURL) &&
                        TextUtils.isEmpty(PF_TWITTERURL) && TextUtils.isEmpty(PF_FBURL)) {
                    CommonMethod.showToastMessage(ctx, "Please enter details");

                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {

                        jsonObject.put("_id", objSPS.getValueFromShared_Pref(Constants.ID));

                        jsonObject.put("facebookLink", etFbUrl.getText().toString().trim());
                        jsonObject.put("instagramLink", etInstagramUrl.getText().toString().trim());
                        jsonObject.put("linkedInLink", etTwitterUrl.getText().toString().trim());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    callUpdateSocialMediaUrls(dialog, jsonObject);

                }
            }
        });
        dialog.show();
    }

    public void openPersonalDetailsDialog() {
        final AppCompatDialog dialog = new AppCompatDialog
                (ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.dialog_personaldetails);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(ctx, R.color.dialog_transparent)));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.CENTER);
        final EditText etFirstName = (EditText) dialog.findViewById(R.id.etFirstName);
        final EditText etLastName = (EditText) dialog.findViewById(R.id.etLastName);
        final EditText etEmail = (EditText) dialog.findViewById(R.id.etEmail);
        final EditText etPhone = (EditText) dialog.findViewById(R.id.etPhone);
        final EditText etDOB = (EditText) dialog.findViewById(R.id.etDOB);
        final EditText etWebUrl = (EditText) dialog.findViewById(R.id.etWebUrl);
        final EditText etOrganisation = (EditText) dialog.findViewById(R.id.etOrganisation);
        final EditText etGSTN = (EditText) dialog.findViewById(R.id.etGSTN);
        final TextView textMin = (TextView) dialog.findViewById(R.id.textMin1);
        final TextView textMax = (TextView) dialog.findViewById(R.id.textMax1);
        final TextView tvPayScaleLabel = (TextView) dialog.findViewById(R.id.tvPayScaleLabel);
        final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.rg);
        final RelativeLayout tvPayMinMax = (RelativeLayout) dialog.findViewById(R.id.tvPayMinMax);

        AutoCompleteTextView autocomplete = (AutoCompleteTextView) dialog.findViewById(R.id.autocomplete);
        final CrystalSeekbar seekBar = (CrystalSeekbar) dialog.findViewById(R.id.seekBar);
        if (!getIntent().hasExtra("ISNOTME") && !objSPS.getValueFromShared_Pref(Constants.ROLE).equalsIgnoreCase("Individual")) {
            //recruiter and me
            etDOB.setVisibility(View.GONE);
            tvPayMinMax.setVisibility(View.GONE);
            tvPayMinMax.setVisibility(View.GONE);
            tvPayScaleLabel.setVisibility(View.GONE);
            rg.setVisibility(View.GONE);
            seekBar.setVisibility(View.GONE);

        }
        final AppCompatRadioButton rbHourly = (AppCompatRadioButton) dialog.findViewById(R.id.rbHourly);
        final AppCompatRadioButton rbMonthly = (AppCompatRadioButton) dialog.findViewById(R.id.rbMonthly);
        final AppCompatRadioButton rbYearly = (AppCompatRadioButton) dialog.findViewById(R.id.rbYearly);
        if (!objSPS.getValueFromShared_Pref(Constants.ROLE).equalsIgnoreCase("Individual")) {
            etOrganisation.setVisibility(View.VISIBLE);
            etGSTN.setVisibility(View.VISIBLE);
        }
        rbHourly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    seekBar.setMinValue(100);
                    textMin.setText("" + 100);
                    seekBar.setMaxValue(2000);
                    seekBar.setSteps(100);

                    textMax.setText("" + 2000);
                }
            }
        });
        rbMonthly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    seekBar.setMinValue(10000);
                    textMin.setText("" + 10000);
                    seekBar.setSteps(1000);

                    seekBar.setMaxValue(70000);
                    textMax.setText("" + 70000);

                }
            }
        });
        rbYearly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    seekBar.setMinValue(100000);
                    textMin.setText("" + 100000);
                    seekBar.setSteps(10000);

                    seekBar.setMaxValue(1000000);
                    textMax.setText("" + 1000000);

                }
            }
        });
        seekBar.setMinValue(100);
        textMin.setText("" + 100);
        seekBar.setMaxValue(2000);
        textMax.setText("" + 2000);


        // set listener
        seekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue) {
                textMin.setText(String.valueOf(minValue));
            }
        });


        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button btnDone = (Button) dialog.findViewById(R.id.btnDone);

        etFirstName.setText(PF_FIRST_NAME);
        etLastName.setText(PF_LAST_NAME);
        etEmail.setText(PF_EMAIL);
        etPhone.setText(PF_MOBILE);
        etWebUrl.setText(PF_WebURL);
        etDOB.setText(PF_DOB);
        autocomplete.setText(address);
        if (!TextUtils.isEmpty(PF_COMPANY)) {
            etOrganisation.setVisibility(View.VISIBLE);
            etOrganisation.setText(PF_COMPANY);
        }
        if (!TextUtils.isEmpty(PF_GSTN)) {
            etGSTN.setVisibility(View.VISIBLE);
            etGSTN.setText(PF_GSTN);
        }
        if (!TextUtils.isEmpty(PF_CHARGETYPE)) {
            if (PF_CHARGETYPE.equalsIgnoreCase("YEARLY")) {
                rbYearly.setChecked(true);

            } else if (PF_CHARGETYPE.equalsIgnoreCase("MONTHLY")) {
                rbMonthly.setChecked(true);

            } else {
                rbHourly.setChecked(true);

            }

        }
        if (!TextUtils.isEmpty(PF_CHARGE)) {
            seekBar.setMinStartValue(Float.parseFloat(PF_CHARGE));
            seekBar.apply();
        }

        final Calendar myCalendar = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                etDOB.setText(sdf.format(myCalendar.getTime()));
            }

        };
        etDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ctx, dateSetListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone;
                if (TextUtils.isEmpty(etPhone.getText())) {
                    phone = "";
                } else {
                    phone = etPhone.getText().toString().trim();

                }
                if (phone.length() == 12 && phone.startsWith("91")) {
                    phone = phone.substring(2);
                }

                if (TextUtils.isEmpty(etFirstName.getText().toString().trim())) {
                    CommonMethod.showToastMessage(ctx, "Please enter first name");
                } else if (TextUtils.isEmpty(etLastName.getText().toString().trim())) {
                    CommonMethod.showToastMessage(ctx, "Please enter last name");
                } else if (phone.length() != 10) {
                    CommonMethod.showToastMessage(ctx, "Please enter mobile no");
                } else if (TextUtils.isEmpty(etDOB.getText().toString().trim())&&objSPS.getValueFromShared_Pref(Constants.ROLE).equalsIgnoreCase("Individual")) {
                    CommonMethod.showToastMessage(ctx, "Please enter your date of birth");
                } else if (seekBar.getVisibility() == View.VISIBLE && seekBar.getSelectedMinValue().intValue() == 0) {
                    CommonMethod.showToastMessage(ctx, "Max pay scale value should be greater than 0");

                } else {
                    //call api
                    PersonalInfoUpdateModel model = new PersonalInfoUpdateModel();
                    model.setDob(etDOB.getText().toString().trim());
                    model.setEmail(CommonMethod.getFirstCapName(etEmail.getText().toString().trim()));
                    model.setFirstName(CommonMethod.getFirstCapName(etFirstName.getText().toString().trim()));
                    model.setLastName(CommonMethod.getFirstCapName(etLastName.getText().toString().trim()));
                    model.setWebsite(etWebUrl.getText().toString().trim());
                    model.setLocationString(address);
                    model.setLatitude("" + latitude);
                    model.setLongitude("" + longitude);
                    model.setCity(city);
                    if (!objSPS.getValueFromShared_Pref(Constants.ROLE).equalsIgnoreCase("Individual")) {
                        model.setCompanyName(CommonMethod.getFirstCapName(etOrganisation.getText().toString().trim()));
                        model.setGstin(CommonMethod.getFirstCapName(etGSTN.getText().toString().trim()));
                    }

                    model.setCharge(seekBar.getSelectedMinValue().toString());

                    if (rbYearly.isChecked()) {
                        model.setChargeType("YEARLY");
                    } else if (rbMonthly.isChecked()) {
                        model.setChargeType("MONTHLY");

                    } else {
                        model.setChargeType("HOURLY");
                    }

                    callPersonalDetailsApi(dialog, model);
                }
            }
        });
        Drawable drawable = getResources().getDrawable(R.drawable.geoicon);
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * 0.6),
                (int) (drawable.getIntrinsicHeight() * 0.6));
        ScaleDrawable sd = new ScaleDrawable(drawable, 0, 40, 40);
        autocomplete.setCompoundDrawables(null, null, sd.getDrawable(), null);
        autocomplete.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.autocomplete_list_item));
        autocomplete.setOnItemClickListener(this);
        dialog.show();
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
            city = address.get(0).getSubAdminArea();
            if (city == null) {
                city = address.get(0).getLocality();
            }
            if (city == null) {
                city = "";
            }
            latitude = location.getLatitude();
            longitude = location.getLongitude();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        latitude = 0.0;
        longitude = 0.0;
        address = str;
        getLocationFromAddress(str);
//        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
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

    public void openEducation() {
        index = 0;
        final Dialog dialog = new Dialog(ctx, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.dialog_education);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(ctx, R.color.dialog_transparent)));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.CENTER);

        Button btnAddRow = (Button) dialog.findViewById(R.id.btnAddRow);
        final Button btnClearRow = (Button) dialog.findViewById(R.id.btnClearRow);
        final EditText etInstitute = (EditText) dialog.findViewById(R.id.etInstitute);
        final EditText etCourse = (EditText) dialog.findViewById(R.id.etCourse);
        final EditText etSpecialization = (EditText) dialog.findViewById(R.id.etSpecialization);
        final EditText etStartDate = (EditText) dialog.findViewById(R.id.etStartDate);
        final EditText etEndDate = (EditText) dialog.findViewById(R.id.etEndDate);
        etStartDate.setTag(0);
        etEndDate.setTag(0);
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy", Locale.US);


        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                final Calendar myCalendar = Calendar.getInstance();

                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR) - 30;
                int month = c.get(Calendar.MONTH);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.YEAR, year);
                MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                        .getInstance(myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.YEAR),
                                c.getTimeInMillis(), myCalendar.getTimeInMillis());


                dialogFragment.show(getSupportFragmentManager(), null);
                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int monthOfYear) {

                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.YEAR, year);

                        etEndDate.setText(sdf.format(myCalendar.getTime()));

                    }
                });

            }

        });
        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar myCalendar = Calendar.getInstance();

                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR) - 30;
                int month = c.get(Calendar.MONTH);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.YEAR, year);
                MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                        .getInstance(myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.YEAR),
                                c.getTimeInMillis(), myCalendar.getTimeInMillis());


                dialogFragment.show(getSupportFragmentManager(), null);
                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int monthOfYear) {
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.YEAR, year);
                        etStartDate.setText(sdf.format(myCalendar.getTime()));

                    }
                });

            }
        });

        final LinearLayout llCVEducation = (LinearLayout) dialog.findViewById(R.id.llCVEducation);
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button btnDone = (Button) dialog.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrEducation.clear();
                if (index == 0) {
                    if (validMainData(etInstitute, etCourse, etSpecialization, etStartDate, etEndDate)) {

                        //callApi
                        educationModel = new UpdateEducationRequestModel().new Education();
                        educationModel.setCourse(CommonMethod.getFirstCapName(etCourse.getText().toString().trim()));
                        educationModel.setSpecialization(CommonMethod.getFirstCapName(etSpecialization.getText().toString().trim()));
                        educationModel.setInstitute(CommonMethod.getFirstCapName(etInstitute.getText().toString().trim()));
                        educationModel.setEnrollmentYear(etStartDate.getText().toString().trim());
                        educationModel.setPassingOutYear(etEndDate.getText().toString().trim());
                        arrEducation.add(educationModel);

                        callUpdateEducationAPi(dialog);
//                        Toast.makeText(ctx, "" + arrEducation.size(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    LinearLayout ll = (LinearLayout) llCVEducation.getChildAt(index);
                    EditText etI = ll.findViewById(R.id.etInstitute);
                    EditText etCourse = ll.findViewById(R.id.etCourse);
                    EditText etSpecialization = ll.findViewById(R.id.etSpecialization);
                    EditText etStartDate = ll.findViewById(R.id.etStartDate);
                    EditText etEndDate = ll.findViewById(R.id.etEndDate);

                    if (validMainData(etI,
                            etCourse,
                            etSpecialization, etStartDate, etEndDate)) {

                        for (int i = 0; i < llCVEducation.getChildCount(); i++) {
                            LinearLayout llInner = (LinearLayout) llCVEducation.getChildAt(i);
                            EditText etIInner = llInner.findViewById(R.id.etInstitute);
                            EditText etCourseInner = llInner.findViewById(R.id.etCourse);
                            EditText etSpecializationInner = llInner.findViewById(R.id.etSpecialization);
                            EditText etStartDateInner = llInner.findViewById(R.id.etStartDate);
                            EditText etEndDateInner = llInner.findViewById(R.id.etEndDate);
                            educationModel = new UpdateEducationRequestModel().new Education();
                            educationModel.setCourse(CommonMethod.getFirstCapName(etCourseInner.getText().toString().trim()));
                            educationModel.setSpecialization(CommonMethod.getFirstCapName(etSpecializationInner.getText().toString().trim()));
                            educationModel.setInstitute(CommonMethod.getFirstCapName(etIInner.getText().toString().trim()));
                            educationModel.setEnrollmentYear(etStartDateInner.getText().toString().trim());
                            educationModel.setPassingOutYear(etEndDateInner.getText().toString().trim());
                            arrEducation.add(educationModel);

                        }
                        callUpdateEducationAPi(dialog);

//                        Toast.makeText(ctx, "" + arrEducation.size(), Toast.LENGTH_LONG).show();
                    }
                }
            }


        });
        btnAddRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validMainData(etInstitute, etCourse, etSpecialization, etStartDate, etEndDate)) {
                    disableAllFieldMain(etInstitute, etCourse, etSpecialization, etStartDate, etEndDate);
                    if (llCVEducation.getChildCount() == 1) {
                        addRowUi(llCVEducation);
                    } else {
                        LinearLayout ll = (LinearLayout) llCVEducation.getChildAt(llCVEducation.getChildCount() - 1);
                        EditText etC = ll.findViewById(R.id.etCourse);
                        EditText etI = ll.findViewById(R.id.etInstitute);
                        EditText etS = ll.findViewById(R.id.etSpecialization);
                        EditText etSD = ll.findViewById(R.id.etStartDate);
                        EditText etED = ll.findViewById(R.id.etEndDate);
                        if (validMainData(etI, etC, etS, etSD, etED)) {
                            disableAllFieldMain(etI, etC, etS, etSD, etED);

                            addRowUi(llCVEducation);
                        }


                    }
                }
            }
        });
        btnClearRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index != 0) {
                    llCVEducation.removeViewAt(index);


                    index = index - 1;
                    if (index == 0) {
                        enableAllFieldMain(etInstitute, etCourse, etSpecialization, etStartDate, etEndDate);
                    } else {
                        LinearLayout ll = (LinearLayout) llCVEducation.getChildAt(llCVEducation.getChildCount() - 1);
                        EditText etC = ll.findViewById(R.id.etCourse);
                        EditText etI = ll.findViewById(R.id.etInstitute);
                        EditText etS = ll.findViewById(R.id.etSpecialization);
                        EditText etSD = ll.findViewById(R.id.etStartDate);
                        EditText etED = ll.findViewById(R.id.etEndDate);
                        enableAllFieldMain(etI, etC, etS, etSD, etED);

                    }
                } else {
                    enableAllFieldMain(etInstitute, etCourse, etSpecialization, etStartDate, etEndDate);
                }
            }
        });

        dialog.show();

    }

    private void callUpdateSocialMediaUrls(final Dialog dialogMain, final JSONObject jsonObject) {
        UpdateSocialMediaController anInterface = GetRetrofit.getInstance().create(UpdateSocialMediaController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));


        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        Call<LoginModel> call = anInterface.getResponse(objSPS.getValueFromShared_Pref(Constants.TOKEN),
                body);
        API.callRun
                (ctx, true, call, new APIResponseListener() {
                    @Override
                    public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                        if (!isFinishing()) {
                            if (response.code() == 200) {
                                CommonMethod.printAPIResponse(response.body());
                                if (response.body() instanceof LoginModel) {
                                    LoginModel model = (LoginModel) response.body();
                                    CommonMethod.cancelDialog(dialog, ctx);

                                    if (model.isStatus()) {
                                        CommonMethod.showToastMessage(ctx, model.getMessage());
                                        cvSocial.setVisibility(View.VISIBLE);
                                        ivSocialMedia.setText("Edit");
                                        dialogMain.dismiss();


                                        try {
                                            if (TextUtils.isEmpty(jsonObject.getString("facebookLink"))) {
                                                jsonObject.put("facebookLink", "");
                                            }
                                            tvFbUrl.setVisibility(View.VISIBLE);
                                            tvFbUrlLabel.setVisibility(View.VISIBLE);
                                            tvFbUrl.setText(jsonObject.getString("facebookLink"));
                                            PF_FBURL = jsonObject.getString("facebookLink");

                                            if (TextUtils.isEmpty(jsonObject.getString("instagramLink"))) {

                                                jsonObject.put("instagramLink", "");

                                            }
                                            tvInstaUrl.setVisibility(View.VISIBLE);
                                            tvInstaUrlLabel.setVisibility(View.VISIBLE);
                                            tvInstaUrl.setText(jsonObject.getString("instagramLink"));
                                            PF_INSTAURL = jsonObject.getString("instagramLink");

                                            if (TextUtils.isEmpty(jsonObject.getString("linkedInLink"))) {
                                                jsonObject.put("linkedInLink", "");

                                            }
                                            tvTwitterUrl.setVisibility(View.VISIBLE);
                                            tvTwitterUrlLabel.setVisibility(View.VISIBLE);
                                            tvTwitterUrl.setText(jsonObject.getString("linkedInLink"));
                                            PF_TWITTERURL = jsonObject.getString("linkedInLink");

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
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

    private void callUpdateExperienceAPi(final Dialog dialogMain) {
        UpdateExperienceController anInterface = GetRetrofit.getInstance().create(UpdateExperienceController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));

        UpdateExperienceRequestModel model = new UpdateExperienceRequestModel();
        model.set_id(objSPS.getValueFromShared_Pref(Constants.ID));
        model.setExperience(arrExperience);
        Gson gson = new Gson();

        String json = gson.toJson(model);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Call<UpdateExperienceResponseModel> call = anInterface.getResponse(objSPS.getValueFromShared_Pref(Constants.TOKEN),
                body);
        API.callRun
                (ctx, true, call, new APIResponseListener() {
                    @Override
                    public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                        if (!isFinishing()) {
                            if (response.code() == 200) {
                                CommonMethod.printAPIResponse(response.body());
                                if (response.body() instanceof UpdateExperienceResponseModel) {
                                    UpdateExperienceResponseModel model = (UpdateExperienceResponseModel) response.body();
                                    CommonMethod.cancelDialog(dialog, ctx);
                                    if (model.isStatus()) {
                                        CommonMethod.showToastMessage(ctx, model.getMessage());
                                        dialogMain.dismiss();
                                        llCardViewExperience.removeAllViews();
                                        PF_EXPERIENCE.clear();
                                        for (int i = 0; i < model.getResult().size(); i++) {
                                            GetAllProfileDetailsModel.Experience modelExp = new GetAllProfileDetailsModel().new Experience();

                                            modelExp.setEmployerName(model.getResult().get(i).getEmployerName());
                                            modelExp.setId(model.getResult().get(i).getId());
                                            modelExp.setJobDescription(model.getResult().get(i).getJobDescription());
                                            modelExp.setJobEndDate(model.getResult().get(i).getJobEndDate());
                                            modelExp.setJobStartDate(model.getResult().get(i).getJobStartDate());
                                            modelExp.setProfileName(model.getResult().get(i).getProfileName());
                                            PF_EXPERIENCE.add(modelExp);
                                            View v = getLayoutInflater().inflate(R.layout.listitem_experience, null, false);
                                            TextView tvCompany = (TextView) v.findViewById(R.id.tvCompany);
                                            TextView tvDate = (TextView) v.findViewById(R.id.tvDate);
                                            TextView tvDescription = (TextView) v.findViewById(R.id.tvDescription);
                                            tvCompany.setText(model.getResult().get(i).getEmployerName());
                                            tvDate.setText(model.getResult().get(i).getProfileName() + " , " +
                                                    model.getResult().get(i).getJobStartDate() + " - " +
                                                    model.getResult().get(i).getJobEndDate());
                                            tvDescription.setText(model.getResult().get(i).getJobDescription());
                                            final RelativeLayout rlDelete = (RelativeLayout) v.findViewById(R.id.rlDelete);
                                            rlDelete.setTag(i);
                                            ImageView ivEdit = (ImageView) v.findViewById(R.id.ivEdit);
                                            if (getIntent().hasExtra("ISNOTME")) {
                                                ivEdit.setVisibility(View.GONE);
                                                rlDelete.setVisibility(View.GONE);
                                            }
                                            ivEdit.setTag(i);
                                            rlDelete.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    int pos = Integer.parseInt(view.getTag().toString());
//                                                    deleteExperience(PF_EXPERIENCE.get(pos).getId(), pos);
                                                    openDeletConfirmationDialog(false, PF_EXPERIENCE.get(pos).getId(), pos);

                                                }
                                            });
                                            ivEdit.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    int pos = Integer.parseInt(view.getTag().toString());
                                                    editExperience(pos);
                                                }
                                            });

                                            v.setTag(i);
                                            llCardViewExperience.addView(v);
                                        }


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

    private void editUpdateExperienceAPi(final Dialog dialogMain, UpdateExperienceRequestModel.Experience experienceModel) {
        UpdateExperienceController anInterface = GetRetrofit.getInstance().create(UpdateExperienceController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));

        UpdateExperienceRequestModel model = new UpdateExperienceRequestModel();
        model.set_id(objSPS.getValueFromShared_Pref(Constants.ID));
        ArrayList<UpdateExperienceRequestModel.Experience> arrExperience = new ArrayList<>();
        arrExperience.add(experienceModel);
        model.setExperience(arrExperience);
        Gson gson = new Gson();

        String json = gson.toJson(model);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Call<UpdateExperienceResponseModel> call = anInterface.getResponse(objSPS.getValueFromShared_Pref(Constants.TOKEN),
                body);
        API.callRun
                (ctx, true, call, new APIResponseListener() {
                    @Override
                    public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                        if (!isFinishing()) {
                            if (response.code() == 200) {
                                CommonMethod.printAPIResponse(response.body());
                                if (response.body() instanceof UpdateExperienceResponseModel) {
                                    UpdateExperienceResponseModel model = (UpdateExperienceResponseModel) response.body();
                                    CommonMethod.cancelDialog(dialog, ctx);
                                    if (model.isStatus()) {
                                        CommonMethod.showToastMessage(ctx, model.getMessage());
                                        dialogMain.dismiss();
                                        llCardViewExperience.removeAllViews();
                                        PF_EXPERIENCE.clear();
                                        for (int i = 0; i < model.getResult().size(); i++) {
                                            GetAllProfileDetailsModel.Experience modelExp = new GetAllProfileDetailsModel().new Experience();

                                            modelExp.setEmployerName(model.getResult().get(i).getEmployerName());
                                            modelExp.setId(model.getResult().get(i).getId());
                                            modelExp.setJobDescription(model.getResult().get(i).getJobDescription());
                                            modelExp.setJobEndDate(model.getResult().get(i).getJobEndDate());
                                            modelExp.setJobStartDate(model.getResult().get(i).getJobStartDate());
                                            modelExp.setProfileName(model.getResult().get(i).getProfileName());
                                            PF_EXPERIENCE.add(modelExp);

                                            View v = getLayoutInflater().inflate(R.layout.listitem_experience, null, false);
                                            TextView tvCompany = (TextView) v.findViewById(R.id.tvCompany);
                                            TextView tvDate = (TextView) v.findViewById(R.id.tvDate);
                                            TextView tvDescription = (TextView) v.findViewById(R.id.tvDescription);
                                            tvCompany.setText(model.getResult().get(i).getEmployerName());
                                            tvDate.setText(model.getResult().get(i).getProfileName() + " , " +
                                                    model.getResult().get(i).getJobStartDate() + " - " +
                                                    model.getResult().get(i).getJobEndDate());
                                            tvDescription.setText(model.getResult().get(i).getJobDescription());
                                            final RelativeLayout rlDelete = (RelativeLayout) v.findViewById(R.id.rlDelete);
                                            rlDelete.setTag(i);
                                            ImageView ivEdit = (ImageView) v.findViewById(R.id.ivEdit);
                                            if (getIntent().hasExtra("ISNOTME")) {
                                                ivEdit.setVisibility(View.GONE);
                                                rlDelete.setVisibility(View.GONE);
                                            }
                                            ivEdit.setTag(i);

                                            rlDelete.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    int pos = Integer.parseInt(view.getTag().toString());
//                                                    deleteExperience(PF_EXPERIENCE.get(pos).getId(), pos);
                                                    openDeletConfirmationDialog(false, PF_EXPERIENCE.get(pos).getId(), pos);

                                                }
                                            });


                                            ivEdit.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    int pos = Integer.parseInt(view.getTag().toString());
                                                    editExperience(pos);
                                                }
                                            });
//                                            new SwipeDetector(v, scrollView).setOnSwipeListener(new SwipeDetector.onSwipeEvent() {
//                                                @Override
//                                                public void SwipeEventDetected(View v, SwipeDetector.SwipeTypeEnum swipeType) {
//
//                                                    RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.rlDelete);
//
//                                                    if (swipeType == SwipeDetector.SwipeTypeEnum.RIGHT_TO_LEFT) {
//                                                        rl.setVisibility(View.VISIBLE);
//                                                        //  rlDelete.setVisibility(View.VISIBLE);
//
//
//                                                    } else {
//                                                        rl.setVisibility(View.GONE);
//
//                                                    }
//                                                }
//                                            });
                                            v.setTag(i);

                                            llCardViewExperience.addView(v);
                                        }
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

    private void openDeletConfirmationDialog(final boolean isEducation, final int educationExperienceId, final int pos) {
        String msg = "Are you sure you want to delete this experience tile?";
        if (isEducation) {
            msg = "Are you sure you want to delete this education tile?";

        }
        SweetAlertDialog pDialog = new SweetAlertDialog(ctx, SweetAlertDialog.WARNING_TYPE);
        pDialog.setCancelable(false);
        pDialog.setTitleText(getResources().getString(R.string.app_name))
                .setContentText(msg)
                .setConfirmText("YES")
                .setCancelText("NO")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();

                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        if (isEducation) {
                            deleteEducation(educationExperienceId, pos);

                        } else {
                            deleteExperience(educationExperienceId, pos);


                        }

                    }
                }).show();

    }

    private void callPersonalDetailsApi(final Dialog dialogMain, final PersonalInfoUpdateModel model) {
        UpdatePersonalDetailsController anInterface = GetRetrofit.getInstance().create(UpdatePersonalDetailsController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));

        model.set_id(objSPS.getValueFromShared_Pref(Constants.ID));
        Gson gson = new Gson();

        String json = gson.toJson(model);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Call<LoginModel> call = anInterface.getResponse(objSPS.getValueFromShared_Pref(Constants.TOKEN),
                body);
        API.callRun
                (ctx, true, call, new APIResponseListener() {
                    @Override
                    public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                        if (!isFinishing()) {
                            if (response.code() == 200) {
                                CommonMethod.printAPIResponse(response.body());
                                if (response.body() instanceof LoginModel) {
                                    LoginModel loginModel = (LoginModel) response.body();
                                    CommonMethod.cancelDialog(dialog, ctx);
                                    if (loginModel.isStatus()) {
                                        CommonMethod.showToastMessage(ctx, loginModel.getMessage());
                                        dialogMain.dismiss();
                                        PF_FIRST_NAME = model.getFirstName();
                                        PF_LAST_NAME = model.getLastName();
                                        PF_EMAIL = model.getEmail();
                                        PF_DOB = model.getDob();
                                        PF_WebURL = model.getWebsite();
                                        address = model.getLocationString();
                                        if (!TextUtils.isEmpty(model.getLatitude())) {
                                            latitude = Double.parseDouble(model.getLatitude());
                                            longitude = Double.parseDouble(model.getLongitude());
                                        }
                                        city = model.getCity();

                                        if (!objSPS.getValueFromShared_Pref(Constants.ROLE).equalsIgnoreCase("Individual")) {
                                       PF_COMPANY=model.getCompanyName();
                                       PF_GSTN=model.getGstin();
                                        }

                                        PF_CHARGE=model.getCharge();


                                        PF_CHARGETYPE=model.getChargeType();


                                        setData();



                                    } else {
                                        SweetAlertController.getInstance().showErrorDialog(ctx, SweetAlertDialog.ERROR_TYPE, loginModel.getMessage(), getString(R.string.app_name));
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

    private void callUpdateEducationAPi(final Dialog dialogMain) {
        UpdateEducationController anInterface = GetRetrofit.getInstance().create(UpdateEducationController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));

        UpdateEducationRequestModel model = new UpdateEducationRequestModel();
        model.set_id(objSPS.getValueFromShared_Pref(Constants.ID));
        model.setEducation(arrEducation);
        Gson gson = new Gson();

        String json = gson.toJson(model);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Call<UpdateEducationResponseModel> call = anInterface.getResponse(objSPS.getValueFromShared_Pref(Constants.TOKEN),
                body);
        API.callRun
                (ctx, true, call, new APIResponseListener() {
                    @Override
                    public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                        if (!isFinishing()) {
                            if (response.code() == 200) {
                                CommonMethod.printAPIResponse(response.body());
                                if (response.body() instanceof UpdateEducationResponseModel) {
                                    UpdateEducationResponseModel model = (UpdateEducationResponseModel) response.body();
                                    CommonMethod.cancelDialog(dialog, ctx);
                                    if (model.isStatus()) {
                                        CommonMethod.showToastMessage(ctx, model.getMessage());
                                        dialogMain.dismiss();
                                        llCardViewEducation.removeAllViews();
                                        PF_EDUCATION.clear();
                                        for (int i = 0; i < model.getResult().size(); i++) {
                                            GetAllProfileDetailsModel.Education modelExp = new GetAllProfileDetailsModel().new Education();

                                            modelExp.setCourse(model.getResult().get(i).getCourse());
                                            modelExp.setId(model.getResult().get(i).getId());
                                            modelExp.setEnrollmentYear(model.getResult().get(i).getEnrollmentYear());
                                            modelExp.setInstitute(model.getResult().get(i).getInstitute());
                                            modelExp.setPassingOutYear(model.getResult().get(i).getPassingOutYear());
                                            modelExp.setSpecialization(model.getResult().get(i).getSpecialization());
                                            PF_EDUCATION.add(modelExp);
                                            View v = getLayoutInflater().inflate(R.layout.listitem_education, null, false);
                                            TextView tvHeading = (TextView) v.findViewById(R.id.tvHeading);
                                            TextView tvDetail = (TextView) v.findViewById(R.id.tvDetail);
                                            RelativeLayout rlDelete = (RelativeLayout) v.findViewById(R.id.rlDelete);
                                            ImageView ivEdit = (ImageView) v.findViewById(R.id.ivEdit);
                                            if (getIntent().hasExtra("ISNOTME")) {
                                                ivEdit.setVisibility(View.GONE);
                                                rlDelete.setVisibility(View.GONE);
                                            }
                                            tvHeading.setText(model.getResult().get(i).getInstitute());
                                            String detail = model.getResult().get(i).getCourse() + " , " + model.getResult().get(i).getSpecialization() + " , " +
                                                    model.getResult().get(i).getEnrollmentYear() + " - " + model.getResult().get(i).getPassingOutYear();
                                            tvDetail.setText(detail);
                                            rlDelete.setTag(i);
                                            ivEdit.setTag(i);
                                            rlDelete.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    int pos = Integer.parseInt(view.getTag().toString());
                                                    openDeletConfirmationDialog(true, PF_EDUCATION.get(pos).getId(), pos);

                                                }
                                            });
                                            ivEdit.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    int pos = Integer.parseInt(view.getTag().toString());
                                                    editEducation(pos);
                                                }
                                            });
//                                            new SwipeDetector(v, scrollView).setOnSwipeListener(new SwipeDetector.onSwipeEvent() {
//                                                @Override
//                                                public void SwipeEventDetected(View v, SwipeDetector.SwipeTypeEnum swipeType) {
//
//                                                    RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.rlDelete);
//
//                                                    if (swipeType == SwipeDetector.SwipeTypeEnum.RIGHT_TO_LEFT) {
//                                                        rl.setVisibility(View.VISIBLE);
//                                                        //  rlDelete.setVisibility(View.VISIBLE);
//
//
//                                                    } else {
//                                                        rl.setVisibility(View.GONE);
//
//                                                    }
//                                                }
//                                            });

                                            v.setTag(i);

                                            llCardViewEducation.addView(v);
                                        }
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

    private void editEducationAPi(final Dialog dialogMain, UpdateEducationRequestModel.Education educationModel) {
        UpdateEducationController anInterface = GetRetrofit.getInstance().create(UpdateEducationController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));

        UpdateEducationRequestModel model = new UpdateEducationRequestModel();
        model.set_id(objSPS.getValueFromShared_Pref(Constants.ID));
        ArrayList<UpdateEducationRequestModel.Education> arrEducation = new ArrayList<>();
        arrEducation.add(educationModel);

        model.setEducation(arrEducation);
        Gson gson = new Gson();

        String json = gson.toJson(model);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Call<UpdateEducationResponseModel> call = anInterface.getResponse(objSPS.getValueFromShared_Pref(Constants.TOKEN),
                body);
        API.callRun
                (ctx, true, call, new APIResponseListener() {
                    @Override
                    public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                        if (!isFinishing()) {
                            if (response.code() == 200) {
                                CommonMethod.printAPIResponse(response.body());
                                if (response.body() instanceof UpdateEducationResponseModel) {
                                    UpdateEducationResponseModel model = (UpdateEducationResponseModel) response.body();
                                    CommonMethod.cancelDialog(dialog, ctx);
                                    if (model.isStatus()) {
                                        CommonMethod.showToastMessage(ctx, model.getMessage());
                                        dialogMain.dismiss();
                                        llCardViewEducation.removeAllViews();
                                        PF_EDUCATION.clear();
                                        for (int i = 0; i < model.getResult().size(); i++) {
                                            GetAllProfileDetailsModel.Education modelExp = new GetAllProfileDetailsModel().new Education();

                                            modelExp.setCourse(model.getResult().get(i).getCourse());
                                            modelExp.setId(model.getResult().get(i).getId());
                                            modelExp.setEnrollmentYear(model.getResult().get(i).getEnrollmentYear());
                                            modelExp.setInstitute(model.getResult().get(i).getInstitute());
                                            modelExp.setPassingOutYear(model.getResult().get(i).getPassingOutYear());
                                            modelExp.setSpecialization(model.getResult().get(i).getSpecialization());
                                            PF_EDUCATION.add(modelExp);
                                            View v = getLayoutInflater().inflate(R.layout.listitem_education, null, false);
                                            TextView tvHeading = (TextView) v.findViewById(R.id.tvHeading);
                                            TextView tvDetail = (TextView) v.findViewById(R.id.tvDetail);
                                            RelativeLayout rlDelete = (RelativeLayout) v.findViewById(R.id.rlDelete);
                                            ImageView ivEdit = (ImageView) v.findViewById(R.id.ivEdit);
                                            if (getIntent().hasExtra("ISNOTME")) {
                                                ivEdit.setVisibility(View.GONE);
                                                rlDelete.setVisibility(View.GONE);
                                            }
                                            tvHeading.setText(model.getResult().get(i).getInstitute());
                                            String detail = model.getResult().get(i).getCourse() + " , " + model.getResult().get(i).getSpecialization() + " , " +
                                                    model.getResult().get(i).getEnrollmentYear() + " - " + model.getResult().get(i).getPassingOutYear();
                                            tvDetail.setText(detail);
                                            rlDelete.setTag(i);
                                            ivEdit.setTag(i);
                                            rlDelete.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    int pos = Integer.parseInt(view.getTag().toString());
//                                                    deleteEducation(PF_EDUCATION.get(pos).getId(), pos);
                                                    openDeletConfirmationDialog(true, PF_EDUCATION.get(pos).getId(), pos);

                                                }
                                            });
                                            ivEdit.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    int pos = Integer.parseInt(view.getTag().toString());
                                                    editEducation(pos);
                                                }
                                            });
//                                            new SwipeDetector(v, scrollView).setOnSwipeListener(new SwipeDetector.onSwipeEvent() {
//                                                @Override
//                                                public void SwipeEventDetected(View v, SwipeDetector.SwipeTypeEnum swipeType) {
//
//                                                    RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.rlDelete);
//
//                                                    if (swipeType == SwipeDetector.SwipeTypeEnum.RIGHT_TO_LEFT) {
//                                                        rl.setVisibility(View.VISIBLE);
//                                                        //  rlDelete.setVisibility(View.VISIBLE);
//
//
//                                                    } else {
//                                                        rl.setVisibility(View.GONE);
//
//                                                    }
//                                                }
//                                            });

                                            v.setTag(i);

                                            llCardViewEducation.addView(v);
                                        }
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

    private void disableAllFieldMain(EditText etInstitute, EditText etCourse, EditText etSpecialization, EditText etStartDate, EditText etEndDate) {
        etInstitute.setEnabled(false);
        etCourse.setEnabled(false);
        etSpecialization.setEnabled(false);
        etStartDate.setEnabled(false);
        etEndDate.setEnabled(false);
    }

    private void enableAllFieldMain(EditText etInstitute, EditText etCourse, EditText etSpecialization, EditText etStartDate, EditText etEndDate) {
        etInstitute.setEnabled(true);
        etCourse.setEnabled(true);
        etSpecialization.setEnabled(true);
        etStartDate.setEnabled(true);
        etEndDate.setEnabled(true);
    }

    private void addRowUiExperience(final LinearLayout llCVExperience) {
        View v = getLayoutInflater().inflate(R.layout.dialog_listitem_experience, null, false);
        index = index + 1;
        v.setTag(index);


        final EditText etEmployerName = (EditText) v.findViewById(R.id.etEmployerName);
        etEmployerName.setTag(index);

        final EditText etDesignation = (EditText) v.findViewById(R.id.etDesignation);
        etDesignation.setTag(index);

        final EditText etJobDescription = (EditText) v.findViewById(R.id.etJobDescription);
        etJobDescription.setTag(index);

        final EditText etStartDate = (EditText) v.findViewById(R.id.etStartDate);
        etStartDate.setTag(index);

        final EditText etEndDate = (EditText) v.findViewById(R.id.etEndDate);
        etEndDate.setTag(index);

        final SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy", Locale.US);


        clickedDateIndex = 0;


        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedDateIndex = Integer.parseInt(view.getTag().toString());
                final Calendar myCalendar = Calendar.getInstance();

                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR) - 30;
                int month = c.get(Calendar.MONTH);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.YEAR, year);
                MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                        .getInstance(myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.YEAR),
                                c.getTimeInMillis(), myCalendar.getTimeInMillis());


                dialogFragment.show(getSupportFragmentManager(), null);
                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int monthOfYear) {
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.YEAR, year);
                        EditText et = llCVExperience.getChildAt(clickedDateIndex).findViewById(R.id.etEndDate);
                        et.setText(sdf.format(myCalendar.getTime()));
                    }
                });


            }
        });
        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedDateIndex = Integer.parseInt(view.getTag().toString());
                final Calendar myCalendar = Calendar.getInstance();

                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR) - 30;
                int month = c.get(Calendar.MONTH);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.YEAR, year);
                MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                        .getInstance(myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.YEAR),
                                c.getTimeInMillis(), myCalendar.getTimeInMillis());


                dialogFragment.show(getSupportFragmentManager(), null);
                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int monthOfYear) {
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.YEAR, year);
                        EditText et = llCVExperience.getChildAt(clickedDateIndex).findViewById(R.id.etStartDate);
                        et.setText(sdf.format(myCalendar.getTime()));
                    }
                });

            }
        });
        llCVExperience.addView(v);

    }

    private void addRowUi(final LinearLayout llCVEducation) {
        View v = getLayoutInflater().inflate(R.layout.dialog_listitem_education, null, false);
        index = index + 1;
        v.setTag(index);
        final EditText etInstitute = (EditText) v.findViewById(R.id.etInstitute);
        etInstitute.setTag(index);
        final EditText etCourse = (EditText) v.findViewById(R.id.etCourse);
        etCourse.setTag(index);

        final EditText etSpecialization = (EditText) v.findViewById(R.id.etSpecialization);
        etSpecialization.setTag(index);

        final EditText etStartDate = (EditText) v.findViewById(R.id.etStartDate);
        etStartDate.setTag(index);

        final EditText etEndDate = (EditText) v.findViewById(R.id.etEndDate);
        etEndDate.setTag(index);

        final SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy", Locale.US);


        clickedDateIndex = 0;


        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedDateIndex = Integer.parseInt(view.getTag().toString());
                final Calendar myCalendar = Calendar.getInstance();

                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR) - 30;
                int month = c.get(Calendar.MONTH);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.YEAR, year);
                MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                        .getInstance(myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.YEAR),
                                c.getTimeInMillis(), myCalendar.getTimeInMillis());


                dialogFragment.show(getSupportFragmentManager(), null);
                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int monthOfYear) {
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.YEAR, year);
                        EditText et = llCVEducation.getChildAt(clickedDateIndex).findViewById(R.id.etEndDate);
                        et.setText(sdf.format(myCalendar.getTime()));

                    }
                });


            }
        });
        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedDateIndex = Integer.parseInt(view.getTag().toString());
                final Calendar myCalendar = Calendar.getInstance();

                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR) - 30;
                int month = c.get(Calendar.MONTH);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.YEAR, year);
                MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                        .getInstance(myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.YEAR),
                                c.getTimeInMillis(), myCalendar.getTimeInMillis());


                dialogFragment.show(getSupportFragmentManager(), null);
                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int monthOfYear) {
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.YEAR, year);
                        EditText et = llCVEducation.getChildAt(clickedDateIndex).findViewById(R.id.etStartDate);
                        et.setText(sdf.format(myCalendar.getTime()));
                    }
                });


            }
        });
        llCVEducation.addView(v);

    }

    private void editEducation(int pos) {
        final Dialog dialog = new Dialog(ctx, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.dialog_education);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(ctx, R.color.dialog_transparent)));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.CENTER);

        LinearLayout llBottom = (LinearLayout) dialog.findViewById(R.id.llBottom);
        llBottom.setVisibility(View.GONE);
        final EditText etInstitute = (EditText) dialog.findViewById(R.id.etInstitute);
        final EditText etCourse = (EditText) dialog.findViewById(R.id.etCourse);
        final EditText etSpecialization = (EditText) dialog.findViewById(R.id.etSpecialization);
        final EditText etStartDate = (EditText) dialog.findViewById(R.id.etStartDate);
        final EditText etEndDate = (EditText) dialog.findViewById(R.id.etEndDate);

        final GetAllProfileDetailsModel.Education model = PF_EDUCATION.get(pos);
        etInstitute.setText(model.getInstitute());
        etCourse.setText(model.getCourse());
        etEndDate.setText(model.getPassingOutYear());
        etStartDate.setText(model.getEnrollmentYear());
        etSpecialization.setText(model.getSpecialization());

        final SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy", Locale.US);


        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar myCalendar = Calendar.getInstance();

                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR) - 30;
                int month = c.get(Calendar.MONTH);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.YEAR, year);
                MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                        .getInstance(myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.YEAR),
                                c.getTimeInMillis(), myCalendar.getTimeInMillis());

                dialogFragment.show(getSupportFragmentManager(), null);
                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int monthOfYear) {
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.YEAR, year);
                        etEndDate.setText(sdf.format(myCalendar.getTime()));

                    }
                });

            }

        });
        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar myCalendar = Calendar.getInstance();

                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR) - 30;
                int month = c.get(Calendar.MONTH);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.YEAR, year);
                MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                        .getInstance(myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.YEAR),
                                c.getTimeInMillis(), myCalendar.getTimeInMillis());

                dialogFragment.show(getSupportFragmentManager(), null);
                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int monthOfYear) {
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.YEAR, year);
                        etStartDate.setText(sdf.format(myCalendar.getTime()));

                    }
                });

            }
        });

        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button btnDone = (Button) dialog.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validMainData(etInstitute, etCourse, etSpecialization, etStartDate, etEndDate)) {
                    //callApi
                    educationModel = new UpdateEducationRequestModel().new Education();
                    educationModel.setCourse(CommonMethod.getFirstCapName(etCourse.getText().toString().trim()));
                    educationModel.setSpecialization(CommonMethod.getFirstCapName(etSpecialization.getText().toString().trim()));
                    educationModel.setInstitute(CommonMethod.getFirstCapName(etInstitute.getText().toString().trim()));
                    educationModel.setEnrollmentYear(etStartDate.getText().toString().trim());
                    educationModel.setPassingOutYear(etEndDate.getText().toString().trim());
                    educationModel.setId("" + model.getId());
                    editEducationAPi(dialog, educationModel);
//                    Toast.makeText(ctx, "" + arrEducation.size(), Toast.LENGTH_LONG).show();
                }

            }


        });

        dialog.show();

    }

    private boolean validMainDataExp(EditText etEmployerName, EditText etDesignation, EditText etJobDescription, EditText etStartDate, EditText etEndDate) {
        boolean isValid = true;
        if (etDesignation.getText().toString().trim().length() == 0) {
            CommonMethod.showToastMessage(ctx, "Please enter designation");
            isValid = false;
        } else if (etEmployerName.getText().toString().trim().length() == 0) {
            CommonMethod.showToastMessage(ctx, "Please enter your employer name");
            isValid = false;

        } else if (etJobDescription.getText().toString().trim().length() == 0) {
            CommonMethod.showToastMessage(ctx, "Please enter your job description");
            isValid = false;

        } else if (TextUtils.isEmpty(etStartDate.getText().toString().trim())) {

            CommonMethod.showToastMessage(ctx, "Please choose start date");
            isValid = false;


        } else if (TextUtils.isEmpty(etEndDate.getText().toString().trim())) {

            CommonMethod.showToastMessage(ctx, "Please choose end date");
            isValid = false;


        } else if (!CheckDates(etStartDate.getText().toString().trim(), etEndDate.getText().toString().trim())) {

            CommonMethod.showToastMessage(ctx, "End date should not be before start date");
            isValid = false;

        }


        return isValid;
    }

    private boolean validMainData(EditText etInstitute, EditText etCourse, EditText etSpecialization, EditText etStartDate, EditText etEndDate) {
        boolean isValid = true;
        if (etInstitute.getText().toString().trim().length() == 0) {
            CommonMethod.showToastMessage(ctx, "Please enter institute");
            isValid = false;
        } else if (etCourse.getText().toString().trim().length() == 0) {
            CommonMethod.showToastMessage(ctx, "Please enter course");
            isValid = false;

        } else if (etSpecialization.getText().toString().trim().length() == 0) {
            CommonMethod.showToastMessage(ctx, "Please enter your specialization");
            isValid = false;

        } else if (TextUtils.isEmpty(etStartDate.getText().toString().trim())) {

            CommonMethod.showToastMessage(ctx, "Please choose start date");
            isValid = false;


        } else if (TextUtils.isEmpty(etEndDate.getText().toString().trim())) {

            CommonMethod.showToastMessage(ctx, "Please choose end date");
            isValid = false;


        } else if (!CheckDates(etStartDate.getText().toString().trim(), etEndDate.getText().toString().trim())) {

            CommonMethod.showToastMessage(ctx, "End date should not be before start date");
            isValid = false;

        }


        return isValid;
    }

    public boolean CheckDates(String startDate, String endDate) {
        boolean b = false;
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy", Locale.US);

        try {
            if (sdf.parse(startDate).before(sdf.parse(endDate))) {
                b = true;//If start date is before end date
            } else if (sdf.parse(startDate).equals(sdf.parse(endDate))) {
                b = false;//If two dates are equal
            } else {
                b = false; //If start date is after the end date
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }

    public void openExperience() {
        index = 0;
        final Dialog dialog = new Dialog(ctx, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.dialog_experience);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(ctx, R.color.dialog_transparent)));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.CENTER);

        Button btnAddRow = (Button) dialog.findViewById(R.id.btnAddRow);
        final Button btnClearRow = (Button) dialog.findViewById(R.id.btnClearRow);
        final EditText etEmployerName = (EditText) dialog.findViewById(R.id.etEmployerName);
        final EditText etDesignation = (EditText) dialog.findViewById(R.id.etDesignation);
        final EditText etJobDescription = (EditText) dialog.findViewById(R.id.etJobDescription);
        final EditText etStartDate = (EditText) dialog.findViewById(R.id.etStartDate);
        final EditText etEndDate = (EditText) dialog.findViewById(R.id.etEndDate);
        etStartDate.setTag(0);
        etEndDate.setTag(0);
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy", Locale.US);


        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar myCalendar = Calendar.getInstance();

                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR) - 30;
                int month = c.get(Calendar.MONTH);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.YEAR, year);
                MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                        .getInstance(myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.YEAR),
                                c.getTimeInMillis(), myCalendar.getTimeInMillis());


                dialogFragment.show(getSupportFragmentManager(), null);
                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int monthOfYear) {
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.YEAR, year);
                        etEndDate.setText(sdf.format(myCalendar.getTime()));

                    }
                });
            }
        });
        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar myCalendar = Calendar.getInstance();

                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR) - 30;
                int month = c.get(Calendar.MONTH);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.YEAR, year);
                MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                        .getInstance(myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.YEAR),
                                c.getTimeInMillis(), myCalendar.getTimeInMillis());


                dialogFragment.show(getSupportFragmentManager(), null);
                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int monthOfYear) {
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.YEAR, year);
                        etStartDate.setText(sdf.format(myCalendar.getTime()));

                    }
                });
            }
        });

        final LinearLayout llCVExperience = (LinearLayout) dialog.findViewById(R.id.llCVExperience);
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button btnDone = (Button) dialog.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrExperience.clear();
                if (index == 0) {
                    if (validMainDataExp(etEmployerName, etDesignation, etJobDescription, etStartDate, etEndDate)) {

                        //callApi
                        experienceModel = new UpdateExperienceRequestModel().new Experience();
                        experienceModel.setProfileName(CommonMethod.getFirstCapName(etDesignation.getText().toString().trim()));
                        experienceModel.setJobDescription(CommonMethod.getFirstCapName(etJobDescription.getText().toString().trim()));
                        experienceModel.setEmployerName(CommonMethod.getFirstCapName(etEmployerName.getText().toString().trim()));
                        experienceModel.setJobStartDate(etStartDate.getText().toString().trim());
                        experienceModel.setJobEndDate(etEndDate.getText().toString().trim());
                        arrExperience.add(experienceModel);

                        callUpdateExperienceAPi(dialog);
//                        Toast.makeText(ctx, "" + arrEducation.size(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    LinearLayout ll = (LinearLayout) llCVExperience.getChildAt(index);
                    EditText etI = ll.findViewById(R.id.etEmployerName);
                    EditText etDesignation = ll.findViewById(R.id.etDesignation);
                    EditText etJobDescription = ll.findViewById(R.id.etJobDescription);
                    EditText etStartDate = ll.findViewById(R.id.etStartDate);
                    EditText etEndDate = ll.findViewById(R.id.etEndDate);

                    if (validMainDataExp(etI,
                            etDesignation,
                            etJobDescription, etStartDate, etEndDate)) {

                        for (int i = 0; i < llCVExperience.getChildCount(); i++) {
                            LinearLayout llInner = (LinearLayout) llCVExperience.getChildAt(i);
                            EditText etEmployerNameInner = llInner.findViewById(R.id.etEmployerName);
                            EditText etDesignationInner = llInner.findViewById(R.id.etDesignation);
                            EditText etJobDescriptionInner = llInner.findViewById(R.id.etJobDescription);
                            EditText etStartDateInner = llInner.findViewById(R.id.etStartDate);
                            EditText etEndDateInner = llInner.findViewById(R.id.etEndDate);
                            experienceModel = new UpdateExperienceRequestModel().new Experience();
                            experienceModel.setProfileName(CommonMethod.getFirstCapName(etDesignationInner.getText().toString().trim()));
                            experienceModel.setJobDescription(CommonMethod.getFirstCapName(etJobDescriptionInner.getText().toString().trim()));
                            experienceModel.setEmployerName(CommonMethod.getFirstCapName(etEmployerNameInner.getText().toString().trim()));
                            experienceModel.setJobStartDate(etStartDateInner.getText().toString().trim());
                            experienceModel.setJobEndDate(etEndDateInner.getText().toString().trim());
                            arrExperience.add(experienceModel);

                        }
                        callUpdateExperienceAPi(dialog);

//                        Toast.makeText(ctx, "" + arrExperience.size(), Toast.LENGTH_LONG).show();
                    }
                }
            }


        });
        btnAddRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validMainDataExp(etEmployerName, etDesignation, etJobDescription, etStartDate, etEndDate)) {
                    disableAllFieldMain(etEmployerName, etDesignation, etJobDescription, etStartDate, etEndDate);
                    if (llCVExperience.getChildCount() == 1) {
                        addRowUiExperience(llCVExperience);
                    } else {
                        LinearLayout ll = (LinearLayout) llCVExperience.getChildAt(llCVExperience.getChildCount() - 1);
                        EditText etD = ll.findViewById(R.id.etDesignation);
                        EditText etEN = ll.findViewById(R.id.etEmployerName);
                        EditText etJD = ll.findViewById(R.id.etJobDescription);
                        EditText etSD = ll.findViewById(R.id.etStartDate);
                        EditText etED = ll.findViewById(R.id.etEndDate);
                        if (validMainDataExp(etEN, etD, etJD, etSD, etED)) {
                            disableAllFieldMain(etEN, etD, etJD, etSD, etED);

                            addRowUiExperience(llCVExperience);
                        }


                    }
                }
            }
        });
        btnClearRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index != 0) {
                    llCVExperience.removeViewAt(index);


                    index = index - 1;
                    if (index == 0) {
                        enableAllFieldMain(etEmployerName, etDesignation, etJobDescription, etStartDate, etEndDate);
                    } else {
                        LinearLayout ll = (LinearLayout) llCVExperience.getChildAt(llCVExperience.getChildCount() - 1);
                        EditText etD = ll.findViewById(R.id.etDesignation);
                        EditText etEN = ll.findViewById(R.id.etEmployerName);
                        EditText etJD = ll.findViewById(R.id.etJobDescription);
                        EditText etSD = ll.findViewById(R.id.etStartDate);
                        EditText etED = ll.findViewById(R.id.etEndDate);
                        enableAllFieldMain(etEN, etD, etJD, etSD, etED);

                    }
                } else {
                    enableAllFieldMain(etEmployerName, etDesignation, etJobDescription, etStartDate, etEndDate);
                }
            }
        });

        dialog.show();

    }

    public void editExperience(int pos) {
        final Dialog dialog = new Dialog(ctx, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.dialog_experience);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(ctx, R.color.dialog_transparent)));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.CENTER);
        LinearLayout llBottom = (LinearLayout) dialog.findViewById(R.id.llBottom);
        llBottom.setVisibility(View.GONE);
        final EditText etEmployerName = (EditText) dialog.findViewById(R.id.etEmployerName);
        final EditText etDesignation = (EditText) dialog.findViewById(R.id.etDesignation);
        final EditText etJobDescription = (EditText) dialog.findViewById(R.id.etJobDescription);
        final EditText etStartDate = (EditText) dialog.findViewById(R.id.etStartDate);
        final EditText etEndDate = (EditText) dialog.findViewById(R.id.etEndDate);
        etStartDate.setTag(0);
        etEndDate.setTag(0);

        final GetAllProfileDetailsModel.Experience model = PF_EXPERIENCE.get(pos);
        etEmployerName.setText(model.getEmployerName());
        etDesignation.setText(model.getProfileName());
        etJobDescription.setText(model.getJobDescription());
        etStartDate.setText(model.getJobStartDate());
        etEndDate.setText(model.getJobEndDate());
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy", Locale.US);


        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar myCalendar = Calendar.getInstance();

                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR) - 30;
                int month = c.get(Calendar.MONTH);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.YEAR, year);
                MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                        .getInstance(myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.YEAR),
                                c.getTimeInMillis(), myCalendar.getTimeInMillis());


                dialogFragment.show(getSupportFragmentManager(), null);
                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int monthOfYear) {
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.YEAR, year);
                        etEndDate.setText(sdf.format(myCalendar.getTime()));

                    }
                });
            }
        });
        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar myCalendar = Calendar.getInstance();

                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR) - 30;
                int month = c.get(Calendar.MONTH);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.YEAR, year);
                MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                        .getInstance(myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.YEAR),
                                c.getTimeInMillis(), myCalendar.getTimeInMillis());


                dialogFragment.show(getSupportFragmentManager(), null);
                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int monthOfYear) {
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.YEAR, year);
                        etStartDate.setText(sdf.format(myCalendar.getTime()));

                    }
                });
            }
        });

        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button btnDone = (Button) dialog.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrExperience.clear();
                if (validMainDataExp(etEmployerName, etDesignation, etJobDescription, etStartDate, etEndDate)) {

                    //callApi
                    experienceModel = new UpdateExperienceRequestModel().new Experience();
                    experienceModel.setProfileName(CommonMethod.getFirstCapName(etDesignation.getText().toString().trim()));
                    experienceModel.setJobDescription(CommonMethod.getFirstCapName(etJobDescription.getText().toString().trim()));
                    experienceModel.setEmployerName(CommonMethod.getFirstCapName(etEmployerName.getText().toString().trim()));
                    experienceModel.setJobStartDate(etStartDate.getText().toString().trim());
                    experienceModel.setJobEndDate(etEndDate.getText().toString().trim());
                    experienceModel.setId("" + model.getId());
                    editUpdateExperienceAPi(dialog, experienceModel);
                }


            }


        });


        dialog.show();

    }
private void setData(){
    if (TextUtils.isEmpty(PF_FBURL)) {
        PF_FBURL = "";
        tvFbUrlLabel.setVisibility(View.GONE);
        tvFbUrl.setVisibility(View.GONE);
    } else {
        tvFbUrl.setText(PF_FBURL);
        tvFbUrlLabel.setVisibility(View.VISIBLE);
        tvFbUrl.setVisibility(View.VISIBLE);
    }

    if (TextUtils.isEmpty(PF_INSTAURL)) {
        PF_INSTAURL = "";
        tvInstaUrlLabel.setVisibility(View.GONE);
        tvInstaUrl.setVisibility(View.GONE);
    } else {
        tvInstaUrl.setText(PF_INSTAURL);
        tvInstaUrlLabel.setVisibility(View.VISIBLE);
        tvInstaUrl.setVisibility(View.VISIBLE);
    }
    if (TextUtils.isEmpty(PF_TWITTERURL)) {
        PF_TWITTERURL = "";
        tvTwitterUrlLabel.setVisibility(View.GONE);
        tvTwitterUrl.setVisibility(View.GONE);
    } else {
        tvTwitterUrl.setText(PF_TWITTERURL);
        tvTwitterUrlLabel.setVisibility(View.VISIBLE);
        tvTwitterUrl.setVisibility(View.VISIBLE);
    }
    if (TextUtils.isEmpty(PF_FBURL) && TextUtils.isEmpty(PF_INSTAURL) && TextUtils.isEmpty(PF_TWITTERURL)) {
        cvSocial.setVisibility(View.GONE);
        ivSocialMedia.setText("Add");
    } else {
        ivSocialMedia.setText("Edit");
    }


    if (TextUtils.isEmpty(PF_WebURL)) {
        PF_WebURL = "";
        tvWebsiteLabel.setVisibility(View.GONE);
        tvWebsite.setVisibility(View.GONE);
    } else {
        tvWebsite.setText(PF_WebURL);
        tvWebsiteLabel.setVisibility(View.VISIBLE);
        tvWebsite.setVisibility(View.VISIBLE);
    }
    if (TextUtils.isEmpty(PF_COMPANY)) {
        PF_COMPANY = "";
        tvOrgLabel.setVisibility(View.GONE);
        tvOrg.setVisibility(View.GONE);
    } else {
        tvOrg.setText(PF_COMPANY);
        tvOrgLabel.setVisibility(View.VISIBLE);
        tvOrg.setVisibility(View.VISIBLE);
    }
    if (TextUtils.isEmpty(PF_GSTN)) {
        PF_GSTN = "";
        tvGSTNLabel.setVisibility(View.GONE);
        tvGSTN.setVisibility(View.GONE);
    } else {
        tvGSTN.setText(PF_GSTN);
        tvGSTNLabel.setVisibility(View.VISIBLE);
        tvGSTN.setVisibility(View.VISIBLE);
    }
    if (TextUtils.isEmpty(PF_CITY)) {
        PF_CITY = "";
    }
    if (TextUtils.isEmpty(PF_DOB)) {
        PF_DOB = "";
    }
    tvFullName.setText(PF_FULL_NAME);
    tvCity.setText(PF_CITY);
    if (TextUtils.isEmpty(address)) {
        tvAddress.setVisibility(View.GONE);
        tvAddressLabel.setVisibility(View.GONE);
    } else {
        tvAddress.setText(address);
        tvAddress.setVisibility(View.VISIBLE);
        tvAddressLabel.setVisibility(View.VISIBLE);
    }
    if (TextUtils.isEmpty(PF_EMAIL)) {
        tvEmail.setVisibility(View.GONE);
        tvEmailLabel.setVisibility(View.GONE);
    } else {
        tvEmail.setText(PF_EMAIL);
        tvEmail.setVisibility(View.VISIBLE);
        tvEmailLabel.setVisibility(View.VISIBLE);
    }
    if (TextUtils.isEmpty(PF_DOB)) {
        tvdob.setVisibility(View.GONE);
        tvdobLabel.setVisibility(View.GONE);
    } else {
        tvdob.setVisibility(View.VISIBLE);
        tvdobLabel.setVisibility(View.VISIBLE);
        tvdob.setText(PF_DOB);
    }
    if (!TextUtils.isEmpty(PF_CHARGETYPE)) {
        if (PF_CHARGETYPE.equalsIgnoreCase("YEARLY")) {
            if (!TextUtils.isEmpty(PF_CHARGE)) {
                tvPay.setText(PF_CHARGE + " INR/Year");
            }
        } else if (PF_CHARGETYPE.equalsIgnoreCase("MONTHLY")) {
            if (!TextUtils.isEmpty(PF_CHARGE)) {
                tvPay.setText(PF_CHARGE + " INR/Month");
            }
        } else {
            if (!TextUtils.isEmpty(PF_CHARGE)) {
                tvPay.setText(PF_CHARGE + " INR/Hour");
            }
        }

    }

    tvMobile.setText(PF_MOBILE);

    if (PF_EDUCATION != null) {
        for (int i = 0; i < PF_EDUCATION.size(); i++) {
            View v = getLayoutInflater().inflate(R.layout.listitem_education, null, false);
            TextView tvHeading = (TextView) v.findViewById(R.id.tvHeading);
            TextView tvDetail = (TextView) v.findViewById(R.id.tvDetail);
            final RelativeLayout rlDelete = (RelativeLayout) v.findViewById(R.id.rlDelete);
            final ImageView ivEdit = (ImageView) v.findViewById(R.id.ivEdit);
            if (getIntent().hasExtra("ISNOTME")) {
                ivEdit.setVisibility(View.GONE);
                rlDelete.setVisibility(View.GONE);
            }
            rlDelete.setTag(i);
            ivEdit.setTag(i);

            rlDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = Integer.parseInt(view.getTag().toString());
//                        deleteEducation(PF_EDUCATION.get(pos).getId(), pos);
                    openDeletConfirmationDialog(true, PF_EDUCATION.get(pos).getId(), pos);

                }
            });
            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = Integer.parseInt(view.getTag().toString());
                    editEducation(pos);
                }
            });
            tvHeading.setText(PF_EDUCATION.get(i).getInstitute());
            String detail = PF_EDUCATION.get(i).getCourse() + " , " + PF_EDUCATION.get(i).getSpecialization() + " , " +
                    PF_EDUCATION.get(i).getEnrollmentYear() + " - " + PF_EDUCATION.get(i).getPassingOutYear();
            tvDetail.setText(detail);


//                new SwipeDetector(v, scrollView).setOnSwipeListener(new SwipeDetector.onSwipeEvent() {
//                    @Override
//                    public void SwipeEventDetected(View v, SwipeDetector.SwipeTypeEnum swipeType) {
//
//                        RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.rlDelete);
//
//                        if (swipeType == SwipeDetector.SwipeTypeEnum.RIGHT_TO_LEFT) {
//                            rl.setVisibility(View.VISIBLE);
//                            //  rlDelete.setVisibility(View.VISIBLE);
//
//
//                        } else {
//                            rl.setVisibility(View.GONE);
//
//                        }
//                    }
//                });
            v.setTag(i);


            llCardViewEducation.addView(v);
        }
    }

    if (PF_EXPERIENCE != null) {
        for (int i = 0; i < PF_EXPERIENCE.size(); i++) {
            View v = getLayoutInflater().inflate(R.layout.listitem_experience, null, false);
            TextView tvCompany = (TextView) v.findViewById(R.id.tvCompany);
            TextView tvDate = (TextView) v.findViewById(R.id.tvDate);
            TextView tvDescription = (TextView) v.findViewById(R.id.tvDescription);
            final RelativeLayout rlDelete = (RelativeLayout) v.findViewById(R.id.rlDelete);
            ImageView ivEdit = (ImageView) v.findViewById(R.id.ivEdit);
            if (getIntent().hasExtra("ISNOTME")) {
                ivEdit.setVisibility(View.GONE);
                rlDelete.setVisibility(View.GONE);
            }
            ivEdit.setTag(i);
            rlDelete.setTag(i);
            rlDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = Integer.parseInt(view.getTag().toString());
//                        deleteExperience(PF_EXPERIENCE.get(pos).getId(), pos);
                    openDeletConfirmationDialog(false, PF_EXPERIENCE.get(pos).getId(), pos);

                }
            });
            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = Integer.parseInt(view.getTag().toString());
                    editExperience(pos);
                }
            });
            v.setTag(i);
            tvCompany.setText(PF_EXPERIENCE.get(i).getEmployerName());
            tvDate.setText(PF_EXPERIENCE.get(i).getProfileName() + " , " + PF_EXPERIENCE.get(i).getJobStartDate() + " - " + PF_EXPERIENCE.get(i).getJobEndDate());
            tvDescription.setText(PF_EXPERIENCE.get(i).getJobDescription());

//                new SwipeDetector(v, scrollView).setOnSwipeListener(new SwipeDetector.onSwipeEvent() {
//                    @Override
//                    public void SwipeEventDetected(View v, SwipeDetector.SwipeTypeEnum swipeType) {
//
//                        RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.rlDelete);
//
//                        if (swipeType == SwipeDetector.SwipeTypeEnum.RIGHT_TO_LEFT) {
//                            rl.setVisibility(View.VISIBLE);
//                            //  rlDelete.setVisibility(View.VISIBLE);
//
//
//                        } else {
//                            rl.setVisibility(View.GONE);
//
//                        }
//                    }
//                });

            llCardViewExperience.addView(v);
        }

    }

    if (PF_SHOWMOBILE) {
        tvMobileLabel.setVisibility(View.VISIBLE);
        tvMobile.setVisibility(View.VISIBLE);
    } else {
        tvMobileLabel.setVisibility(View.GONE);
        tvMobile.setVisibility(View.GONE);
    }
}
    private void getBundleData() {


        PF_FULL_NAME = getIntent().getStringExtra(Constants.BundleData.Profile.PF_FULL_NAME);
        PF_FIRST_NAME = getIntent().getStringExtra(Constants.BundleData.Profile.PF_FIRST_NAME);
        PF_LAST_NAME = getIntent().getStringExtra(Constants.BundleData.Profile.PF_LAST_NAME);

        PF_CITY = getIntent().getStringExtra(Constants.BundleData.Profile.PF_CITY);
        PF_DOB = getIntent().getStringExtra(Constants.BundleData.Profile.PF_DOB);
        PF_EMAIL = getIntent().getStringExtra(Constants.BundleData.Profile.PF_EMAIL);
        PF_MOBILE = getIntent().getStringExtra(Constants.BundleData.Profile.PF_MOBILE);
        PF_WebURL = getIntent().getStringExtra(Constants.BundleData.Profile.PF_WebURL);
        address = getIntent().getStringExtra(Constants.BundleData.Profile.PF_ADDRESS);
        latitude = Double.parseDouble(getIntent().getStringExtra(Constants.BundleData.Profile.PF_LATITUDE));
        longitude = Double.parseDouble(getIntent().getStringExtra(Constants.BundleData.Profile.PF_LONGITUDe));
        PF_CHARGE = getIntent().getStringExtra(Constants.BundleData.Profile.PF_CHARGE);
        PF_CHARGETYPE = getIntent().getStringExtra(Constants.BundleData.Profile.PF_CHARGETYPE);
        PF_SHOWMOBILE = getIntent().getBooleanExtra(Constants.BundleData.Profile.PF_SHOWMOBILE, false);
        PF_EDUCATION = (ArrayList<GetAllProfileDetailsModel.Education>) getIntent().getSerializableExtra(Constants.BundleData.Profile.PF_EDUCATION);
        PF_EXPERIENCE = (ArrayList<GetAllProfileDetailsModel.Experience>) getIntent().getSerializableExtra(Constants.BundleData.Profile.PF_EXPERIENCE);
        PF_COMPANY = getIntent().getStringExtra(Constants.BundleData.Profile.PF_COMPANY);
        PF_GSTN = getIntent().getStringExtra(Constants.BundleData.Profile.PF_GSTN);
        PF_FBURL = getIntent().getStringExtra(Constants.BundleData.Profile.PF_FBURL);
        PF_TWITTERURL = getIntent().getStringExtra(Constants.BundleData.Profile.PF_TWITTERURL);
        PF_INSTAURL = getIntent().getStringExtra(Constants.BundleData.Profile.PF_INSTAURL);
        PID = getIntent().getStringExtra("PID");
       setData();
    }

    private void deleteEducation(int educationId, final int pos) {
        DeleteEducationController anInterface = GetRetrofit.getInstance().create(DeleteEducationController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));


        Call<LoginModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN), objSPS.getValueFromShared_Pref(Constants.ID), "" + educationId);
        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    if (response.code() == 200) {
                        if (response.body() instanceof LoginModel) {
                            LoginModel model = (LoginModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (model.isStatus()) {
                                int position = pos;
                                for (int i = 0; i < llCardViewEducation.getChildCount(); i++) {
                                    if (Integer.parseInt(llCardViewEducation.getChildAt(i).getTag().toString()) == pos) {
                                        position = i;
                                        break;
                                    }
                                }
                                llCardViewEducation.removeViewAt(position);
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

    private void deleteExperience(int experienceId, final int pos) {
        DeleteExperienceController anInterface = GetRetrofit.getInstance().create(DeleteExperienceController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));


        Call<LoginModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN), objSPS.getValueFromShared_Pref(Constants.ID), "" + experienceId);
        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    if (response.code() == 200) {
                        if (response.body() instanceof LoginModel) {
                            LoginModel model = (LoginModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (model.isStatus()) {
                                int position = pos;
                                for (int i = 0; i < llCardViewExperience.getChildCount(); i++) {
                                    if (Integer.parseInt(llCardViewExperience.getChildAt(i).getTag().toString()) == pos) {
                                        position = i;
                                        break;
                                    }
                                }

                                llCardViewExperience.removeViewAt(position);
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
    public void onItemDeleted(int positionParent, int childPosition, boolean isParent) {
        if (!getIntent().hasExtra("ISNOTME")) {
            //me inside isnotme-false
            if (arrayListHashMap.size() == 1) {
                Toast.makeText(ctx, "You can't remove all the skills.", Toast.LENGTH_LONG).show();
            } else {

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
                isSkillUpdated = true;

                CloudAdapter adapter = new CloudAdapter(ctx, arrayListHashMap, getIntent().hasExtra("ISNOTME"));
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
            }
        }

    }

    String skills = "";

    private void callSkillsUpdateApi() {
        SkillsUpdateController anInterface = GetRetrofit.getInstance().create(SkillsUpdateController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        String json = "";
        skills = "";
        UpdateSkillsRequestModel requestModel = new UpdateSkillsRequestModel();
        ArrayList<UpdateSkillsRequestModel.UpdateSkillModel> arrSkillRequestModel = new ArrayList<>();
        List<String> keys = new ArrayList<String>(arrayListHashMap.keySet()); // <== Set to List
        UpdateSkillsRequestModel.UpdateSkillModel model;
        ArrayList<String> strSubCategory;
        for (int i = 0; i < keys.size(); i++) {
            model = new UpdateSkillsRequestModel().new UpdateSkillModel();
            model.setCategory(keys.get(i));
            // for skills update for home page
            if (skills.equals("")) {
                skills = keys.get(i);
            } else {
                skills = skills + "," + keys.get(i);

            }
            //end
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
        requestModel.set_id(objSPS.getValueFromShared_Pref(Constants.ID));
        requestModel.setSkills(arrSkillRequestModel);
        Gson gson = new Gson();
        json = gson.toJson(requestModel);


        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Call<LoginModel> call = anInterface.getResponse(objSPS.getValueFromShared_Pref(Constants.TOKEN),
                body);
        API.callRun(ctx, false, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    if (response.code() == 200) {
                        CommonMethod.printAPIResponse(response.body());
                        if (response.body() instanceof LoginModel) {
                            objSPS.setValueToSharedPref(Constants.SKILLS, skills);

                            LoginModel model = (LoginModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (model.isStatus()) {
                                finish();
                            }
                        }
                    } else {
                        CommonMethod.cancelDialog(dialog, ctx);
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
                                //  CommonMethod.showToastMessage(ctx, getString(R.string.UNKNOWN_HOST));
                            } else if (t instanceof ConnectException) {
                                // CommonMethod.showToastMessage(ctx, getString(R.string.UNKNOWN_HOST));
                            } else if (t instanceof SocketTimeoutException) {
                                //  CommonMethod.showToastMessage(ctx, getString(R.string.SOCKET_TIME_OUT));
                            } else if (t instanceof SocketException) {
                                //  CommonMethod.showToastMessage(ctx, getString(R.string.UNABLE_TO_CONNECT));
                            } else if (t instanceof JsonSyntaxException) {
                                //  CommonMethod.showToastMessage(ctx, getString(R.string.JSON_SYNTAX));
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


}
