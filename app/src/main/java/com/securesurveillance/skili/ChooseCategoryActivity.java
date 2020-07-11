package com.securesurveillance.skili;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

/**
 * Created by adarsh on 10/4/2018.
 */

public class ChooseCategoryActivity extends Activity implements View.OnClickListener {
    Button btnRecruiter, btnBoth, btnJobSeeker;
    String role = "";
    Context ctx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_choosecategory_skilli);
        btnJobSeeker = (Button) findViewById(R.id.btnJobSeeker);
        btnBoth = (Button) findViewById(R.id.btnBoth);
        btnRecruiter = (Button) findViewById(R.id.btnRecruiter);
        btnJobSeeker.setOnClickListener(this);
        btnBoth.setOnClickListener(this);
        btnRecruiter.setOnClickListener(this);
        btnBoth.setVisibility(View.GONE);
    }
private void openSignUpPage(){
    Intent i = new Intent(ctx, SkilliSignupActivity.class);
    i.putExtra("ROLE", role);
    startActivity(i);
}
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnJobSeeker:
                role = "Individual";
                openSignUpPage();
                break;
            case R.id.btnRecruiter:
                role = "Recruiter";
                openSignUpPage();

                break;
            case R.id.btnBoth:
                role = "Both";
                openSignUpPage();

                break;
            default:

                break;
        }
    }
}
