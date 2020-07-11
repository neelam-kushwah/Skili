package com.securesurveillance.skili.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.eftimoff.androidplayer.Player;
import com.eftimoff.androidplayer.actions.arc.CurveAction;
import com.eftimoff.androidplayer.actions.property.PropertyAction;
import com.eftimoff.androidplayer.listeners.PlayerEndListener;
import com.eftimoff.androidplayer.listeners.PlayerStartListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.SkilliSignupActivity;
import com.securesurveillance.skili.adapter.HomeJobsAdapter;
import com.securesurveillance.skili.apiHandler.API;
import com.securesurveillance.skili.apiHandler.APIResponseListener;
import com.securesurveillance.skili.apiHandler.AppConstants;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.GetRetrofit;
import com.securesurveillance.skili.apiinterfaces.FeedbackController;
import com.securesurveillance.skili.apiinterfaces.SearchJobController;
import com.securesurveillance.skili.apiinterfaces.UploadProfilePicController;
import com.securesurveillance.skili.compressor.FileUtil;
import com.securesurveillance.skili.model.requestmodel.SearchJobsDefaultRequestModel;
import com.securesurveillance.skili.model.responsemodel.LoginModel;
import com.securesurveillance.skili.model.responsemodel.ProfilePicResponseModel;
import com.securesurveillance.skili.model.responsemodel.SearchJobsResponse;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.MarshMPermission;
import com.securesurveillance.skili.utility.SharePreferanceWrapperSingleton;
import com.securesurveillance.skili.utility.SweetAlertController;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by adarsh on 6/19/2018.
 */


public class FeedbackFragment extends Fragment {
    RelativeLayout rlAddSkill;
    private final int GALLERY_IMAGE_REQUEST_CODE = 200;
    private SharePreferanceWrapperSingleton objSPS;
    ProfilePicResponseModel model = new ProfilePicResponseModel();
    EditText etTitle;
    EditText etFeedback;
    Button btnSubmit;
ImageView ivThumbNail;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(getActivity());
        rlAddSkill = (RelativeLayout) view.findViewById(R.id.rlAddSkill);
        etTitle = (EditText) view.findViewById(R.id.etTitle);
        etFeedback = (EditText) view.findViewById(R.id.etFeedback);
        ivThumbNail=(ImageView)view.findViewById(R.id.ivThumbNail);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        rlAddSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCameraAndStoragePermission();

            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etTitle.getText().toString().trim())) {
                    Toast.makeText(getActivity(), "Please enter title.", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(etFeedback.getText().toString().trim())) {
                    Toast.makeText(getActivity(), "Please enter feedback.", Toast.LENGTH_LONG).show();

                } else {
                    callSubmitFeedbackApi();
                }
            }
        });
        return view;
    }

    private void callSubmitFeedbackApi() {
        FeedbackController anInterface = GetRetrofit.getInstance().create(FeedbackController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", CommonMethod.getFirstCapName(etTitle.getText().toString().trim()));
            jsonObject.put("feedback", CommonMethod.getFirstCapName(etFeedback.getText().toString().trim()));
            jsonObject.put("mobile", objSPS.getValueFromShared_Pref(Constants.MOBILE));
            jsonObject.put("userid", objSPS.getValueFromShared_Pref(Constants.ID));
            if (model.isStatus()) {
                JSONObject jsonScreenshot = new JSONObject();
                jsonScreenshot.put("bucket", model.getResult().getBucket());
                jsonScreenshot.put("generation", model.getResult().getGeneration());
                jsonScreenshot.put("name", model.getResult().getName());
                jsonObject.put("screenshot", jsonScreenshot);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        Call<LoginModel> call = anInterface.getResponse( objSPS.getValueFromShared_Pref(Constants.TOKEN),
                body);
        API.callRun
                (getActivity(), true, call, new APIResponseListener() {
                    @Override
                    public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                        if (getActivity()!=null&&!getActivity().isFinishing()) {

                            if (response.code() == 200) {
                                CommonMethod.printAPIResponse(response.body());
                                if (response.body() instanceof LoginModel) {
                                    LoginModel loginModel = (LoginModel) response.body();
                                    CommonMethod.cancelDialog(dialog, getActivity());
                                    if (loginModel.isStatus()) {
                                        CommonMethod.showToastMessage(getActivity(), loginModel.getMessage());
                                        etFeedback.setText("");
                                        etTitle.setText("");
                                        ivThumbNail.setImageBitmap(null);
                                        model = new ProfilePicResponseModel();
                                    } else {
                                        SweetAlertController.getInstance().showErrorDialog(getActivity(), SweetAlertDialog.ERROR_TYPE, loginModel.getMessage(), getString(R.string.app_name));
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

    private void checkCameraAndStoragePermission() {
        if (CommonMethod.isMarshMallow()) {
//            if (!MarshMPermission.checkPermission(getActivity(), AppConstants.MPermission.M_CAMERA)) {
//                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, AppConstants.MPermission.M_CAMERA);
//            } else
                if (!MarshMPermission.checkPermission(getActivity(), AppConstants.MPermission.M_READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstants.MPermission.M_READ_EXTERNAL_STORAGE);
            } else {
                selectImage();
            }
        } else {
            selectImage();
        }
    }

    private void onAction(int resultCode, Intent data) throws Exception {
        if (resultCode == RESULT_OK) {
            if (data != null) {
                uploadProfilePic(FileUtil.from(getActivity(), data.getData()));

            } else {
                CommonMethod.showToastMessage(getActivity(), getString(R.string.unable_to_get_image_from_gallery));
            }

        } else if (resultCode == RESULT_CANCELED) {
            CommonMethod.showToastMessage(getActivity(), getString(R.string.image_loading_cancelled_by_user));
        } else {
            CommonMethod.showToastMessage(getActivity(), getString(R.string.sorry_failed_to_capture_image));
        }
    }

    private void uploadProfilePic(File profileFile) {
        UploadProfilePicController anInterface = GetRetrofit.getInstance().create(UploadProfilePicController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        String json = new String();

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), profileFile);

// MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", profileFile.getName(), requestFile);


        Call<ProfilePicResponseModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN),
                body);
        API.callRun(getActivity(), true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (getActivity()!=null&&!getActivity().isFinishing()) {
                    if (response.code() == 200) {
                        CommonMethod.printAPIResponse(response.body());
                        if (response.body() instanceof ProfilePicResponseModel) {
                            model = (ProfilePicResponseModel) response.body();
                            CommonMethod.cancelDialog(dialog, getActivity());
                            if (model.isStatus()) {
                                Picasso.get().load(model.getResult().getMediaLink()).into(ivThumbNail);
                            } else {
                                SweetAlertController.getInstance().showErrorDialog(getActivity(), SweetAlertDialog.ERROR_TYPE, model.getMessage(), getString(R.string.app_name));
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

    private void selectImage() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
//            case AppConstants.MPermission.M_CAMERA:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    checkCameraAndStoragePermission();
//                } else {
//                    CommonMethod.showToastMessage(getActivity(), getString(R.string.permission_denied_you_cannot_access_camera));
//                }
//                break;
            case AppConstants.MPermission.M_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkCameraAndStoragePermission();
                } else {
                    CommonMethod.showToastMessage(getActivity(), getString(R.string.permission_denied_you_cannot_read_external_storage));
                }
                break;
            default:
                break;
        }
    }


}
