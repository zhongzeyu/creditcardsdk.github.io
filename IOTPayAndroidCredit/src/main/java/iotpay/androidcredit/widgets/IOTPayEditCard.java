package iotpay.androidcredit.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import iotpay.androidcredit.R;
import iotpay.androidcredit.pojo.IOTPayCreditCard;
import iotpay.androidcredit.util.IOTPayConstants;
import iotpay.androidcredit.util.IOTPayEditText;
import iotpay.androidcredit.util.IOTPayUtilCommon;

public class IOTPayEditCard extends IOTPayEditText {
    String type = "UNKNOWN";
    String delimeter = " ";
    private static List<IOTPayEditText> iotPayEditTextList;
    public IOTPayEditCard(Context context) {
        super(context);
        init();
    }
    public static void RegisterIotpayEditTexts(IOTPayEditText iotPayEditText){
        if(iotPayEditTextList == null){
            iotPayEditTextList = new ArrayList<IOTPayEditText>();
        }
        if(iotPayEditTextList.contains(iotPayEditText)){
            return;
        }
        iotPayEditTextList.add(iotPayEditText);
    }

    public IOTPayEditCard(Context context, AttributeSet attrs) {
        super(context);
        init();
    }

    public IOTPayEditCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IOTPayEditCard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context);
        init();
    }

    private void init() {
        changeIcon();
        // Adding the TextWatcher
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int position, int before, int action) {
                if (action == 1) {
                    if (type.equals(IOTPayConstants.AMERICAN_EXPRESS.label+"") || type.equals(IOTPayConstants.DINER_ClUB.label+"")) {
                        if (position == 3 || position == 10) {
                            if (!s.toString().endsWith(delimeter)) {
                                append(delimeter);
                            }
                        }else if (position != 4 & position != 11){
                            if ((s.charAt(position)+"").equals(delimeter)) {
                                setText(s.subSequence(0,position).toString() + s.subSequence(position + 1,s.length()));
                            }
                        }
                    }else{
                        if (position == 3 || position == 8 || position == 13) {
                            if (!s.toString().endsWith(delimeter)) {
                                append(delimeter);
                            }
                        }else if (position != 4 && position != 9 && position != 14) {
                            if ((s.charAt(position)+"").equals(delimeter)) {
                                setText(s.subSequence(0,position).toString() + s.subSequence(position + 1,s.length()));
                            }
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                changeIcon();
            }
        });
        // The input filters
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; ++i) {
                    if (!Pattern.compile("[0-9\\s]*").matcher(String.valueOf(source)).matches()) {
                        return "";
                    }
                }

                return null;
            }
        };
        // Setting the filters
        setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(29)});
    }

    private void changeIcon() {
        String s = getText().toString().replace(delimeter, "").trim();
        IOTPayCreditCard iotPayCreditCard = IOTPayUtilCommon.getCardByCardNumber(s);
        if(iotPayCreditCard == null){
            iotPayCreditCard = new IOTPayCreditCard();
        }
        type = iotPayCreditCard.code;
        setCompoundDrawablesWithIntrinsicBounds(0, 0, iotPayCreditCard.IconID, 0);

        IOTPayUtilCommon.currentCreditCard = iotPayCreditCard;
    }

    public String getValue() {
        return getText().toString().replace(delimeter, "").trim();
    }

    @Override
    public String getInvalidMsg() {
        String value = getValue();
        if(value == null || value.length() < 9){
            return getResources().getString(R.string.cardnumber_invalid);
        }
        return null;
    }

}
