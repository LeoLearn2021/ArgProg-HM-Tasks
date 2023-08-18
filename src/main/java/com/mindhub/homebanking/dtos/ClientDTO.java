package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;

import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {

   private Long id;
   private String firstName;
   private String lastName;
   private String email;
   private Set<AccountDTO> accounts;
   private Set<ClientLoanDTO> loans;
   private Set<CardDTO> cards;
   public ClientDTO(Client client) {
      this.id = client.getId();
      this.firstName = client.getFirstName();
      this.lastName = client.getLastName();
      this.email = client.getEmail();
      this.accounts = client.getAccounts().stream().map(el -> new AccountDTO(el)).collect(Collectors.toSet());
      loans = client.getLoans().stream().map(clientLoan -> new ClientLoanDTO(clientLoan)).collect(Collectors.toSet());
      cards = client.getCards().stream().map(card -> new CardDTO(card)).collect(Collectors.toSet());
   }

   public Long getId() {
      return id;
   }

   public String getFirstName() {
      return firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public String getEmail() {
      return email;
   }

   public Set<AccountDTO> getAccounts() {
      return accounts;
   }

   public Set<ClientLoanDTO> getLoans(){
      return loans;
   }

   public Set<CardDTO> getCards(){
      return cards;
   }
}