package iotpay.androidcredit.util;

import org.json.JSONObject;

import iotpay.androidcredit.business.IOTPayCallback;

public class IOTPayHttpService {
    private static IOTPayHttpService hInstance;
    private IOTPayOKHttpWrapper wrapper;
    private IOTPayHttpService() throws Exception{
        wrapper = IOTPayOKHttpWrapper.getInstance();
    }
    public static IOTPayHttpService getInstance() throws Exception{
        if(hInstance == null){
            hInstance = new IOTPayHttpService();
        }
        return hInstance;
    }
    public void post(String url, JSONObject jsonParam, IOTPayCallback iotPayCallback) throws Exception{
        String jsonString = jsonParam.toString();
        post(url, jsonString,iotPayCallback);
    }

    private void executeTask(Runnable run) {
            IOTPayThreadPoolManager.executeInCachePool(run);
    }

    public void post(String url, String jsonParam, IOTPayCallback iotPayCallback) throws Exception{
        executeTask(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String response = wrapper.post(url, jsonParam);
                            //Gson gson = new Gson();

                            //HttpRes res = Gson.fromJson(response, HttpRes.class);
                            iotPayCallback.onResultIOTPay(response);
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
