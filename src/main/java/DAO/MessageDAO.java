package DAO;

import Model.Message;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Account;

import Util.ConnectionUtil;

public class MessageDAO {

    public Message createMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet pkResultSet = preparedStatement.getGeneratedKeys();
            if(pkResultSet.next()){
                int genMsgId = (int) pkResultSet.getLong(1);
                return new Message(genMsgId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
            

        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();

        List<Message> messages = new ArrayList<>();

        try{
            String sql = "SELECT * FROM Message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
            return messages;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public Message getMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                return message;
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message deleteMessageById(int id){
        //if(getMessageById(id) == null){ return null; }

        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "DELETE FROM Message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            int eu = preparedStatement.executeUpdate();
            
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessagesById(int id){
        Connection connection = ConnectionUtil.getConnection();

        List<Message> messages = new ArrayList<>();

        try{
            String sql = "SELECT * FROM Message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
            return messages;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public Message updateMessageText(int id, Message message) {
        Connection connection = ConnectionUtil.getConnection();
    
        try {
            // Update
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
    
            preparedStatement.setString(1, message.getMessage_text());
            preparedStatement.setInt(2, id);
    
            int rAffected = preparedStatement.executeUpdate();
    
            // Retrieve
            if (rAffected > 0) {
                String sql2 = "SELECT * FROM Message WHERE message_id = ?";
                PreparedStatement ps = connection.prepareStatement(sql2);
                ps.setInt(1, id);  // Use `ps` and `setInt` here
    
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    Message retMsg = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                    );
                    return retMsg;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
}
