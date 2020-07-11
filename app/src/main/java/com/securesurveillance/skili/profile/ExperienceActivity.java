package com.securesurveillance.skili.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.securesurveillance.skili.MyApplication;
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.apiHandler.API;
import com.securesurveillance.skili.apiHandler.APIResponseListener;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.GetRetrofit;
import com.securesurveillance.skili.apiinterfaces.ExperienceUpdateController;
import com.securesurveillance.skili.model.responsemodel.LoginModel;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.SharePreferanceWrapperSingleton;
import com.securesurveillance.skili.utility.SweetAlertController;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by adarsh on 7/23/2018.
 */

public class ExperienceActivity extends AppCompatActivity {
    ImageView iv_LeftOption;
    TextView tv_Title;
    String designation, noticePeriod, experience, ctc;
    Context ctx;
    EditText etProfileTitle, etSalary, etTotalExperience, etNoticePeriod;
    Button btnSubmit;
    private MyApplication application;
    private SharePreferanceWrapperSingleton objSPS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_experience);
        ctx = this;
        ctx = this;
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(ctx);
        application = (MyApplication) getApplicationContext();

        Bundle bundle = getIntent().getBundleExtra("bundle");
        designation = bundle.getString("TITLE");
        ctc = bundle.getString("CTC");
        experience = bundle.getString("EXPERIENCE");
        noticePeriod = bundle.getString("NOTICE");
        etProfileTitle = (EditText) findViewById(R.id.etProfileTitle);
        etTotalExperience = (EditText) findViewById(R.id.etTotalExperience);
        etSalary = (EditText) findViewById(R.id.etSalary);
        etNoticePeriod = (EditText) findViewById(R.id.etNoticePeriod);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        if (!designation.equalsIgnoreCase("-")) {
            etProfileTitle.setText(designation);
        }
        if (!ctc.equalsIgnoreCase("-")) {
            etSalary.setText(ctc);
        }
        if (!experience.equalsIgnoreCase("-")) {
            etTotalExperience.setText(experience);
        }
        if (!noticePeriod.equalsIgnoreCase("-")) {
            etNoticePeriod.setText(noticePeriod);
        }
        iv_LeftOption = (ImageView) findViewById(R.id.iv_LeftOption);
        tv_Title = (TextView) findViewById(R.id.tv_Title);
        tv_Title.setText("Experience Details");
        iv_LeftOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callUpdateApi();
            }
        });
    }

    private void callUpdateApi() {
        ExperienceUpdateController anInterface = GetRetrofit.getInstance().create(ExperienceUpdateController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        JSONObject json = new JSONObject();
        try {


            JSONObject jsonObjectEducation = new JSONObject();

            json.put("_id", objSPS.getValueFromShared_Pref(Constants.ID));
            jsonObjectEducation.put("annualSalary", etSalary.getText().toString().trim());
            jsonObjectEducation.put("profileTitle",  CommonMethod.getFirstCapName(etProfileTitle.getText().toString().trim()));
           jsonObjectEducation.put("expInYears", etTotalExperience.getText().toString().trim());
           jsonObjectEducation.put("noticePeriodInDays", etNoticePeriod.getText().toString().trim());


            json.put("experience", jsonObjectEducation);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
        Call<LoginModel> call = anInterface.getResponse( objSPS.getValueFromShared_Pref(Constants.TOKEN),
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
                                CommonMethod.showToastMessage(ctx, model.getMessage());

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
}
