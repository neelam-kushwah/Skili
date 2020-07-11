package com.securesurveillance.skili.apiHandler;


import android.app.ProgressDialog;

import retrofit2.Call;
import retrofit2.Response;

public interface APIResponseListener<Request> {
    void onSuccess(Call<Request> call, Response<Request> response, ProgressDialog dialog);

    void onFailure(Call<Request> call, Throwable t);
}
