package com.mindhub.homebanking.models;

public enum CardType {
    CREDIT,
    DEBIT;

    public static CardType findByName(String name) {
        CardType result = null;
        for (CardType cType : values()) {
            if (cType.name().equalsIgnoreCase(name)) {
                result = cType;
                break;
            }
        }
        return result;
    }
}
