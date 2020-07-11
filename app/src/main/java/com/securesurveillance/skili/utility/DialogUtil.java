package com.securesurveillance.skili.utility;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.securesurveillance.skili.PostJobStepAActivity;
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.SkilliSignupActivity;
import com.securesurveillance.skili.adapter.CloudAdapter;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.fragments.GlobalFeedFragment;
import com.securesurveillance.skili.fragments.HomeJobFragment;
import com.securesurveillance.skili.materialspinner.MaterialSpinner;
import com.securesurveillance.skili.materialspinner.MaterialSpinnerMultiple;

import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by adarsh on 11/7/2018.
 */

public class DialogUtil {
    static String selectedCity = "";
    static String myFormat = "dd/MM/yyyy"; //In which you need put here
    static SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    AppCompatCheckBox cbF, cbP;

    public static void openFilterDialog(final Context ctx, Activity activity,
                                        final ArrayList<String> city, final ArrayList<String> skills,
                                        final FilterCandidate filterCandidate, Fragment fragment) {
        final ArrayList<String> arrSkills = new ArrayList<>();
        selectedCity = "";

        final AppCompatDialog dialog = new AppCompatDialog
                (ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.dialog_filter);
//        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(ctx, R.color.dialog_transparent)));
        activity.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        activity.getWindow().setGravity(Gravity.CENTER);
        MaterialSpinnerMultiple spinnerSkills = (MaterialSpinnerMultiple) dialog.findViewById(R.id.spinnerSkills);
        final EditText etStartDate = (EditText) dialog.findViewById(R.id.etStartDate);
        final EditText etEndDate = (EditText) dialog.findViewById(R.id.etEndDate);
        final Calendar myCalendar = Calendar.getInstance();
        final Button tvClear = (Button) dialog.findViewById(R.id.tvClear);
        final LinearLayout llDate=(LinearLayout)dialog.findViewById(R.id.llDate);
        final LinearLayout llBudgetLayout=(LinearLayout)dialog.findViewById(R.id.llBudgetLayout);
        Button btnSearch = (Button) dialog.findViewById(R.id.btnSearch);
        final AppCompatCheckBox cbF = (AppCompatCheckBox) dialog.findViewById(R.id.cbF);
        final AppCompatCheckBox cbP = (AppCompatCheckBox) dialog.findViewById(R.id.cbP);
        cbF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    cbP.setChecked(false);
//
//
//                } else {
//                    cbP.setChecked(true);
//                }
            }
        });
        cbP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    cbF.setChecked(false);
//
//
//                } else {
//                    cbF.setChecked(true);
//                }
            }
        });
        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterCandidate.onClear();
                dialog.dismiss();
            }
        });
        final DatePickerDialog.OnDateSetListener dateStart = new DatePickerDialog.OnDateSetListener() {

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
        final DatePickerDialog.OnDateSetListener dateEnd = new DatePickerDialog.OnDateSetListener() {

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

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ctx, dateStart, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ctx, dateEnd, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        spinnerSkills.setItems(skills);
        spinnerSkills.setOnItemSelectedListener(new MaterialSpinnerMultiple.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinnerMultiple view, int position, long id, Object item, ArrayList arrSelectedSubCategory) {
                if (arrSkills.contains(skills.get(position))) {
                    arrSkills.remove(skills.get(position));
                }
                arrSkills.add(skills.get(position));


            }
        });

        final MaterialSpinner spinnerCity = (MaterialSpinner) dialog.findViewById(R.id.spinnerCity);
        spinnerCity.setItems(city);
        if (city.size() > 0) {
            selectedCity = city.get(0);
        }
        spinnerCity.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                selectedCity = city.get(position);
            }
        });
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        // Setup the new range seek bar
        // Seek bar for which we will set text color in code
        final CrystalRangeSeekbar seekbar_distance = (CrystalRangeSeekbar) dialog.findViewById(R.id.seekbar_distance);
        final CrystalRangeSeekbar seekbar_payscale = (CrystalRangeSeekbar) dialog.findViewById(R.id.seekbar_payscale);
        final TextView textMax = (TextView) dialog.findViewById(R.id.textMax);
        final TextView textMax1 = (TextView) dialog.findViewById(R.id.textMax1);
        final TextView textMin = (TextView) dialog.findViewById(R.id.textMin);
        final TextView textMin1 = (TextView) dialog.findViewById(R.id.textMin1);
        final RadioButton rbYearly = (RadioButton) dialog.findViewById(R.id.rbYearly);
        final RadioButton rbMonthly = (RadioButton) dialog.findViewById(R.id.rbMonthly);
        final RadioButton rbHourly = (RadioButton) dialog.findViewById(R.id.rbHourly);

        seekbar_payscale.setMinValue(100);
        textMin1.setText("" + 100);
        seekbar_payscale.setMaxValue(2000);
        textMax1.setText("" + 2000);
