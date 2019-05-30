# CustomRetrofit
Retrofit library for api call with all pre functions

# Add below dependencies in App level

implementation 'com.squareup.retrofit2:retrofit:2.5.0'
    
implementation 'com.squareup.retrofit2:converter-scalars:2.5.0'

implementation 'com.squareup.okhttp3:logging-interceptor:3.12.1'

# How to Use

Communication communication = new Communication(this,this);

String url = "";

HashMap<String, String> params = new HashMap<>();

params.put("key", value);

# *for GET CALL*
communication.callGET(url,TAG);

# *for POST CALL*
communication.callPOST(url,"TAG",params);

# *for File Upload*
ArrayList<PART> list = new ArrayList<>();
    
File file = ;
 
PART part = new PART();

part.setParamKey("img");

part.setFile(file);

list.add(part);

communication.callPOSTWithFiles(url, tag, param, Params.createPartList(list));

        
