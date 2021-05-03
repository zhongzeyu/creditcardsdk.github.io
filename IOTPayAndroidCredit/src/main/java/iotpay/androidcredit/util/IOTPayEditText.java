package iotpay.androidcredit.util;

import android.content.Context;
import android.widget.EditText;

import iotpay.androidcredit.pojo.IOTPayCreditCard;


public class IOTPayEditText extends androidx.appcompat.widget.AppCompatEditText implements IOTPayICardtypeChange {
    public IOTPayEditText(Context context) {
        super(context);
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public String getInvalidMsg() {
        return null;
    }
}