//distance
        seekbar_distance.setMinValue(0);
        textMin.setText("" + 0);
        seekbar_distance.setMaxValue(1000);
        textMax.setText("" + 1000);
        rbHourly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    seekbar_payscale.setMinValue(100);
                    textMin1.setText("" + 100);
                    seekbar_payscale.setMaxValue(2000);
                    textMax1.setText("" + 2000);
                    seekbar_payscale.setSteps(100);
                }
            }
        });
        rbMonthly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    seekbar_payscale.setMinValue(10000);
                    textMin1.setText("" + 10000);
                    seekbar_payscale.setSteps(1000);
                    seekbar_payscale.setMaxValue(70000);
                    textMax1.setText("" + 70000);

                }
            }
        });
        rbYearly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    seekbar_payscale.setMinValue(100000);
                    textMin1.setText("" + 100000);
                    seekbar_payscale.setSteps(10000);
                    seekbar_payscale.setMaxValue(1000000);
                    textMax1.setText("" + 1000000);

                }
            }
        });
        seekbar_payscale.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                textMin1.setText(String.valueOf(minValue));
                textMax1.setText(String.valueOf(maxValue));
            }
        });

        seekbar_distance.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                textMin.setText(String.valueOf(minValue));
                textMax.setText(String.valueOf(maxValue));
            }
        });

        if (fragment instanceof GlobalFeedFragment) {
            llDate.setVisibility(View.GONE);
            llBudgetLayout.setVisibility(View.GONE);
            GlobalFeedFragment globalFeedFragment = (GlobalFeedFragment) fragment;
            ArrayList<Integer> selectedPositons = new ArrayList<>();
            if (globalFeedFragment.arrLastSelectedSkills.size() > 0) {
                for (int i = 0; i < skills.size(); i++) {
                    for (int j = 0; j < globalFeedFragment.arrLastSelectedSkills.size(); j++) {
                        if (skills.get(i).equals(globalFeedFragment.arrLastSelectedSkills.get(j))) {
                            selectedPositons.add(i);
                            arrSkills.add(skills.get(i));
                        }
                    }
                }
                spinnerSkills.getAdapter().setSelectedArr(selectedPositons);

                for (int k = 0; k < city.size(); k++) {
                    if (city.get(k).equals(globalFeedFragment.lastSelectedCity)) {
                        spinnerCity.setSelectedIndex(k);
                        break;
                    }
                }
                selectedCity = globalFeedFragment.lastSelectedCity;
                if (globalFeedFragment.lastJobType.equalsIgnoreCase("PART_TIME")) {
                    cbP.setChecked(true);
                    cbF.setChecked(false);

                } else {
                    cbF.setChecked(true);
                    cbP.setChecked(false);

                }

                if (globalFeedFragment.lastIsBothTimeEnable) {
                    cbP.setChecked(true);
                    cbF.setChecked(true);

                }

                etStartDate.setText(globalFeedFragment.lastStartDate);
                etEndDate.setText(globalFeedFragment.lastEndDate);
                if (globalFeedFragment.lastBudgetType.equals("YEARLY")) {
                    rbYearly.setChecked(true);
                    seekbar_payscale.setMinValue(100000);
                    textMin1.setText("" + 100000);
                    seekbar_payscale.setSteps(10000);
                    seekbar_payscale.setMaxValue(1000000);
                    textMax1.setText("" + 1000000);


                } else if (globalFeedFragment.lastBudgetType.equals("MONTHLY")) {
                    rbMonthly.setChecked(true);
                    seekbar_payscale.setMinValue(10000);
                    textMin1.setText("" + 10000);
                    seekbar_payscale.setSteps(1000);
                    seekbar_payscale.setMaxValue(70000);
                    textMax1.setText("" + 70000);


                } else {
                    rbHourly.setChecked(true);
                    seekbar_payscale.setMinValue(100);
                    textMin1.setText("" + 100);
                    seekbar_payscale.setSteps(100);

                    seekbar_payscale.setMaxValue(2000);
                    textMax1.setText("" + 2000);


                }


                seekbar_distance.setMinStartValue(Integer.parseInt(globalFeedFragment.lastSelectedDisMinValue));
                seekbar_distance.setMaxStartValue(Integer.parseInt(globalFeedFragment.lastSelectedDisMaxValue));
                seekbar_payscale.setMinStartValue(Integer.parseInt(globalFeedFragment.lastSelectedMinPay));
                seekbar_payscale.setMaxStartValue(Integer.parseInt(globalFeedFragment.lastSelectedMAxPay));
                seekbar_distance.apply();
                seekbar_payscale.apply();


            }
        } else {

                HomeJobFragment homeJobFragment = (HomeJobFragment) fragment;
                ArrayList<Integer> selectedPositons = new ArrayList<>();
                if (homeJobFragment.arrLastSelectedSkills.size() > 0) {
                    for (int i = 0; i < skills.size(); i++) {
                        for (int j = 0; j < homeJobFragment.arrLastSelectedSkills.size(); j++) {
                            if (skills.get(i).equals(homeJobFragment.arrLastSelectedSkills.get(j))) {
                                selectedPositons.add(i);
                                arrSkills.add(skills.get(i));

                            }
                        }
                    }
                    spinnerSkills.getAdapter().setSelectedArr(selectedPositons);

                    for (int k = 0; k < city.size(); k++) {
                        if (city.get(k).equals(homeJobFragment.lastSelectedCity)) {

                            spinnerCity.setSelectedIndex(k);
                            break;
                        }
                    }
                    selectedCity = homeJobFragment.lastSelectedCity;
                    if (homeJobFragment.lastJobType.equalsIgnoreCase("PART_TIME")) {
                        cbP.setChecked(true);
                        cbF.setChecked(false);
                    } else {
                        cbF.setChecked(true);
                        cbP.setChecked(false);

                    }
                    if (homeJobFragment.lastIsBothTimeEnable) {
                        cbP.setChecked(true);
                        cbF.setChecked(true);

                    }


                    etStartDate.setText(homeJobFragment.lastStartDate);
                    etEndDate.setText(homeJobFragment.lastEndDate);

                    seekbar_distance.setMinStartValue(Integer.parseInt(homeJobFragment.lastSelectedDisMinValue));
                    seekbar_distance.setMaxStartValue(Integer.parseInt(homeJobFragment.lastSelectedDisMaxValue));
                    seekbar_payscale.setMinStartValue(Integer.parseInt(homeJobFragment.lastSelectedMinPay));
                    seekbar_payscale.setMaxStartValue(Integer.parseInt(homeJobFragment.lastSelectedMAxPay));
                    seekbar_distance.apply();
                    seekbar_payscale.apply();


                }

        }

        // Add to layout
