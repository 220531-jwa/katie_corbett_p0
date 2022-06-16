package dev.corbett.controller;

import dev.corbett.model.Client;
import dev.corbett.service.ClientService;
import dev.corbett.repository.ClientDAO;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.List;

public class ClientController {
    private static ClientService cs;

    public ClientController(ClientService cs){
        this.cs = cs;
    }

    public static void getAllClients(Context ctx){
        ctx.status(200);
        List<Client> clients = cs.getAllClients();
        ctx.json(clients);
    }

    public static void createClient(Context ctx){
        ctx.status(201);
        Client clientFromRequestBody = ctx.bodyAsClass(Client.class);
        Client c = cs.createClient(clientFromRequestBody);
        ctx.json(c);
    }

    public static void getClientById(Context ctx){
        int clientId = Integer.parseInt(ctx.pathParam("clientId"));
        Client c = null;
        try{
            c = cs.getClientByID(clientId);
        }catch(Exception e){
            e.printStackTrace();
        }
        if(c != null){
            ctx.status(200);
            ctx.json(c);
        } else {
            ctx.status(404);
        }
    }

    public static void deleteClient(Context ctx){
        int clientId = Integer.parseInt(ctx.pathParam("clientId"));
        Client c = null;
        try{
            c = cs.getClientByID(clientId);
        } catch(Exception e){
            e.printStackTrace();
        }
        if(c != null){
            cs.deleteClient(clientId);
            ctx.status(205);
        }
        else{
            ctx.status(404);
        }
    }

    public static void updateClient(Context ctx){
        Client cChanged = ctx.bodyAsClass(Client.class);
        int clientId = Integer.parseInt(ctx.pathParam("clientId"));
        Client c = null;
        try{
            c = cs.getClientByID(clientId);
        } catch(Exception e){
            e.printStackTrace();
        }
        if (c != null) {
            cs.updateClient(cChanged);
            ctx.status(205);
        } else{
            ctx.status(404);
        }
    }
}
