package com.mindhub.homebanking.models;

public enum CardColor {
    GOLD,
    SILVER,
    TITANIUM;

    public static CardColor findByName(String name) {
        CardColor result = null;
        for (CardColor cColor : values()) {
            if (cColor.name().equalsIgnoreCase(name)) {
                result = cColor;
                break;
            }
        }
        return result;
    }
}
