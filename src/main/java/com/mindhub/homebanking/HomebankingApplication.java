package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);

	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository){
		return args -> {

			//Create Client
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			System.out.println(client1);
			//Save client to DB
			clientRepository.save(client1);
			System.out.println(client1);
			Client client2 = new Client("Oreo", "Milka", "oreo@Choko.com");
			clientRepository.save(client2);

			//Create accounts
			Account account1 = new Account();
			account1.setNumber("VIN001");
			account1.setCreationDate(LocalDate.now());
			account1.setBalance(5000);
			//Add account to specific client
			client1.addAccount(account1);
			//Save account to DB
			accountRepository.save(account1);

			Account account2 = new Account();
			account2.setNumber("VIN002");
			account2.setCreationDate(LocalDate.now().plusDays(1));
			account2.setBalance(7500);
			//Add account to specific client
			client1.addAccount(account2);
			//Save account to DB
			accountRepository.save(account2);

			//Create accounts
			Account account3 = new Account();
			account3.setNumber("VIN003");
			account3.setCreationDate(LocalDate.now());
			account3.setBalance(5500);
			//Add account to specific client
			client2.addAccount(account3);
			//Save account to DB
			accountRepository.save(account3);

			Account account4 = new Account();
			account4.setNumber("VIN004");
			account4.setCreationDate(LocalDate.now().plusDays(1));
			account4.setBalance(54500);
			//Add account to specific client
			client2.addAccount(account4);
			//Save account to DB
			accountRepository.save(account4);

			//Create some transactions for account 1
			Transaction transaction1 = new Transaction();
			transaction1.setType(TransactionType.DEBIT);
			transaction1.setAmount(-500);
			transaction1.setDate(LocalDateTime.now());
			transaction1.setDescription("Some item.");
			//link transaction to account
			account1.addTransaction(transaction1);
			account1.setBalance(account1.getBalance() + transaction1.getAmount());
			accountRepository.save(account1);
			//Save transaction to DB
			transactionRepository.save(transaction1);

			Transaction transaction2 = new Transaction(TransactionType.CREDIT, 350d, "Returned Item", LocalDateTime.now());
			account1.addTransaction(transaction2);
			account1.setBalance(account1.getBalance() + transaction2.getAmount());
			accountRepository.save(account1);
			transactionRepository.save(transaction2);

			//Create some transactions for account 2

			Transaction transaction3 = new Transaction(TransactionType.DEBIT, -3543.5d, "Something Usefull", LocalDateTime.now());
			account2.addTransaction(transaction3);
			account2.setBalance(account2.getBalance() + transaction3.getAmount());
			accountRepository.save(account2);
			transactionRepository.save(transaction3);

			Transaction transaction4 = new Transaction(TransactionType.CREDIT, 343.32d, "Sale product", LocalDateTime.now());
			account2.addTransaction(transaction4);
			account2.setBalance(account2.getBalance() + transaction4.getAmount());
			accountRepository.save(account2);
			transactionRepository.save(transaction4);

		};
	}
}
