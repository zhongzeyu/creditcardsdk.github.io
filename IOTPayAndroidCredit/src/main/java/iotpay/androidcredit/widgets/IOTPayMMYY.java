package iotpay.androidcredit.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;


import java.util.regex.Pattern;

import iotpay.androidcredit.R;
import iotpay.androidcredit.util.IOTPayEditText;
import iotpay.androidcredit.util.IOTPayUtilCommon;


public class IOTPayMMYY extends IOTPayEditText {
    String delimeter = "/";
    public IOTPayMMYY(Context context) {
        super(context);
        init();
    }

    public IOTPayMMYY(Context context, AttributeSet attrs) {
        super(context);
        init();
    }

    public IOTPayMMYY(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IOTPayMMYY(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context);
        init();
    }

    private void init() {
        setHint(R.string.mmyy_default);
        // Adding the TextWatcher
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int position, int before, int action) {
                if (position == 1) {
                    String sValue = s.toString();
                    if (!sValue.endsWith(delimeter) && sValue.length() > 1) {
                        append(delimeter);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // The input filters
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; ++i) {
                    if (!Pattern.compile("[0-9\\/]*").matcher(String.valueOf(source)).matches()) {
                        return "";
                    }
                    if(source.length() > 0){
                        String textValue = getValue();
                        int texLength = textValue.length();

                        if(texLength == 0 && (source.charAt(0) != '0' && source.charAt(0) != '1')){
                            return "";
                        }else if(texLength == 1){
                            if(textValue.equals("0") && source.charAt(0) == '0'){
                                return "";
                            }
                            if(textValue.equals("1") && source.charAt(0)!='0' && source.charAt(0)!='1' && source.charAt(0)!='2'){
                                return "";
                            }
                        }
                    }
                }
                return null;
            }
        };
        // Setting the filters
        setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(5)});
    }



    public String getValue() {
        return getText().toString().replace(delimeter, "").trim();
    }


    @Override
    public String getInvalidMsg() {
        String value = getValue();
        if(value == null || value.length() < 4){
            return getResources().getString(R.string.cardmmyy_invalid);
        }
        return null;
    }

}
