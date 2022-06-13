package dev.corbett.utils;

//defines methods needed to create connection to DB

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {
    private static ConnectionUtil cu;
    private static Properties dbProps;

    private ConnectionUtil(){ //private constructor
        //initialize properties object to hold db credentials
        dbProps = new Properties();
        //stream credentials from connection.properties to dbprops
        InputStream props = ConnectionUtil.class.getClassLoader().getResourceAsStream("connection.properties");
        try{
            dbProps.load(props);
        } catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    public static synchronized ConnectionUtil getConnectionUtil(){ //synchronized = only 1 thread can access method at a time.
        //check if instance already exists
        if(cu == null){
            cu = new ConnectionUtil();
        }
        return cu;
    }

    //est connection to db
    public Connection getConnection(){
        Connection connect = null;
        //get credentials from ConnectionUtil's properties object (created in constructor)
        String url = dbProps.getProperty("url");
        String username = dbProps.getProperty("username");
        String password = dbProps.getProperty("password");
        //use credentials + driver manager to connect to db
        try{
            connect = DriverManager.getConnection(url, username, password);
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
        return connect;
    }
}
