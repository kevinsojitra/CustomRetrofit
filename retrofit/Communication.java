package com.developer.webservice.retrofit;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;


import com.developer.webservice.retrofit.broadcast.NetworkChangeReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Communication implements NetworkChangeReceiver.OnConnectionChangeListener {

    private final Activity activity;
    private OnCommunicationCallBack callBack;
    private boolean instantRetry = false;
    private String headerKey, headerValue;

    private BroadcastReceiver connectionReceiver = new NetworkChangeReceiver();


    public Communication(@NonNull Activity activity, @NonNull OnCommunicationCallBack callback) {
        this.callBack = callback;
        this.activity = activity;

    }


    private void sendCallBack(Call<String> call, Response<String> response) {
        revokeConnectionChange(activity);
        try {
            JSONObject jsonObject = new JSONObject(response.body());
            jsonObject.put("api_req_tag", call.request().tag());
            callBack.onSuccessCallBack(call.request().tag(), jsonObject);
        } catch (JSONException e) {
            callBack.onFailCallBack(call.request().tag(), e);
        }
    }

    private void sendCallError(Call<String> call, Throwable t) {
        revokeConnectionChange(activity);
        if (instantRetry && !call.isCanceled()) {
            retry(call.request().tag());
        }
        if (callBack != null) {
            callBack.onFailCallBack(call.request().tag(), t);
        } else {
            Log.e("ERROR:::", call.request().tag() + "--CallBack Revoked");
        }
    }

    public void callPOST(@NonNull String url, Object tag, @NonNull HashMap<String, String> param) {
        setConnectionChange(activity);
        RetroInterface service = Client.getRetrofitClient(tag, headerKey, headerValue).create(RetroInterface.class);
        Call<String> stringCall = service.getPostAsString(url, param);
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                Log.e("HEADERS:::", String.valueOf(call.request().headers().names()));
                if (response.isSuccessful()) {
                    if (callBack != null) {
                        sendCallBack(call, response);
                    } else {
                        Log.e("ERROR:::", call.request().tag() + "--CallBack Revoked");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                sendCallError(call, t);
            }
        });
    }

    public void callGET(@NonNull String url, Object tag) {
        setConnectionChange(activity);
        RetroInterface service = Client.getRetrofitClient(tag, headerKey, headerValue).create(RetroInterface.class);
        Call<String> stringCall = service.getGetAsString(url);

        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    if (callBack != null) {
                        sendCallBack(call, response);
                    } else {
                        Log.e("ERROR:::", call.request().tag() + "--CallBack Revoked");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                sendCallError(call, t);
            }
        });
    }

    private static RequestBody toRequestBody(String value) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, value);
    }

    public void callPOSTWithFiles(@NonNull String url, Object tag, @NonNull HashMap<String, String> param, @NonNull List<MultipartBody.Part> list) {
        setConnectionChange(activity);
        RetroInterface service = Client.getRetrofitClient(tag, headerKey, headerValue).create(RetroInterface.class);
        HashMap<String, RequestBody> tempParam = new HashMap<>();
        for (String key : param.keySet()) {
            tempParam.put(key, toRequestBody(param.get(key)));
        }

        Call<String> stringCall = service.getPostAsStringWithPart(url, tempParam, list);

        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    if (callBack != null) {
                        sendCallBack(call, response);
                    } else {
                        Log.e("ERROR:::", call.request().tag() + "--CallBack Revoked");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                sendCallError(call, t);
            }
        });
    }

    public void callPOSTWithFile(@NonNull String url, Object tag, @NonNull HashMap<String, String> param, @NonNull File file) {
        setConnectionChange(activity);
        RetroInterface service = Client.getRetrofitClient(tag, headerKey, headerValue).create(RetroInterface.class);
        HashMap<String, RequestBody> tempParam = new HashMap<>();
        for (String key : param.keySet()) {
            tempParam.put(key, toRequestBody(param.get(key)));
        }
        PART part = new PART();
        part.setParamKey("img");
        part.setFile(file);
        ArrayList<PART> list = new ArrayList<>();
        list.add(part);

        Call<String> stringCall = service.getPostAsStringWithPart(url, tempParam, Params.createPartList(list));

        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    if (callBack != null) {
                        sendCallBack(call, response);
                    } else {
                        Log.e("ERROR:::", call.request().tag() + "--CallBack Revoked");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                sendCallError(call, t);
            }
        });
    }


    public void callMultiFile(@NonNull String url, Object tag, @NonNull List<MultipartBody.Part> list) {
        setConnectionChange(activity);
        RetroInterface service = Client.getRetrofitClient(tag, headerKey, headerValue).create(RetroInterface.class);
        Call<String> stringCall = service.getPartAsString(url, list);

        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    if (callBack != null) {
                        sendCallBack(call, response);
                    } else {
                        Log.e("ERROR:::", call.request().tag() + "--CallBack Revoked");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                sendCallError(call, t);
            }
        });
    }

    public void setCallBack(OnCommunicationCallBack callBack) {
        this.callBack = callBack;
    }

    public void revokeCallBack() {
        callBack = null;
    }


    public void setHeaderAuthentication(final String headerKey, final String headerValue) {
        this.headerKey = headerKey;
        this.headerValue = headerValue;
    }

    public void setAutoRetry(boolean b) {
        this.instantRetry = b;
    }

    public void cancelAllCalls() {
        Client.getClient().dispatcher().cancelAll();
    }

    public static void cancel(Object tag) {
        List<okhttp3.Call> list = Client.getClient().dispatcher().runningCalls();
        for (okhttp3.Call call :
                list) {
            if (call.request().tag() == tag) {
                call.cancel();
            }
        }
    }

    public void RetryCall(Object tag) {
        retry(tag);
    }

    private void retry(Object tag) {
        List<okhttp3.Call> list = Client.getClient().dispatcher().runningCalls();
        for (okhttp3.Call call :
                list) {
            if (call.request().tag() == tag) {
                call.clone().enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                        if (instantRetry && !call.isCanceled()) {
                            call.clone().enqueue(this);
                        }
                        if (callBack != null) {
                            callBack.onFailCallBack(call.request().tag(), e);
                        } else {
                            Log.e("ERROR:::", call.request().tag() + "--CallBack Revoked");
                        }
                    }

                    @Override
                    public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                        if (response.isSuccessful() && response.body() != null) {
                            if (callBack != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body().string());
                                    JSONObject tagObject = new JSONObject();
                                    tagObject.put("tag", call.request().tag());
                                    jsonObject.put("callTag", tagObject);
                                    callBack.onSuccessCallBack(call.request().tag(), jsonObject);
                                } catch (JSONException e) {
                                    callBack.onFailCallBack(call.request().tag(), e);
                                }
                            } else {
                                Log.e("ERROR:::", call.request().tag() + "--CallBack Revoked");
                            }
                        }
                    }
                });
            }
        }
    }


    public void removeHeaderAuthentication() {
        this.headerValue = null;
        this.headerKey = null;
    }


    private void setConnectionChange(Activity activity) {
        activity.registerReceiver(connectionReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        NetworkChangeReceiver.setOnConnectionChangeListener(this);
    }

    private void revokeConnectionChange(Activity activity) {
        if (connectionReceiver != null) {
            try {
                activity.unregisterReceiver(connectionReceiver);
            } catch (Exception e) {
                Log.e("ERROR:::", e.getMessage());
            }
        }
    }

    @Override
    public void onConnectionChange(boolean b) {
        if (callBack != null) {
            callBack.onConnectionChange(b);
        }
    }


    public List<okhttp3.Call> queuedCalls() {
        return Client.getClient().dispatcher().queuedCalls();

    }

    public List<okhttp3.Call> runningCalls() {
        return Client.getClient().dispatcher().runningCalls();
    }

    public static boolean isInRunning(Object tag) {
        List<okhttp3.Call> list = Client.getClient().dispatcher().runningCalls();
        for (okhttp3.Call call :
                list) {
            if (call.request().tag() == tag) {
                return true;
            }
        }
        return false;
    }
}
