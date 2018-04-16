package com.nasoftware;
import com.nasoftware.GameLayer.GameViewController;
import com.nasoftware.LogicLayer.AccountService;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        AccountService accountService = AccountService.getAccountService();
        accountService.login("shan28", (response) -> {
        });
        GameViewController gameViewController = GameViewController.getGameViewController();
    }
}
