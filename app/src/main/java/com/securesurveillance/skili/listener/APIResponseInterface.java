package com.securesurveillance.skili.listener;

import org.json.JSONObject;


public interface APIResponseInterface {
    /**
     * Give the response of called web service(API).
     * @param response JSONObject object in response.
     * @param type  type is representing a int number in behalf of API calling(Which API you called and get response).
     */
    void onGetAPIResponse(JSONObject response, int type);

    /**
     * Give the error response of web service(API).
     * @param error  error response.
     * @param code
     * @param type
     */
    void onGetAPIErrorResponse(String error, int code, int type);

}
