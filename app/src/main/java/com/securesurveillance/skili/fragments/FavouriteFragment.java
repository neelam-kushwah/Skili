package com.securesurveillance.skili.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.JsonSyntaxException;
import com.squareup.picasso.Picasso;
import com.securesurveillance.skili.JobDetailRecruiterActivity;
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.SliderActivity;
import com.securesurveillance.skili.adapter.FavouriteAdapter;
import com.securesurveillance.skili.adapter.JobAppliedAdapter;
import com.securesurveillance.skili.apiHandler.API;
import com.securesurveillance.skili.apiHandler.APIResponseListener;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.GetRetrofit;
import com.securesurveillance.skili.apiinterfaces.GetApplicantListController;
import com.securesurveillance.skili.apiinterfaces.GetFav_ApplicantListController;
import com.securesurveillance.skili.apiinterfaces.GetFav_RecruiterListController;
import com.securesurveillance.skili.apiinterfaces.RemoveFavApplicantController;
import com.securesurveillance.skili.model.RecommendedModel;
import com.securesurveillance.skili.model.responsemodel.GetApplicantListModel;
import com.securesurveillance.skili.model.responsemodel.GetFavApplicantListModel;
import com.securesurveillance.skili.model.responsemodel.LoginModel;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.SharePreferanceWrapperSingleton;
import com.securesurveillance.skili.utility.SweetAlertController;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Response;

