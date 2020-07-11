package com.securesurveillance.skili;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.securesurveillance.skili.adapter.CloudAdapter;
import com.securesurveillance.skili.adapter.PostJobCloudAdapter;
import com.securesurveillance.skili.apiHandler.API;
import com.securesurveillance.skili.apiHandler.APIResponseListener;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.GetRetrofit;
import com.securesurveillance.skili.apiinterfaces.EditJobController;
import com.securesurveillance.skili.apiinterfaces.GetCategoryController;
import com.securesurveillance.skili.apiinterfaces.PostJobController;
import com.securesurveillance.skili.materialspinner.MaterialSpinner;
import com.securesurveillance.skili.materialspinner.MaterialSpinnerMultiple;
import com.securesurveillance.skili.model.SkillModel;
import com.securesurveillance.skili.model.requestmodel.PostJobRequestModel;
import com.securesurveillance.skili.model.requestmodel.SkilliSignupRequestModel;
import com.securesurveillance.skili.model.responsemodel.GetAllJobRecruiterDetailModel;
import com.securesurveillance.skili.model.responsemodel.GetCategoryModel;
import com.securesurveillance.skili.model.responsemodel.LoginModel;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.SharePreferanceWrapperSingleton;
import com.securesurveillance.skili.utility.SweetAlertController;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.securesurveillance.skili.fragments.HomeFragment.pager;


