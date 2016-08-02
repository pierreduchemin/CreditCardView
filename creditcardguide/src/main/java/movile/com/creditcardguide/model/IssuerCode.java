package movile.com.creditcardguide.model;


import java.io.Serializable;
import java.util.regex.Pattern;

import movile.com.creditcardguide.R;

public enum IssuerCode implements Serializable {

    AMEX(R.string.amex, R.drawable.card_american, R.drawable.american, R.drawable.american_back, R.drawable.sign_american, R.color.gray_hard_text, R.color.white, "^3[47][0-9]{13}$"),
    AURA(R.string.aura, R.drawable.card_aura, R.drawable.aura, R.drawable.aura_back, R.drawable.sign_master, R.color.gray_hard_text, R.color.gray_hard_text, "^(5078\\d{2})(\\d{2})(\\d{11})$"),
    DINERS(R.string.diners, R.drawable.card_diners, R.drawable.diners, R.drawable.diners_back, R.drawable.sign_diners, R.color.white, R.color.gray_hard_text, "^3(?:0[0-5]|[68][0-9])[0-9]{11}$"),
    PAYPAL(R.string.paypal, R.drawable.card_paypal, R.drawable.paypal, R.drawable.paypal_back, R.drawable.sign_paypal, R.color.white, R.color.gray_hard_text),
    ELO(R.string.elo, R.drawable.card_elo, R.drawable.elo, R.drawable.elo_back, R.drawable.sign_diners, R.color.white, R.color.gray_hard_text, "^(401178|401179|431274|438935|451416|457393|457631|457632|504175|627780|636297|636368|(506699|5067[0-6]\\d|50677[0-8])|(50900\\d|5090[1-9]\\d|509[1-9]\\d{2})|65003[1-3]|(65003[5-9]|65004\\d|65005[0-1])|(65040[5-9]|6504[1-3]\\d)|(65048[5-9]|65049\\d|6505[0-2]\\d|65053[0-8])|(65054[1-9]|6505[5-8]\\d|65059[0-8])|(65070\\d|65071[0-8])|65072[0-7]|(65090[1-9]|65091\\d|650920)|(65165[2-9]|6516[6-7]\\d)|(65500\\d|65501\\d)|(65502[1-9]|6550[3-4]\\d|65505[0-8]))[0-9]{10,12}"),
    HIPERCARD(R.string.hipercard, R.drawable.card_hiper, R.drawable.hipercard, R.drawable.hipercard_back, R.drawable.sign_hipercard, R.color.white, R.color.gray_hard_text, "^(606282\\d{10}(\\d{3})?)|(3841\\d{15})$"),
    MASTERCARD(R.string.redecardcredito, R.drawable.card_master, R.drawable.mastercard, R.drawable.mastercard_back, R.drawable.sign_master, R.color.white, R.color.gray_hard_text, "^5[1-5][0-9]{14}$"),
    NUBANK(R.string.nubank, R.drawable.card_nubank, R.drawable.nubank, R.drawable.nubank_back, R.drawable.sign_master, R.color.white, R.color.gray_hard_text),
    VISA_ELECTRON(R.string.visaelectron, R.drawable.card_visa, R.drawable.visa, R.drawable.visa_back, R.drawable.sign_visa, R.color.white, R.color.gray_hard_text, "^((4026|4508|4844|491(3|7))[0-9]{12})|((417500)[0-9]{10})$"),
    VISA_INTERNATIONAL(R.string.visacredito, R.drawable.card_visa, R.drawable.visa, R.drawable.visa_back, R.drawable.sign_visa, R.color.white, R.color.gray_hard_text, "^(?:4[0-9]{12}(?:[0-9]{3})?"),
    OTHER(R.string.other, R.drawable.card_placeholder, R.drawable.default_card, R.drawable.default_card_back, R.drawable.sign_diners, R.color.white, R.color.gray_hard_text);

    private int nameId;
    private Integer iconId;
    private Integer imageCardFront;
    private Integer imageCardBack;
    private Integer imageSignCard;
    private Integer colorText;
    private Integer colorShadowText;
    private Pattern pattern;

    IssuerCode(int nameId, Integer iconId, Integer imageCardFront, Integer imageCardBack, Integer imageSignCard, Integer colorText, Integer colorShadowText) {
        this(nameId, iconId, imageCardFront, imageCardBack, imageSignCard, colorText, colorShadowText, null);
    }

    IssuerCode(int nameId, Integer iconId, Integer imageCardFront, Integer imageCardBack, Integer imageSignCard, Integer colorText, Integer colorShadowText, String pattern) {
        this.nameId = nameId;
        this.iconId = iconId;
        this.imageCardFront = imageCardFront;
        this.imageCardBack = imageCardBack;
        this.imageSignCard = imageSignCard;
        this.colorText = colorText;
        this.colorShadowText = colorShadowText;
        this.pattern = Pattern.compile(pattern);
    }

    public int getNameId() {
        return nameId;
    }

    public Integer getIconId() {
        return iconId;
    }

    public Integer getImageCardFront() {
        return imageCardFront;
    }

    public Integer getImageCardBack() {
        return imageCardBack;
    }

    public Integer getImageSignCard() {
        return imageSignCard;
    }

    public Integer getColorText() {
        return colorText;
    }

    public Integer getColorShadowText() {
        return colorShadowText;
    }

    public static IssuerCode detect(String cardNumber) {
        if (cardNumber == null) {
            return OTHER;
        }
        for (IssuerCode issuerCode : IssuerCode.values()) {
            if (null == issuerCode.pattern) {
                continue;
            }
            if (issuerCode.pattern.matcher(cardNumber).matches()) {
                return issuerCode;
            }
        }
        return OTHER;
    }
}
