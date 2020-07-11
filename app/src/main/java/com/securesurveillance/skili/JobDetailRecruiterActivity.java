package com.securesurveillance.skili;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.securesurveillance.skili.adapter.HomeJobsAdapter;
import com.securesurveillance.skili.apiinterfaces.ApplyJobController;
import com.securesurveillance.skili.fragments.HomeJobFragment;
import com.securesurveillance.skili.profile.MyProfileActivity;
import com.squareup.picasso.Picasso;
import com.securesurveillance.skili.adapter.FavouriteAdapter;
import com.securesurveillance.skili.adapter.JobAppliedAdapter;
import com.securesurveillance.skili.adapter.JobDetailCloudAdapter;
import com.securesurveillance.skili.adapter.SliderJobsAdapter;
import com.securesurveillance.skili.apiHandler.API;
import com.securesurveillance.skili.apiHandler.APIResponseListener;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.GetRetrofit;
import com.securesurveillance.skili.apiinterfaces.AcceptRejectApplicantController;
import com.securesurveillance.skili.apiinterfaces.AddFavApplicantController;
import com.securesurveillance.skili.apiinterfaces.AddFavJobController;
import com.securesurveillance.skili.apiinterfaces.GetAllJobRecruiterController;
import com.securesurveillance.skili.apiinterfaces.GetApplicantListController;
import com.securesurveillance.skili.apiinterfaces.PostJobController;
import com.securesurveillance.skili.apiinterfaces.RemoveFavApplicantController;
import com.securesurveillance.skili.apiinterfaces.UpdateJobStatusController;
import com.securesurveillance.skili.fragments.FavouriteFragment;
import com.securesurveillance.skili.materialspinner.MaterialSpinner;
import com.securesurveillance.skili.model.GlobalFeedModel;
import com.securesurveillance.skili.model.requestmodel.PersonalInfoUpdateModel;
import com.securesurveillance.skili.model.responsemodel.GetAllJobRecruiterDetailModel;
import com.securesurveillance.skili.model.responsemodel.GetAllJobRecruiterModel;
import com.securesurveillance.skili.model.responsemodel.GetApplicantListModel;
import com.securesurveillance.skili.model.responsemodel.LoginModel;
import com.securesurveillance.skili.profile.PersonalDetailsActivity;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.RoundedImageView;
import com.securesurveillance.skili.utility.SharePreferanceWrapperSingleton;
import com.securesurveillance.skili.utility.SweetAlertController;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.securesurveillance.skili.fragments.HomeFragment.pager;

/**
 * Created by adarsh on 11/3/2018.
 */

