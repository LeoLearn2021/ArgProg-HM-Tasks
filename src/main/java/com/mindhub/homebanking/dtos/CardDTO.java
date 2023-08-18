package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.time.LocalDate;

public class CardDTO {

    private Long id;
    private String number;
    private String cardHolder;
    private CardType type;
    private CardColor color;
    private Integer cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;

    public CardDTO(Card card){
        id = card.getId();
        number = card.getNumber();
        cardHolder = card.getCardholder();
        type = card.getType();
        color = card.getColor();
        cvv = card.getCvv();
        fromDate = card.getFromDate();
        thruDate = card.getThruDate();
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }

    public Integer getCvv() {
        return cvv;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }
}
