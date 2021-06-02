package iotpay.androidcredit.business;

import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONObject;

import iotpay.androidcredit.R;
import iotpay.androidcredit.config.IOTPayConfig;
import iotpay.androidcredit.pojo.IOTPayCardInfo;
import iotpay.androidcredit.pojo.IOTPayRes;
import iotpay.androidcredit.util.IOTPayConstants;
import iotpay.androidcredit.util.IOTPayHttpService;
import iotpay.androidcredit.widgets.IOTCardInfoView;

import static iotpay.androidcredit.util.IOTPayConstants.URL_ADDCARD;
import static iotpay.androidcredit.util.IOTPayConstants.URL_PURCHASE;

public class IOTPayService {
    private static String urlRoot = IOTPayConstants.URLROOT.label+"";


    public static IOTCardInfoView IOTCardInfoView(ViewGroup viewGroup, int creditCardStyle) {

        IOTCardInfoView creditForm = new IOTCardInfoView(viewGroup.getContext(), null, creditCardStyle);// inflate(viewGroup.getContext(), null,null);

        viewGroup.addView(creditForm);
        return creditForm;
    }

    public static void sendRequest(String secureId, Object layoutType, IOTCardInfoView creditForm,IOTPayCallback iotPayCallback) throws Exception{

        if(layoutType.equals(IOTPayConfig.AddCard)){
            addCard(iotPayCallback, creditForm, secureId);
        }else if(layoutType.equals(IOTPayConfig.SimplePurchase)){
            pay( iotPayCallback, creditForm,  secureId);
        }

    }


    public static void addCard(IOTPayCallback iotPayCallback, IOTCardInfoView creditForm, String secureId) throws Exception {
        String url = urlRoot + URL_ADDCARD.label;
        httpResult(url,iotPayCallback,creditForm,secureId);
    }
    private static void httpResult(String url, IOTPayCallback iotPayCallback, IOTCardInfoView creditForm, String secureId) throws Exception{
        if (secureId == null || secureId.length() < 1) {
            throw new Exception(creditForm.getResources().getString(R.string.secureid_invalid));
        }
        IOTPayCardInfo IOTPayCardInfo = creditForm.getCardInfo();
        if(IOTPayCardInfo == null){
            return;
        }


        JSONObject params = new JSONObject();
        params.put(IOTPayConstants.PARAMS_SECUREID.label+"",secureId);
        params.put(IOTPayConstants.PARAMS_CARD_NUM.label+"", IOTPayCardInfo.cardNumber);
        params.put(IOTPayConstants.PARAMS_CVV.label+"", IOTPayCardInfo.cvc);
        params.put(IOTPayConstants.PARAMS_EXPIRYDATE.label+"", IOTPayCardInfo.expiryDate);
        params.put(IOTPayConstants.PARAMS_HOLDER.label+"", IOTPayCardInfo.holder);
        IOTPayHttpService iOTPayHttpService = IOTPayHttpService.getInstance();
        iOTPayHttpService.post(url,params,iotPayCallback);
    }
    public static void pay(IOTPayCallback iotPayCallback, IOTCardInfoView creditForm, String secureId) throws Exception{
        String url = urlRoot + URL_PURCHASE.label;
        httpResult(url,iotPayCallback,creditForm,secureId);
    }
}
