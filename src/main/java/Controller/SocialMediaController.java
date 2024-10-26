package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import Util.ConnectionUtil;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();

    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/login", this::loginHandler);
        app.post("/register", this::regHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByIdHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void loginHandler(Context context) throws JsonProcessingException {

        Account account = context.bodyAsClass(Account.class);

        Account login = accountService.login(account);

        if(login != null){ context.json(login); context.status(200); }
        else{ context.status(401); }

    }

    private void regHandler(Context context) throws JsonProcessingException {
        
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount!=null){
            context.json(mapper.writeValueAsString(addedAccount));
            context.status(200);
        }
        else{
            context.status(400);
        }
    }

    private void getAllMessagesHandler(Context context) throws JsonProcessingException{
        
        context.json(messageService.getAllMessages());
    }

    private void postMessageHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if(addedMessage!=null){
            context.json(mapper.writeValueAsString(addedMessage));
            //context.status(200);
        }
        else{
            context.status(400);
        }
    }
    
    private void getMessageByIdHandler(Context context) throws JsonProcessingException {
        int id = Integer.parseInt(context.pathParam("message_id"));

        Message hold = messageService.getMessageById(id);
        if(hold != null){
            context.json(hold);
        }
        
    }

    private void updateMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message hold = context.bodyAsClass(Message.class);
        Message updMessage = messageService.updateMessage(hold.message_id, message);
        if(updMessage != null){
            context.status(200);
            context.json(mapper.writeValueAsString(updMessage));            
        }
        else{
            context.status(400);
        }
    }

    private void deleteMessageByIdHandler(Context context) throws JsonProcessingException {

        int id = Integer.parseInt(context.pathParam("message_id"));

        Message actDelete = messageService.deleteMessageById(id);

        if(actDelete!=null){
            context.json(actDelete);
        }
        context.status(200);

    }

    private void getAllMessagesByIdHandler(Context context) throws JsonProcessingException {
        int id = Integer.parseInt(context.pathParam("account_id"));
        List<Message> hold = new ArrayList<>();
        hold = messageService.getMessagesByUser(id);
        System.out.println(hold);
            context.json(hold);
        
    }

}