import static com.securesurveillance.skili.SliderActivity.hotlist_hot;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends Fragment implements FavouriteAdapter.RemoveFav {


    RecyclerView rvRecommended;
    public static boolean isVertical = false;
    ArrayList<GetFavApplicantListModel.Applicant> arrList;
    SharePreferanceWrapperSingleton objSPS;
    Context ctx;
    MaterialRefreshLayout refresh;

    public FavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        ctx = getActivity();
        rvRecommended = (RecyclerView) view.findViewById(R.id.rvRecommended);
        refresh = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
        objSPS = SharePreferanceWrapperSingleton.getSingletonInstance();
        objSPS.setPref(ctx);
        if (objSPS.getValueFromShared_Pref(Constants.ROLE).equals("Individual")) {
            getFavRecruiterList(true);

        } else {
            getFavApplicantList(true);

        }
        isVertical = false;
        hotlist_hot.setVisibility(View.GONE);
        Picasso.get().load(R.drawable.fav_vertical).into(SliderActivity.ivRight);
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                //refreshing...
                if (objSPS.getValueFromShared_Pref(Constants.ROLE).equals("Individual")) {
                    getFavRecruiterList(false);

                } else {
                    getFavApplicantList(false);

                }
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //load more refreshing...
            }
        });

        return view;
    }

    private void getFavApplicantList(boolean isNotRefresh) {
        GetFav_ApplicantListController anInterface = GetRetrofit.getInstance().create(GetFav_ApplicantListController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));

        Call<GetFavApplicantListModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN), objSPS.getValueFromShared_Pref(Constants.ID));
        API.callRun(ctx, isNotRefresh, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (getActivity()!=null&&!getActivity().isFinishing()) {
                    // refresh complete
                    refresh.finishRefresh();
                    if (response.code() == 200) {
                        if (response.body() instanceof GetFavApplicantListModel) {
                            GetFavApplicantListModel model = (GetFavApplicantListModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (model.isStatus()) {
                                if (model.getResult() != null) {
                                    arrList = model.getResult();
                                    changeAdapterView();
                                } else {
                                    SweetAlertController.getInstance().showErrorDialog(ctx, SweetAlertDialog.ERROR_TYPE,
                                            "Favourite list is empty", getString(R.string.app_name));
                                }


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
                if (getActivity()!=null&&!getActivity().isFinishing()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // refresh complete
                            refresh.finishRefresh();
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

    private void getFavRecruiterList(boolean isNotRefresh) {
        GetFav_RecruiterListController anInterface = GetRetrofit.getInstance().create(GetFav_RecruiterListController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));

        Call<GetFavApplicantListModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN), objSPS.getValueFromShared_Pref(Constants.ID));
        API.callRun(ctx, isNotRefresh, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (getActivity()!=null&&!getActivity().isFinishing()) {
                    // refresh complete
                    refresh.finishRefresh();
                    if (response.code() == 200) {
                        if (response.body() instanceof GetFavApplicantListModel) {
                            GetFavApplicantListModel model = (GetFavApplicantListModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (model.isStatus()) {
                                if (model.getResult() != null) {
                                    arrList = model.getResult();
                                    changeAdapterView();
                                } else {
                                    SweetAlertController.getInstance().showErrorDialog(ctx, SweetAlertDialog.ERROR_TYPE,
                                            "Favourite list is empty", getString(R.string.app_name));
                                }


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
                if (getActivity()!=null&&!getActivity().isFinishing()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // refresh complete
                            refresh.finishRefresh();
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

    public void changeAdapterView() {
        if (arrList != null && arrList.size() > 0) {
            FavouriteAdapter adapter = new FavouriteAdapter(FavouriteFragment.this, arrList, isVertical);
            rvRecommended.setAdapter(adapter);
            if (isVertical) {
                rvRecommended.setLayoutManager(new GridLayoutManager(getActivity(), 2));

            } else {
                rvRecommended.setLayoutManager(new LinearLayoutManager(getActivity()));

            }
        }
    }

    private void deleteFavRecruiter(final int pos) {
        RemoveFavApplicantController anInterface = GetRetrofit.getInstance().create(RemoveFavApplicantController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        Call<LoginModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN), arrList.get(pos).getFavoriteId());

        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (getActivity()!=null&&!getActivity().isFinishing()) {
                    if (response.code() == 200) {
                        if (response.body() instanceof LoginModel) {
                            LoginModel model = (LoginModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (model.isStatus()) {
                                CommonMethod.showToastMessage(ctx, model.getMessage());
                                arrList.remove(pos);
                                FavouriteAdapter adapter = new FavouriteAdapter(FavouriteFragment.this, arrList, isVertical);
                                rvRecommended.setAdapter(adapter);
                                if (isVertical) {
                                    rvRecommended.setLayoutManager(new GridLayoutManager(getActivity(), 2));

                                } else {
                                    rvRecommended.setLayoutManager(new LinearLayoutManager(getActivity()));

                                }

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
                if (getActivity()!=null&&!getActivity().isFinishing()) {
                    getActivity().runOnUiThread(new Runnable() {
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

    private void deleteFavApplicant(final int pos) {
        RemoveFavApplicantController anInterface = GetRetrofit.getInstance().create(RemoveFavApplicantController.class);
        Map<String, String> data = new HashMap<>();
        data.put("authorization", objSPS.getValueFromShared_Pref(Constants.TOKEN));
        Call<LoginModel> call = anInterface.getResponse(
                objSPS.getValueFromShared_Pref(Constants.TOKEN), arrList.get(pos).getFavoriteId());

        API.callRun(ctx, true, call, new APIResponseListener() {
            @Override
            public void onSuccess(Call call, Response response, ProgressDialog dialog) {
                if (getActivity()!=null&&!getActivity().isFinishing()) {
                    if (response.code() == 200) {
                        if (response.body() instanceof LoginModel) {
                            LoginModel model = (LoginModel) response.body();
                            CommonMethod.cancelDialog(dialog, ctx);
                            if (model.isStatus()) {
                                CommonMethod.showToastMessage(ctx, model.getMessage());
                                arrList.remove(pos);
                                FavouriteAdapter adapter = new FavouriteAdapter(FavouriteFragment.this, arrList, isVertical);
                                rvRecommended.setAdapter(adapter);
                                if (isVertical) {
                                    rvRecommended.setLayoutManager(new GridLayoutManager(getActivity(), 2));

                                } else {
                                    rvRecommended.setLayoutManager(new LinearLayoutManager(getActivity()));

                                }

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
                if (getActivity()!=null&&!getActivity().isFinishing()) {
                    getActivity().runOnUiThread(new Runnable() {
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

    @Override
    public void onRemovefav(int pos) {
        if (objSPS.getValueFromShared_Pref(Constants.ROLE).equals("Individual")) {
            deleteFavRecruiter(pos);

        } else {
            deleteFavApplicant(pos);

        }
    }
}
