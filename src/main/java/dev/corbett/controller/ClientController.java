package dev.corbett.controller;

import dev.corbett.model.Client;
import dev.corbett.service.ClientService;
import dev.corbett.repository.ClientDAO;
import io.javalin.http.Context;

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
}
