package dev.corbett.service;

import dev.corbett.model.Client;
import dev.corbett.repository.AccountDAO;
import dev.corbett.model.Account;
import dev.corbett.repository.ClientDAO;


import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class AccountService {
    private static AccountDAO aDAO;

    public AccountService(AccountDAO aDAO) {
        this.aDAO = aDAO;
    }

    private static ClientDAO cDAO;

    public AccountService(ClientDAO cDAO) {
        this.cDAO = cDAO;
    }

    public Account createAccount(Account a) {
        Account createdAccount = aDAO.createAccount(a);
        return createdAccount;
    }

    public List<Account> getAllAccounts(int clientId){
        return aDAO.getAllAccounts(clientId);
    }

    public Account getAccountByNumber(int clientId, int accNum){
        return aDAO.getAccountByNumber(clientId, accNum);
    }

    public List<Account> getAccountsByBalance(float ceiling, float floor, int clientId){
        return aDAO.getAccountsByBalance(ceiling, floor, clientId);
    }

    public Account deleteAccount(int clientId, int accNum){
        return aDAO.deleteAccount(clientId, accNum);
    }

    public Account updateAccount(int clientId, int accNum, float total, boolean type){
        return aDAO.updateAccount(total, clientId, accNum, type);
    }

    public float getAccountBalance(int clientId, int accNum){
        return aDAO.getAccountBalance(clientId, accNum);
    }

    public String getAccountType(int clientId, int accNum){
        return aDAO.getAccountType(clientId, accNum);
    }

    public List<Account> transferBetweenAccounts (float total, int accNum1, int accNum2, int clientId1, int clientId2, float bal1, float bal2, boolean type1, boolean type2){
        return aDAO.transferBetweenAccounts(total, accNum1, accNum2, clientId1, clientId2, bal1, bal2, type1, type2);
    }
}