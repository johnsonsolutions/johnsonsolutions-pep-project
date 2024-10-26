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
        return accountDAO.registerAccount(account);
    }
    public Account login(Account account){
        if((!accountDAO.isDupe(account)) && (accountDAO.isValid(account))){
            return accountDAO.login(account);
        }
        return null;
    }
}
