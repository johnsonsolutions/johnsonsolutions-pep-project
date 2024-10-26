package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Account;

import Util.ConnectionUtil;

public class AccountDAO {

    public Account registerAccount(Account account){
        if(!isValid(account) || isDupe(account)){ return null; }

        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO Account (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();
            ResultSet pkResultSet = preparedStatement.getGeneratedKeys();
            if(pkResultSet.next()){
                int genAccId = (int) pkResultSet.getLong(1);
                return new Account(genAccId, account.getUsername(), account.getPassword());
            }
            
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account login(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT username, password FROM Account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeQuery();
            ResultSet pkResultSet = preparedStatement.getGeneratedKeys();
            if(pkResultSet.next()){
                //int genAccId = (int) pkResultSet.getLong(1);
                //return new Account(genAccId, account.getUsername(), account.getPassword());
                return new Account(account.getUsername(), account.getPassword());
            }
            
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean isValid(Account account){
        return (account.getUsername()!=null) && (account.getPassword()!=null);
    }

    public boolean isDupe(Account subject) {
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM Account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, subject.getUsername());
            preparedStatement.setString(2, subject.getPassword());

            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();

        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}