public class PostJobStepBActivity extends Activity implements OnItemClickListener, CloudAdapter.ItemDelete,
        View.OnClickListener {
    boolean isFromSeekbar = false;
 //   boolean isKeyboardShown = false;
    private static final String LOG_TAG = "EmpuApp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    AutoCompleteTextView autoCompView;
    //------------ make your specific key ------------
    //  private static final String API_KEY = "AIzaSyDJTNLcwSz1VQ4HSehQgulxs-H1Mf5jCNA";
    private static final String API_KEY = "AIzaSyDPFjRVRPEL0Mhqwd5NrJzi5FzmjtFOONg";
    TextView tv_Title;
    ImageView iv_LeftOption;
    private SharePreferanceWrapperSingleton objSPS;
    MyApplication application;
    RecyclerView recyclerView;
    Context ctx;
    MaterialSpinner spinner_MainCategory;
    List<SkillModel> arrCategory = new ArrayList<>();
    List<String> arrMainCategoryName = new ArrayList<>();
    ArrayList<String> arrsubCategoryName = new ArrayList<>();
    RelativeLayout rlAddSkill;
    int selectedCategoryPosition = 0;
    HashMap<Integer, ArrayList<Integer>> arrSelectedCategory = new HashMap<>();
    HashMap<String, ArrayList<String>> arrayListHashMap = new HashMap<>();
    HashMap<String, Integer> arrayListCBHashMap = new HashMap<>();
    CrystalSeekbar seekBar;
    String jobTitle, jobDescription, jobPurpose, startDate, endDate, startTime, endTime;
    boolean isFulltime;
    double latitude = 0.0;
    double longitude = 0.0;
    String address = "";
    String city = "";
    Button btnPublish, btnDraft;
    TextView textMin, textMax;
    RadioButton rbYearly, rbMonthly, rbHourly;
    GetAllJobRecruiterDetailModel model;
    EditText etPay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postjob_stepb);
        ctx = this;
        initView();
        getIntentData();
        if (getIntent().hasExtra("DATA")) {
            model = (GetAllJobRecruiterDetailModel) getIntent().getSerializableExtra("DATA");
            tv_Title.setText("Edit Job");
            if (!TextUtils.isEmpty(model.getJobLocation())) {
                autoCompView.setText(model.getJobLocation());
                getLocationFromAddress(model.getJobLocation());
                address = model.getJobLocation();
            }
            if (!TextUtils.isEmpty(model.getBudgetType())) {
                if (model.getBudgetType().equals("YEARLY")) {
                    rbYearly.setChecked(true);
                    seekBar.setMinValue(100000);
                    etPay.setText("" + 100000);
                    textMin.setText("" + 100000);
                    seekBar.setMaxValue(1000000);
                    // seekBar.setSteps(10000);
                    textMax.setText("" + 1000000);


                } else if (model.getBudgetType().equals("MONTHLY")) {
                    rbMonthly.setChecked(true);
                    seekBar.setMinValue(10000);
                    etPay.setText("" + 10000);

                    textMin.setText("" + 10000);
                    // seekBar.setSteps(1000);

                    seekBar.setMaxValue(70000);
                    textMax.setText("" + 70000);

                } else {
                    rbHourly.setChecked(true);
                    seekBar.setMinValue(100);
                    etPay.setText("" + 100);

                    textMin.setText("" + 100);
                    // seekBar.setSteps(100);

                    seekBar.setMaxValue(2000);
                    textMax.setText("" + 2000);
                }
                seekBar.setMinStartValue(Integer.parseInt(model.getBudget()));
                seekBar.apply();
                isFromSeekbar = true;
                etPay.setText("" + Integer.parseInt(model.getBudget()));
            }
            //
            if (model.getSkills() != null) {
                ArrayList<String> arrSubName = new ArrayList<>();
                for (int k = 0; k < model.getSkills().getSubcategory().size(); k++) {
                    arrSubName.add(model.getSkills().getSubcategory().get(k));
                }
                arrayListHashMap.put(model.getSkills().getCategory(),
                        arrSubName);
                CloudAdapter adapter = new CloudAdapter(ctx, arrayListHashMap);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
            }

            //
            btnPublish.setText("Edit Job");
            btnDraft.setVisibility(View.INVISIBLE);
        }

    }

    private void initView() {
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(ctx);
        application = (MyApplication) getApplicationContext();
        tv_Title = (TextView) findViewById(R.id.tv_Title);
        iv_LeftOption = (ImageView) findViewById(R.id.iv_LeftOption);
        autoCompView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        spinner_MainCategory = (MaterialSpinner) findViewById(R.id.spinner_MainCategory);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        rlAddSkill = (RelativeLayout) findViewById(R.id.rlAddSkill);
        seekBar = (CrystalSeekbar) findViewById(R.id.seekBar);
        textMin = (TextView) findViewById(R.id.textMin1);
        textMax = (TextView) findViewById(R.id.textMax1);
        tvSkillAdd = (TextView) findViewById(R.id.tvSkillAdd);
        btnPublish = (Button) findViewById(R.id.btnPublish);
        btnDraft = (Button) findViewById(R.id.btnDraft);
        rbHourly = (RadioButton) findViewById(R.id.rbHourly);
        rbMonthly = (RadioButton) findViewById(R.id.rbMonthly);
        rbYearly = (RadioButton) findViewById(R.id.rbYearly);
        etPay = (EditText) findViewById(R.id.etPay);
//        seekBar.setTextAboveThumbsColorResource(android.R.color.black);
        // set listener


        rbHourly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    seekBar.setMinValue(100);
                    textMin.setText("" + 100);
                    seekBar.setMaxValue(2000);
                    textMax.setText("" + 2000);
                    // seekBar.setSteps(100);
                    etPay.setText("" + 100);
                }
            }
        });
        rbMonthly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    seekBar.setMinValue(10000);
                    textMin.setText("" + 10000);
                    // seekBar.setSteps(1000);
                    etPay.setText("" + 10000);

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
                    // seekBar.setSteps(10000);
                    etPay.setText("" + 100000);

                    seekBar.setMaxValue(1000000);
                    textMax.setText("" + 1000000);

                }
            }
        });
        etPay.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
