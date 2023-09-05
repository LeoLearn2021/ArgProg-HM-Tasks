package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static com.mindhub.homebanking.utils.NameGenerator.cvvGenerator;
import static com.mindhub.homebanking.utils.NameGenerator.newCardNumber;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CardRepository cardRepository;

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(
            Authentication authentication, @RequestParam String cardColor,
            @RequestParam String cardType){
        if (authentication != null){
            CardType cType = CardType.findByName(cardType);
            CardColor cColor = CardColor.findByName(cardColor);
            if (cType != null && cColor != null){
                Client client = clientRepository.findByEmail(authentication.getName());
                int countCards = (int) client.getCards().stream().filter(card -> card.getType().equals(cType)).count();
                if (countCards < 3) {
                    Card card = new Card(checkedNewCardNumber(cardRepository), cType, cColor, cvvGenerator(), LocalDate.now(), LocalDate.now().plusYears(5));
                    client.addCard(card);
                    clientRepository.save(client);
                    cardRepository.save(card);
                    return new ResponseEntity<>("Card created to client", HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>("Limit by Card-type reached.", HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>("Incorrect data submitted.", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("No client.", HttpStatus.FORBIDDEN);
    }

    public static String checkedNewCardNumber(CardRepository cardRepository1){
        String number = newCardNumber();

        while (cardRepository1.findByNumber(number) != null){
            number = newCardNumber();
        }
        return number;
    }
}
