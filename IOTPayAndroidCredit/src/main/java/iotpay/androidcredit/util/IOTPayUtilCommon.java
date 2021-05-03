package iotpay.androidcredit.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import iotpay.androidcredit.R;
import iotpay.androidcredit.pojo.IOTPayCreditCard;

public class IOTPayUtilCommon {
    private static List<IOTPayCreditCard> creditCardList = null;
    public static IOTPayCreditCard currentCreditCard = null;
    public static final int CreditMargin = 20;
    public static List<IOTPayCreditCard> getCreditCardList(){
        if(creditCardList == null){
            creditCardList = new ArrayList<IOTPayCreditCard>();
            {
                IOTPayCreditCard aCard = new IOTPayCreditCard();
                aCard.code = IOTPayConstants.AMERICAN_EXPRESS.label+"";
                aCard.displayName = "American Express";
                aCard.IconID = R.drawable.amex;
                aCard.cvcLength = 4;
                aCard.cvcLabel = "CVC";
                aCard.cardNumberLength = 15;
                aCard.cardNumberPattern = "^(34|37)[0-9]*$";

                aCard.partialPatterns = new HashMap<>();
                aCard.partialPatterns.put(2,"^(34|37)$");

                creditCardList.add(aCard);
            }
            {
                IOTPayCreditCard aCard = new IOTPayCreditCard();
                aCard.code = IOTPayConstants.DISCOVER.label+"";
                aCard.displayName = "Discover";
                aCard.IconID = R.drawable.discover;
                aCard.cardNumberPattern = "^(60|64|65)[0-9]*$";

                aCard.partialPatterns = new HashMap<>();
                aCard.partialPatterns.put(2,"^(60|64|65)$");

                creditCardList.add(aCard);
            }
            {
                IOTPayCreditCard aCard = new IOTPayCreditCard();
                aCard.code = IOTPayConstants.JCB.label+"";
                aCard.displayName = "JCB";
                aCard.IconID = R.drawable.jcb;
                aCard.cardNumberPattern = "^(352[89]|35[3-8][0-9])[0-9]*$";
                aCard.partialPatterns = new HashMap<>();
                aCard.partialPatterns.put(3,"^(352[89]|35[3-8][0-9])$");
                aCard.partialPatterns.put(4,"^(352[89]|35[3-8][0-9])$");

                creditCardList.add(aCard);
            }
            {
                IOTPayCreditCard aCard = new IOTPayCreditCard();
                aCard.code = IOTPayConstants.DINER_ClUB.label+"";
                aCard.displayName = "Diners Club";
                aCard.IconID = R.drawable.diners;
                aCard.cardNumberPattern = "^(36|30|38|39)[0-9]*$";
                aCard.partialPatterns = new HashMap<>();
                aCard.partialPatterns.put(2,"^(36|30|38|39)$");

                creditCardList.add(aCard);
            }
            {
                IOTPayCreditCard aCard = new IOTPayCreditCard();
                aCard.code = IOTPayConstants.VISA.label+"";
                aCard.displayName = "Visa";
                aCard.IconID = R.drawable.visa;
                aCard.cardNumberPattern = "^(4)[0-9]*$";
                aCard.partialPatterns = new HashMap<>();
                aCard.partialPatterns.put(1,"^4$");
                creditCardList.add(aCard);
            }
            {
                IOTPayCreditCard aCard = new IOTPayCreditCard();
                aCard.code = IOTPayConstants.MASTERCARD.label+"";
                aCard.displayName = "Mastercard";
                aCard.IconID = R.drawable.mastercard;
                aCard.cardNumberPattern = "^(2221|2222|2223|2224|2225|2226|2227|2228|2229|222|223|224|225|226|227|228|229|23|24|25|26|270|271|2720|50|51|52|53|54|55|56|57|58|59|67)[0-9]*$";
                aCard.partialPatterns = new HashMap<>();
                aCard.partialPatterns.put(2,"^(22|23|24|25|26|27|50|51|52|53|54|55|56|57|58|59|67)$");
                creditCardList.add(aCard);
            }
            {
                IOTPayCreditCard aCard = new IOTPayCreditCard();
                aCard.code = IOTPayConstants.UNIONPAY.label+"";
                aCard.displayName = "UnionPay";
                aCard.IconID = R.drawable.unionpay;
                aCard.cardNumberPattern = "^(62|81)[0-9]*$";
                aCard.partialPatterns = new HashMap<>();
                aCard.partialPatterns.put(2,"^(62|81)$");
                creditCardList.add(aCard);
            }
        }
        return creditCardList;
    }
    public static IOTPayCreditCard getCardByCardNumberSub(String cardNumber, int compareLength) {
        List<IOTPayCreditCard> listCards = getCreditCardList();

        for(IOTPayCreditCard ic: listCards) {
            if (cardNumber.length() > 8) {
                String strPatterns = ic.cardNumberPattern;
                if (cardNumber.matches(strPatterns)) {
                    return ic;
                }
            }
        }
        for(IOTPayCreditCard ic: listCards) {
            String strShort = cardNumber.substring(0, compareLength);
            Map<Integer, String> partialPatterns = ic.partialPatterns;
            if (partialPatterns == null) {
                continue;
            }
            Iterator<Map.Entry<Integer, String>> itr = partialPatterns.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<Integer, String> entry = itr.next();
                if (!entry.getKey().equals(compareLength)) {
                    continue;
                }
                if (strShort.matches(entry.getValue())) {
                    return ic;
                }
            }
        }
        return null;

    }
    public static IOTPayCreditCard getCardByCardNumber(String cardNumber){

        for(int i = 4; i > 0; i--){
            if(cardNumber.length() <i){
                continue;
            }
            IOTPayCreditCard iotPayCreditCard = getCardByCardNumberSub(cardNumber,i);
            if(iotPayCreditCard != null){
                return iotPayCreditCard;
            }
        }
        return null;
    }
    /*public static boolean isValidCreditCardNumber(String cardNumber){
        List<IOTPayCreditCard> listCards = getCreditCardList();

        for(IOTPayCreditCard ic: listCards)

        {
            String strPatterns = ic.cardNumberPattern;
            if (cardNumber.matches(strPatterns)) {
                return true;
            }
        }

        return false;
    }
    public static boolean isValidMMYY(String mmyy){
        if(mmyy == null || mmyy.length() != 4){
            return false;
        }
        try{

            int month = new Integer(mmyy.substring(0,2));
            if(month < 1 || month > 12){
                return false;
            }
            int year = 2000 + new Integer(mmyy.substring(2,4));
            Date date = new Date(); // your date
            // Choose time zone in which you want to interpret your Date
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int yearNew = cal.get(Calendar.YEAR);
            if(year < yearNew || year > yearNew + 10){
                return false;
            }

        }catch (Exception e){
            return false;
        }
        return true;
    }*/

}
