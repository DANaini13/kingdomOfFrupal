package com.nasoftware.DataLayer;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PlayerDataService {

    static private PlayerDataService playerDataService = null;
    static public synchronized PlayerDataService getPlayerDataService() {
        if(playerDataService == null) {
            playerDataService = new PlayerDataService();
            playerDataService.init();
        }
        return playerDataService;
    }

    private HashMap<String, String> playerMap;
    private Lock lock = new ReentrantLock();

    private void init() {
        File rootFolder = new File("Data");
        if(!rootFolder.exists())
            rootFolder.mkdir();
        File accountFile = new File("Data/players.xml");
        if(!accountFile.exists()) {
            try {
                accountFile.createNewFile();
                FileWriter fileWriter = new FileWriter(accountFile);
                fileWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                fileWriter.write("<players>\n");
                fileWriter.write("</players>\n");
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        lock.lock();
        playerMap = new HashMap<String, String>();
        lock.unlock();
        try {
            lock.lock();
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(accountFile);
            Element rootElement = document.getRootElement();
            List<Element> accountList = rootElement.getChildren();
            for( int i=0; i<accountList.size(); ++i) {
                Element account = accountList.get(i);
                Attribute attribute = account.getAttribute("accountID");
                playerMap.put(attribute.getValue(), account.getChild("wealth").getText());
            }
            lock.unlock();
        }catch(JDOMException e){
            e.printStackTrace();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void setPlayerWealth(String account, String wealth) {
        lock.lock();
        playerMap.put(account, wealth);
        lock.unlock();
        updateFile();
    }

    public int getPlayerWealth(String account) {
        lock.lock();
        int result = Integer.parseInt(playerMap.get(account));
        lock.unlock();
        return result;
    }


    private void updateFile() {
        lock.lock();
        Element rootElement = new Element("players");
        Document document = new Document(rootElement);
        Iterator it = playerMap.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Element accountElement = new Element("player");
            accountElement.setAttribute(new Attribute("accountID", (String)entry.getKey()));
            Element passwordElement = new Element("wealth");
            passwordElement.setText((String) entry.getValue());
            accountElement.addContent(passwordElement);
            document.getRootElement().addContent(accountElement);
        }
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        try {
            FileOutputStream out;
            File file = new File("Data/players.xml");
            out = new FileOutputStream(file);
            xmlOutput.output(document, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        lock.unlock();
    }

}
