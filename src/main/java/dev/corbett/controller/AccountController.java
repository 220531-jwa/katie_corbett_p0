package dev.corbett.controller;

import dev.corbett.model.Account;
import dev.corbett.model.Client;
import dev.corbett.service.AccountService;
import dev.corbett.service.ClientService;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class AccountController {
    private static AccountService as;
    private static ClientService cs;

    public AccountController(AccountService as, ClientService cs){
        this.as = as;
        this.cs = cs;
    }

    public static void createAccount(Context ctx){
        ctx.status(201);
        Account accountFromBodyRequest = ctx.bodyAsClass(Account.class);
        int clientId = Integer.parseInt(ctx.pathParam("clientId"));
        Account a = as.createAccount(accountFromBodyRequest, clientId);
        ctx.json(a);
    }

    public static void getAllAccounts(Context ctx){
        int clientId = Integer.parseInt(ctx.pathParam("clientId"));
        Client c = null;
        try{
            c = cs.getClientByID(clientId);
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

    public static void getAccountByNumber(Context ctx){
        int clientId = Integer.parseInt(ctx.pathParam("clientId"));
        int accNum = Integer.parseInt(ctx.pathParam("accNum"));
        Client c = null;
        try{
            c = cs.getClientByID(clientId);
        } catch(Exception e){
            e.printStackTrace();
        }
        if(c != null){
            ctx.status(404);
        } else{
            Account a = null;
            try{
                as.getAccountByNumber(accNum, clientId);
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
        float ceiling = Float.parseFloat(ctx.queryParam("amountLessThan"));
        float floor = Float.parseFloat(ctx.queryParam("amountGreaterThan"));
        int clientId = Integer.parseInt(ctx.pathParam("clientId"));
        Client c = null;
        try{
            c = cs.getClientByID(clientId);
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
            cs.getClientByID(clientId);
        } catch(Exception e){
            e.printStackTrace();
        }
        if(c == null){
            ctx.status(404);
        } else{
            Account a = null;
            try{
                as.getAccountByNumber(accNum, clientId);
            } catch(Exception e){
                e.printStackTrace();
            }
            if (a != null){
                as.deleteAccount(accNum, clientId);
            } else{
                ctx.status(404);
            }
        }
    }

    public static void updateAccount(Context ctx){
        int clientId = Integer.parseInt(ctx.pathParam("clientId"));
        int accNum = Integer.parseInt(ctx.pathParam("accNum"));
        //String operation = ctx.body();
        //float total = Float.parseFloat(ctx.body());
        //need to figure out how to get total and operation from body
        Client c = null;
        try{
            c = cs.getClientByID(clientId);
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
                boolean updated = as.updateAccount();
                if(updated == false){
                    ctx.status(422);
                }
            }
            else{
                ctx.status(404);
            }
        } else{
            ctx.status(404);
        }
    }

    public static void transferBetweenAccounts(Context ctx){
        int clientId = Integer.parseInt(ctx.pathParam("clientId"));
        int accNum1 = Integer.parseInt(ctx.pathParam("accNum"));
        int accNum2 = Integer.parseInt(ctx.pathParam("accNum2"));
        //float total = Float.parseFloat(ctx.body()); //help
        //String operation = ctx.body(); //help
        Client c = null;
        try{
            c = cs.getClientByID(clientId);
        } catch(Exception e){
            e.printStackTrace();
        }
        if(c != null){
            Account a = null;
            try{
                a = as.getAccountByNumber(accNum1, clientId);
            } catch(Exception e){
                e.printStackTrace();
            }
            if(a != null){
                Account b = null;
                try{
                    b = as.getAccountByNumber(accNum2, clientId);
                } catch(Exception e){
                    e.printStackTrace();
                }
                if(b != null) {
                    boolean updated = as.transferBetweenAccounts();
                    if(updated == false){
                        ctx.status(422);
                    }
                } else{
                    ctx.status(404);
                }
            } else{
                ctx.status(404);
            }
        } else{
            ctx.status(404);
        }
    }
}
