package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
public class LoanController {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientService clientService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ClientLoanRepository clientLoanRepository;

    @GetMapping("/loans")
    public ResponseEntity<Object> getLoans(Authentication authentication) {
        if (authentication.isAuthenticated()) {
            List<LoanDTO> loanTypes = loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
            return new ResponseEntity<>(loanTypes, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("No client.", HttpStatus.FORBIDDEN);
    }
    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> applyToLoan(
            Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO) {
        if (authentication.isAuthenticated()) {
            Client client = clientService.getClientAuth(authentication);
            Account account = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());
            if (account != null && client.getAccounts().contains(account)) {
                if (!loanApplicationDTO.getAmount().equals(0) && !loanApplicationDTO.getPayments().equals(0)) {
                    Loan loan = loanRepository.findById(loanApplicationDTO.getLoanId()).orElse(null);
                    if (loan != null) {
                        double amount = (double) Math.abs(loanApplicationDTO.getAmount());
                        if (amount <= loan.getMaxAmount()) {
                            if (loan.getPayments().contains(loanApplicationDTO.getPayments())) {
                                amount *= 1.2d;
                                ClientLoan clientLoan = new ClientLoan(amount, loanApplicationDTO.getPayments());
                                Transaction transaction = new Transaction(TransactionType.CREDIT, amount, loan.getName() +" loan approved", LocalDateTime.now());
                                client.addLoan(clientLoan);
                                clientService.saveClient(client);
                                account.addTransaction(transaction);
                                account.setBalance(account.getBalance() + (double) Math.abs(loanApplicationDTO.getAmount()));
                                accountRepository.save(account);
                                transactionRepository.save(transaction);
                                loan.addClient(clientLoan);
                                clientLoanRepository.save(clientLoan);

                                return new ResponseEntity<>("Success, loan granted.", HttpStatus.CREATED);
                            }
                            return new ResponseEntity<>("Not valid payments option.", HttpStatus.FORBIDDEN);
                        }
                        return new ResponseEntity<>("Not allowed amount.", HttpStatus.FORBIDDEN);
                    }
                    return new ResponseEntity<>("Non valid loan submitted.", HttpStatus.FORBIDDEN);
                }
                return new ResponseEntity<>("Nor amount neither payments must be null.", HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>("No valid account submitted.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("No client.", HttpStatus.FORBIDDEN);
    }
}