//                    isKeyboardShown = false;
//                    if (etPay.getText().toString().trim().length() > 0) {
//                        if (etPay.getText().toString().trim().length() + 2 <= (String.valueOf(seekBar.getMinValue()).length()) &&
//                                Integer.parseInt(etPay.getText().toString().trim()) < seekBar.getMinValue()) {
//                            Toast.makeText(ctx, "Pay scale value should not be less than " + seekBar.getMinValue(), Toast.LENGTH_SHORT).show();
//                            etPay.setText(String.format("%.0f", seekBar.getMinValue()));
//                            seekBar.setMinStartValue(seekBar.getMinValue());
//                            seekBar.apply();
//                        }
//                    } else {
//                        Toast.makeText(ctx, "Pay scale value should not be less than " + seekBar.getMinValue(), Toast.LENGTH_SHORT).show();
//                        etPay.setText(String.format("%.0f", seekBar.getMinValue()));
//                        seekBar.setMinStartValue(seekBar.getMinValue());
//                        seekBar.apply();
//                    }

                    if(keyEvent.getKeyCode()==KeyEvent.KEYCODE_ENTER){
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }

                isFromSeekbar = false;
                return false;
            }
        });
        KeyboardVisibilityEvent.setEventListener(
                PostJobStepBActivity.this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (!isOpen) {
                            if (etPay.getText().toString().trim().length() > 0) {

                                if (etPay.getText().toString().trim().length() + 2 <= (String.valueOf(seekBar.getMinValue()).length()) &&
                                        Integer.parseInt(etPay.getText().toString().trim()) < seekBar.getMinValue()) {
                                    Toast.makeText(ctx, "Pay scale value should not be less than " + seekBar.getMinValue(), Toast.LENGTH_SHORT).show();

                                    isFromSeekbar = true;
                                    etPay.setText(String.format("%.0f", seekBar.getMinValue()));
                                    seekBar.setMinValue(seekBar.getMinValue());
                                    seekBar.apply();
                                }
                            } else {
                                isFromSeekbar = true;
                                Toast.makeText(ctx, "Pay scale value should not be less than " + seekBar.getMinValue(), Toast.LENGTH_SHORT).show();
                                etPay.setText(String.format("%.0f", seekBar.getMinValue()));
                                seekBar.setMinValue(seekBar.getMinValue());
                                seekBar.apply();
                            }
                        }
                    }
                });
        etPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromSeekbar = false;
                //isKeyboardShown = true;

            }
        });
        etPay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(etPay.getText().toString().trim())) {
                    if (!isFromSeekbar) {
                        if (etPay.getText().toString().trim().length() + 2 == (String.valueOf(seekBar.getMinValue()).length()) &&
                                Integer.parseInt(etPay.getText().toString().trim()) < seekBar.getMinValue()) {
                            Toast.makeText(ctx, "Pay scale value should not be less than " + seekBar.getMinValue(), Toast.LENGTH_SHORT).show();
                            etPay.setText(String.format("%.0f", seekBar.getMinValue()));
                        } else if (etPay.getText().toString().trim().length() + 2 >= (String.valueOf(seekBar.getMaxValue()).length()) && Integer.parseInt(etPay.getText().toString().trim()) > seekBar.getMaxValue()) {
                            Toast.makeText(ctx, "Pay scale value should not be greater than " + seekBar.getMaxValue(), Toast.LENGTH_SHORT).show();
                            etPay.setText(String.format("%.0f", seekBar.getMaxValue()));

                        } else {
                            if (Integer.parseInt(etPay.getText().toString().trim()) >= seekBar.getMinValue() &&
                                    Integer.parseInt(etPay.getText().toString().trim()) <= seekBar.getMaxValue()) {
                                seekBar.setMinStartValue(Integer.parseInt(etPay.getText().toString().trim()));
                                seekBar.apply();
                            }
                        }
                    }
                }
            }
        });
        seekBar.setMinValue(100);
        etPay.setText("" + 100);

        textMin.setText("" + 100);

        seekBar.setMaxValue(2000);
        textMax.setText("" + 2000);

        tv_Title.setText("Post a Job");

        Drawable drawable = getResources().getDrawable(R.drawable.geoicon);
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * 0.6),
                (int) (drawable.getIntrinsicHeight() * 0.6));
        ScaleDrawable sd = new ScaleDrawable(drawable, 0, 40, 40);
        autoCompView.setCompoundDrawables(null, null, sd.getDrawable(), null);
        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.autocomplete_list_item));
        autoCompView.setOnItemClickListener(this);
        rlAddSkill.setOnClickListener(this);
        btnPublish.setOnClickListener(this);
        btnDraft.setOnClickListener(this);
        iv_LeftOption.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
      //  if (!isKeyboardShown) {
            super.onBackPressed();

      //  }
    }

    @Override
    protected void onStart() {
        super.onStart();
        seekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue) {
                //   textMin.setText(String.valueOf(minValue));
                //
                isFromSeekbar = true;
                etPay.setText(String.valueOf(minValue));
            }
        });
    }

    private void getIntentData() {

        jobTitle = getIntent().getStringExtra(Constants.BundleData.JOB_TITLE);
        jobDescription = getIntent().getStringExtra(Constants.BundleData.JOB_DESCRIPTION);
        jobPurpose = getIntent().getStringExtra(Constants.BundleData.JOB_PURPOSE);
        isFulltime = getIntent().getBooleanExtra(Constants.BundleData.IS_FULL_TIME, false);
        if (!isFulltime) {
            startDate = getIntent().getStringExtra(Constants.BundleData.START_DATE);
            endDate = getIntent().getStringExtra(Constants.BundleData.END_DATE);
            startTime = getIntent().getStringExtra(Constants.BundleData.START_TIME);
            endTime = getIntent().getStringExtra(Constants.BundleData.END_TIME);
        }
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

    TextView tvSkillAdd;

    private void openSkillDialog() {

        final Dialog dialog = new Dialog(ctx, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.dialog_selectskill);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(PostJobStepBActivity.this, R.color.dialog_transparent)));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.CENTER);
        MaterialSpinner spinner_MainCategory = (MaterialSpinner) dialog.findViewById(R.id.spinner_MainCategory);
        final MaterialSpinnerMultiple spinner_SubCategory = (MaterialSpinnerMultiple) dialog.findViewById(R.id.spinner_SubCategory);
        LinearLayout llTime = (LinearLayout) dialog.findViewById(R.id.llTime);
        llTime.setVisibility(View.GONE);
        Button btnDone = (Button) dialog.findViewById(R.id.btnDone);
        final AppCompatCheckBox cbP = (AppCompatCheckBox) dialog.findViewById(R.id.cbP);
        final AppCompatCheckBox cbF = (AppCompatCheckBox) dialog.findViewById(R.id.cbF);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrSelectedCategory.size() == 0) {
                    CommonMethod.showToastMessage(ctx, "Please choose sub category");
                } else {
                    ArrayList<String> arrSubName = new ArrayList<>();
                    for (int k = 0; k < arrSelectedCategory.get(selectedCategoryPosition).size(); k++) {
                        Log.i("POS", arrSelectedCategory.get(selectedCategoryPosition).get(k).toString());
                        arrSubName.add(arrsubCategoryName.get(arrSelectedCategory.get(selectedCategoryPosition).get(k)));
                    }
                    arrayListCBHashMap.clear();
                    arrayListHashMap.clear();
                    arrayListHashMap.put(arrMainCategoryName.get(selectedCategoryPosition),
                            arrSubName);
                    tvSkillAdd.setText("EDIT SKILL");
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

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        latitude = 0.0;
        longitude = 0.0;
        address = str;
        getLocationFromAddress(str);
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
        if (arrayListHashMap.size() > 0) {
            tvSkillAdd.setText("EDIT SKILL");
        } else {
            tvSkillAdd.setText("ADD SKILL");

        }

        CloudAdapter adapter = new CloudAdapter(ctx, arrayListHashMap);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_LeftOption:
                finish();
                break;
            case R.id.rlAddSkill:
                callCategoryApi();
                break;

            case R.id.btnPublish:
                checkValidationJob("PUBLISHED");
                break;
            case R.id.btnDraft:

                checkValidationJob("DRAFT");
                break;

        }
    }

    private void checkValidationJob(String status) {
        if (TextUtils.isEmpty(address) && status.equals("PUBLISHED")) {
            CommonMethod.showToastMessage(ctx, "Please enter job location");
        } else if (arrayListHashMap.size() == 0 && status.equals("PUBLISHED")) {
            CommonMethod.showToastMessage(ctx, "Please add skills.");
        } else if (seekBar.getSelectedMinValue().intValue() == 0 && status.equals("PUBLISHED")) {
            CommonMethod.showToastMessage(ctx, "Max pay scale value should be greater than 0");

        } else {
            if (model == null) {
                callPostJobApi(status);
            } else {
                callEditJob();
            }
        }
    }

    private void callEditJob() {
        EditJobController anInterface = GetRetrofit.getInstance().create(EditJobController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        String json = "";
        try {
            PostJobRequestModel model = new PostJobRequestModel();
            model.setProfileId(objSPS.getValueFromShared_Pref(Constants.ID));
            model.setJobTitle(jobTitle);
            model.setJobDescription(jobDescription);
            model.setPurposeOfJob(jobPurpose);
            //add for edit job
            model.setExistingJobId(this.model.getJobId());
            //end
            if (isFulltime) {
                model.setJobType("FULL_TIME");

            } else {
                model.setJobType("PART_TIME");
                model.setStartDate(startDate);
                model.setEndDate(endDate);
                model.setStartTime(startTime);
                model.setEndtime(endTime);

            }
            model.setJobLocation(address);
            model.setLatitude("" + latitude);
            model.setLongitude("" + longitude);
            model.setCity(city);
            model.setBudget(seekBar.getSelectedMinValue().toString());
            model.setStatus(this.model.getStatus());

            if (rbYearly.isChecked()) {
                model.setBudgetType("YEARLY");
            } else if (rbMonthly.isChecked()) {
                model.setBudgetType("MONTHLY");

            } else {
                model.setBudgetType("HOURLY");
            }

            List<String> keys = new ArrayList<String>(arrayListHashMap.keySet()); // <== Set to List
            PostJobRequestModel.SkillRequestModel skillRequestModel = new PostJobRequestModel().new SkillRequestModel();
            ArrayList<String> strSubCategory;
            for (int i = 0; i < keys.size(); i++) {
                skillRequestModel = new PostJobRequestModel().new SkillRequestModel();
                skillRequestModel.setCategory(keys.get(i));
                strSubCategory = new ArrayList<>();
                for (int j = 0; j < arrayListHashMap.get(keys.get(i)).size(); j++) {
                    strSubCategory.add(arrayListHashMap.get(keys.get(i)).get(j));

                }
                skillRequestModel.setSubcategory(strSubCategory);

            }

            model.setSkills(skillRequestModel);
            Gson gson = new Gson();
            json = gson.toJson(model);
        } catch (Exception e) {

        }
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
                                    LoginModel model = (LoginModel) response.body();
                                    CommonMethod.cancelDialog(dialog, ctx);
                                    if (model.isStatus()) {
                                        CommonMethod.showToastMessage(ctx, model.getMessage());
                                        Intent i = new Intent(ctx, SliderActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
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

    private void callPostJobApi(String status) {
        PostJobController anInterface = GetRetrofit.getInstance().create(PostJobController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        String json = "";
        try {
            PostJobRequestModel model = new PostJobRequestModel();
            model.setProfileId(objSPS.getValueFromShared_Pref(Constants.ID));
            model.setJobTitle(jobTitle);
            model.setJobDescription(jobDescription);
            model.setPurposeOfJob(jobPurpose);
            if (isFulltime) {
                model.setJobType("FULL_TIME");

            } else {
                model.setJobType("PART_TIME");
                model.setStartDate(startDate);
                model.setEndDate(endDate);
                model.setStartTime(startTime);
                model.setEndtime(endTime);
            }
            model.setJobLocation(address);
            model.setLatitude("" + latitude);
            model.setLongitude("" + longitude);
            model.setCity(city);

            model.setBudget(seekBar.getSelectedMinValue().toString());
            model.setStatus(status);

            if (rbYearly.isChecked()) {
                model.setBudgetType("YEARLY");
            } else if (rbMonthly.isChecked()) {
                model.setBudgetType("MONTHLY");

            } else {
                model.setBudgetType("HOURLY");
            }

            List<String> keys = new ArrayList<String>(arrayListHashMap.keySet()); // <== Set to List
            PostJobRequestModel.SkillRequestModel skillRequestModel = new PostJobRequestModel().new SkillRequestModel();
            ArrayList<String> strSubCategory;
            for (int i = 0; i < keys.size(); i++) {
                skillRequestModel = new PostJobRequestModel().new SkillRequestModel();
                skillRequestModel.setCategory(keys.get(i));
                strSubCategory = new ArrayList<>();
                for (int j = 0; j < arrayListHashMap.get(keys.get(i)).size(); j++) {
                    strSubCategory.add(arrayListHashMap.get(keys.get(i)).get(j));

                }
                skillRequestModel.setSubcategory(strSubCategory);

            }

            model.setSkills(skillRequestModel);
            Gson gson = new Gson();
            json = gson.toJson(model);
        } catch (Exception e) {

        }
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
                                    LoginModel model = (LoginModel) response.body();
                                    CommonMethod.cancelDialog(dialog, ctx);
                                    if (model.isStatus()) {
                                        CommonMethod.showToastMessage(ctx, model.getMessage());
                                        Intent i = new Intent(ctx, SliderActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        i.putExtra("SCREEN", "JOBS");
                                        startActivity(i);
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

}