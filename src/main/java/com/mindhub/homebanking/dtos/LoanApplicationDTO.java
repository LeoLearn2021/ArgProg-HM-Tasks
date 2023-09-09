package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {

    private Long loanId;

    private Integer amount;

    private Integer payments;

    private String toAccountNumber;

    public LoanApplicationDTO() {};

    public LoanApplicationDTO(Long loanId, Integer amount, Integer payments, String toAccountNumber) {
        this.loanId = loanId;
        this.amount = amount;
        this.payments = payments;
        this.toAccountNumber = toAccountNumber;
    }

    public Long getLoanId() {
        return loanId;
    }

    public Integer getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    @Override
    public String toString(){
        return "LoanApplication{" +
                " loanId='" + loanId +
                ", amount='" + amount + '\'' +
                ", payments='" + payments + '\'' +
                ", toAccountNumber='" + toAccountNumber + '\'' +
                '}';
    }
}
