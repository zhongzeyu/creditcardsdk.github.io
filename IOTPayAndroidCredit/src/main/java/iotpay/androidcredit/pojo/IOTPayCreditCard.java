package iotpay.androidcredit.pojo;

import java.util.List;
import java.util.Map;

import iotpay.androidcredit.R;

public class IOTPayCreditCard {
    public String code = "unknown";
    public String displayName = "Unknown";
    public int IconID = R.drawable.card;
    public int cvcLength = 3;
    public int cardNumberLength = 26;
    public String cardNumberPattern = null;
    public Map<Integer, String> partialPatterns = null;
    public String cvcLabel = "CVV";
    public List<Integer> defaultSpacePositions;
}
