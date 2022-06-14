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
    private static ConnectionUtil cu = ConnectionUtil.getConnectionUtil();

    public Account createAccount(Account a, int clientID) {
        String sql = "insert into accounts value(default, ?, ?, ?) returning *;";
        try (Connection connect = cu.getConnection();) {
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setInt(1, a.getAccountNumber());
            ps.setFloat(2, a.getBalance());
            ps.setInt(3, clientID);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Account(
                        rs.getInt("account_number"),
                        rs.getFloat("balance"),
                        rs.getInt("client_id")
                );
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return null;
    }

    public List<Account> getAllAccounts(int clientID) {
        List<Account> accounts = new ArrayList<>();
        String sql = "select * from accounts where client_id = ?;";

        try (Connection connect = cu.getConnection();) {
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setInt(1, clientID);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int accNum = rs.getInt("account_number");
                float bal = rs.getFloat("balance");
                Account a = new Account(accNum, bal, id);
                accounts.add(a);
            }
            return accounts;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        }
    }

    public Account getAccountByNumber(int accNum, int clientID){
        String sql = "select * from accounts where account_number = ? and client_id = ?;";

        try(Connection connect = cu.getConnection()){
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setInt(1, accNum);
            ps.setInt(2, clientID);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int accNumber = rs.getInt("account_number");
                float bal = rs.getFloat("balance");
                int clientIdentity = rs.getInt("client_id");
                Account a = new Account(accNumber, bal, clientIdentity);
                return a;
            }
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
        return null;
    }

    public List<Account> getAccountsByBalance(float balanceUpper, float balanceLower, int clientID){
        List<Account> accounts = new ArrayList<>();

        String sql = "select * from accounts where client_id = ? and balance > ? and balance < ?;";

        try(Connection connect = cu.getConnection()){
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setInt(1, clientID);
            ps.setFloat(2, balanceLower);
            ps.setFloat(3, balanceUpper);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int accountNumber = rs.getInt("account_number");
                float bal = rs.getFloat("balance");
                int clientNum = rs.getInt("client_id");
                Account a = new Account(accountNumber, bal, clientNum);
                accounts.add(a);
            }
            return accounts;
        } catch(SQLException sqle){
            sqle.printStackTrace();
            return null;
        }
    }

    public void deleteAccount(int clientID, int accountID){
        String sql = "delete * from accounts where client_id = ? and account_number = ?;";

        try(Connection connect = cu.getConnection()){
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setInt(1, clientID);
            ps.setInt(2, accountID);
            ps.executeQuery();
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
    }

    public void updateAccount(float balance, int userID, int accountID){
        String sql = "update accounts set balance = ? where user_id = ? and account_number = ?;";

        try(Connection connect = cu.getConnection()){
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setFloat(1, balance);
            ps.setInt(2, userID);
            ps.setInt(3, accountID);
            ps.executeQuery();
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
    }



}

