package com.securesurveillance.skili;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.securesurveillance.skili.apiHandler.API;
import com.securesurveillance.skili.apiHandler.APIResponseListener;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.GetRetrofit;
import com.securesurveillance.skili.apiinterfaces.PostJobController;
import com.securesurveillance.skili.apiinterfaces.RateFreelancerController;
import com.securesurveillance.skili.model.responsemodel.LoginModel;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.RoundedImageView;
import com.securesurveillance.skili.utility.SharePreferanceWrapperSingleton;
import com.securesurveillance.skili.utility.SweetAlertController;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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

import static com.securesurveillance.skili.fragments.HomeFragment.pager;

/**
 * Created by adarsh on 4/20/2019.
 */

public class RatingReviewActivity extends AppCompatActivity {
    TextView tv_Title, tvName;
    ImageView iv_LeftOption;
    CheckBox star1, star2, star3, star4, star5;
    String applicantId, jobId;
    RoundedImageView ivProfile;
    Context ctx;
    SharePreferanceWrapperSingleton objSPS;
    EditText etFeedback;
    String starGiven = "";
    Button btnSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratecandidate);
        ctx = this;
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(ctx);
        tv_Title = (TextView) findViewById(R.id.tv_Title);
        tvName = (TextView) findViewById(R.id.tvName);
        iv_LeftOption = (ImageView) findViewById(R.id.iv_LeftOption);
        etFeedback = (EditText) findViewById(R.id.etFeedback);
        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        star1 = (CheckBox) findViewById(R.id.star1);
        star2 = (CheckBox) findViewById(R.id.star2);
        star3 = (CheckBox) findViewById(R.id.star3);
        star4 = (CheckBox) findViewById(R.id.star4);
        star5 = (CheckBox) findViewById(R.id.star5);
        ivProfile = (RoundedImageView) findViewById(R.id.ivProfile);
        tvName.setText(getIntent().getStringExtra("NAME"));
        applicantId = getIntent().getStringExtra("ID");
        jobId = getIntent().getStringExtra("JOBID");
        iv_LeftOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_Title.setText("Rating & Review");
        Picasso.get().load(getIntent().getStringExtra("PIC")).into(ivProfile, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                ivProfile.setBackground(getDrawable(R.drawable.user_profile));
            }
        });
        star1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    starGiven = "1";
                    star1.setBackground(null);
                    star1.setBackground(getResources().getDrawable(R.drawable.star_review));
                } else {
                    starGiven = "";

                    star1.setBackground(null);
                    star1.setBackground(getResources().getDrawable(R.drawable.star));
                    star2.setBackground(null);
                    star2.setBackground(getResources().getDrawable(R.drawable.star));
                    star3.setBackground(null);
                    star3.setBackground(getResources().getDrawable(R.drawable.star));
                    star4.setBackground(null);
                    star4.setBackground(getResources().getDrawable(R.drawable.star));
                    star5.setBackground(null);
                    star5.setBackground(getResources().getDrawable(R.drawable.star));
                }
            }
        });

        star2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    starGiven = "2";

                    star1.setBackground(null);
                    star1.setBackground(getResources().getDrawable(R.drawable.star_review));
                    star2.setBackground(null);
                    star2.setBackground(getResources().getDrawable(R.drawable.star_review));
                } else {
                    starGiven = "1";

                    star2.setBackground(null);
                    star2.setBackground(getResources().getDrawable(R.drawable.star));
                    star3.setBackground(null);
                    star3.setBackground(getResources().getDrawable(R.drawable.star));
                    star4.setBackground(null);
                    star4.setBackground(getResources().getDrawable(R.drawable.star));
                    star5.setBackground(null);
                    star5.setBackground(getResources().getDrawable(R.drawable.star));
                }
            }
        });

        star3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    starGiven = "3";
                    star1.setBackground(null);
                    star1.setBackground(getResources().getDrawable(R.drawable.star_review));
                    star2.setBackground(null);
                    star2.setBackground(getResources().getDrawable(R.drawable.star_review));
                    star3.setBackground(null);
                    star3.setBackground(getResources().getDrawable(R.drawable.star_review));
                } else {
                    starGiven = "2";

                    star3.setBackground(null);
                    star3.setBackground(getResources().getDrawable(R.drawable.star));
                    star4.setBackground(null);
                    star4.setBackground(getResources().getDrawable(R.drawable.star));
                    star5.setBackground(null);
                    star5.setBackground(getResources().getDrawable(R.drawable.star));
                }
            }
        });

        star4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    starGiven = "4";

                    star1.setBackground(null);
                    star1.setBackground(getResources().getDrawable(R.drawable.star_review));
                    star2.setBackground(null);
                    star2.setBackground(getResources().getDrawable(R.drawable.star_review));
                    star3.setBackground(null);
                    star3.setBackground(getResources().getDrawable(R.drawable.star_review));
                    star4.setBackground(null);

                    star4.setBackground(getResources().getDrawable(R.drawable.star_review));
                } else {
                    starGiven = "3";

                    star4.setBackground(null);
                    star4.setBackground(getResources().getDrawable(R.drawable.star));
                    star5.setBackground(null);
                    star5.setBackground(getResources().getDrawable(R.drawable.star));
                }
            }
        });

        star5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    starGiven = "5";

                    star1.setBackground(null);
                    star1.setBackground(getResources().getDrawable(R.drawable.star_review));
                    star2.setBackground(null);
                    star2.setBackground(getResources().getDrawable(R.drawable.star_review));
                    star3.setBackground(null);
                    star3.setBackground(getResources().getDrawable(R.drawable.star_review));
                    star4.setBackground(null);
                    star4.setBackground(getResources().getDrawable(R.drawable.star_review));
                    star5.setBackground(null);
                    star5.setBackground(getResources().getDrawable(R.drawable.star_review));
                } else {
                    starGiven = "4";

                    star5.setBackground(null);
                    star5.setBackground(getResources().getDrawable(R.drawable.star));
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(starGiven)){
                    Toast.makeText(ctx, "Please provide rating.", Toast.LENGTH_SHORT).show();
                }
//                else if(TextUtils.isEmpty(etFeedback.getText().toString().trim())){
//                    Toast.makeText(ctx, "Please provide review.", Toast.LENGTH_SHORT).show();
//                }
                else {
                    callRateIndiApi();
                }
            }
        });

    }

    private void callRateIndiApi() {
        RateFreelancerController anInterface = GetRetrofit.getInstance().create(RateFreelancerController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        JSONObject json = new JSONObject();
        try {

            json.put("ratingBy", objSPS.getValueFromShared_Pref(Constants.ID));

            json.put("ratingTo", applicantId);
            json.put("description", etFeedback.getText().toString().trim());
            json.put("star", starGiven);
            json.put("jobId", jobId);

        } catch (Exception e) {

        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
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
                                        if(objSPS.getValueFromShared_Pref(Constants.ROLE).contains("Individual")){
                                            finish();
                                        }else {
                                            Intent i = new Intent(ctx, SliderActivity.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            i.putExtra("SCREEN", "JOBS");
                                            startActivity(i);
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

}
