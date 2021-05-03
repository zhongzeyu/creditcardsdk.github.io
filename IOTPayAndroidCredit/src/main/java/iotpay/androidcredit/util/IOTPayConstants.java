package iotpay.androidcredit.util;

public enum IOTPayConstants {
    SUCCESS("SUCCESS"),
    FAIL("FAIL"),
    URLROOT("https://ccdev.iotpaycloud.com/v3/"),
    VISA("Visa"),
    MASTERCARD("MasterCard"),
    AMERICAN_EXPRESS("American_Express"),
    DISCOVER("Discover"),
    JCB("JCB"),
    DINER_ClUB("Diners_Club"),
    UNIONPAY("UnionPay"),
    UNKNOWN("UNKNOWN"),
    SingleLine(0),
    TripleLine(1),
    IOTPayCard("IOTPayCard"),
    IOTPayCardShadow("IOTPayCardShadow"),
    IOTPayEditCard("IOTPayEditCard"),
    IOTPayHolder("IOTPayHolder"),
    IOTPayCVC("IOTPayCVC"),
    IOTPayMMYY("IOTPayMMYY"),
    PARAMS_SECUREID("secureId"),
    PARAMS_CARD_NUM("cardNum"),
    PARAMS_HOLDER("holder"),
    PARAMS_CVV("cvv"),
    PARAMS_EXPIRYDATE("expiryDate"),
    CreditCardStyle("creditCardStyle")
    ;
    public final Object label;
    private IOTPayConstants(Object label){
        this.label = label;
    }
}
