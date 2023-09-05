package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.NameGenerator.newAccountNumber;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    //Account Servlet
    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        //System.out.pr
        // intln("id= "+id);
        return new AccountDTO(accountRepository.findById(id).orElse(null));
    }

    @RequestMapping("/current/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id, Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findById(id).orElse(null);
        //assert account != null;
        if (account.getClient().equals(client)) {
            return new AccountDTO(account);
        }
        return null;
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<String> createAccount(Authentication authentication){
        if  (authentication != null){
            Client client = clientRepository.findByEmail(authentication.getName());
            if (client.getAccounts().size()<3){
                //Create new account to client
                Account account = new Account(checkedNewAccountNumber(accountRepository), LocalDate.now(), 0.0d);
                client.addAccount(account);
                clientRepository.save(client);
                accountRepository.save(account);
                return new ResponseEntity<>("Account created", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Limit reached, not able to comply.", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("No client.", HttpStatus.FORBIDDEN);
        }
    }

    public static String checkedNewAccountNumber(AccountRepository accountRepository1){
        //NameGenerator gen = new NameGenerator();
        String number = newAccountNumber();

        while (accountRepository1.findByNumber(number) != null){
            number = newAccountNumber();
        }
        return number;
    }
}
