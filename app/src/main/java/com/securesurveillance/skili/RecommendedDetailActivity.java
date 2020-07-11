package com.securesurveillance.skili;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.securesurveillance.skili.model.Jobdetail;

/**
 * Created by adarsh on 7/8/2018.
 */

public class RecommendedDetailActivity extends Activity {
    private ImageView iv_LeftOption;
    Jobdetail data;
    TextView tvPostedOn, tvExperience, tvLocation, tvSalary, tvSkills, tvDescription, tvEmployer, tvFunctional, tvIndustry, tvOtherSkills;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_details);
        Bundle bundle = getIntent().getBundleExtra("BUNDLE");
        if(bundle!=null) {
            data = (Jobdetail) bundle.getSerializable("DETAIL");
            tvPostedOn = (TextView) findViewById(R.id.tvPostedOn);
            tvExperience = (TextView) findViewById(R.id.tvExperience);
            tvLocation = (TextView) findViewById(R.id.tvLocation);
            tvSalary = (TextView) findViewById(R.id.tvSalary);
            tvSkills = (TextView) findViewById(R.id.tvSkills);
            tvEmployer = (TextView) findViewById(R.id.tvEmployer);
            tvDescription = (TextView) findViewById(R.id.tvDescription);
            tvFunctional = (TextView) findViewById(R.id.tvFunctional);
            tvDescription = (TextView) findViewById(R.id.tvDescription);
            tvIndustry = (TextView) findViewById(R.id.tvIndustry);
            tvOtherSkills = (TextView) findViewById(R.id.tvOtherSkills);

            tvPostedOn.setText(data.getCreatedOn());
            tvDescription.setText(data.getDescription());
            tvEmployer.setText(data.getEmployerDetails());
            tvFunctional.setText(data.getFunctionalArea());
            tvLocation.setText(data.getJobLocation());
            if (data.getOtherSkills() != null)
                tvOtherSkills.setText(data.getOtherSkills().get(0));
            if (data.getSkills() != null)
                tvSkills.setText(data.getSkills().get(0));
            tvExperience.setText(data.getMinRequiredExp() + " - " + data.getMaxRequiredExp());
            tvSalary.setText(data.getMinOfferedSalary() + " - " + data.getMaxOfferedSalary() + " " + data.getSalaryUnit() + " " +
                    data.getCtcType());

        }
        iv_LeftOption = (ImageView) findViewById(R.id.iv_LeftOption);
        iv_LeftOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
