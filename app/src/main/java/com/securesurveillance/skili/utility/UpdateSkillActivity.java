package com.securesurveillance.skili.utility;//package com.securesurveillance.skili.utility;
//
//import android.annotation.TargetApi;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.drawable.ColorDrawable;
//import android.graphics.drawable.Drawable;
//import android.graphics.drawable.ScaleDrawable;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.annotation.RequiresApi;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.CardView;
//import android.text.TextUtils;
//import android.util.DisplayMetrics;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonSyntaxException;
//import com.google.gson.reflect.TypeToken;
//import com.securesurveillance.skili.MyApplication;
//import com.securesurveillance.skili.OnBoardingActivity;
//import com.securesurveillance.skili.R;
//import com.securesurveillance.skili.SliderActivity;
//import com.securesurveillance.skili.adapter.SkillAdaptorCb;
//import com.securesurveillance.skili.adapter.SpinnerAdaptor;
//import com.securesurveillance.skili.adapter.SpinnerSkillAdaptor;
//import com.securesurveillance.skili.apiHandler.API;
//import com.securesurveillance.skili.apiHandler.APIResponseListener;
//import com.securesurveillance.skili.apiHandler.CommonMethod;
//import com.securesurveillance.skili.apiHandler.GetRetrofit;
//import com.securesurveillance.skili.apiinterfaces.GetCategoryController;
//import com.securesurveillance.skili.apiinterfaces.IndividualController;
//import com.securesurveillance.skili.model.responsemodel.GetCategoryModel;
//import com.securesurveillance.skili.model.responsemodel.LoginModel;
//import com.securesurveillance.skili.model.SkillModel;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.net.ConnectException;
//import java.net.SocketException;
//import java.net.SocketTimeoutException;
//import java.net.UnknownHostException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import cn.pedant.SweetAlert.SweetAlertDialog;
//import okhttp3.MediaType;
//import okhttp3.RequestBody;
//import retrofit2.Call;
//import retrofit2.Response;
//
///**
// * Created by adarsh on 8/17/2018.
// */
//
//public class UpdateSkillActivity extends AppCompatActivity implements SkillAdaptorCb.CbListener {
//    private EditText etSubCategory;
//    private ArrayList<SkillModel.InnerSkillModel> selectedArrListCategory = new ArrayList<>();
//    Dialog dialogCompany;
//    ArrayList<SkillModel.InnerSkillModel> checkedValue = new ArrayList<>();
//    ArrayList<Integer> checkedPosition = new ArrayList<>();
//    Context ctx;
//    ListView listView;
//    SpinnerSkillAdaptor spinnerSkillAdaptor;
//    private SharePreferanceWrapperSingleton objSPS;
//    Button btnSubmit;
//    MyApplication application;
//    ArrayList<SkillModel.InnerSkillModel> arrSubCategory = new ArrayList<>();
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signup_individual_skill);
//        ctx = this;
//        etSubCategory = (EditText) findViewById(R.id.etSubCategory);
//        listView = (ListView) findViewById(R.id.listView);
//        btnSubmit = (Button) findViewById(R.id.btnSubmit);
//        application = (MyApplication) getApplicationContext();
//
//        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
//        objSPS.setPref(ctx);
//        getBundleData();
//        callCategoryApi();
//
//        Drawable drawable = getResources().getDrawable(R.drawable.downarrow_black);
//        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * 0.6),
//                (int) (drawable.getIntrinsicHeight() * 0.6));
//        ScaleDrawable sd = new ScaleDrawable(drawable, 0, 40, 40);
//        etSubCategory.setCompoundDrawables(null, null, sd.getDrawable(), null);
//
//        etSubCategory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                    showSubCategoryDialog();
//
//
//            }
//        });
//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @TargetApi(Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onClick(View v) {
//
//                    //proceed
//                    callIndividualSignupApi();
//            }
//        });
//
//
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void callIndividualSignupApi() {
//        IndividualController anInterface = GetRetrofit.getInstance().create(IndividualController.class);
//        Map<String, String> data = new HashMap<>();
//        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
//        JSONObject json = new JSONObject();
//        try {
//            Gson gson = new Gson();
//            JsonElement element = gson.toJsonTree(selectedArrListCategory, new TypeToken<List<SkillModel.InnerSkillModel>>() {}.getType());
//            json.put("subCategories", new JSONArray (element.getAsJsonArray().toString()));
//           } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
//        Call<LoginModel> call = anInterface.getResponse(data,
//                body);
//        API.callRun(ctx, true, call, new APIResponseListener() {
//            @Override
//            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
//                if (!isFinishing()) {
//                    if (response.code() == 200) {
//                        CommonMethod.printAPIResponse(response.body());
//                        if (response.body() instanceof LoginModel) {
//                            LoginModel model = (LoginModel) response.body();
//                            CommonMethod.cancelDialog(dialog, ctx);
//                            if (model.isStatus()) {
//                                // CommonMethod.showToastMessage(ctx, model.getMessage());
//                                objSPS.setValueToSharedPref(Constants.LOGGED_IN, true);
//                                objSPS.setValueToSharedPref(Constants.ID, model.getResult().get_id());
//                                application.set_id(model.getResult().get_id());
//                                application.setEmail(model.getResult().getEmail());
//                                application.setUseName(model.getResult().getUsername());
//
//                                Intent i = new Intent(ctx, SliderActivity.class);
//                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(i);
//                                finish();
//                            } else {
//                                SweetAlertController.getInstance().showErrorDialog(ctx, SweetAlertDialog.ERROR_TYPE, model.getMessage(), getString(R.string.app_name));
//                            }
//                        }
//                    } else {
//                        CommonMethod.cancelDialog(dialog, ctx);
//                        CommonMethod.showApiMsgToast(ctx, response.code());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call call, final Throwable t) {
//                if (!isFinishing()) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (t instanceof UnknownHostException) {
//                                CommonMethod.showToastMessage(ctx, getString(R.string.UNKNOWN_HOST));
//                            } else if (t instanceof ConnectException) {
//                                CommonMethod.showToastMessage(ctx, getString(R.string.UNKNOWN_HOST));
//                            } else if (t instanceof SocketTimeoutException) {
//                                CommonMethod.showToastMessage(ctx, getString(R.string.SOCKET_TIME_OUT));
//                            } else if (t instanceof SocketException) {
//                                CommonMethod.showToastMessage(ctx, getString(R.string.UNABLE_TO_CONNECT));
//                            } else if (t instanceof JsonSyntaxException) {
//                                CommonMethod.showToastMessage(ctx, getString(R.string.JSON_SYNTAX));
//                            } else {
//                                CommonMethod.showToastMessage(ctx, getString(R.string.UNKNOWN_ERROR));
//                            }
//                            t.printStackTrace();
//                        }
//                    });
//                }
//            }
//        });
//    }
//
//
//    private void getBundleData() {
//
//
//
//    }
//
//
//    private void showSubCategoryDialog() {
//        selectedArrListCategory.clear();
//        dialogCompany = new Dialog(ctx, android.R.style.Theme_Material_Light_Dialog_NoActionBar_MinWidth);
//
//        dialogCompany.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
//        dialogCompany.setContentView(R.layout.dialog_spinner);
//        dialogCompany.setCancelable(false);
//        dialogCompany.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        getWindow().setGravity(Gravity.CENTER);
//        LinearLayout llDialog = (LinearLayout) dialogCompany.findViewById(R.id.llDialog);
//        CardView cvHeader = (CardView) dialogCompany.findViewById(R.id.cvHeader);
//        cvHeader.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int screenHeight = displayMetrics.heightPixels;
//        int screenWidth = displayMetrics.widthPixels;
//        llDialog.getLayoutParams().width = (int) (screenWidth * .9);
//        llDialog.getLayoutParams().height = (int) (screenHeight * .7);
//        TextView tvHeaderSpinner = (TextView) dialogCompany.findViewById(R.id.tvHeaderSpinner);
//        ListView listView = (ListView) dialogCompany.findViewById(R.id.listView);
//        ImageView ivClose = (ImageView) dialogCompany.findViewById(R.id.ivClose);
//        ivClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogCompany.dismiss();
//            }
//        });
//        SkillAdaptorCb spinnerAdaptor = new SkillAdaptorCb(ctx, arrSubCategory, checkedPosition);
//        listView.setAdapter(spinnerAdaptor);
//        tvHeaderSpinner.setText("Select Sub-Category");
//
//        dialogCompany.show();
//
//
//    }
//
//    @Override
//    public void onItemClicked(View v, int pos) {
//        CheckBox cb = (CheckBox) v;//.findViewById(R.id.cb);
//        selectedArrListCategory.clear();
//        // cb.performClick();
//        if (cb.isChecked()) {
//            checkedValue.add(arrSubCategory.get(pos));
//            checkedPosition.add(pos);
//
//        } else if (!cb.isChecked()) {
//            checkedValue.remove(arrSubCategory.get(pos));
//            if (checkedPosition.contains(pos)) {
//                for (int i = 0; i < checkedPosition.size(); i++) {
//                    if (checkedPosition.get(i) == pos) {
//                        checkedPosition.remove(i);
//                        selectedArrListCategory.remove(arrSubCategory.get(i));
//                    }
//                }
//            }
//        }
//
//        for (int i = 0; i < checkedValue.size(); i++) {
////            if (TextUtils.isEmpty(str)) {
////                str = checkedValue.get(i);
////            } else {
////                str = str + newLine + newLine + checkedValue.get(i);
////
////            }
//            if (!selectedArrListCategory.contains(checkedValue.get(i))) {
//                selectedArrListCategory.add(checkedValue.get(i));
//            }
//
//        }
//        if (checkedValue.size() == arrSubCategory.size()) {
//            dialogCompany.dismiss();
//        }
//        if (spinnerSkillAdaptor == null) {
//            spinnerSkillAdaptor = new SpinnerSkillAdaptor(ctx, selectedArrListCategory);
//            listView.setAdapter(spinnerSkillAdaptor);
//        } else {
//            spinnerSkillAdaptor.notifyDataSetChanged();
//        }
//    }
//
//
//
//    private void callCategoryApi() {
//        GetCategoryController anInterface = GetRetrofit.getInstance().create(GetCategoryController.class);
//        Map<String, String> data = new HashMap<>();
//        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
//
//
//        Call<GetCategoryModel> call = anInterface.getResponse(
//                data);
//        API.callRun(ctx, true, call, new APIResponseListener() {
//            @Override
//            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
//                if (!isFinishing()) {
//                    if (response.code() == 200) {
//                        if (response.body() instanceof GetCategoryModel) {
//                            GetCategoryModel model = (GetCategoryModel) response.body();
//                            CommonMethod.cancelDialog(dialog, ctx);
//                            if (model.isStatus()) {
//                                arrSubCategory = new ArrayList<>();
////                                arrSubCategory = model.getResult().g;
//
//                            } else {
//                                SweetAlertController.getInstance().showErrorDialogClickListener(ctx, SweetAlertDialog.ERROR_TYPE, "Failed to get Category list", getString(R.string.app_name), "Try again", new SweetAlertController.SweetAlertClickListener() {
//                                    @Override
//                                    public void onSweetAlertClicked() {
//                                        callCategoryApi();
//                                    }
//                                });
//
//                            }
//                        }
//                    } else {
//                        CommonMethod.cancelDialog(dialog, ctx);
//                        CommonMethod.showApiMsgToast(ctx, response.code());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call call, final Throwable t) {
//                if (!isFinishing()) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (t instanceof UnknownHostException) {
//                                CommonMethod.showToastMessage(ctx, getString(R.string.UNKNOWN_HOST));
//                            } else if (t instanceof ConnectException) {
//                                CommonMethod.showToastMessage(ctx, getString(R.string.UNKNOWN_HOST));
//                            } else if (t instanceof SocketTimeoutException) {
//                                CommonMethod.showToastMessage(ctx, getString(R.string.SOCKET_TIME_OUT));
//                            } else if (t instanceof SocketException) {
//                                CommonMethod.showToastMessage(ctx, getString(R.string.UNABLE_TO_CONNECT));
//                            } else if (t instanceof JsonSyntaxException) {
//                                CommonMethod.showToastMessage(ctx, getString(R.string.JSON_SYNTAX));
//                            } else {
//                                CommonMethod.showToastMessage(ctx, getString(R.string.UNKNOWN_ERROR));
//                            }
//                            t.printStackTrace();
//                        }
//                    });
//                }
//            }
//        });
//
//    }
//}