//        seekbar_placeholder.addView(rangeSeekBar);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrSkills.size() == 0) {
                    Toast.makeText(ctx, "Please select skills.", Toast.LENGTH_LONG).show();
                } else

// else if (TextUtils.isEmpty(selectedCity)) {
//                    Toast.makeText(ctx, "Please select city.", Toast.LENGTH_LONG).show();
//
//                } else if (TextUtils.isEmpty(etStartDate.getText().toString().trim())) {
//
//                    CommonMethod.showToastMessage(ctx, "Please choose start date");
//
//                } else if (TextUtils.isEmpty(etEndDate.getText().toString().trim())) {
//
//                    CommonMethod.showToastMessage(ctx, "Please choose end date");
//
//                } else

                    if (!TextUtils.isEmpty(etStartDate.getText().toString().trim()) && !TextUtils.isEmpty(etEndDate.getText().toString().trim()) && !CheckDates(etStartDate.getText().toString().trim(), etEndDate.getText().toString().trim())) {

                        CommonMethod.showToastMessage(ctx, "Job end date should not be before start date");
                    }
//                else
//                    if (seekbar_distance.getSelectedMaxValue().intValue() == 0) {
//                    CommonMethod.showToastMessage(ctx, "Distance value should be greater than 0 km");
//
//                } else if (seekbar_payscale.getSelectedMaxValue().intValue() == 0) {
//                    CommonMethod.showToastMessage(ctx, "Max pay scale value should be greater than 0");
//
//                }
                    else {
                        String budgetType, jobType;
                        if (rbYearly.isChecked()) {
                            budgetType = ("YEARLY");
                        } else if (rbMonthly.isChecked()) {
                            budgetType = ("MONTHLY");

                        } else {
                            budgetType = ("HOURLY");
                        }
                        if (cbF.isChecked()) {
                            jobType = "FULL_TIME";

                        } else {
                            jobType = "PART_TIME";
                        }
                        boolean isBothEnable = false;
                        if (cbF.isChecked() && cbP.isChecked()) {
                            isBothEnable = true;
                        }

                        filterCandidate.onSearch(arrSkills, selectedCity, etStartDate.getText().toString(), etEndDate.getText().toString(),
                                seekbar_payscale.getSelectedMaxValue().toString(), seekbar_distance.getSelectedMaxValue().toString(),
                                seekbar_payscale.getSelectedMinValue().toString(),
                                seekbar_distance.getSelectedMinValue().toString(), budgetType, jobType, isBothEnable);
                        dialog.dismiss();
                    }
            }
        });
        dialog.show();

    }

    public interface FilterCandidate {
        void onSearch(ArrayList<String> arrSkills, String selectedCity, String startDate, String endDate,
                      String selectedMaxValue, String selectedDistanceMaxValue, String selectedMinValue,
                      String selectedDistanceMinValue, String budgettype, String jobType, boolean bothTimeEnable);

        void onClear();
    }


    public static boolean CheckDates(String startDate, String endDate) {
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
