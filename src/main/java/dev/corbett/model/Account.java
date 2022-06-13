package dev.corbett.model;

public class Account {
    private int accountNumber;
    private float balance;
    private int clientID;

    public Account(int accountNumber, float balance, int clientID){
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.clientID = clientID;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
