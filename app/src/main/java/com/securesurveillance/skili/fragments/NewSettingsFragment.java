package com.securesurveillance.skili.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.JsonSyntaxException;
import com.securesurveillance.skili.OnBoardingActivity;
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.apiHandler.API;
import com.securesurveillance.skili.apiHandler.APIResponseListener;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.GetRetrofit;
import com.securesurveillance.skili.apiinterfaces.DeactivateAccountController;
import com.securesurveillance.skili.apiinterfaces.DeleteEducationController;
import com.securesurveillance.skili.apiinterfaces.GetFav_JobListController;
import com.securesurveillance.skili.apiinterfaces.GetSettingController;
import com.securesurveillance.skili.apiinterfaces.PostJobController;
import com.securesurveillance.skili.apiinterfaces.UpdateSettingController;
import com.securesurveillance.skili.materialspinner.MaterialSpinner;
import com.securesurveillance.skili.model.responsemodel.GetFavJobListModel;
import com.securesurveillance.skili.model.responsemodel.GetSettingResponseModel;
import com.securesurveillance.skili.model.responsemodel.LoginModel;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.SharePreferanceWrapperSingleton;
import com.securesurveillance.skili.utility.SweetAlertController;

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

import static com.securesurveillance.skili.fragments.HomeFragment.pager;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewSettingsFragment extends Fragment {
    TextView tvActive;
    SharePreferanceWrapperSingleton objSPS;
    MaterialSpinner spinner_MainCategory,spinner_MainCategoryMobile,spinner_MainCategoryEmail,spinner_MainCategoryAddress;
    Switch switchToggle;
    List<String> arrMainCategoryName;
    ToggleButton toggleReceiveSms, toggleReceiveEmail, toggleRecruiter;
    public NewSettingsFragment() {
        // Required empty public constructor
    }

    private void updateSettingApi() {
        UpdateSettingController anInterface = GetRetrofit.getInstance().create(UpdateSettingController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        JSONObject json = new JSONObject();
        try {
            json.put("activeAsFreelancerRecruiter", toggleRecruiter.isChecked());
            json.put("recevieEmail", toggleReceiveEmail.isChecked());
            json.put("recevieSms", toggleReceiveSms.isChecked());

            json.put("whoCanSeePost", arrMainCategoryName.get(spinner_MainCategory.getSelectedIndex()));
            json.put("whoCanSeeMobile", arrMainCategoryName.get(spinner_MainCategoryMobile.getSelectedIndex()));
            json.put("whoCanSeeEmail", arrMainCategoryName.get(spinner_MainCategoryEmail.getSelectedIndex()));
            json.put("whoCanSeeAddress", arrMainCategoryName.get(spinner_MainCategoryAddress.getSelectedIndex()));

            json.put("profileId", objSPS.getValueFromShared_Pref(Constants.ID));
        } catch (Exception e) {

        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
        Call<LoginModel> call = anInterface.getResponse( objSPS.getValueFromShared_Pref(Constants.TOKEN),
                body);
        API.callRun
                (getActivity(), false, call, new APIResponseListener() {
                    @Override
                    public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                        if (getActivity()!=null&&!getActivity().isFinishing()) {
                            if (response.code() == 200) {
                                CommonMethod.printAPIResponse(response.body());
                                if (response.body() instanceof LoginModel) {
                                    LoginModel model = (LoginModel) response.body();
                                    CommonMethod.cancelDialog(dialog, getActivity());
                                    if (model.isStatus()) {
                                     //   CommonMethod.showToastMessage(getActivity(), model.getMessage());
                                    } else {
                                      //  SweetAlertController.getInstance().showErrorDialog(getActivity(), SweetAlertDialog.ERROR_TYPE, model.getMessage(), getString(R.string.app_name));
                                    }
                                }
                            } else {
                                CommonMethod.cancelDialog(dialog, getActivity());
                                CommonMethod.showApiMsgToast(getActivity(), response.code());
                            }
                        }
                    }


                    @Override
                    public void onFailure(Call call, final Throwable t) {
                        if (getActivity()!=null&&!getActivity().isFinishing()) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (t instanceof UnknownHostException) {
                                        CommonMethod.showToastMessage(getActivity(), getString(R.string.UNKNOWN_HOST));
                                    } else if (t instanceof ConnectException) {
                                        CommonMethod.showToastMessage(getActivity(), getString(R.string.UNKNOWN_HOST));
                                    } else if (t instanceof SocketTimeoutException) {
                                        CommonMethod.showToastMessage(getActivity(), getString(R.string.SOCKET_TIME_OUT));
                                    } else if (t instanceof SocketException) {
                                        CommonMethod.showToastMessage(getActivity(), getString(R.string.UNABLE_TO_CONNECT));
                                    } else if (t instanceof JsonSyntaxException) {
                                        CommonMethod.showToastMessage(getActivity(), getString(R.string.JSON_SYNTAX));
                                    } else {
                                        CommonMethod.showToastMessage(getActivity(), getString(R.string.UNKNOWN_ERROR));
                                    }
                                    t.printStackTrace();
                                }
                            });
                        }
                    }
                });
    }

    private void getSetting() {
        GetSettingController anInterface = GetRetrofit.getInstance().create(GetSettingController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));

        Call<GetSettingResponseModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN), objSPS.getValueFromShared_Pref(Constants.ID));
        API.callRun(getActivity(), true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (getActivity()!=null&&!getActivity().isFinishing()) {

                    if (response.code() == 200) {
                        if (response.body() instanceof GetSettingResponseModel) {
                            GetSettingResponseModel model = (GetSettingResponseModel) response.body();
                            CommonMethod.cancelDialog(dialog, getActivity());
                            if (model.isStatus()) {
                                toggleReceiveEmail.setChecked(model.getResult().isRecevieEmail());
                                toggleReceiveSms.setChecked(model.getResult().isRecevieSms());
                                toggleRecruiter.setChecked(model.getResult().isActiveAsFreelancerRecruiter());
                                if (model.getResult().getWhoCanSeePost().equals("ALL")) {
                                    spinner_MainCategory.setSelectedIndex(0);
                                } else if (model.getResult().getWhoCanSeePost().equals("RECRUITER")) {
                                    spinner_MainCategory.setSelectedIndex(1);
                                } else if (model.getResult().getWhoCanSeePost().equals("FREELANCERS")) {
                                    spinner_MainCategory.setSelectedIndex(2);
                                } else if (model.getResult().getWhoCanSeePost().equals("SELF")) {
                                    spinner_MainCategory.setSelectedIndex(3);
                                }
                                if (model.getResult().getWhoCanSeeAddress().equals("ALL")) {
                                    spinner_MainCategoryAddress.setSelectedIndex(0);
                                } else if (model.getResult().getWhoCanSeeAddress().equals("RECRUITER")) {
                                    spinner_MainCategoryAddress.setSelectedIndex(1);
                                } else if (model.getResult().getWhoCanSeeAddress().equals("FREELANCERS")) {
                                    spinner_MainCategoryAddress.setSelectedIndex(2);
                                } else if (model.getResult().getWhoCanSeeAddress().equals("SELF")) {
                                    spinner_MainCategoryAddress.setSelectedIndex(3);
                                }
                                if (model.getResult().getWhoCanSeeEmail().equals("ALL")) {
                                    spinner_MainCategoryEmail.setSelectedIndex(0);
                                } else if (model.getResult().getWhoCanSeeEmail().equals("RECRUITER")) {
                                    spinner_MainCategoryEmail.setSelectedIndex(1);
                                } else if (model.getResult().getWhoCanSeeEmail().equals("FREELANCERS")) {
                                    spinner_MainCategoryEmail.setSelectedIndex(2);
                                } else if (model.getResult().getWhoCanSeeEmail().equals("SELF")) {
                                    spinner_MainCategoryEmail.setSelectedIndex(3);
                                }
                                if (model.getResult().getWhoCanSeeMobile().equals("ALL")) {
                                    spinner_MainCategoryMobile.setSelectedIndex(0);
                                } else if (model.getResult().getWhoCanSeeMobile().equals("RECRUITER")) {
                                    spinner_MainCategoryMobile.setSelectedIndex(1);
                                } else if (model.getResult().getWhoCanSeeMobile().equals("FREELANCERS")) {
                                    spinner_MainCategoryMobile.setSelectedIndex(2);
                                } else if (model.getResult().getWhoCanSeeMobile().equals("SELF")) {
                                    spinner_MainCategoryMobile.setSelectedIndex(3);
                                }
                            } else {
                                SweetAlertController.getInstance().showErrorDialog(getActivity(), SweetAlertDialog.ERROR_TYPE,
                                        model.getMessage(), getString(R.string.app_name));

                            }
                        }
                    } else {
                        CommonMethod.cancelDialog(dialog, getActivity());
                        CommonMethod.showApiMsgToast(getActivity(), response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call call, final Throwable t) {
                if (getActivity()!=null&&!getActivity().isFinishing()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (t instanceof UnknownHostException) {
                                CommonMethod.showToastMessage(getActivity(), getString(R.string.UNKNOWN_HOST));
                            } else if (t instanceof ConnectException) {
                                CommonMethod.showToastMessage(getActivity(), getString(R.string.UNKNOWN_HOST));
                            } else if (t instanceof SocketTimeoutException) {
                                CommonMethod.showToastMessage(getActivity(), getString(R.string.SOCKET_TIME_OUT));
                            } else if (t instanceof SocketException) {
                                CommonMethod.showToastMessage(getActivity(), getString(R.string.UNABLE_TO_CONNECT));
                            } else if (t instanceof JsonSyntaxException) {
                                CommonMethod.showToastMessage(getActivity(), getString(R.string.JSON_SYNTAX));
                            } else {
                                CommonMethod.showToastMessage(getActivity(), getString(R.string.UNKNOWN_ERROR));
                            }
                            t.printStackTrace();
                        }
                    });
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_new_settings, container, false);
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(getActivity());
        tvActive = (TextView) view.findViewById(R.id.tvActive);
        switchToggle = (Switch) view.findViewById(R.id.switchToggle);
        toggleReceiveSms = (ToggleButton) view.findViewById(R.id.toggleReceiveSms);
        toggleReceiveEmail = (ToggleButton) view.findViewById(R.id.toggleReceiveEmail);
        toggleRecruiter = (ToggleButton) view.findViewById(R.id.toggleRecruiter);

        spinner_MainCategory = (MaterialSpinner) view.findViewById(R.id.spinner_MainCategory);
        spinner_MainCategoryMobile = (MaterialSpinner) view.findViewById(R.id.spinner_MainCategoryMobile);
        spinner_MainCategoryEmail = (MaterialSpinner) view.findViewById(R.id.spinner_MainCategoryEmail);
        spinner_MainCategoryAddress = (MaterialSpinner) view.findViewById(R.id.spinner_MainCategoryAddress);

        arrMainCategoryName = new ArrayList<>();
        arrMainCategoryName.add("ALL");
        arrMainCategoryName.add("RECRUITER");
        arrMainCategoryName.add("FREELANCERS");
        arrMainCategoryName.add("SELF");

        spinner_MainCategory.setItems(arrMainCategoryName);
        spinner_MainCategoryMobile.setItems(arrMainCategoryName);
        spinner_MainCategoryEmail.setItems(arrMainCategoryName);
        spinner_MainCategoryAddress.setItems(arrMainCategoryName);

        String role = objSPS.getValueFromShared_Pref(Constants.ROLE);

        if (role.equalsIgnoreCase("Individual")) {
            tvActive.setText("Active as JobSeeker");
        } else {
            tvActive.setText("Active as Recruiter");
        }
        switchToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    callDeactivateApi();

                }
            }
        });
        getSetting();

        toggleReceiveSms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateSettingApi();
            }
        });
        toggleReceiveEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateSettingApi();
            }
        });
        toggleRecruiter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateSettingApi();
            }
        });
        spinner_MainCategory.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                    updateSettingApi();


            }
        });
        spinner_MainCategoryMobile.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                updateSettingApi();


            }
        }); spinner_MainCategoryEmail.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                updateSettingApi();


            }
        }); spinner_MainCategoryAddress.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                updateSettingApi();


            }
        });

        return view;
    }

    private void callDeactivateApi() {
        DeactivateAccountController anInterface = GetRetrofit.getInstance().create(DeactivateAccountController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));


        Call<LoginModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN), objSPS.getValueFromShared_Pref(Constants.ID));
        API.callRun(getActivity(), true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (getActivity()!=null&&!getActivity().isFinishing()) {
                    if (response.code() == 200) {
                        if (response.body() instanceof LoginModel) {
                            LoginModel model = (LoginModel) response.body();
                            CommonMethod.cancelDialog(dialog, getActivity());
                            if (model.isStatus()) {
                                CommonMethod.showToastMessage(getActivity(), model.getMessage());
                                SharePreferanceWrapperSingleton.getSingletonInstance().setValueToSharedPref(Constants.LOGGED_IN, false);
                                SharePreferanceWrapperSingleton.getSingletonInstance().setValueToSharedPref(Constants.SIGNEDUP, false);
                                SharePreferanceWrapperSingleton.getSingletonInstance().setValueToSharedPref(Constants.ID, "");

                                Intent i = new Intent(getContext(), OnBoardingActivity.class);

                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                startActivity(i);
                            } else {
                                SweetAlertController.getInstance().showErrorDialog(getActivity(), SweetAlertDialog.ERROR_TYPE,
                                        model.getMessage(), getString(R.string.app_name));

                            }
                        }
                    } else {
                        CommonMethod.cancelDialog(dialog, getActivity());
                        CommonMethod.showApiMsgToast(getActivity(), response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call call, final Throwable t) {
                if (getActivity()!=null&&!getActivity().isFinishing()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (t instanceof UnknownHostException) {
                                CommonMethod.showToastMessage(getActivity(), getString(R.string.UNKNOWN_HOST));
                            } else if (t instanceof ConnectException) {
                                CommonMethod.showToastMessage(getActivity(), getString(R.string.UNKNOWN_HOST));
                            } else if (t instanceof SocketTimeoutException) {
                                CommonMethod.showToastMessage(getActivity(), getString(R.string.SOCKET_TIME_OUT));
                            } else if (t instanceof SocketException) {
                                CommonMethod.showToastMessage(getActivity(), getString(R.string.UNABLE_TO_CONNECT));
                            } else if (t instanceof JsonSyntaxException) {
                                CommonMethod.showToastMessage(getActivity(), getString(R.string.JSON_SYNTAX));
                            } else {
                                CommonMethod.showToastMessage(getActivity(), getString(R.string.UNKNOWN_ERROR));
                            }
                            t.printStackTrace();
                        }
                    });
                }
            }
        });

    }


}
