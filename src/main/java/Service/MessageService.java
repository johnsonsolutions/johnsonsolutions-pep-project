package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public Message addMessage(Message message){
        return messageDAO.createMessage(message);
    }
    public Message updateMessage(int id, Message message){
        Message hold = messageDAO.getMessageById(id);

        if(hold != null){
            this.messageDAO.updateMessageText(id, message);
            return this.messageDAO.getMessageById(id);
        }
        return null;
    }
    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }
    public Message getMessageById(int id){
        return messageDAO.getMessageById(id);
    }
    public Message deleteMessageById(int id){
        Message hold = messageDAO.getMessageById(id);
        if (hold!=null) {
            messageDAO.deleteMessageById(id);
            return hold;
        }
        
        return null;
    }
    public List<Message> getMessagesByUser(int id){
        return messageDAO.getAllMessagesById(id);
    }
}
