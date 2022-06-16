package dev.corbett.repository;

import dev.corbett.model.Client;
import dev.corbett.utils.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    private static ConnectionUtil cu = ConnectionUtil.getConnectionUtil();

    public Client createClient(Client c){
        String sql = "insert into clients values (default, ?, ?) returning *";
        try (Connection connect = cu.getConnection();){
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setString(1, c.getUsername());
            ps.setString(2, c.getPassword());

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return new Client(
                        rs.getString("username"),
                        rs.getString("pass_word"),
                        rs.getInt("id")
                );
            }
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
        return null;
    }

    public List<Client> getAllClients(){
        List<Client> clients = new ArrayList<>();
        String sql = "select * from clients";

        try (Connection connect = cu.getConnection();) {
            PreparedStatement ps = connect.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("pass_word");
                Client c = new Client(username, password, id);
                clients.add(c);
            }
            return clients;
        } catch(SQLException sqle){
            sqle.printStackTrace();
            return null;
        }
    }

    public Client getClientById(int clientId){
        String sql = "select * from clients where id = ?";

        try (Connection connect = cu.getConnection()){
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setInt(1, clientId);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                Client c = new Client(
                        rs.getString("username"),
                        rs.getString("pass_word"),
                        rs.getInt("id")
                );
                return c;
            }
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
        return null;
    }

    public void updateClient(Client c){
        String sql = "update client set username = ?, pass_word = ? where id = ?";

        try(Connection connect = cu.getConnection()){
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setString(1, c.getUsername());
            ps.setString(2, c.getPassword());
            ps.setInt(3, c.getClientId());

            ps.executeQuery();
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
    }

    public void deleteClient(int clientId){
        String sql = "delete from client where id = ?";

        try(Connection connect = cu.getConnection()){
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setInt(1, clientId);
            ps.executeQuery();
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
    }
}
