package dev.corbett.repository;

import dev.corbett.model.Account;
import dev.corbett.utils.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    public ConnectionUtil cu = ConnectionUtil.getConnectionUtil();

    public Account createAccount(Account a) {
        String sql = "insert into accounts values (default, ?, ?, ?, ?) returning *;";
        try (Connection connect = cu.getConnection()) {
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setInt(1, a.getAccountNumber());
            ps.setFloat(2, a.getBalance());
            ps.setInt(3, a.getClientId());
            ps.setBoolean(4, a.getChecking());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Account(
                        rs.getInt("account_number"),
                        rs.getFloat("balance"),
                        rs.getInt("client_id"),
                        rs.getBoolean("checking")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Account> getAllAccounts(int clientId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "select * from accounts where client_id = ?;";

        try (Connection connect = cu.getConnection()) {
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setInt(1, clientId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int accNum = rs.getInt("account_number");
                float bal = rs.getFloat("balance");
                boolean type = rs.getBoolean("checking");
                Account a = new Account(accNum, bal, id, type);
                accounts.add(a);
            }
            return accounts;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Account getAccountByNumber(int clientId, int accNum){
        String sql = "select * from accounts where account_number = ? and client_id = ?;";

        try(Connection connect = cu.getConnection()){
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setInt(1, accNum);
            ps.setInt(2, clientId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int accNumber = rs.getInt("account_number");
                float bal = rs.getFloat("balance");
                int clientIdentity = rs.getInt("client_id");
                boolean type = rs.getBoolean("checking");
                Account a = new Account(accNumber, bal, clientIdentity, type);
                return a;
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Account> getAccountsByBalance(float ceiling, float floor, int clientId){
        List<Account> accounts = new ArrayList<>();
        String sql = "select * from accounts where client_id = ? and balance < ? and balance > ?;";

        try(Connection connect = cu.getConnection()){
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setInt(1, clientId);
            ps.setFloat(2, ceiling);
            ps.setFloat(3, floor);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int accountNumber = rs.getInt("account_number");
                float bal = rs.getFloat("balance");
                int clientNum = rs.getInt("client_id");
                boolean type = rs.getBoolean("checking");
                Account a = new Account(accountNumber, bal, clientNum, type);
                accounts.add(a);
            }
            return accounts;
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public Account deleteAccount(int clientId, int accNum){
        String sql = "delete from accounts where client_id = ? and account_number = ? returning *;";

        try(Connection connect = cu.getConnection()){
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setInt(1, clientId);
            ps.setInt(2, accNum);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account deleted = new Account(
                        rs.getInt("account_number"),
                        rs.getFloat("balance"),
                        rs.getInt("client_id"),
                        rs.getBoolean("checking")
                );
                return deleted;
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Account updateAccount(float balance, int clientId, int accNum, boolean type){
        String sql = "update accounts set checking = ?, balance = ? where client_id = ? and account_number = ?;";

        try(Connection connect = cu.getConnection()){
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setBoolean(1, type);
            ps.setFloat(2, balance);
            ps.setInt(3, clientId);
            ps.setInt(4, accNum);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account updated = new Account(
                        rs.getInt("account_number"),
                        rs.getFloat("balance"),
                        rs.getInt("client_id"),
                        rs.getBoolean("checking")
                );
                return updated;
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public float getAccountBalance(int clientId, int accNum){
        String sql = "select balance from accounts where client_id = ? and account_number = ?;";

        try(Connection connect = cu.getConnection()){
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setInt(1, clientId);
            ps.setInt(2, accNum);

            ResultSet rs = ps.executeQuery();

            float balance = rs.getFloat("balance");

            return balance;
        } catch(SQLException e){
            e.printStackTrace();
            return 0;
        }
    }

    public String getAccountType(int clientId, int accNum){
        String sql = "select checking from accounts where client_id = ? and account_number = ?;";

        try(Connection connect = cu.getConnection()){
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setInt(1, clientId);
            ps.setInt(2, accNum);

            ResultSet rs = ps.executeQuery();

            boolean checking = rs.getBoolean("checking");
            String type = String.valueOf(checking);
            return type;
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public List<Account> transferBetweenAccounts(float total, int accNum1, int accNum2, int clientId1, int clientId2, float bal1, float bal2, boolean type1, boolean type2){
        Account account1 = new Account(accNum1, bal1, clientId1, type1);
        Account account2 = new Account(accNum2, bal2, clientId2, type2);
        List<Account> accountsChanged = new ArrayList<>();
        String sql = "update accounts set balance = (case \n" +
                "\twhen client_id = ? and account_number = ? then ?\n" +
                "\twhen client_id = ? and account_number = ? then ?\n" +
                "end)\n" +
                "where client_id in (?, ?) and account_number in (?, ?) returning *;";
        try(Connection connect = cu.getConnection()){
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setInt(1, clientId1);
            ps.setInt(2, accNum1);
            ps.setFloat(3, bal1);
            ps.setInt(4, clientId2);
            ps.setInt(5, accNum2);
            ps.setFloat(6, bal2);
            ps.setInt(7, clientId1);
            ps.setInt(8, clientId2);
            ps.setInt(9, accNum1);
            ps.setInt(10, accNum2);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                float balance1 = (account1.getBalance() - total);
                if(balance1 < 0){
                    return null;
                } else {
                    account1.setBalance(account1.getBalance() - total);
                    account2.setBalance(account2.getBalance() + total);
                    accountsChanged.add(account1);
                    accountsChanged.add(account2);
                    return accountsChanged;
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}

