package dev.corbett.model;

public class Account {
    private int accountNumber;
    private float balance;
    private int clientId;
    private boolean checking;

    public Account(){

    }

    public Account(int accountNumber, float balance, int clientID, boolean checking){
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.clientId = clientID;
        this.checking = checking;
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

    public int getClientId(){
        return clientId;
    }

    public void setClientId(){
        this.clientId = clientId;
    }

    public boolean getChecking(){
        return checking;
    }
    public void setChecking(){
        this.checking = checking;
    }
}
