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
    public AccountService(AccountDAO aDAO){
        this.aDAO = aDAO;
    }
    private static ClientDAO cDAO;
    public AccountService(ClientDAO cDAO){
        this.cDAO = cDAO;
    }

    public Account createAccount(Account a, int clientId){
        Account createdAccount = aDAO.createAccount(a, clientId);
        return createdAccount;
    }

    public List<Account> getAllAccounts(int clientId){
        return aDAO.getAllAccounts(clientId);
    }

    public Account getAccountByNumber(int accNum, int clientId){
        return aDAO.getAccountByNumber(accNum, clientId);
    }

    public List<Account> getAccountsByBalance(float ceiling, float floor, int clientId){
        return aDAO.getAccountsByBalance(ceiling, floor, clientId);
    }

    public void deleteAccount(int clientId, int accNum){
        aDAO.deleteAccount(clientId, accNum);
    }

    public void updateAccount(int clientId, int accNum, String operation, float total) throws Exception{
        float bal = aDAO.getAccountBalance(clientId, accNum);
        if(operation.equals("deposit")){
            bal += total;
        } else
            bal -= total;
        boolean changed = true;
        if(bal < 0.00){
            changed = false;
        } else {
            aDAO.updateAccount(bal, clientId, accNum);
        }
    }

    public void transferBetweenAccounts (float total, String operation, int accNum1, int accNum2, int clientId) throws Exception{
        //remove total from account 1 - throw error if balance < 0
        float acc1bal = aDAO.getAccountBalance(clientId, accNum1);
        acc1bal -= total;
        boolean changed = true;
        if(acc1bal < 0){
            changed = false;
        } else{
            float acc2bal = aDAO.getAccountBalance(clientId, accNum2);
            acc2bal += total;
            //update account 1
            aDAO.updateAccount(acc1bal, clientId, accNum1);
            //update account 2
            aDAO.updateAccount(acc2bal, clientId, accNum2);
        }
    }
}
