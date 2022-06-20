package dev.corbett.service;

import dev.corbett.model.Account;
import dev.corbett.repository.AccountDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTests {
    @InjectMocks
    private static AccountService accountService;

    @Mock
    private static AccountDAO mockADao;

    @Mock
    private static AccountService asMock;

    @Test
    public void should_createAccount(){
        //given
        Account mockAccount = new Account(3, 500, 1, false);
        //when
        when(mockADao.createAccount(mockAccount)).thenReturn(mockAccount);
        //then
        assertEquals(mockAccount, accountService.createAccount(mockAccount));
    }

    @Test
    public void should_getAllAccounts(){
        //given
        List<Account> mockAccounts = new ArrayList<>();
        mockAccounts.add(new Account(1, 600, 1, true));
        mockAccounts.add(new Account(2, 7000, 1, false));
        mockAccounts.add(new Account(8, 123, 1, false));
        //when
        when(mockADao.getAllAccounts(1)).thenReturn(mockAccounts);
        //then
        assertEquals(mockAccounts, accountService.getAllAccounts(1));
    }

    @Test
    public void should_getAccountByNumber(){
        //given
        List<Account> mockAccounts = new ArrayList<>();
        mockAccounts.add(new Account(1, 600, 1, true));
        mockAccounts.add(new Account(2, 7000, 1, false));
        mockAccounts.add(new Account(8, 123, 1, false));
        //when
        when(mockADao.getAccountByNumber(1, 1)).thenReturn(mockAccounts.get(0));
        //then
        assertEquals(mockAccounts.get(0), accountService.getAccountByNumber(1,1));
    }

    @Test
    public void should_getAccountsByBalance(){
        //given
        List<Account> mockAccounts = new ArrayList<>();
        mockAccounts.add(new Account(1, 600, 1, true));
        mockAccounts.add(new Account(2, 7000, 1, false));
        mockAccounts.add(new Account(8, 123, 1, false));
        //when
        when(mockADao.getAccountsByBalance(7000, 20, 1)).thenReturn(mockAccounts);
        //then
        assertEquals(mockAccounts, accountService.getAccountsByBalance(7000, 20, 1));
    }

    @Test
    public void should_deleteAccount(){
        //given
        Account mockAccount = new Account(2, 750, 1, true);
        //when
        when(mockADao.deleteAccount(1, 2)).thenReturn(mockAccount);
        //then
        assertEquals(mockAccount, accountService.deleteAccount(1, 2));
    }

    @Test
    public void should_updateAccount() throws Exception {
        //given
        Account mockAccount = new Account(1, 1000, 1, true);
        Account mockAccount2 = new Account(1, 1500, 1, false);
        //when
        when(mockADao.updateAccount(1500, 1, 1, false)).thenReturn(mockAccount2);
        //then
        assertEquals(mockAccount2, accountService.updateAccount(1, 1, 1500, false));
    }

    @Test
    public void should_getBalance(){
        //given
        Account mockAccount = new Account(1, 1000, 1, true);
        //when
        when(mockADao.getAccountBalance(1, 1)).thenReturn(1000F);
        //then
        assertEquals(1000F, accountService.getAccountBalance(1, 1));
    }

    @Test
    public void should_getType(){
        //given
        Account mockAccount = new Account(1, 1000, 1, true);
        //when
        when(mockADao.getAccountType(1, 1)).thenReturn("true");
        //then
        assertEquals("true", accountService.getAccountType(1, 1));
    }

    @Test
    public void should_transferBetweenAccounts1Client(){
        //given
        Account mockAccount1 = new Account(1, 1000, 1, true);
        Account mockAccount2 = new Account(2, 1500, 1, false);
        List<Account> accountsChanged = new ArrayList<>();
        Account mockAccountChanged1 = new Account(1, 0, 1, true);
        Account mockAccountChanged2 = new Account(2, 2500, 1, false);
        accountsChanged.add(mockAccountChanged1);
        accountsChanged.add(mockAccountChanged2);
        //when
        when(mockADao.transferBetweenAccounts(1000, 1, 2, 1, 1, 1000, 1500, true, false)).thenReturn(accountsChanged);
        //then
        assertEquals(accountsChanged, accountService.transferBetweenAccounts(1000, 1, 2, 1, 1, 1000, 1500, true, false));
    }

    @Test
    public void should_transferBetweenAccounts2Clients(){
        //given
        Account mockAccount1 = new Account(1, 1000, 1, true);
        Account mockAccount2 = new Account(2, 1500, 2, false);
        List<Account> accountsChanged = new ArrayList<>();
        Account mockAccountChanged1 = new Account(1, 0, 1, true);
        Account mockAccountChanged2 = new Account(2, 2500, 2, false);
        accountsChanged.add(mockAccountChanged1);
        accountsChanged.add(mockAccountChanged2);
        //when
        when(mockADao.transferBetweenAccounts(1000, 1, 2, 1, 2, 1000, 1500, true, false)).thenReturn(accountsChanged);
        //then
        assertEquals(accountsChanged, accountService.transferBetweenAccounts(1000, 1, 2, 1, 2, 1000, 1500, true, false));
    }
}
