package com.iot.iotpayandroidcreditdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;

import iotpay.androidcredit.business.IOTPayCallback;
import iotpay.androidcredit.business.IOTPayService;
import iotpay.androidcredit.config.IOTPayConfig;
import iotpay.androidcredit.pojo.IOTPayRes;
import iotpay.androidcredit.util.IOTPayConstants;
import iotpay.androidcredit.widgets.IOTCardInfoView;

public class MainActivity extends AppCompatActivity {

    EditText secureIdPay,secureIdAddCard;
    Switch switchStyle;
    IOTCardInfoView iotCardInfoView;  //the card info from order payment
    int queryOrderCnt = 0;
    int maxQueryOrderCnt = 30;  //cycle call order query until status is 2 or 3
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        secureIdPay = findViewById(R.id.editText1);
        secureIdAddCard = findViewById(R.id.editText2);
        switchStyle = findViewById(R.id.switch1);
        switchStyle.setOnCheckedChangeListener(
                ((buttonView, isChecked) -> openStyleSwitch(isChecked))
        );
        iotCardInfoView = IOTPayService.IOTCardInfoView(findViewById(R.id.tripleLine),(int)IOTPayConstants.TripleLine.label);

    }
    private void openStyleSwitch(boolean isChecked){
        iotCardInfoView.switchType(isChecked? (int) IOTPayConstants.TripleLine.label:(int) IOTPayConstants.SingleLine.label);
    }

   public void simplePurchase(View v){
       String secureId = secureIdPay.getText() + "";

       try{
           IOTPayService.sendRequest(secureId,IOTPayConfig.SimplePurchase, iotCardInfoView,new IOTPayCallback(){
               public void onResultIOTPay(String result) { //result
                   Gson gson = new Gson();
                   IOTPayRes iotPayRes = gson.fromJson(result, IOTPayRes.class);
                   if(!iotPayRes.retCode.equals("SUCCESS")){ //error
                       showMsg(result);
                       return;
                   }
                   if(iotPayRes.retData!=null) {
                       if (iotPayRes.retData.status != null &&
                               (iotPayRes.retData.status.equals("2") || iotPayRes.retData.status.equals("3"))) { //success
                           showMsg("Result:" + result);
                           //please redirect to your success page

                       }else{ //order status unknown
                           //do robin call to query order status in Merchant server
                           showMsg("Order status unknown, please robin call order detail from your server," +
                                   " and your server query order details from IOTPay server. If order status keep unknow for over 30 secs, " +
                                   "please contact IOTPay");
                       }
                   }

               }

           });
       }catch (Exception e){
           showMsg("Error:" + e.getMessage());
       }

   }
   private void showMsg(String msg){
       ResultActivity.startAction(this, msg);
   }
   public void addCard(View v) throws Exception{
       String secureId = secureIdAddCard.getText() + "";
       /*IOTPayCallback iotPayCallback = new IOTPayCallback(){
           public void onResultIOTPay(String result) { //result

               System.out.println("Add card Result:" + result);
               showMsg("Add card Result:" + result);
           }

       };*/
       try{
           IOTPayService.sendRequest(secureId,IOTPayConfig.AddCard,iotCardInfoView,new IOTPayCallback(){
               public void onResultIOTPay(String result) { //result
                   System.out.println("Add card Result:" + result);
                   showMsg("Add card Result:" + result);


               }

           });
       }catch (Exception e){
           e.printStackTrace();
           showMsg("Error:" + e.getMessage());
       }

   }

}