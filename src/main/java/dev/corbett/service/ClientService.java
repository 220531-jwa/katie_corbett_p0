package dev.corbett.service;

import dev.corbett.model.Client;
import dev.corbett.repository.ClientDAO;

import java.util.List;

public class ClientService {
    private static ClientDAO cDAO;
    public ClientService(ClientDAO cDAO){
        this.cDAO = cDAO;
    }

    public Client createClient(Client c){
        Client createdClient = cDAO.createClient(c);
        return createdClient;
    }

    public Client getClientByID(int id) throws Exception{
        Client c = cDAO.getClientById(id);
        if(c == null){
            throw new Exception("User not found");
        }
        return c;
    }

    public List<Client> getAllClients(){
        return cDAO.getAllClients();
    }

    public void deleteClient(int clientID){
        cDAO.deleteClient(clientID);
    }

    public void updateClient(Client cChanged){
        cDAO.updateClient(cChanged);
    }
}
