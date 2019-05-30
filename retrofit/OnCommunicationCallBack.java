package com.developer.webservice.retrofit;

import org.json.JSONObject;

public interface OnCommunicationCallBack  {
     void onSuccessCallBack(Object tag, JSONObject jsonObject);

     void onFailCallBack(Object tag, Throwable t);

    void onConnectionChange(boolean b);
}
