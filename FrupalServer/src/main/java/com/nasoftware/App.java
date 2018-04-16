package com.nasoftware;

import com.nasoftware.DataLayer.MapService;
import com.nasoftware.NetworkLayer.ServerManager;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        GameItem gameItem[][] = MapService.getMap();
        ServerManager serverManager = ServerManager.getServerManager(2202);
    }
}
