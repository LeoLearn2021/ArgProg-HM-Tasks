package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.controllers.AccountController.checkedNewAccountNumber;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //Clients  Servlet
    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
        List<Client> clientList = clientRepository.findAll();

        List<ClientDTO> clientDTOList =
                clientList
                        .stream()
                        .map( client -> new ClientDTO(client))
                        .collect(Collectors.toList());
        return clientDTOList;
    }
    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return new ClientDTO(clientRepository.findById(id).orElse(null));
    }

    @RequestMapping("clients/current")
    public ResponseEntity<Object> getClientAuth(Authentication authentication){
        if (authentication != null){
            return new ResponseEntity<>( new ClientDTO(clientRepository.findByEmail(authentication.getName())), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("No client logged", HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password){
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        //Create and associate new account to newly created client
        Account account = new Account(checkedNewAccountNumber(accountRepository), LocalDate.now(), 0.0d);
        System.out.println(account.toString());
        client.addAccount(account);
        clientRepository.save(client);
        accountRepository.save(account);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/online")
    public ResponseEntity<String> connection(Authentication authentication){

        if (authentication != null){
            Client client = clientRepository.findByEmail(authentication.getName());
            return new ResponseEntity<>("Client logged: "+client.getFirstName(), HttpStatus.ACCEPTED);
        } else {
            return  new ResponseEntity<>("No usr logged", HttpStatus.FORBIDDEN);
        }
    }

}
