package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository){
		return args -> {

			//Create Client
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("melba"));
			System.out.println(client1);
			//Save client to DB
			clientRepository.save(client1);
			System.out.println(client1);
			Client client2 = new Client("Oreo", "Milka", "oreo@Choko.com", passwordEncoder.encode("oreo"));
			clientRepository.save(client2);

			//Create Admin
			Client admin = new Client("Admin", "Admin", "admin@admin.com", passwordEncoder.encode("admin"));
			clientRepository.save(admin);

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

			// Assigments for Spring 4 (Task 4: Implement Loans)

			Loan mortgageLoan1 = new Loan("Hipotecario", 500000, List.of(12, 24, 36, 48, 60));
			Loan personalLoan1 = new Loan("Personal", 100000, List.of(6,12,24));
			Loan carLoan1 = new Loan("Automotriz", 300000, List.of(6,12,24,36));

			loanRepository.save(mortgageLoan1);
			loanRepository.save(personalLoan1);
			loanRepository.save(carLoan1);

			//Create loans for client1 ("Melba")

			//ClientLoan clientLoan1 = new ClientLoan(400000, 60, client1, mortgageLoan1);
			//ClientLoan clientLoan2 = new ClientLoan(50000,12, client1, personalLoan1);
			// Constructor must instaciate objects, not make relations. For relations use methods add created in
			// classes assosiated with the OneToMany anotations (note in corrections).
			ClientLoan clientLoan1 = new ClientLoan(400000d, 60);
			ClientLoan clientLoan2 = new ClientLoan(50000d,12);
			client1.addLoan(clientLoan1);
			client1.addLoan(clientLoan2);
			clientRepository.save(client1);
			mortgageLoan1.addClient(clientLoan1);
			personalLoan1.addClient(clientLoan2);
			loanRepository.save(mortgageLoan1);
			loanRepository.save(personalLoan1);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);

			//Create loans for client2

			//ClientLoan clientLoan3 = new ClientLoan(100000, 24, client2, personalLoan1);
			//ClientLoan clientLoan4 = new ClientLoan(200000,36, client2, carLoan1);
			ClientLoan clientLoan3 = new ClientLoan(100000d, 24);
			ClientLoan clientLoan4 = new ClientLoan(200000d,36);
			client2.addLoan(clientLoan3);
			client2.addLoan(clientLoan4);
			clientRepository.save(client2);
			personalLoan1.addClient(clientLoan3);
			carLoan1.addClient(clientLoan4);
			loanRepository.save(personalLoan1);
			loanRepository.save(carLoan1);

			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			// Assigments for Spring5 (Task 5: Implement Cards)

			Card card1 = new Card("1111-2222-3333-4444", CardType.DEBIT, CardColor.GOLD, 123, LocalDate.now(), LocalDate.now().plusYears(5));
			Card card2 = new Card("2222-3333-4444-5555", CardType.CREDIT, CardColor.TITANIUM, 234, LocalDate.now(), LocalDate.now().plusYears(5));
			Card card3 = new Card("3333-4444-5555-6666", CardType.CREDIT, CardColor.SILVER, 345, LocalDate.now(), LocalDate.now().plusYears(5));

			client1.addCard(card1);
			client1.addCard(card2);
			clientRepository.save(client1);
			cardRepository.save(card1);
			cardRepository.save(card2);
			client2.addCard(card3);
			clientRepository.save(client2);
			cardRepository.save(card3);

		};
	}
}
