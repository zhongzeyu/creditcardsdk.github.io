package iotpay.androidcredit.widgets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;

import android.graphics.Rect;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import iotpay.androidcredit.R;

import iotpay.androidcredit.pojo.IOTPayCardInfo;
import iotpay.androidcredit.util.IOTPayConstants;
import iotpay.androidcredit.util.IOTPayEditText;
import iotpay.androidcredit.util.IOTPayUtilCommon;

public class IOTCardInfoView extends ViewGroup {
    private int layoutStyle = (int)IOTPayConstants.TripleLine.label;
    private int cardWidth;
    private int cardHeight,cardHeightReal;
    private int cardMMYYWidth;
    private int cardMinUnitWidth = 38;
    private int cardCVCWidth;
    private int cardNumWidth;
    private int cardItemHeight = 60;
    private int holderWidth;
    private Integer screenWidth = null;
    private final int shadowThick = 3;
    private String focusedClassName = IOTPayConstants.IOTPayEditCard.label + "";
    IOTPayCVC etCVV;
    IOTPayHolder etHolder;
    IOTPayEditCard etEditCard;
    IOTPayCard iotPayCard;
    IOTPayCardShadow iotPayCardShadow;
    IOTPayMMYY etMMYY;
    private TextView magnetlabel, holderlabel, cvclabel;
    private ImageView cardImage,cardImageShadow;

