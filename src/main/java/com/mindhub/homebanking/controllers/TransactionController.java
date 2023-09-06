package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @Transactional
    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> transaction(
            Authentication authentication, @RequestParam Double amount,
            @RequestParam String description,@RequestParam String fromAccountNumber, @RequestParam String toAccountNumber ){

        if (authentication != null){
            if (amount.isNaN() || description.isEmpty() || fromAccountNumber.isEmpty() || toAccountNumber.isEmpty()){
                return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
            }
            if (fromAccountNumber.equals(toAccountNumber)){
                return new ResponseEntity<>("Accounts must be different.", HttpStatus.FORBIDDEN);
            }
            Account fromAccount = accountRepository.findByNumber(fromAccountNumber);
            if (fromAccount != null && fromAccount.getClient().getEmail().equals(authentication.getName())){
                // Account existing from client
                Account toAccount = accountRepository.findByNumber(toAccountNumber);
                if (toAccount != null){
                    if (fromAccount.getBalance() >= amount){
                        LocalDateTime transactionTime = LocalDateTime.now();
                        double checkedAmount = Math.abs(amount);
                        Transaction debitTransaction = new Transaction( TransactionType.DEBIT, -1*checkedAmount, description +" / "+ fromAccountNumber, transactionTime);
                        Transaction creditTransaction = new Transaction( TransactionType.CREDIT, checkedAmount, description +" / "+ toAccountNumber, transactionTime);

                        fromAccount.addTransaction(debitTransaction);
                        fromAccount.setBalance(fromAccount.getBalance() + debitTransaction.getAmount());
                        accountRepository.save(fromAccount);
                        transactionRepository.save(debitTransaction);

                        toAccount.addTransaction(creditTransaction);
                        toAccount.setBalance(toAccount.getBalance() + creditTransaction.getAmount());
                        accountRepository.save(toAccount);
                        transactionRepository.save(creditTransaction);

                        return  new ResponseEntity<>("Transaction created succesfully.", HttpStatus.CREATED);
                    } else {
                        return new ResponseEntity<>("Insufficient funds.", HttpStatus.FORBIDDEN);
                    }
                } else {
                    return new ResponseEntity<>("No valid Account to transfer.", HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>("No Account from client.", HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>("No client.", HttpStatus.FORBIDDEN);
    }
}
