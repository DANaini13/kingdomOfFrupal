package com.nasoftware;

import com.nasoftware.GameLayer.GameViewController;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        LoginPage loginPage = LoginPage.getLoginPage();
        GameViewController gameViewController = GameViewController.getGameViewController();
    }

}
