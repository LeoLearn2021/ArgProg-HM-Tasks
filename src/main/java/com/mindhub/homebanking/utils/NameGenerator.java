package com.mindhub.homebanking.utils;

public class NameGenerator {

    public NameGenerator(){}

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static String newAccountNumber(){
        return "VIN-" + getRandomNumber(1, 99_999_999);
    }

    public static int cvvGenerator(){
        return getRandomNumber(1, 999);
    }

    public static String newCardNumber(){
        return getRandomNumber(1, 9999) +"-"+getRandomNumber(1, 9999)+"-"+getRandomNumber(1, 9999)+"-"+getRandomNumber(1, 9999);
    }
}
