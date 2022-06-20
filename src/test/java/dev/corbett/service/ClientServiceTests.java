package dev.corbett.service;

import dev.corbett.model.Client;
import dev.corbett.repository.ClientDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTests {
    @InjectMocks
    private static ClientService clientService;

    @Mock
    private static ClientDAO mockCDao;
    @Mock
    private static ClientService mockCs;

    @Test
    public void should_createClient(){
        //given
        Client mockClient = new Client("newClient", "passpass", 10);
        //when
        when(mockCDao.createClient(mockClient)).thenReturn(mockClient);
        //then
        assertEquals(mockClient, clientService.createClient(mockClient));
    }

    @Test
    public void should_getAllClients(){
        //given
        List<Client> mockClients = new ArrayList<>();
        mockClients.add(new Client("katiec", "freddie", 1));
        mockClients.add(new Client("jessicar", "tucker", 2));
        mockClients.add(new Client("brittanyl", "nutmeg", 3));
        mockClients.add(new Client("susanc", "snuzzy", 4));
        mockClients.add(new Client("edc", "jack", 5));
        //when
        when(mockCDao.getAllClients()).thenReturn(mockClients);
        //then
        assertEquals(mockClients, clientService.getAllClients());
    }

    @Test
    public void should_getClientById(){
        //given
        List<Client> mockClients = new ArrayList<>();
        mockClients.add(new Client("katiec", "freddie", 1));
        mockClients.add(new Client("jessicar", "tucker", 2));
        mockClients.add(new Client("brittanyl", "nutmeg", 3));
        mockClients.add(new Client("susanc", "snuzzy", 4));
        mockClients.add(new Client("edc", "jack", 5));
        //when
        when(mockCDao.getClientById(1)).thenReturn(mockClients.get(1));
        //then
        assertEquals(mockClients.get(1), clientService.getClientById(1));
    }

    @Test
    public void should_updateClient(){
        //given
        Client c = new Client("ersatz", "securePass", 1);
        //when
        when(mockCDao.updateClient(c)).thenReturn(c);
        //then
        assertEquals(c, clientService.updateClient(c));
    }

    @Test
    public void should_deleteClient(){
        //given
        List<Client> mockClients = new ArrayList<>();
        mockClients.add(new Client("katiec", "freddie", 1));
        mockClients.add(new Client("jessicar", "tucker", 2));
        mockClients.add(new Client("brittanyl", "nutmeg", 3));
        mockClients.add(new Client("susanc", "snuzzy", 4));
        mockClients.add(new Client("edc", "jack", 5));
        //when
        when(mockCDao.deleteClient(5)).thenReturn(mockClients.get(4));
        //then
        assertEquals(mockClients.get(4), clientService.deleteClient(5));
    }

    @Test
    public void should_loginClient(){
        //given
        Client mockClient = new Client("katiec", "freddie", 1);
        //when
        when(mockCDao.getClientByUsername("katiec")).thenReturn(mockClient);
        //then
        assertEquals(mockClient, clientService.loginClient("katiec", "freddie"));
    }
}
