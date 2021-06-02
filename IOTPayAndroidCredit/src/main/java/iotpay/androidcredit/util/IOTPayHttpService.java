package iotpay.androidcredit.util;

import com.google.gson.Gson;

import org.json.JSONObject;

import iotpay.androidcredit.business.IOTPayCallback;
import iotpay.androidcredit.pojo.IOTPayRes;


public class IOTPayHttpService {
    private static IOTPayHttpService hInstance;
    private IOTPayOKHttpWrapper wrapper;
    int queryCnt = 0;
    int maxQueryCnt = 30;
    Gson gson = null;
    String queryUrl = IOTPayConstants.URLROOT.label + ""+ IOTPayConstants.URL_QUERYORDER.label;
    private IOTPayHttpService() throws Exception{
        wrapper = IOTPayOKHttpWrapper.getInstance();
    }
    public static IOTPayHttpService getInstance() throws Exception{
        if(hInstance == null){
            hInstance = new IOTPayHttpService();
        }
        return hInstance;
    }

    private void executeTask(Runnable run) {
            IOTPayThreadPoolManager.executeInCachePool(run);
    }

    private void queryOrder(String response, IOTPayCallback iotPayCallback, JSONObject jsonParam) throws Exception{
        if(gson == null){ gson = new Gson();}
        IOTPayRes iotPayRes  = gson.fromJson(response, IOTPayRes.class);
        if(iotPayRes != null && iotPayRes.retData != null && iotPayRes.retData != null
                && iotPayRes.retData.status != null && !iotPayRes.retData.status.equals("2")
                && !iotPayRes.retData.status.equals("3") && !iotPayRes.retData.status.equals("9") && queryCnt > 0){ //if order status is unknown, do order query.
            queryCnt--;
            //call order query afer one second
            Thread.sleep(1000);
            JSONObject params = new JSONObject();
            params.put(IOTPayConstants.PARAMS_SECUREID.label+"",jsonParam.getString(IOTPayConstants.PARAMS_SECUREID.label+""));
            post(queryUrl, params, iotPayCallback);

        }else{
            queryCnt = 0;
            iotPayCallback.onResultIOTPay(response);
        }
    }
    public void post(String url, JSONObject jsonParam, IOTPayCallback iotPayCallback) throws Exception{
        String jsonString = jsonParam.toString();
        executeTask(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String response = wrapper.post(url, jsonString);
                            //
                            //HttpRes res = Gson.fromJson(response, HttpRes.class);
                            //if is simple purchase, and order status not in(2,3), need robin call.
                            if(url.endsWith(IOTPayConstants.URL_PURCHASE.label+"")){
                                queryCnt = maxQueryCnt;
                                queryOrder(response,iotPayCallback,jsonParam);
                            }else if(url.equals(queryUrl)){
                                queryOrder(response,iotPayCallback,jsonParam);
                            }else{
                                iotPayCallback.onResultIOTPay(response);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            JSONObject jsonObject = new JSONObject();
                            try{
                                jsonObject.put("retCode", IOTPayConstants.FAIL+"");
                                jsonObject.put("retMsg",e.getMessage());
                                iotPayCallback.onResultIOTPay(jsonObject.toString());

                            }catch (Exception e1){
                                e.printStackTrace();
                            }

                        }
                    }
                });
    }


}
