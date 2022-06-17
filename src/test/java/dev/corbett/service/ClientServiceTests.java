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


    @Test
    public void should_returnAllClients(){
        List<Client> mockClients = new ArrayList<>();
        //given
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
    public void should_createClient(){
        //given
        Client mockClient = new Client("lilac5", "passpass", 7);

        //when
        when(mockCDao.createClient(mockClient)).thenReturn(mockClient);

        //then
        assertEquals(mockClient, clientService.createClient(mockClient));
    }

    @Test
    public void should_getClientById() throws Exception {
        //given
        Client mockClient = new Client("userTest", "passpass", 7);

        //when
        when(mockCDao.getClientById(7)).thenReturn(mockClient);

        //then
        assertEquals(mockClient, clientService.getClientByID(7));
    }

}
