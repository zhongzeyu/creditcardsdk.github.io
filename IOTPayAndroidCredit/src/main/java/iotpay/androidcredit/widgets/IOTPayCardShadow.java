package iotpay.androidcredit.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.List;

import iotpay.androidcredit.util.IOTPayEditText;

public class IOTPayCardShadow extends RelativeLayout {
    public IOTPayCardShadow(Context context) {
        super(context);
    }

    public IOTPayCardShadow(Context context, AttributeSet attrs) {
        super(context);
    }

    public IOTPayCardShadow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IOTPayCardShadow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context);
    }


}
