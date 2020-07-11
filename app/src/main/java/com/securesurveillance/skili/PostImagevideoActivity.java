package com.securesurveillance.skili;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.securesurveillance.skili.apiinterfaces.DeleteEducationController;
import com.securesurveillance.skili.apiinterfaces.DeletePostController;
import com.squareup.picasso.Picasso;
import com.securesurveillance.skili.adapter.CloudAdapter;
import com.securesurveillance.skili.apiHandler.API;
import com.securesurveillance.skili.apiHandler.APIResponseListener;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.GetRetrofit;
import com.securesurveillance.skili.apiinterfaces.GetCategoryController;
import com.securesurveillance.skili.apiinterfaces.PostJobController;
import com.securesurveillance.skili.materialspinner.MaterialSpinner;
import com.securesurveillance.skili.materialspinner.MaterialSpinnerMultiple;
import com.securesurveillance.skili.model.SkillModel;
import com.securesurveillance.skili.model.requestmodel.PostJobRequestModel;
import com.securesurveillance.skili.model.responsemodel.GetCategoryModel;
import com.securesurveillance.skili.model.responsemodel.LoginModel;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.SharePreferanceWrapperSingleton;
import com.securesurveillance.skili.utility.SweetAlertController;

import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;


public class PostImagevideoActivity extends Activity {

    TextView tv_Title;
    ImageView iv_LeftOption, ivPost,iv_RightOption;
    private SharePreferanceWrapperSingleton objSPS;
    Context ctx;
    tcking.github.com.giraffeplayer2.VideoView videoView;
    MediaController mediaController;
RelativeLayout rl_Header;
String postId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_img_video);
        ctx = this;
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(ctx);
        tv_Title = (TextView) findViewById(R.id.tv_Title);
        iv_LeftOption = (ImageView) findViewById(R.id.iv_LeftOption);
        iv_RightOption=(ImageView)findViewById(R.id.iv_RightOption);
        ivPost = (ImageView) findViewById(R.id.ivPost);
        videoView = (tcking.github.com.giraffeplayer2.VideoView) findViewById(R.id.videoView);
        videoView.getVideoInfo().setPortraitWhenFullScreen(true);
        postId=getIntent().getStringExtra("POSTID");
        tv_Title.setText("POST");
        rl_Header=(RelativeLayout)findViewById(R.id.rl_Header);
        iv_LeftOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
if(!getIntent().getBooleanExtra("ISME",false)){
    iv_RightOption.setVisibility(View.GONE);
}

        iv_RightOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePost();
            }
        });
        if (getIntent().hasExtra("IMAGE_URL")) {
            Picasso.get().load(getIntent().getStringExtra("IMAGE_URL")).into(ivPost);
        } else {
            ivPost.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoPath(getIntent().getStringExtra("VIDEO_URL")).getPlayer().start();

        }

    }
    private void deletePost() {
        DeletePostController anInterface = GetRetrofit.getInstance().create(DeletePostController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));


        Call<LoginModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN),postId);
        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    if (response.code() == 200) {
                        if (response.body() instanceof LoginModel) {
                            LoginModel model = (LoginModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (model.isStatus()) {
                               finish();
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
    protected void onResume() {
        super.onResume();
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            rl_Header.setVisibility(View.GONE);
        }else{
            rl_Header.setVisibility(View.VISIBLE);

        }
    }
}