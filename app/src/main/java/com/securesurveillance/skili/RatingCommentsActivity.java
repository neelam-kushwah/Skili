package com.securesurveillance.skili;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.securesurveillance.skili.adapter.CloudAdapter;
import com.securesurveillance.skili.adapter.RatingCommentsAdapter;
import com.securesurveillance.skili.apiHandler.API;
import com.securesurveillance.skili.apiHandler.APIResponseListener;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.GetRetrofit;
import com.securesurveillance.skili.apiinterfaces.RateFreelancerController;
import com.securesurveillance.skili.model.responsemodel.GetAllProfileDetailsModel;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by adarsh on 4/20/2019.
 */

public class RatingCommentsActivity extends AppCompatActivity {
    TextView tv_Title, tvRating,tvPeople,tvName;
    ImageView iv_LeftOption;
    CheckBox star1, star2, star3, star4, star5;

    Context ctx;
    SharePreferanceWrapperSingleton objSPS;
    RecyclerView recyclerView;
    RoundedImageView ivProfile;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratecomments);
        ctx = this;
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(ctx);
        tv_Title = (TextView) findViewById(R.id.tv_Title);
        tvRating = (TextView) findViewById(R.id.tvRating);
        tvPeople = (TextView) findViewById(R.id.tvPeople);
        tvName = (TextView) findViewById(R.id.tvName);
        ivProfile = (RoundedImageView) findViewById(R.id.ivProfile);
        Picasso.get().load(getIntent().getStringExtra("PIC")).into(ivProfile, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                ivProfile.setBackground(getDrawable(R.drawable.user_profile));
            }
        });
        tvName.setText(getIntent().getStringExtra("NAME"));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        iv_LeftOption = (ImageView) findViewById(R.id.iv_LeftOption);
        String star = getIntent().getStringExtra("RATING");
        if(getIntent().hasExtra("RATINGDATA")) {
            ArrayList<GetAllProfileDetailsModel.ProfileRatingFeedbackDto> data =
                    (ArrayList<GetAllProfileDetailsModel.ProfileRatingFeedbackDto>) getIntent().getSerializableExtra("RATINGDATA");
            RatingCommentsAdapter adapter = new RatingCommentsAdapter(ctx, data);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
            tvPeople.setText("from "+data.size()+" peoples");
        }

        star1 = (CheckBox) findViewById(R.id.star1);
        star2 = (CheckBox) findViewById(R.id.star2);
        star3 = (CheckBox) findViewById(R.id.star3);
        star4 = (CheckBox) findViewById(R.id.star4);
        star5 = (CheckBox) findViewById(R.id.star5);
        tvRating.setText(star);
        double starGiven = Double.parseDouble(star);
        if (starGiven > 4.5) {
            //give 5
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
        } else if (starGiven > 3.5) {
            //give 4
            star1.setBackground(null);
            star1.setBackground(getResources().getDrawable(R.drawable.star_review));
            star2.setBackground(null);
            star2.setBackground(getResources().getDrawable(R.drawable.star_review));
            star3.setBackground(null);
            star3.setBackground(getResources().getDrawable(R.drawable.star_review));
            star4.setBackground(null);
            star4.setBackground(getResources().getDrawable(R.drawable.star_review));
        } else if (starGiven > 2.5) {
            //give 3
            star1.setBackground(null);
            star1.setBackground(getResources().getDrawable(R.drawable.star_review));
            star2.setBackground(null);
            star2.setBackground(getResources().getDrawable(R.drawable.star_review));
            star3.setBackground(null);
            star3.setBackground(getResources().getDrawable(R.drawable.star_review));
        } else if (starGiven > 1.5) {
            //give 2
            star1.setBackground(null);
            star1.setBackground(getResources().getDrawable(R.drawable.star_review));
            star2.setBackground(null);
            star2.setBackground(getResources().getDrawable(R.drawable.star_review));
        } else if (starGiven > 0.5) {
            //give 1
            star1.setBackground(null);
            star1.setBackground(getResources().getDrawable(R.drawable.star_review));
        }
        iv_LeftOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_Title.setText("Rating & Comments");


    }


}
