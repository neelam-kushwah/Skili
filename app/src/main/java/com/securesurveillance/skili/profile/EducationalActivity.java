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
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.securesurveillance.skili.MyApplication;
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.apiHandler.API;
import com.securesurveillance.skili.apiHandler.APIResponseListener;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.GetRetrofit;
import com.securesurveillance.skili.apiinterfaces.EducationUpdateController;
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

public class EducationalActivity extends AppCompatActivity {
    ImageView iv_LeftOption;
    TextView tv_Title;
    String degree, branch, clg, passoutYears, enrollYear;
    EditText etPassout, etClg, etBranch, etDegree,etEnroll;
    RadioButton rbFT, rbPT, rbC;
    Button btnSubmit;
    Context ctx;
    private SharePreferanceWrapperSingleton objSPS;
    MyApplication application;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_education);
        ctx = this;
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(ctx);
        application = (MyApplication) getApplicationContext();

        iv_LeftOption = (ImageView) findViewById(R.id.iv_LeftOption);
        tv_Title = (TextView) findViewById(R.id.tv_Title);
        tv_Title.setText("Educational Details");
        iv_LeftOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Bundle bundle = getIntent().getBundleExtra("bundle");
        degree = bundle.getString("DEGREE");
        branch = bundle.getString("BRANCH");
        clg = bundle.getString("UNIVERSITY");
        enrollYear = bundle.getString("ENROLL");
        passoutYears = bundle.getString("PASSOUT");
        etBranch = (EditText) findViewById(R.id.etBranch);
        etPassout = (EditText) findViewById(R.id.etPassout);
        etClg = (EditText) findViewById(R.id.etClg);
        etDegree = (EditText) findViewById(R.id.etDegree);
        etEnroll=(EditText)findViewById(R.id.etEnroll);
        rbFT = (RadioButton) findViewById(R.id.rbFT);
        rbPT = (RadioButton) findViewById(R.id.rbPT);
        rbC = (RadioButton) findViewById(R.id.rbC);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        if (!degree.equalsIgnoreCase("-")) {
            etDegree.setText(degree);
        }
        if (!branch.equalsIgnoreCase("-")) {
            etBranch.setText(branch);
        }
        if (!clg.equalsIgnoreCase("-")) {
            etClg.setText(clg);
        }
        if (!passoutYears.equalsIgnoreCase("-")) {
            etPassout.setText(passoutYears);
        }
        if (!enrollYear.equalsIgnoreCase("-")) {
            etEnroll.setText(enrollYear);
        }
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callUpdateApi();

            }
        });
    }

    private void callUpdateApi() {
        EducationUpdateController anInterface = GetRetrofit.getInstance().create(EducationUpdateController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        JSONObject json = new JSONObject();
        try {


            JSONObject jsonObjectEducation = new JSONObject();

            json.put("_id", objSPS.getValueFromShared_Pref(Constants.ID));
            jsonObjectEducation.put("course",  CommonMethod.getFirstCapName(etDegree.getText().toString().trim()));
            jsonObjectEducation.put("enrollmentYear", etEnroll.getText().toString().trim());
            jsonObjectEducation.put("passingOutYear", etPassout.getText().toString().trim());
            jsonObjectEducation.put("institute",  CommonMethod.getFirstCapName(etClg.getText().toString().trim()));
            jsonObjectEducation.put("specialization",  CommonMethod.getFirstCapName(etBranch.getText().toString().trim()));


            json.put("education", jsonObjectEducation);

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
