package movile.com.creditcardguide.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import movile.com.creditcardguide.R;
import movile.com.creditcardguide.anim.AnimateImageTransitionFade;
import movile.com.creditcardguide.anim.FlipAnimation;
import movile.com.creditcardguide.model.IssuerCode;
import movile.com.creditcardguide.util.FontUtils;

/**
 * Created by FelipeSilvestre on 16/02/16.
 */
public class CreditCardView extends FrameLayout {

    private FrameLayout frameLayoutFrontCard;
    private FrameLayout frameLayoutBackCard;
    private FrameLayout rootCreditCard;

    private TextView textLabelOwner;
    private FontFitTextView textOwner;
    private TextView textLabelExpDate;
    private FontFitTextView textExpDate;
    private TextView textNumber;
    private FontFitTextView textCVV;

    private ImageView cardFront;
    private ImageView cardBack;
    private ImageView cardSign;
    private ImageView reversedNumberBack;

    private String labelCardOwner;
    private String labelCardDateExp;

    private IssuerCode currentIssuer;


    public CreditCardView(Context context) {
        super(context);
        init();
    }

    public CreditCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CreditCardView);

        if (typedArray != null) {
            labelCardOwner = typedArray.getString(R.styleable.CreditCardView_labelOwner);
            labelCardDateExp = typedArray.getString(R.styleable.CreditCardView_labelDateExp);
            typedArray.recycle();
        }

        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.credit_card_view, this, true);
        FontUtils.loadFonts(this);

        rootCreditCard = (FrameLayout) findViewById(R.id.container_card);
        frameLayoutFrontCard = (FrameLayout) findViewById(R.id.frg_input_card_front);
        frameLayoutBackCard = (FrameLayout) findViewById(R.id.frg_input_card_back);

        textNumber = (TextView) findViewById(R.id.txt_number_credit_card);
        textExpDate = (FontFitTextView) findViewById(R.id.txt_expire_credit_card);
        textOwner = (FontFitTextView) findViewById(R.id.txt_name_credit_card);
        cardFront = (ImageView) findViewById(R.id.view_front_card_image);
        cardBack = (ImageView) findViewById(R.id.view_back_card_image);
        cardSign = (ImageView) findViewById(R.id.view_back_card_image_sign);
        textLabelExpDate = (TextView) findViewById(R.id.txt_expire_credit_card_label);
        textLabelOwner = (TextView) findViewById(R.id.txt_name_credit_card_label);
        textCVV = (FontFitTextView) findViewById(R.id.back_credit_card_txt_cvv);
        reversedNumberBack = (ImageView) findViewById(R.id.view_reversed_number);

        if (!TextUtils.isEmpty(labelCardOwner)) {
            setTextLabelOwner(labelCardOwner);
        }

        if (!TextUtils.isEmpty(labelCardDateExp)) {
            setTextLabelExpDate(labelCardDateExp);
        }
    }

    public void setTextCVV(CharSequence textCVV) {
        this.textCVV.setText(textCVV, TextView.BufferType.NORMAL);
    }

    public void setTextNumber(CharSequence textNumber) {
        setTextNumber(textNumber, false);
    }

    public void setTextNumber(CharSequence textNumber, boolean hideNumbers) {
        if (hideNumbers) {
            this.textNumber.setText(replaceCharacters(textNumber.toString(), 7, '#', ' '));
        } else {
            this.textNumber.setText(textNumber);
        }
    }

    private String replaceCharacters(@NonNull String s, @IntRange(from = 1) int length, char replacement, char ignoreChar) {
        s = s.trim();
        if (s.length() < length) {
            length = s.length();
        }
        char[] data = new char[length];
        for (int i = 0; i < length; i++) {
            if (s.charAt(i) == ignoreChar) {
                data[i] = ignoreChar;
            } else {
                data[i] = replacement;
            }
        }
        return new String(data) + s.substring(length);
    }

    public void setTextExpDate(CharSequence textExpDate) {
        this.textExpDate.setText(textExpDate);
    }

    public void setTextLabelExpDate(CharSequence textLabelExpDate) {
        this.textLabelExpDate.setText(textLabelExpDate.toString().toUpperCase());
    }

    public void setTextOwner(CharSequence textOwner) {
        this.textOwner.setText(textOwner.toString().toUpperCase(), TextView.BufferType.NORMAL);
    }

    public void setTextLabelOwner(CharSequence textLabelOwner) {
        this.textLabelOwner.setText(textLabelOwner.toString().toUpperCase());
    }

    public void flipToBack() {
        reversedNumberBack.setImageBitmap(getMirrorTransparentBitmap(getContext(), textNumber, currentIssuer));

        FlipAnimation flipAnimation = new FlipAnimation(frameLayoutFrontCard, frameLayoutBackCard);
        rootCreditCard.startAnimation(flipAnimation);
    }

    public void flipToFront() {
        FlipAnimation flipAnimation = new FlipAnimation(frameLayoutBackCard, frameLayoutFrontCard);
        rootCreditCard.startAnimation(flipAnimation);
    }

    public boolean isShowingFront() {
        return frameLayoutFrontCard.getVisibility() == VISIBLE;
    }

    public void clear() {
        textCVV.setText("");
        textExpDate.setText(getContext().getString(R.string.hint_credit_expire_date));
        textNumber.setText("");
        textOwner.setText("");
    }

    public void chooseFlag(IssuerCode issuerCode) {
        this.currentIssuer = issuerCode;

        if (issuerCode == IssuerCode.AMEX) {
            textCVV.setText("0000", TextView.BufferType.NORMAL);
        } else {
            textCVV.setText("000", TextView.BufferType.NORMAL);
        }

        AnimateImageTransitionFade.imageViewAnimatedChange(getContext(), cardFront, issuerCode.getImageCardFront());

        cardBack.setImageResource(issuerCode.getImageCardBack());
        cardSign.setImageResource(issuerCode.getImageSignCard());

        getImageColor(getContext(), issuerCode.getImageCardBack());

        textLabelExpDate.setTextColor(ContextCompat.getColor(getContext(), issuerCode.getColorText()));
        textExpDate.setTextColor(ContextCompat.getColor(getContext(), issuerCode.getColorText()));
        textLabelOwner.setTextColor(ContextCompat.getColor(getContext(), issuerCode.getColorText()));
        textOwner.setTextColor(ContextCompat.getColor(getContext(), issuerCode.getColorText()));
        textNumber.setTextColor(ContextCompat.getColor(getContext(), issuerCode.getColorText()));

        textLabelExpDate.setShadowLayer(1, 1, 1, ContextCompat.getColor(getContext(), issuerCode.getColorShadowText()));
        textExpDate.setShadowLayer(1, 1, 1, ContextCompat.getColor(getContext(), issuerCode.getColorShadowText()));
        textLabelOwner.setShadowLayer(1, 1, 1, ContextCompat.getColor(getContext(), issuerCode.getColorShadowText()));
        textOwner.setShadowLayer(1, 1, 1, ContextCompat.getColor(getContext(), issuerCode.getColorShadowText()));
        textNumber.setShadowLayer(1, 1, 1, ContextCompat.getColor(getContext(), issuerCode.getColorShadowText()));
    }

    private Bitmap getMirrorTransparentBitmap(Context context, TextView textView, IssuerCode issuerCode) {
        // Invert colors
        textView.setTextColor(getDarkerColor(getImageColor(context, issuerCode.getImageCardBack()), 0.7f));
        textView.setShadowLayer(1, 0, 1, ContextCompat.getColor(context, R.color.white));

        // Draw current TextView on a canvas
        Bitmap source = Bitmap.createBitmap(textView.getWidth(), textView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(source);
        textView.draw(canvas);

        // Invert colors
        textView.setTextColor(ContextCompat.getColor(context, issuerCode.getColorText()));
        textView.setShadowLayer(1, 1, 1, ContextCompat.getColor(context, issuerCode.getColorShadowText()));

        // Reverse source
        int width = source.getWidth();
        int height = source.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(source, 0, 0, width, height, matrix, false);
    }

    public static int getDarkerColor(int color, float factor) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= factor;
        return Color.HSVToColor(hsv);
    }

    public static int getImageColor(Context context, int drawable) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawable);
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 11, 11, true);
        final int color = newBitmap.getPixel(10, 10);
        newBitmap.recycle();
        return color;
    }
}
