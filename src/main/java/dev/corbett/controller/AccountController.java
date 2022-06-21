package dev.corbett.controller;

import dev.corbett.model.Account;
import dev.corbett.model.Client;
import dev.corbett.service.AccountService;
import dev.corbett.service.ClientService;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccountController {
    private static AccountService as;
    private static ClientService cs;

    public AccountController(AccountService as, ClientService cs){
        AccountController.as = as;
        AccountController.cs = cs;
    }

    public static void createAccount(Context ctx){
        Account accountFromBodyRequest = ctx.bodyAsClass(Account.class);
        Account a = as.createAccount(accountFromBodyRequest);
        if(a != null){
            ctx.status(201);
            ctx.json(a);
        } else {
            ctx.status(404);
        }
    }

    public static void getAllAccounts(Context ctx){
        int clientId = Integer.parseInt(ctx.pathParam("clientId"));
        String ceiling = ctx.queryParam("amountLessThan");
        String floor = ctx.queryParam("amountGreaterThan");
        Client c = null;
        if(ceiling != null && floor != null){
            float ceilingPass = Float.parseFloat(ceiling);
            float floorPass = Float.parseFloat(floor);
            List<Account> accounts = as.getAccountsByBalance(ceilingPass, floorPass, clientId);
            ctx.json(accounts);
        } else {
            try{
                c = cs.getClientById(clientId);
            } catch(Exception e){
                e.printStackTrace();
            }
            if(c != null){
                List<Account> accounts = as.getAllAccounts(clientId);
                ctx.json(accounts);
            } else{
                ctx.status(404);
            }
        }

    }

    public static void getAccountByNumber(Context ctx){
        int clientId = Integer.parseInt(ctx.pathParam("clientId"));
        int accNum = Integer.parseInt(ctx.pathParam("accNum"));
        Client c = null;
        try{
            c = cs.getClientById(clientId);
            ctx.json(c);
        } catch(Exception e){
            e.printStackTrace();
        }
        if(c == null){
            ctx.status(404);
        } else{
            Account a = null;
            try{
                a = as.getAccountByNumber(accNum, clientId);
            } catch(Exception e){
                e.printStackTrace();
            }
            if (a != null){
                Account account = as.getAccountByNumber(accNum, clientId);
                ctx.json(account);
            } else{
                ctx.status(404);
            }
        }
    }

    public static void getAccountsByBalance(Context ctx){
        float ceiling = Float.parseFloat(ctx.pathParam("amountLessThan"));
        float floor = Float.parseFloat(ctx.pathParam("amountGreaterThan"));
        int clientId = Integer.parseInt(ctx.pathParam("clientId"));
        Client c = null;
        try{
            c = cs.getClientById(clientId);
        } catch(Exception e){
            e.printStackTrace();
        }
        if(c != null){
            List<Account> accounts = as.getAccountsByBalance(ceiling, floor, clientId);
            ctx.json(accounts);
        } else{
            ctx.status(404);
        }
    }

    public static void deleteAccount(Context ctx){
        int clientId = Integer.parseInt(ctx.pathParam("clientId"));
        int accNum = Integer.parseInt(ctx.pathParam("accNum"));
        Client c = null;
        try{
            c = cs.getClientById(clientId);
        } catch(Exception e){
            e.printStackTrace();
        }
        if(c == null){
            ctx.status(404);
        } else{
            Account a = null;
            try{
                a = as.getAccountByNumber(accNum, clientId);
            } catch(Exception e){
                e.printStackTrace();
            }
            if (a == null){
                ctx.status(404);
            } else{
                Account deleted = as.deleteAccount(accNum, clientId);
                ctx.json(deleted);
            }
        }
    }

    public static void updateAccount(Context ctx){
        int clientId = Integer.parseInt(ctx.pathParam("clientId"));
        int accNum = Integer.parseInt(ctx.pathParam("accNum"));
        String bodyString = ctx.body();
        String operation = "";
        if(bodyString.contains("deposit")){
            operation = "deposit";
        } else if(bodyString.contains("withdrawal")) {
            operation = "withdrawal";
        }
        String trimmedString = bodyString.replaceAll("[^0-9.]", "");
        float total = Float.parseFloat(trimmedString);
        Client c = null;
        try{
            c = cs.getClientById(clientId);
        } catch(Exception e){
            e.printStackTrace();
        }
        if(c != null){
            Account a = null;
            try{
                a = as.getAccountByNumber(accNum, clientId);
            } catch(Exception e){
                e.printStackTrace();
            }
            if(a != null){
                try {
                    float totalOperation = 0;
                    float balance = a.getBalance();
                    if(operation.equals("deposit")){
                        totalOperation = balance + total;
                    } else if(operation.equals("withdrawal")){
                        totalOperation = balance - total;
                    }
                    if(totalOperation < 0){
                        ctx.status(422);
                    } else{
                        boolean type = a.getChecking();
                        Account updated = as.updateAccount(clientId, accNum, totalOperation, type);
                        ctx.json(updated);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            else{
                ctx.status(404);
            }
        } else{
            ctx.status(404);
        }
    }

    public static void updateAccountType(Context ctx){
        Account updatedAccount = ctx.bodyAsClass(Account.class);
        int clientId = updatedAccount.getClientId();
        int accNum = updatedAccount.getAccountNumber();
        boolean type = updatedAccount.getChecking();
        float balance = updatedAccount.getBalance();
        Client c = null;
        try {
            c = cs.getClientById(clientId);
        } catch(Exception e){
            e.printStackTrace();
        }
        if(c == null){
            ctx.status(404);
        } else {
            Account a = null;
            try{
                a = as.getAccountByNumber(clientId, accNum);
                if(a == null){
                    ctx.status(404);
                } else{
                    Account updated = as.updateAccount(clientId, accNum, balance, type);
                    ctx.json(updated);
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void transferBetweenAccounts(Context ctx) {
        int clientId1 = Integer.parseInt(ctx.pathParam("clientId"));
        int clientId2 = Integer.parseInt(ctx.pathParam("clientId2"));
        int accNum1 = Integer.parseInt(ctx.pathParam("accNum"));
        int accNum2 = Integer.parseInt(ctx.pathParam("accNum2"));
        String bodyString = ctx.body();
        String trimmedBody = bodyString.replaceAll("[^0-9.]", "");
        float total = Float.parseFloat(trimmedBody);
        Client c1 = null;
        try {
            c1 = cs.getClientById(clientId1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(c1 != null){
            Client c2 = null;
            try{
                c2 = cs.getClientById(clientId2);
            } catch(Exception e){
                e.printStackTrace();
            }
            if (c2 != null) {
                Account a1 = null;
                try{
                    a1 = as.getAccountByNumber(clientId1, accNum1);
                } catch(Exception e){
                    e.printStackTrace();
                }
                if(a1 != null){
                    Account a2 = null;
                    try{
                        a2 = as.getAccountByNumber(clientId2, accNum2);
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                    if(a2 != null){
                        try{
                            float bal1 = a1.getBalance();
                            float bal2 = a2.getBalance();
                            boolean type1 = a1.getChecking();
                            boolean type2 = a2.getChecking();
                            List<Account> updated =  as.transferBetweenAccounts(total, accNum1, accNum2, clientId1, clientId2, bal1, bal2, type1, type2);
                            if(updated == null){
                                ctx.status(422);
                            } else{
                                ctx.json(updated);
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    } else {
                        ctx.status(404);
                    }
                } else {
                    ctx.status(404);
                }
            } else {
                ctx.status(404);
            }
        } else {
            ctx.status(404);
        }
    }
}