    public IOTCardInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.layoutStyle = defStyleAttr;
        init();
    }
    public IOTCardInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public IOTCardInfoView(Context context) {
        super(context);
        init();
    }
    private int getCardNumWidthDefault(){
        return cardWidth - IOTPayUtilCommon.CreditMargin * 2;
    }
    private int getCardNumWidthMax(){return cardMinUnitWidth * 12;}
    private int getCardNumWidthMin(){
        return cardMinUnitWidth * 5;
    }
    private int getHolderWidthDefault(){
        return cardWidth - IOTPayUtilCommon.CreditMargin * 2;
    }
    private int getHolderWidthMax(){
        return cardMinUnitWidth * 8;
    }
    private int getHolderWidthMin(){
        return cardMinUnitWidth * 5;
    }
    private int getCVCWidthDefault(){
        return cardMinUnitWidth * 3;
    }
    private int getCVCWidthMax(){
        return cardMinUnitWidth * 3;
    }
    private int getCVCWidthMin(){
        return cardMinUnitWidth * 3;
    }
    private int getMMYYWidthDefault(){
        return cardMinUnitWidth * 4;
    }
    private int getMMYYWidthMax(){
        return cardMinUnitWidth * 4;
    }
    private int getMMYYWidthMin(){
        return cardMinUnitWidth * 4;
    }

    private Rect getLocation(String className){
        if(className == null){
            return null;
        }
        if(className.indexOf(".") > 0){
            className = className.substring(className.lastIndexOf(".") + 1);
        }

        int x=0, y=0, right = 0;

        if(className.equals(IOTPayConstants.IOTPayCard.label+"")){
            x = IOTPayUtilCommon.CreditMargin;
            y = 0;
            right = x + cardWidth;
            return new Rect(x,y,right ,cardHeightReal);
        }else if(className.equals(IOTPayConstants.IOTPayCardShadow.label+"")){
            x = IOTPayUtilCommon.CreditMargin + shadowThick;
            y = shadowThick;
            right = x + cardWidth + shadowThick;
            return new Rect(x,y,right ,cardHeightReal + shadowThick);
        }else if(className.equals(IOTPayConstants.IOTPayEditCard.label+"")){
            if(isSingle()){
                x = IOTPayUtilCommon.CreditMargin;
                y = IOTPayUtilCommon.CreditMargin;
            }else{
                x = IOTPayUtilCommon.CreditMargin * 2;
                y = cardHeight / 3 - cardItemHeight - IOTPayUtilCommon.CreditMargin;
            }
            right = x + cardNumWidth;
            magnetlabel.setY(y - IOTPayUtilCommon.CreditMargin / 4);
        }else if(className.equals(IOTPayConstants.IOTPayHolder.label+"")){
            if(isSingle()){
                x = IOTPayUtilCommon.CreditMargin  + cardNumWidth;
                y = IOTPayUtilCommon.CreditMargin;
            }else{
                x = IOTPayUtilCommon.CreditMargin * 2;
                y = cardHeight * 2 / 3 - cardItemHeight - IOTPayUtilCommon.CreditMargin * 2 ;
            }
            right = x + holderWidth;
            holderlabel.setX(x - IOTPayUtilCommon.CreditMargin);
            holderlabel.setY(y - IOTPayUtilCommon.CreditMargin / 4);
            cvclabel.setY(y - IOTPayUtilCommon.CreditMargin / 4);
        }else if(className.equals(IOTPayConstants.IOTPayMMYY.label+"")){
            if(isSingle()){
                x = IOTPayUtilCommon.CreditMargin + cardNumWidth + holderWidth ;
                y = IOTPayUtilCommon.CreditMargin;
            }else{
                x = IOTPayUtilCommon.CreditMargin * 2;
                y = cardHeight - cardItemHeight - IOTPayUtilCommon.CreditMargin * 3;
            }
            right = x + cardMMYYWidth;
        }else if(className.equals(IOTPayConstants.IOTPayCVC.label+"")){
            if(isSingle()){
                x = IOTPayUtilCommon.CreditMargin  + cardNumWidth + holderWidth + cardMMYYWidth;
                y = IOTPayUtilCommon.CreditMargin;
            }else{
                x = cardWidth - getCVCWidthDefault();
                y = cardHeight - cardItemHeight - IOTPayUtilCommon.CreditMargin * 3;
            }
            right = x + cardCVCWidth;
        }
        return new Rect(x,y,right ,y + cardItemHeight);
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        drawItems();
    }
    private void drawItems(){
        setSizes();
        final int count = getChildCount();
        //set every  child view's position
        int index = 0;
        for(int i = 0; i < count; i++){
            final View child = getChildAt(index++);
            if(child.getVisibility() != GONE){
                String className = child.getClass().getName();
                Rect itemLocation = getLocation(className);
                child.layout(itemLocation.left, itemLocation.top, itemLocation.right, itemLocation.bottom);
            }
        }
    }
    private void setSizes(){
        if(screenWidth == null){
            DisplayMetrics dm = new DisplayMetrics();
            Activity activity = (Activity) getContext();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            screenWidth = dm.widthPixels;
        }
        cardWidth = screenWidth - IOTPayUtilCommon.CreditMargin * 2;
        cardMinUnitWidth = cardWidth/ 18;
        cardHeight = cardWidth * 3 / 5;
        cardItemHeight = cardWidth /12;
        cardNumWidth = getCardNumWidthDefault();
        holderWidth = getHolderWidthDefault();
        cardMMYYWidth = getMMYYWidthDefault();
        cardCVCWidth = getCVCWidthDefault();
        cardHeightReal = cardHeight;
        if(isSingle()){
            cardHeightReal = cardItemHeight + IOTPayUtilCommon.CreditMargin * 2;

            if(focusedClassName.equals(IOTPayConstants.IOTPayEditCard.label+"")){
                cardNumWidth = getCardNumWidthMax();
            }else{
                cardNumWidth = getCardNumWidthMin();
            }

            if(focusedClassName.equals(IOTPayConstants.IOTPayHolder.label+"")){
                holderWidth = getHolderWidthMax();
            }else{
                holderWidth = getHolderWidthMin();
            }

            if(focusedClassName.equals(IOTPayConstants.IOTPayCVC.label+"")){
                cardCVCWidth = getCVCWidthMax();
            }else{
                cardCVCWidth = getCVCWidthMin();
            }

            if(focusedClassName.equals(IOTPayConstants.IOTPayMMYY.label+"")){
                cardMMYYWidth = getMMYYWidthMax();
            }else{
                cardMMYYWidth = getMMYYWidthMin();
            }
        }
    }
    private boolean isSingle(){
        return layoutStyle == (int)IOTPayConstants.SingleLine.label;
    }
    private void displayBackLabels(boolean show){
        if(show){
            if(magnetlabel != null){
                if(magnetlabel.getVisibility() == VISIBLE){
                    return;
                }
                magnetlabel.setVisibility(VISIBLE);
            }
            if(holderlabel != null)holderlabel.setVisibility(VISIBLE);
            if(cvclabel != null)cvclabel.setVisibility(VISIBLE);
            cardImage.setImageResource(R.drawable.iotpaycard_gradient_back);
        }else{
            if(magnetlabel != null){
                if(magnetlabel.getVisibility() == INVISIBLE){
                    return;
                }
                magnetlabel.setVisibility(INVISIBLE);
            }
            if(holderlabel != null)holderlabel.setVisibility(INVISIBLE);
            if(cvclabel != null)cvclabel.setVisibility(INVISIBLE);
            cardImage.setImageResource(R.drawable.iotpaycard_gradient);
        }
        ObjectAnimator mAnimatorA1 = ObjectAnimator.ofFloat(cardImage, View.ROTATION_Y, 0, 180).setDuration(500);

        mAnimatorA1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                cardImageShadow.setVisibility(INVISIBLE);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                cardImageShadow.setVisibility(VISIBLE);
            }
        });
        mAnimatorA1.start();
    }
    private void init(){
        setSizes();
        {
            IOTPayCardShadow etCardItem = (IOTPayCardShadow) inflate(getContext(), R.layout.iotpaycard_shadow,null);
            iotPayCardShadow = etCardItem;
            LayoutParams params = new LayoutParams(cardWidth, cardHeightReal);
            iotPayCardShadow.setLayoutParams(params);
            addView(etCardItem);
        }
        {
            IOTPayCard etCardItem = (IOTPayCard) inflate(getContext(), R.layout.iotpaycard,null);
            iotPayCard = etCardItem;
            LayoutParams params = new LayoutParams(cardWidth, cardHeightReal);
            iotPayCard.setLayoutParams(params);
            addView(etCardItem);
        }

        {
            IOTPayEditCard etCardItem = (IOTPayEditCard) inflate(getContext(), R.layout.iotpaycard_num,null);
            setCommonAttr(etCardItem);
            etEditCard = etCardItem;

            etCardItem.setInputType(InputType.TYPE_CLASS_PHONE);
            etCardItem.setTextColor(getResources().getColor(R.color.white));
            etCardItem.setHintTextColor(getResources().getColor(R.color.white));
            LayoutParams params = new LayoutParams(cardNumWidth, cardItemHeight);
            etCardItem.setLayoutParams(params);

            addView(etCardItem);
            setEtItemListener(etCardItem);
        }

        {
            IOTPayHolder etCardItem = (IOTPayHolder) inflate(getContext(), R.layout.iotpayholder,null);
            setCommonAttr(etCardItem);
            etHolder = etCardItem;

            LayoutParams params = new LayoutParams(holderWidth, cardItemHeight);
            etCardItem.setLayoutParams(params);
            addView(etCardItem);
            setEtItemListener(etCardItem);

        }

        {
            IOTPayMMYY etCardItem = (IOTPayMMYY) inflate(getContext(), R.layout.iotpaymmyy,null);
            setCommonAttr(etCardItem);
            etMMYY = etCardItem;
            etCardItem.setHint( R.string.mmyy_default);
            etCardItem.setInputType(InputType.TYPE_CLASS_PHONE);

            LayoutParams params = new LayoutParams(cardMMYYWidth, cardItemHeight);
            etCardItem.setLayoutParams(params);
            addView(etCardItem);

            setEtItemListener(etCardItem);
        }

        {
            IOTPayCVC etCardItem = (IOTPayCVC) inflate(getContext(), R.layout.iotpaycvc,null);
            setCommonAttr(etCardItem);
            etCVV = etCardItem;

            etCardItem.setInputType(InputType.TYPE_CLASS_NUMBER);

            LayoutParams params = new LayoutParams(cardCVCWidth, cardItemHeight);
            etCardItem.setLayoutParams(params);
            addView(etCardItem);
            setEtItemListener(etCardItem);
        }
        magnetlabel = findViewById(R.id.magnetlabel);
        holderlabel = findViewById(R.id.namelabel);
        cvclabel = findViewById(R.id.cvclabel);
        cardImage = findViewById(R.id.cardImage);
        cardImageShadow = findViewById(R.id.cardImageShadow);
        setHint();

    }
    private void setHint(){
        if(isSingle()){
            etCVV.setHint( R.string.cvc);
            etHolder.setHint( R.string.holder_name);
            etEditCard.setHint( R.string.card_num);

        }else{
            etCVV.setHint( R.string.cvc_default);
            etHolder.setHint( R.string.holder_default);
            etEditCard.setHint( R.string.card_num_default);
        }
        int childCount = getChildCount();
        for(int i = 0; i < childCount; i++){
            try{
                String value = null;
                IOTPayEditText child = (IOTPayEditText)getChildAt(i);
                if(child != null){
                    if(isSingle()){
                        child.setBackground(null);
                    }else{
                        child.setBackground(getResources().getDrawable(R.drawable.bg_edittext));
                    }
                }
            }catch (Exception e){}
        }
    }
    private void setEtItemListener(IOTPayEditText etCardNumber0) {
        etCardNumber0.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    focus(etCardNumber0);
                }else{
                    lostFocus(etCardNumber0);
                }
            }
        });
    }
    private void setCommonAttr(IOTPayEditText etCardNumber0){
        etCardNumber0.setPadding(20,5,10,5);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //get initial dimension of ViewGroup
        int width = MeasureSpec.getSize(widthMeasureSpec) + getPaddingLeft() + getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec) + getPaddingTop() + getPaddingBottom();
        int index = 0;
        int line_height = 0;
        int yPos = getPaddingTop();

        for(int i = 0; i < getChildCount(); i++){
            final View child = getChildAt(index++);
            if(child.getVisibility() != GONE){
                final LayoutParams layoutParams = child.getLayoutParams();
                //calc child width
                int wSpec = MeasureSpec.makeMeasureSpec(layoutParams.width, MeasureSpec.EXACTLY);
                int hSpec = MeasureSpec.makeMeasureSpec(layoutParams.height, MeasureSpec.EXACTLY);
                child.measure(wSpec, hSpec);
                //calc width of view
                line_height = Math.max(line_height, child.getMeasuredHeight());

            }
        }
        //set viewgroup dimension
        height += yPos;
        setMeasuredDimension(width, height);
    }

    public IOTPayCardInfo getCardInfo() throws Exception{
        IOTPayCardInfo IOTPayCardInfo = new IOTPayCardInfo();

        int childCount = getChildCount();
        for(int i = 0; i < childCount; i++){
            try{
                String value = null;
                IOTPayEditText child = (IOTPayEditText)getChildAt(i);
                String msg = child.getInvalidMsg();
                if(msg != null){
                    child.setError(msg);
                    return null;
                }
                if(child != null){
                    String className = child.getClass().getName();
                    if(className.indexOf(".") > 0){
                        className = className.substring(className.lastIndexOf(".") + 1);
                    }
                    value = child.getValue() + "";
                    if(className.equals(IOTPayConstants.IOTPayEditCard.label + "")){
                        IOTPayCardInfo.cardNumber = value;
                    }else if(className.equals(IOTPayConstants.IOTPayHolder.label + "")){
                        IOTPayCardInfo.holder = value;
                    }else if(className.equals(IOTPayConstants.IOTPayCVC.label + "")){
                        IOTPayCardInfo.cvc = value;
                    }else if(className.equals(IOTPayConstants.IOTPayMMYY.label + "")){
                        IOTPayCardInfo.expiryDate = value;
                    }
                }
            }catch (Exception e){}
        }

        return IOTPayCardInfo;
    }
    private void focus(EditText etCardNumber0){
        String className = etCardNumber0.getClass().getName();
        if(className == null){
            return;
        }
        if(className.indexOf(".") > 0){
            className = className.substring(className.lastIndexOf(".") + 1);
        }
        if(screenWidth == null){
            return;
        }
        if(!isSingle()){
            if(className.equals(IOTPayConstants.IOTPayCVC.label+"")){
                displayBackLabels(true);
            }
            return;
        }

        focusedClassName = className;
        drawItems();
    }
    private void lostFocus(EditText etCardNumber0){
        String className = etCardNumber0.getClass().getName();
        if(className == null){
            return;
        }
        if(className.indexOf(".") > 0){
            className = className.substring(className.lastIndexOf(".") + 1);
        }
        if(!isSingle()){
            if(className.equals(IOTPayConstants.IOTPayCVC.label+"")){
                displayBackLabels(false);
            }
        }
    }

        public void switchType(int layoutStyle){
        boolean isSingle = (layoutStyle == (int)IOTPayConstants.SingleLine.label);
        if(isSingle == isSingle()){

            return;
        }
            if(isSingle){
                displayBackLabels(false);
            }else{
                if(etCVV.isFocused()){
                    displayBackLabels(true);
                }
            }

        this.layoutStyle = layoutStyle;
        setHint();
        drawItems();
        LayoutParams params = new LayoutParams(cardWidth, cardHeightReal);
        iotPayCard.setLayoutParams(params);
        iotPayCardShadow.setLayoutParams(params);
    }
}
