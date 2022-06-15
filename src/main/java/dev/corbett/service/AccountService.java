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
        //test whether client exists
        Client hypothetical = cDAO.getClientById(clientId);
        if(hypothetical == null){
            throw new Exception("Client does not exist");
        }
        //test whether account exists
        Account possible = aDAO.getAccountByNumber(accNum, clientId);
        if(possible == null){
            throw new Exception("Account does not exist");
        }
        float bal = aDAO.getAccountBalance(clientId, accNum);
        if(operation.equals("deposit")){
            bal += total;
        } else
            bal -= total;

        if(bal < 0.00){
            throw new Exception("Insufficient funds");
        }
        aDAO.updateAccount(bal, clientId, accNum);
    }

    public void transferBetweenAccounts (float total, String operation, int accNum1, int accNum2, int clientId) throws Exception{
        //test whether client exists
        Client hypothetical = cDAO.getClientById(clientId);
        if(hypothetical == null){
            throw new Exception("Client does not exist");
        }

        //test whether each account exists
        Account possible1 = aDAO.getAccountByNumber(accNum1, clientId);
        Account possible2 = aDAO.getAccountByNumber(accNum2, clientId);
        if(possible1 == null){
            throw new Exception("First account does not exist");
        }
        if(possible2 == null){
            throw new Exception("Second account does not exist");
        }

        //remove total from account 1 - throw error if balance < 0
        float acc1bal = aDAO.getAccountBalance(clientId, accNum1);
        acc1bal -= total;
        if(acc1bal < 0){
            throw new Exception("Insufficient funds in first account");
        }

        //add total to account 2
        float acc2bal = aDAO.getAccountBalance(clientId, accNum2);
        acc2bal += total;

        //update account 1
        aDAO.updateAccount(acc1bal, clientId, accNum1);

        //update account 2
        aDAO.updateAccount(acc2bal, clientId, accNum2);
    }
}
