package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "native" )
    @GenericGenerator( name="native", strategy = "native" )
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Client client;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "account")
    private Set<Transaction> transactions = new HashSet<>();

    private String number;

    private LocalDate creationDate;

    private double balance;

    //Added constructors for ease Loading data
    public Account() { }

    public Account(String number, LocalDate creationDate, Double balance) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
    }

    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addTransaction(Transaction transaction) {
        transaction.setAccount(this);
        this.transactions.add(transaction);
    }
    public Set<Transaction> getTransactions() {
        return transactions;
    }

}
