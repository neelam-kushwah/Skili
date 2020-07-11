package com.securesurveillance.skili;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.model.responsemodel.GetAllJobRecruiterDetailModel;
import com.securesurveillance.skili.utility.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by adarsh on 11/5/2018.
 */

public class PostJobStepAActivity extends AppCompatActivity {
    TextView tv_Title;
    ImageView iv_LeftOption;
    AppCompatCheckBox cbF, cbP;
    Button btnNext;
    LinearLayout llDate, llTime;
    Calendar myCalendar;
    EditText etStartDate, etEndDate, etStartTime, etEndTime, etJobtitle, etJobDescription, etPurpose;
    String myFormat = "dd/MM/yyyy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    GetAllJobRecruiterDetailModel model = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postjob_stepa);
        initView();
        myCalendar = Calendar.getInstance();
        if (getIntent().hasExtra("DATA")) {
            model = (GetAllJobRecruiterDetailModel) getIntent().getSerializableExtra("DATA");
            tv_Title.setText("Edit Job");
            etJobtitle.setText(model.getJobTitle());
            etJobDescription.setText(model.getJobDescription());
            etPurpose.setText(model.getPurposeOfJob());

            if (model.getJobType().contains("FULL")) {
                cbF.setChecked(true);
            } else {
                etStartDate.setText(model.getStartDate());
                etEndDate.setText(model.getEndDate());
                etStartTime.setText(model.getStartTime());
                etEndTime.setText(model.getEndTime());
                cbP.setChecked(true);

            }
        }


    }

    DatePickerDialog.OnDateSetListener dateStart = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            etStartDate.setText(sdf.format(myCalendar.getTime()));
        }

    };
    DatePickerDialog.OnDateSetListener dateEnd = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            etEndDate.setText(sdf.format(myCalendar.getTime()));
        }

    };

    private void initView() {
        tv_Title = (TextView) findViewById(R.id.tv_Title);
        iv_LeftOption = (ImageView) findViewById(R.id.iv_LeftOption);
        cbF = (AppCompatCheckBox) findViewById(R.id.cbF);
        cbP = (AppCompatCheckBox) findViewById(R.id.cbP);
        llDate = (LinearLayout) findViewById(R.id.llDate);
        etStartDate = (EditText) findViewById(R.id.etStartDate);
        etEndDate = (EditText) findViewById(R.id.etEndDate);
        llTime = (LinearLayout) findViewById(R.id.llTime);
        etStartTime = (EditText) findViewById(R.id.etStartTime);
        etEndTime = (EditText) findViewById(R.id.etEndTime);
        etJobtitle = (EditText) findViewById(R.id.etJobtitle);

        etPurpose = (EditText) findViewById(R.id.etPurpose);
        etJobDescription = (EditText) findViewById(R.id.etJobDescription);
        iv_LeftOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(PostJobStepAActivity.this, dateStart, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(PostJobStepAActivity.this, dateEnd, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        etStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(PostJobStepAActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String strH = "" + selectedHour;
                        String strM = "" + selectedMinute;
                        if (strH.length() != 2) {
                            strH = "0" + selectedHour;
                        }
                        if (strM.length() != 2) {
                            strM = "0" + selectedMinute;
                        }
                        etStartTime.setText(strH + ":" + strM);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        etEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(PostJobStepAActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String strH = "" + selectedHour;
                        String strM = "" + selectedMinute;
                        if (strH.length() != 2) {
                            strH = "0" + selectedHour;
                        }
                        if (strM.length() != 2) {
                            strM = "0" + selectedMinute;
                        }

                        etEndTime.setText(strH + ":" + strM);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        cbF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cbP.setChecked(false);
                    llDate.setVisibility(View.GONE);
                    llTime.setVisibility(View.GONE);


                } else {
                    cbP.setChecked(true);
                }
            }
        });
        cbP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cbF.setChecked(false);
                    llDate.setVisibility(View.VISIBLE);
                    llTime.setVisibility(View.VISIBLE);

                } else {
                    cbF.setChecked(true);
                }
            }
        });
        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(etJobtitle.getText().toString().trim())) {
                    CommonMethod.showToastMessage(PostJobStepAActivity.this, "Please enter job title");
                } else if (TextUtils.isEmpty(etJobDescription.getText().toString().trim())) {
                    CommonMethod.showToastMessage(PostJobStepAActivity.this, "Please enter job description");
                } else if (TextUtils.isEmpty(etPurpose.getText().toString().trim())) {
                    CommonMethod.showToastMessage(PostJobStepAActivity.this, "Please enter purpose of job");
                } else if (TextUtils.isEmpty(etJobtitle.getText().toString().trim())) {
                    CommonMethod.showToastMessage(PostJobStepAActivity.this, "Please enter job title");
                } else if (cbP.isChecked() && TextUtils.isEmpty(etStartDate.getText().toString().trim())) {

                    CommonMethod.showToastMessage(PostJobStepAActivity.this, "Please choose start date");

                } else if (cbP.isChecked() && TextUtils.isEmpty(etEndDate.getText().toString().trim())) {

                    CommonMethod.showToastMessage(PostJobStepAActivity.this, "Please choose end date");

                } else if (cbP.isChecked() && !CheckDates(etStartDate.getText().toString().trim(), etEndDate.getText().toString().trim())) {

                    CommonMethod.showToastMessage(PostJobStepAActivity.this, "Job end date should not be before start date");
                } else if (cbP.isChecked() && TextUtils.isEmpty(etStartTime.getText().toString().trim())) {

                    CommonMethod.showToastMessage(PostJobStepAActivity.this, "Please choose start time");

                } else if (cbP.isChecked() && TextUtils.isEmpty(etEndTime.getText().toString().trim())) {

                    CommonMethod.showToastMessage(PostJobStepAActivity.this, "Please choose end time");

                } else if (cbP.isChecked() && !checktimings(etStartTime.getText().toString().trim(), etEndTime.getText().toString().trim())) {

                    CommonMethod.showToastMessage(PostJobStepAActivity.this, "Job end time should not be before start time");
                } else {
                    Intent i = new Intent(PostJobStepAActivity.this, PostJobStepBActivity.class);
                    i.putExtra(Constants.BundleData.JOB_TITLE, CommonMethod.getFirstCapName(etJobtitle.getText().toString().trim()));
                    i.putExtra(Constants.BundleData.JOB_DESCRIPTION,  CommonMethod.getFirstCapName(etJobDescription.getText().toString().trim()));
                    i.putExtra(Constants.BundleData.JOB_PURPOSE, CommonMethod.getFirstCapName( etPurpose.getText().toString().trim()));
                    i.putExtra(Constants.BundleData.IS_FULL_TIME, cbF.isChecked());
                    if (cbP.isChecked()) {
                        i.putExtra(Constants.BundleData.START_DATE, etStartDate.getText().toString().trim());
                        i.putExtra(Constants.BundleData.END_DATE, etEndDate.getText().toString().trim());
                        i.putExtra(Constants.BundleData.START_TIME, etStartTime.getText().toString().trim());
                        i.putExtra(Constants.BundleData.END_TIME, etEndTime.getText().toString().trim());
                    }
                    if (getIntent().hasExtra("DATA")) {
                        i.putExtra("DATA", model);
                    }

                    startActivity(i);
                }
            }
        });
        tv_Title.setText("Post a Job");
    }

    private boolean checktimings(String time, String endtime) {

        String pattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(endtime);

            if (date1.before(date2)) {
                return true;
            } else {

                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean CheckDates(String startDate, String endDate) {
        boolean b = false;
        try {
            if (sdf.parse(startDate).before(sdf.parse(endDate))) {
                b = true;//If start date is before end date
            } else if (sdf.parse(startDate).equals(sdf.parse(endDate))) {
                b = true;//If two dates are equal
            } else {
                b = false; //If start date is after the end date
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }
}
