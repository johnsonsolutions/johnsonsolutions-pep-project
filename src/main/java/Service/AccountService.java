package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account addAccount(Account account){
        if(!accountDAO.isValid(account)){ return null; }
        if(accountDAO.isPresent(account)){ return null; }
        return accountDAO.registerAccount(account);
    }
    public Account login(Account account){ 
        
        if((accountDAO.isValid(account))){
            if(!accountDAO.isPresent(account)){ return null; }
            return accountDAO.login(account);
        }
        return null;
    }
}
