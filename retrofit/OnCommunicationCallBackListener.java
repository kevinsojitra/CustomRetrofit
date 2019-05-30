

import org.json.JSONObject;

public abstract class OnCommunicationCallBackListener {
    public abstract void onSuccessCallBack(Object tag, JSONObject jsonObject);

    public abstract void onFailCallBack(Object tag, Throwable t);

    void onConnectionChange(boolean b) {

    }
}
