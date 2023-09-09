package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.mindhub.homebanking.controllers.AccountController.checkedNewAccountNumber;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //Clients  Servlet
    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientService.getClientsDTO();
    }
    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientService.getClientDTO(id);
    }

    @RequestMapping("clients/current")
    public ResponseEntity<Object> getClientAuth(Authentication authentication){
        if (authentication != null){
            return new ResponseEntity<>(clientService.getClientAuth(authentication), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("No client logged", HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password){
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()){
            return new ResponseEntity<>("All fields must be completed.", HttpStatus.FORBIDDEN);
        }
        if (clientService.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        //Create and associate new account to newly created client
        Account account = new Account(checkedNewAccountNumber(accountRepository), LocalDate.now(), 0.0d);
        client.addAccount(account);
        clientService.saveClient(client);
        accountRepository.save(account);

        return new ResponseEntity<>("Client created.", HttpStatus.CREATED);
    }

    @GetMapping("/clients/online")
    public ResponseEntity<String> connection(Authentication authentication){
        if (authentication != null){
            Client client = clientService.getClientAuth(authentication);
            return new ResponseEntity<>("Client logged: "+client.getFirstName(), HttpStatus.ACCEPTED);
        } else {
            return  new ResponseEntity<>("No usr logged", HttpStatus.FORBIDDEN);
        }
    }

}
