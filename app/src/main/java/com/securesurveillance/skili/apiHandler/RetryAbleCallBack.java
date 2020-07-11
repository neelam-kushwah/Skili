package com.securesurveillance.skili.apiHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class RetryAbleCallBack<ResModel> implements Callback<ResModel> {
    private final Call<ResModel> call;
    private int totalRetries = 5;
    private int retryCount = 0;

    public RetryAbleCallBack(Call<ResModel> call, int totalRetries) {
        this.call = call;
        this.totalRetries = totalRetries;
    }

    @Override
    public void onResponse(Call<ResModel> call, Response<ResModel> response) {
        if (!API.isCallSuccess(response))
            if (retryCount++ < totalRetries) {
                retry();
            } else
                onFinalResponse(call, response);
        else
            onFinalResponse(call, response);
    }

    @Override
    public void onFailure(Call<ResModel> call, Throwable t) {
        if (retryCount++ < totalRetries) {
            retry();
        } else
            onFinalFailure(call, t);
    }

    public void onFinalResponse(Call<ResModel> call, Response<ResModel> response) {

    }

    public void onFinalFailure(Call<ResModel> call, Throwable t) {
    }

    private void retry() {
        call.clone().enqueue(this);
    }

}
