package com.securesurveillance.skili.chat;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.securesurveillance.skili.ChooseCategoryActivity;
import com.securesurveillance.skili.OnBoardingActivity;
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.SliderActivity;
import com.securesurveillance.skili.apiHandler.API;
import com.securesurveillance.skili.apiHandler.APIResponseListener;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.GetRetrofit;
import com.securesurveillance.skili.apiinterfaces.GetAppliedJobListController;
import com.securesurveillance.skili.apiinterfaces.GetConversationList_OnetoOneController;
import com.securesurveillance.skili.apiinterfaces.LoginController;
import com.securesurveillance.skili.apiinterfaces.SendMessageController;
import com.securesurveillance.skili.model.ChatModel;
import com.securesurveillance.skili.model.responsemodel.ConversationOnetoOneModel;
import com.securesurveillance.skili.model.responsemodel.GetAppliedJobListModel;
import com.securesurveillance.skili.model.responsemodel.LoginModel;
import com.securesurveillance.skili.profile.MyProfileActivity;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.RoundedImageView;
import com.securesurveillance.skili.utility.SharePreferanceWrapperSingleton;
import com.securesurveillance.skili.utility.SweetAlertController;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by adarsh on 10/7/2018.
 */

public class ChatOneToOneActivity extends AppCompatActivity {

    Context ctx;
    public static ChatOneToOneActivity chatActivity;
    public static final String chatReceiver = "CHATMSGRECEIVED";
    public static String TO_PROFILE_ID = "";

    public String name="",pic="",FAV="false";
     ChatView chatView;
    SharePreferanceWrapperSingleton objSPS;
    TextView tvName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat_one_to_one);
        ctx = this;
        chatActivity = this;
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(ctx);
        TO_PROFILE_ID = getIntent().getStringExtra("TOPROFILEID");
        if(!TextUtils.isEmpty(getIntent().getStringExtra("NAME"))) {
            name = CommonMethod.getFirstCapName(getIntent().getStringExtra("NAME"));
            pic = getIntent().getStringExtra("PIC");
        }
        final ImageView ivProfile = (ImageView) findViewById(R.id.ivProfile);
        Picasso.get().load(pic).into(ivProfile, new Callback() {
            @Override
            public void onSuccess() {

            }


            @Override
            public void onError(Exception e) {
                ivProfile.setBackground(getDrawable(R.drawable.user_profile));
            }
        });
        ImageView ivBack=(ImageView)findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvName=(TextView)findViewById(R.id.tvName);
        chatView=(ChatView) findViewById(R.id.chat_view);
        tvName.setText(name);
//        chatView.addMessage(new ChatMessage("Message received", System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
//        chatView.addMessage(new ChatMessage("A message with a sender name",
//                System.currentTimeMillis(), ChatMessage.Type.RECEIVED, "Ryan Java"));

        getConversationList();
        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener() {
            @Override
            public boolean sendMessage(ChatMessage chatMessage) {
                callSendMessageApi(chatMessage.getMessage());
                return true;
            }
        });

        chatView.setTypingListener(new ChatView.TypingListener() {
            @Override
            public void userStartedTyping() {

            }

            @Override
            public void userStoppedTyping() {

            }
        });


        LocalBroadcastManager.getInstance(ctx).registerReceiver(
                mMessageReceiver, new IntentFilter(chatReceiver));

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ctx, MyProfileActivity.class);
                i.putExtra("PID", "");
                i.putExtra("PROFILEID", TO_PROFILE_ID);
                i.putExtra("ISFAV", FAV);
                startActivity(i);
            }
        });
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ctx, MyProfileActivity.class);
                i.putExtra("PID", "");
                i.putExtra("PROFILEID", TO_PROFILE_ID);
                i.putExtra("ISFAV", FAV);
                startActivity(i);
            }
        });
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("body");
            String tag1 = intent.getStringExtra("fromProfileId");
            ChatView chatView = (ChatView) findViewById(R.id.chat_view);
             if (TO_PROFILE_ID.equalsIgnoreCase(tag1)) {
                chatView.addMessage(new ChatMessage(message, System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
            }

        }
    };
    private void getConversationList() {
        GetConversationList_OnetoOneController anInterface = GetRetrofit.getInstance().create(GetConversationList_OnetoOneController.class);

        Call<ConversationOnetoOneModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN), objSPS.getValueFromShared_Pref(Constants.ID),TO_PROFILE_ID);
        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    // refresh complete
                    if (response.code() == 200) {
                        if (response.body() instanceof ConversationOnetoOneModel) {
                            ConversationOnetoOneModel model = (ConversationOnetoOneModel) response.body();
                            if (model.isStatus()) {
                                FAV=model.getResult().getFavouriteProfile();
                                Collections.reverse(model.getResult().getConversation());
                                for(int i=0;i<model.getResult().getConversation().size();i++) {
                                    if (model.getResult().getConversation().get(i).getFromProfileId().equals(objSPS.getValueFromShared_Pref(Constants.ID))) {
                                        chatView.addMessage(new ChatMessage(model.getResult().getConversation().get(i).getMessage(), Long.parseLong(model.getResult().getConversation().get(i).getMsgTimeInMillisecond()), ChatMessage.Type.SENT));

                                    } else {
                                        chatView.addMessage(new ChatMessage(model.getResult().getConversation().get(i).getMessage(), Long.parseLong(model.getResult().getConversation().get(i).getMsgTimeInMillisecond()), ChatMessage.Type.RECEIVED));

                                    }
                                }

                                CommonMethod.cancelDialog(dialog, ctx);

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


    private void callSendMessageApi(final String msg) {
        SendMessageController anInterface = GetRetrofit.getInstance().create(SendMessageController.class);
        JSONObject json = new JSONObject();
        try {
            json.put("message", msg);
            json.put("fromProfileId", objSPS.getValueFromShared_Pref(Constants.ID));
            json.put("toProfileId", TO_PROFILE_ID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        Call<LoginModel> call = anInterface.getResponse( objSPS.getValueFromShared_Pref(Constants.TOKEN),
                body);
        API.callRun(ctx, false, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    if (response.code() == 200) {
                        CommonMethod.printAPIResponse(response.body());
                        if (response.body() instanceof LoginModel) {
                            LoginModel loginResponse = (LoginModel) response.body();
                            if (!loginResponse.isStatus()) {
                                  callSendMessageApi(msg);

                            }
                        }
                    } else {
                        CommonMethod.cancelDialog(dialog, ctx);
                        CommonMethod.showApiMsgToast(ctx, response.code());
                        callSendMessageApi(msg);
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
                             callSendMessageApi(msg);

                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(mMessageReceiver!=null)
            unregisterReceiver(mMessageReceiver);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}