public class JobDetailRecruiterActivity extends AppCompatActivity implements View.OnClickListener, JobAppliedAdapter.AddApplicant {
    RecyclerView recyclerView, recyclerJobApplied;
    ImageView ivBack, imgApplied, ivGoogleMap, ivMenu, ivShare, ivFav;
    TextView tvJob, tvName, tvPurpose, tvRating, tvBudget, tvDays, tvDateRange, tvProjectDesc, tvLocation, tvJobType, tvApplied;
    GetAllJobRecruiterDetailModel model;
    ArrayList<String> arrList = new ArrayList<>();
    RoundedImageView ivPic;
    SharePreferanceWrapperSingleton objSPS;
    TextView tvGetDirection;
    Context ctx;
    ArrayList<GetApplicantListModel.Result.Applicant> arrListGlobal = new ArrayList<GetApplicantListModel.Result.Applicant>();
    String itemSelected;
    RelativeLayout rlBottom;
    Button btnApply;
    int isFavourite = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobdetail_recruiter);
        ctx = this;
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(ctx);
        initView();
        isFavourite = getIntent().getIntExtra("ISFAV", 0);
        if (objSPS.getValueFromShared_Pref(Constants.ROLE).contains("Individual")) {
            rlBottom.setVisibility(View.GONE);

            ivMenu.setVisibility(View.GONE);
//            ivShare.setVisibility(View.VISIBLE);//commented of now, will use later
            ivShare.setVisibility(View.GONE);

            ivFav.setVisibility(View.VISIBLE);


        } else {
            ivFav.setVisibility(View.GONE);

        }
        model = (GetAllJobRecruiterDetailModel) getIntent().getSerializableExtra("DATA");
        if (model.isSavedAsFav()) {
            ivFav.setBackground(null);
            ivFav.setBackground(getDrawable(R.drawable.heart_fill));
        } else {
            ivFav.setBackground(null);
            ivFav.setBackground(getDrawable(R.drawable.heart));
        }
        if (objSPS.getValueFromShared_Pref(Constants.ROLE).contains("Individual")) {

            if (model.getApplicationStatus() != null && (model.getApplicationStatus()
                    .equalsIgnoreCase("APPLIED") ||
                    model.getApplicationStatus().equalsIgnoreCase("REJECTED") ||
                    model.getApplicationStatus().equalsIgnoreCase("ACCEPTED")
                    || model.getApplicationStatus().equalsIgnoreCase("CANCELLED"))) {

            } else {
                btnApply.setVisibility(View.VISIBLE);
            }
        }
        //rating is missing// days// daterange
        setData();
    }

    private void updateJobStatus(final String status, final Dialog dialogParent) {
        UpdateJobStatusController anInterface = GetRetrofit.getInstance().create(UpdateJobStatusController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        JSONObject json = new JSONObject();
        try {

            json.put("jobId", model.getJobId());
            json.put("status", status);


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
                                    LoginModel loginModel = (LoginModel) response.body();
                                    CommonMethod.cancelDialog(dialog, ctx);
                                    if (loginModel.isStatus()) {
                                        CommonMethod.showToastMessage(ctx, loginModel.getMessage());
                                        dialogParent.dismiss();
                                        if (status.equals("COMPLETED")) {
                                            Intent i = new Intent(ctx, RatingReviewActivity.class);
                                            i.putExtra("NAME", model.getAcceptedApplicant().get(0).getName());
                                            i.putExtra("ID", model.getAcceptedApplicant().get(0).getProfileId());
                                            i.putExtra("JOBID", model.getJobId());
                                            i.putExtra("PIC", model.getAcceptedApplicant().get(0).getProfilePic());
                                            startActivity(i);
                                        }
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

    private void getApplicantList() {
        GetApplicantListController anInterface = GetRetrofit.getInstance().create(GetApplicantListController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));


        Call<GetApplicantListModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN), model.getJobId());
        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    if (response.code() == 200) {
                        if (response.body() instanceof GetApplicantListModel) {
                            GetApplicantListModel getApplicantListModel = (GetApplicantListModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (getApplicantListModel.isStatus()) {
                                arrListGlobal = getApplicantListModel.getResult().getApplicants();
                                boolean isAccepted = false;
                                int pos = 0;
                                if (model.getAcceptedApplicant().size() > 0) {
                                    for (int i = 0; i < arrListGlobal.size(); i++) {
                                        if (model.getAcceptedApplicant().get(0).getProfileId().equals(arrListGlobal.get(i).getProfileId())) {
                                            isAccepted = true;
                                            pos = i;
                                            break;
                                        }
                                    }
                                }
                                JobAppliedAdapter adapter1;
                                if (isAccepted) {
                                    adapter1 = new JobAppliedAdapter(ctx, arrListGlobal, true, pos);

                                } else {
                                    adapter1 = new JobAppliedAdapter(ctx, arrListGlobal, false, 0);
                                }
                                recyclerJobApplied.setAdapter(adapter1);
                                recyclerJobApplied.setLayoutManager(new LinearLayoutManager(JobDetailRecruiterActivity.this));

                            } else {
                                SweetAlertController.getInstance().showErrorDialog(ctx, SweetAlertDialog.ERROR_TYPE,
                                        getApplicantListModel.getMessage(), getString(R.string.app_name));

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

    private void removeFavJob() {
        RemoveFavApplicantController anInterface = GetRetrofit.getInstance().create(RemoveFavApplicantController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        Call<LoginModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN), model.getFavoriteId());

        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    if (response.code() == 200) {
                        if (response.body() instanceof LoginModel) {

                            LoginModel loginModel = (LoginModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (loginModel.isStatus()) {
                                Toast.makeText(ctx, loginModel.getMessage(), Toast.LENGTH_LONG).show();
                                ivFav.setBackground(null);
                                ivFav.setBackground(getDrawable(R.drawable.heart));
                                model.setSavedAsFav(false);
                            } else {
                                SweetAlertController.getInstance().showErrorDialog(ctx, SweetAlertDialog.ERROR_TYPE,
                                        loginModel.getMessage(), getString(R.string.app_name));

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

    private void addFavJob() {
        AddFavJobController anInterface = GetRetrofit.getInstance().create(AddFavJobController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        Call<LoginModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN), objSPS.getValueFromShared_Pref(Constants.ID), model.getJobId());
        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    if (response.code() == 200) {
                        if (response.body() instanceof LoginModel) {
                            LoginModel loginModel = (LoginModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (loginModel.isStatus()) {
                                Toast.makeText(ctx, loginModel.getMessage(), Toast.LENGTH_LONG).show();
                                ivFav.setBackground(null);
                                ivFav.setBackground(getDrawable(R.drawable.heart_fill));
                                model.setSavedAsFav(true);
                                model.setFavoriteId(loginModel.getResult().getReference_id());
                            } else {
                                SweetAlertController.getInstance().showErrorDialog(ctx, SweetAlertDialog.ERROR_TYPE,
                                        loginModel.getMessage(), getString(R.string.app_name));

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

    private void addFavApplicant(int pos) {
        AddFavApplicantController anInterface = GetRetrofit.getInstance().create(AddFavApplicantController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        Call<LoginModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN), objSPS.getValueFromShared_Pref(Constants.ID), arrListGlobal.get(pos).getProfileId());
        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    if (response.code() == 200) {
                        if (response.body() instanceof LoginModel) {
                            LoginModel model = (LoginModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (model.isStatus()) {
                                Toast.makeText(ctx, model.getMessage(), Toast.LENGTH_LONG).show();

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

    private void acceptApiCall(final int pos, final String str) {
        AcceptRejectApplicantController anInterface = GetRetrofit.getInstance().create(AcceptRejectApplicantController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        JSONObject json = new JSONObject();

        try {
            json.put("action", str);
            json.put("applicantId", arrListGlobal.get(pos).getProfileId());
            json.put("jobId", model.getJobId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        Call<LoginModel> call = anInterface.getResponse(objSPS.getValueFromShared_Pref(Constants.TOKEN),
                body);
        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    if (response.code() == 200) {
                        if (response.body() instanceof LoginModel) {
                            LoginModel loginModel = (LoginModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (loginModel.isStatus()) {
                                Toast.makeText(ctx, loginModel.getMessage(), Toast.LENGTH_LONG).show();
                                if (str.equals("ACCEPT")) {
                                    ArrayList<GetAllJobRecruiterDetailModel.ApplicantDetail> arr = new ArrayList<>();
                                    GetAllJobRecruiterDetailModel.ApplicantDetail applicantDetail = new GetAllJobRecruiterDetailModel().new ApplicantDetail();

                                    applicantDetail.setProfileId(arrListGlobal.get(pos).getProfileId());
                                    applicantDetail.setName(arrListGlobal.get(pos).getName());
                                    applicantDetail.setProfilePic(arrListGlobal.get(pos).getProfilePic());
                                    applicantDetail.setRating(arrListGlobal.get(pos).getRating());
                                    if (model.getAcceptedApplicant().size() > 0) {
                                        model.setAcceptedApplicant(new ArrayList<GetAllJobRecruiterDetailModel.ApplicantDetail>());
                                    }
                                    arr.add(applicantDetail);
                                    model.setAcceptedApplicant(arr);

                                    JobAppliedAdapter adapter1 = new JobAppliedAdapter(ctx, arrListGlobal, true, pos);
                                    recyclerJobApplied.setAdapter(adapter1);
                                    recyclerJobApplied.setLayoutManager(new LinearLayoutManager(JobDetailRecruiterActivity.this));

                                } else {
                                    model.setAcceptedApplicant(new ArrayList<GetAllJobRecruiterDetailModel.ApplicantDetail>());

                                    JobAppliedAdapter adapter1 = new JobAppliedAdapter(ctx, arrListGlobal, false, pos);
                                    recyclerJobApplied.setAdapter(adapter1);
                                    recyclerJobApplied.setLayoutManager(new LinearLayoutManager(JobDetailRecruiterActivity.this));

                                }
                            } else {
                                SweetAlertController.getInstance().showErrorDialog(ctx, SweetAlertDialog.ERROR_TYPE,
                                        loginModel.getMessage(), getString(R.string.app_name));

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

    private void setData() {
        if (TextUtils.isEmpty(model.getJobTitle())) {
            tvJob.setText("");
        } else {
            tvJob.setText(model.getJobTitle());
        }
        if (TextUtils.isEmpty(model.getPostedBy().getName())) {
            tvName.setText("");
        } else {
            tvName.setText(model.getPostedBy().getName());
        }
        if (TextUtils.isEmpty(model.getPurposeOfJob())) {
            tvPurpose.setText("");
        } else {
            tvPurpose.setText(model.getPurposeOfJob());

        }
        if (TextUtils.isEmpty(model.getBudget())) {
            tvBudget.setText("");
        } else {
            tvBudget.setText(model.getBudget());
        }
        if (TextUtils.isEmpty(model.getJobDescription())) {
            tvProjectDesc.setText("");
        } else {
            tvProjectDesc.setText(model.getJobDescription());
        }

        if (TextUtils.isEmpty(model.getJobLocation())) {
            tvLocation.setText("");
        } else {
            tvLocation.setText(model.getJobLocation());
        }
        if (TextUtils.isEmpty(model.getJobType())) {
            tvJobType.setText("");
            tvDateRange.setText("");
        } else {
            if (model.getJobType().contains("PART")) {
                tvJobType.setText("Part Time");
                tvDateRange.setText(model.getStartDate() + "  to  " + model.getEndDate());

            } else {
                tvJobType.setText("Full Time");
                tvDateRange.setVisibility(View.GONE);

            }
        }
        if (TextUtils.isEmpty(model.getApplicantCount())) {
            tvApplied.setText("");
            imgApplied.setVisibility(View.GONE);
        } else {
            tvApplied.setText(model.getApplicantCount() + " PEOPLE APPLIED");
            if (model.getApplicantCount().equals("0")) {
                imgApplied.setVisibility(View.GONE);
            }
        }
        if (TextUtils.isEmpty(model.getBudgetType())) {
            tvDays.setText("");
        } else {
            String statusFirstChar = String.valueOf(model.getBudgetType().charAt(0)).toUpperCase();
            String loweCaseStatus = model.getBudgetType().toLowerCase();

            loweCaseStatus = statusFirstChar.toCharArray()[0] + loweCaseStatus.substring(1, loweCaseStatus.length());
            tvDays.setText(loweCaseStatus);
        }
        if(model.getSkills()!=null){
            arrList.addAll(model.getSkills().getSubcategory());
            arrList.add(model.getSkills().getCategory());
        }
        JobDetailCloudAdapter adapter = new JobDetailCloudAdapter(JobDetailRecruiterActivity.this, arrList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(JobDetailRecruiterActivity.this, LinearLayoutManager.HORIZONTAL, true));
        Picasso.get().load(model.getPostedBy().getProfilePic()).into(ivPic);

        if (model.getStatus().contains("omplete")) {
            ivMenu.setVisibility(View.GONE);
            rlBottom.setVisibility(View.GONE);
            ivFav.setVisibility(View.GONE);
            ivShare.setVisibility(View.GONE);
        }
        ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRecruiterProfile();

            }
        });
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRecruiterProfile();
            }
        });
    }

    private void openRecruiterProfile() {
        Intent i = new Intent(JobDetailRecruiterActivity.this, MyProfileActivity.class);
        if (!model.getPostedBy().getMobileNumber().equalsIgnoreCase(objSPS.getValueFromShared_Pref(Constants.MOBILE))) {
            i.putExtra("PID", model.getPostedBy().getMobileNumber());
            i.putExtra("PROFILEID", model.getPostedBy().getProfileId());
            i.putExtra("ISFAV", model.getPostedBy().getFavRecruiter());
        }

        startActivity(i);
    }

    public void openUpdateStatus() {
        final Dialog dialog = new Dialog(ctx, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.dialog_updatestatus);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(ctx, R.color.dialog_transparent)));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.CENTER);
        MaterialSpinner spinner_SelectStatus = (MaterialSpinner) dialog.findViewById(R.id.spinner_SelectStatus);
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button btnDone = (Button) dialog.findViewById(R.id.btnDone);

        final ArrayList<String> arrList = new ArrayList<>();
        //  arrList.add("DRAFT");
        arrList.add("PUBLISHED");
        arrList.add("CANCELLED");
        arrList.add("ONGOING");
        arrList.add("COMPLETED");
        for (int i = 0; i < arrList.size(); i++) {
            if (model.getStatus().equals(arrList.get(i))) {
                arrList.remove(i);
            }
        }
        spinner_SelectStatus.setItems(arrList);
        itemSelected = arrList.get(0);
        spinner_SelectStatus.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                itemSelected = arrList.get(position);

            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(model.getJobLocation()) && itemSelected.equals("PUBLISHED")) {
                    CommonMethod.showToastMessage(ctx, "Please enter job location");
                    openEditJobScreen();
                } else if (model.getSkills()!=null&&TextUtils.isEmpty(model.getSkills().getCategory()) && itemSelected.equals("PUBLISHED")) {
                    CommonMethod.showToastMessage(ctx, "Please add skills.");
                    openEditJobScreen();

                } else if (model.getBudget()!=null&& Integer.parseInt(model.getBudget())==0 && itemSelected.equals("PUBLISHED")) {
                    CommonMethod.showToastMessage(ctx, "Max pay scale value should be greater than 0");
                    openEditJobScreen();

                }else {
                    updateJobStatus(itemSelected, dialog);
                }

            }
        });

        dialog.show();
    }

    private void initView() {
        btnApply=(Button)findViewById(R.id.btnApply); 
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerJobApplied = (RecyclerView) findViewById(R.id.recyclerJobApplied);
        imgApplied = (ImageView) findViewById(R.id.imgApplied);
        ivGoogleMap = (ImageView) findViewById(R.id.ivGoogleMap);
        tvJob = (TextView) findViewById(R.id.tvJob);
        tvName = (TextView) findViewById(R.id.tvName);
        tvPurpose = (TextView) findViewById(R.id.tvPurpose);
        tvRating = (TextView) findViewById(R.id.tvRating);
        tvBudget = (TextView) findViewById(R.id.tvBudget);
        tvDays = (TextView) findViewById(R.id.tvDays);
        tvDateRange = (TextView) findViewById(R.id.tvDateRange);
        tvProjectDesc = (TextView) findViewById(R.id.tvProjectDesc);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvJobType = (TextView) findViewById(R.id.tvJobType);
        tvApplied = (TextView) findViewById(R.id.tvApplied);
        ivPic = (RoundedImageView) findViewById(R.id.ivPic);
        tvGetDirection = (TextView) findViewById(R.id.tvGetDirection);
        ivMenu = (ImageView) findViewById(R.id.ivMenu);
        ivShare = (ImageView) findViewById(R.id.ivShare);
        ivFav = (ImageView) findViewById(R.id.ivFav);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        rlBottom = (RelativeLayout) findViewById(R.id.rlBottom);
        ivMenu.setOnClickListener(this);
        tvGetDirection.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        imgApplied.setOnClickListener(this);
        rlBottom.setOnClickListener(this);
        tvApplied.setOnClickListener(this);
        ivGoogleMap.setOnClickListener(this);
        ivFav.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivMenu.setOnClickListener(this);
        btnApply.setOnClickListener(this);
    }
    private void callApplyJobAPI() {
        ApplyJobController anInterface = GetRetrofit.getInstance().create(ApplyJobController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        JSONObject json = new JSONObject();
        try {

            json.put("jobID", model.getJobId());

            json.put("appliedBy", objSPS.getValueFromShared_Pref(Constants.ID));

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
finish();

                                    } else {
                                        SweetAlertController.getInstance().showErrorDialog(ctx, SweetAlertDialog.ERROR_TYPE, model.getMessage(), getString(R.string.app_name));
                                    }
                                }
                            } else {
                                CommonMethod.cancelDialog(dialog,ctx);
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
            case R.id.ivShare:

//                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//                String shareBody =model.getJobTitle()
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, model.getjo);
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//                startActivity(Intent.createChooser(sharingIntent, "Share via"));

                break;
            case R.id.ivFav:
                if (isFavourite == 1) {
                    if (model.isSavedAsFav()) {
                        removeFavJob();

                    } else {
                        addFavJob();

                    }
                } else {
                    if (model.isSavedAsFav()) {
                        removeFavJob();
                        
                    } else {
                        addFavJob();
                    }
                }
                break;
            case R.id.ivMenu:
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(ctx, ivMenu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.profile_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Edit job")) {
                            openEditJobScreen();
                        } else {
                            openUpdateStatus();

                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
                break;
            case R.id.imgApplied:

                getApplicantList();
                break;
            case R.id.rlBottom:

                getApplicantList();
                break;
            case R.id.btnApply:
                callApplyJobAPI();
                break;
            case R.id.tvApplied:

                getApplicantList();
                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvGetDirection:
                Intent i = new Intent(JobDetailRecruiterActivity.this, GetDirectionActivity.class);
                i.putExtra("LAT", model.getLatitude());
                i.putExtra("LONGI", model.getLongitude());
                startActivity(i);
//                Uri gmmIntentUri = Uri.parse("google.navigation:q="+model.getLattitude()+","+model.getLongitude());
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//               // mapIntent.setPackage("com.google.android.apps.maps");
//                startActivity(mapIntent);
                break;
            case R.id.ivGoogleMap:
                Intent i1 = new Intent(JobDetailRecruiterActivity.this, GetDirectionActivity.class);
                i1.putExtra("LAT", model.getLatitude());
                i1.putExtra("LONGI", model.getLongitude());
                startActivity(i1);
//                Uri gmmIntentUri1 = Uri.parse("google.navigation:q="+model.getLattitude()+","+model.getLongitude());
//                Intent mapIntent1 = new Intent(Intent.ACTION_VIEW, gmmIntentUri1);
//              //  mapIntent.setPackage("com.google.android.apps.maps");
//                startActivity(mapIntent1);
                break;
        }
    }

    private void openEditJobScreen() {

        Intent i = new Intent(ctx, PostJobStepAActivity.class);
        i.putExtra("DATA", model);
        startActivity(i);
    }

    @Override
    public void onFavAdd(int pos) {
        addFavApplicant(pos);
    }

    @Override
    public void onAccept(int pos) {
        acceptApiCall(pos, "ACCEPT");
    }

    @Override
    public void onReject(int pos) {
        acceptApiCall(pos, "REJECT");

    }
}

