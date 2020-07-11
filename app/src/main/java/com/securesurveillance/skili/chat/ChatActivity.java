package com.securesurveillance.skili.chat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.apiHandler.API;
import com.securesurveillance.skili.apiHandler.APIResponseListener;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.GetRetrofit;
import com.securesurveillance.skili.apiinterfaces.GetConversationListController;
import com.securesurveillance.skili.apiinterfaces.GetConversationList_OnetoOneController;
import com.securesurveillance.skili.model.ChatModel;
import com.securesurveillance.skili.model.responsemodel.ConversationModel;
import com.securesurveillance.skili.model.responsemodel.ConversationOnetoOneModel;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.SharePreferanceWrapperSingleton;
import com.securesurveillance.skili.utility.SweetAlertController;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;

import cn.pedant.SweetAlert.SweetAlertDialog;
import co.intentservice.chatui.models.ChatMessage;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by adarsh on 10/7/2018.
 */

public class ChatActivity extends AppCompatActivity {
    SearchView searchView;
    SearchView.SearchAutoComplete searchEditText;
    Context ctx;
    RecyclerView recyclerView;
    RelativeLayout rlSearch;
    SharePreferanceWrapperSingleton objSPS;
    public String name="",pic="",toProfileId="",FAV="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat);
        ctx = this;
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(ctx);

        searchView = findViewById(R.id.searchView);
        rlSearch = (RelativeLayout) findViewById(R.id.rlSearch);
        searchEditText = searchView.findViewById(R.id.search_src_text);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        searchEditText.setTextColor(ContextCompat.getColor(ChatActivity.this, R.color.text_color_black));
        searchView.getLayoutParams().height = (int) (getResources().getDimension(R.dimen.etHeight));
        if(!TextUtils.isEmpty(getIntent().getStringExtra("NAME"))) {
            name = CommonMethod.getFirstCapName(getIntent().getStringExtra("NAME"));
            pic = getIntent().getStringExtra("PIC");
            toProfileId= getIntent().getStringExtra("TOPROFILEID");
            Intent i = new Intent(this, ChatOneToOneActivity.class);
            i.putExtra("TOPROFILEID", toProfileId);
            i.putExtra("NAME", name);
            i.putExtra("PIC", pic);
            i.putExtra("FAV", FAV);
            startActivity(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getConversationList();

    }

    private void getConversationList() {
        GetConversationListController anInterface = GetRetrofit.getInstance().create(GetConversationListController.class);

        Call<ConversationModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN), objSPS.getValueFromShared_Pref(Constants.ID));
        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (!isFinishing()) {
                    // refresh complete
                    if (response.code() == 200) {
                        if (response.body() instanceof ConversationModel) {
                            ConversationModel model = (ConversationModel) response.body();
                            if (model.isStatus()) {
                                Collections.reverse(model.getResult());
                                if(model.getResult().size()>0) {
                                    ChatListAdapter adapter = new ChatListAdapter(ctx, model.getResult(),name,pic,FAV);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
                                }else{
                                    SweetAlertController.getInstance().showErrorDialogClickListener(ctx,
                                            SweetAlertDialog.ERROR_TYPE, "No recent chats available.", getString(R.string.app_name), "Try again", new SweetAlertController.SweetAlertClickListener() {
                                        @Override
                                        public void onSweetAlertClicked() {
                                           finish();
                                        }
                                    });
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

}


