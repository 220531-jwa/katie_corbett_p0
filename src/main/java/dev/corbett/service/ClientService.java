package dev.corbett.service;

import dev.corbett.model.Client;
import dev.corbett.repository.ClientDAO;

import java.util.List;

public class ClientService {
    private static ClientDAO cd;
    public ClientService(ClientDAO cd){
        this.cd = cd;
    }

    public Client createClient(Client c){
        Client createdClient = cd.createClient(c);
        return createdClient;
    }

    public List<Client> getAllClients(){
        return cd.getAllClients();
    }

    public Client getClientById(int clientId){
        Client c = cd.getClientById(clientId);
        return c;
    }

    public Client updateClient(Client cChanged){
        Client cChangedTest = cd.updateClient(cChanged);
        return cChangedTest;
    }

    public Client deleteClient(int clientId){
        Client deleted = cd.deleteClient(clientId);
        return deleted;
    }

    public Client loginClient(String username, String password){
        Client c = cd.getClientByUsername(username);
        if(c.getPassword().equals(password)){
            return c;
        } else {
            return null;
        }
    }
}
