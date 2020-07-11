package com.securesurveillance.skili.utility;

import android.app.Activity;
import android.content.Context;


import com.securesurveillance.skili.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by ruplee on 13/2/17.
 */
public class SweetAlertController {
    private static SweetAlertController sweetAlertController;
    private SweetAlertDialog pDialog;

    private SweetAlertController() {
    }

    public static SweetAlertController getInstance() {
        if (sweetAlertController == null) {
            sweetAlertController = new SweetAlertController();
        }
        return sweetAlertController;
    }


    public void showErrorDialogClickListener(Context context, int alertType, String content, String title,
                                             String btnText, final SweetAlertClickListener listener) {
        pDialog = getSweetAlertDialog(context, alertType);
        pDialog.setCancelable(false);
        pDialog.setTitleText(title)
                .setContentText(content)
                .setConfirmText(btnText)
                .showCancelButton(false)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        listener.onSweetAlertClicked();
                        sDialog.cancel();
                        loadingDialogDismissSA();
                    }
                }).show();
    }

    public void showErrorDialog(Context context, int alertType, String content, String title) {
        pDialog = getSweetAlertDialog(context, alertType);
        pDialog.setTitleText(title);
        pDialog.setContentText(content);
        pDialog.show();
    }

    public void showExitDialog(final Context context, int alertType) {
        pDialog = getSweetAlertDialog(context, alertType);
        pDialog.setTitleText(context.getString(R.string.app_name));
        pDialog.setContentText("Are you sure, you want to exit?").showCancelButton(false).setCancelText("No").setConfirmText("Yes")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                        loadingDialogDismissSA();

                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        ((Activity) context).finish();
                        sDialog.cancel();
                        loadingDialogDismissSA();

                    }
                })
                .show();
    }

    public void loadingDialogDismissSA() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

    private SweetAlertDialog getSweetAlertDialog(Context context, int alertType) {
        switch (alertType) {
            case 1:
                //ERROR_TYPE = 1
                pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
                return pDialog;
            case 2:
                //SUCCESS_TYPE = 2
                pDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
                return pDialog;
            case 3:
                //WARNING_TYPE = 3
                pDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
                return pDialog;
            case 4:
                //CUSTOM_IMAGE_TYPE = 4
                pDialog = new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
                return pDialog;
            case 5:
                //PROGRESS_TYPE = 5
                pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                return pDialog;
            default:
                //NORMAL_TYPE = 0
                pDialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
                return pDialog;
        }
    }


    public interface SweetAlertClickListener {
        void onSweetAlertClicked();
    }
}
