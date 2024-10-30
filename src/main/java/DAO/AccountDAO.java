package DAO;

//import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.*;



import Model.Account;

import Util.ConnectionUtil;

public class AccountDAO {

    public Account registerAccount(Account account) {
        if (!isValid(account) || isPresent(account)) { return null; }
    
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Account (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                int genAccId = (int) rs.getLong(1);
                return new Account(genAccId, account.getUsername(), account.getPassword());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    

    public Account login(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT account_id, username, password FROM Account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()){
                Account nAcc = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"), 
                    rs.getString("password"));
                return nAcc;
            }
            
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean isValid(Account account){
        boolean[] checks = new boolean[]{
            (account.getUsername()!=null),
            (account.getPassword()!=null),
            (account.getPassword().length() > 4),
            (account.getUsername() != "")
        };
        for(boolean check: checks){
            if (!check) { return false; }
        }
        return true;
    }

    public boolean isPresent(Account subject) {
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
