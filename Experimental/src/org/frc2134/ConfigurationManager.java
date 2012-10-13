package org.frc2134;

import edu.wpi.first.wpilibj.parsing.IUtility;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.io.*;

public class ConfigurationManager implements IUtility{ //singleton class to read properties from a file. Similar to class Properties in Java SE.
    InputConnection configurationConnection = null;
    int mode = Connector.READ;
    
    String[][] properties; //holds all the properties
    
    Timer threadTimer = new Timer();
    
    private class ConfigTask extends TimerTask {
        private ConfigurationManager cm;

        public  ConfigTask(ConfigurationManager config) {
            if (config == null) {
                throw new NullPointerException("Given ConfigurationManager was null");
            }
            cm = config;
        }

        public void run() {
            cm.update(); //we want this to run once
            while (true) {
                if (cm.checkIfUpdated() == 1) {}
            }
        }
    }
    
    private ConfigurationManager() {} //private so only one instance is created
    
    public void init(String configurationUrl, int constants) { //file location and number of properties
        try {
            configurationConnection = (InputConnection) Connector.open(configurationUrl, mode);
        } 
        catch (IOException ex) {}
        properties = new String[constants][2];
        threadTimer.schedule(new ConfigurationManager.ConfigTask(this), 0L, (long) (5000)); //starts thread
    }
    
    public void run() {
        update(); //we want this to run once
        while (true) {
            if (checkIfUpdated() == 1) {

            }
            try {
                Thread.sleep(5000); //thread will automatically update settings if need every 5 seconds.
            } catch (InterruptedException ex) {}
        }
    }
    
    private int update () { //updates all properties.
        InputStream stream;
        int status = -1;
        try {
            stream = configurationConnection.openInputStream();
            int i = 0;
            int j = 0;
            
            while(true) {
                if (stream.available() != -1) {
                    char ch = (char) stream.read();
                    if(ch == '=') { //Property and Value are separated by equla sign
                        j = 1;
                    }
                    else if(ch == '\n'){ //properties are separated by newlines
                        i++;
                        j = 0;
                    }
                    else if(ch == ';') //signifies end of file 
                    {
                        stream.close(); //saves memory
                        status =1;
                    }
                    else if(j == 0 ) {
                        properties[i][0] += (char) ch;  
                    }
                    else if(j == 1) {
                        properties[i][1] += (char) ch; 
                    }
                }
                else {
                    stream.close();
                    status = 0;
                }
            }
        } 
        catch (IOException ex) {}
        return status;
    }
    
    public String getProperty(String property) {
        String returnString = "";
        for(int i = 0; i < properties.length; i++) {
            if (property.equals(properties[i][0])) {
                returnString = properties[i][1];
                break;
            }
        }
        return returnString;
    }
    
    public int checkIfUpdated() { //checks the first value
        int value = 0;
        try {
            InputStream stream = configurationConnection.openInputStream();
            int j = 0;
            while(true) {
                if (stream.available() != -1) {
                    char ch = (char) stream.read();
                    if(ch == '=') {
                        j = 1;
                    }
                    else if(ch == '\n'){
                        stream.close();
                        break;
                    }
                    else if(j == 0 ) {
                        properties[0][0] += (char) ch;  
                    }
                    else if(j == 1) {
                        properties[0][1] += (char) ch; 
                    }
                }
                else {
                    stream.close();
                }
            }
        } catch (IOException ex) {}
        if (properties[0][1] != null) {
            if (properties[0][1].equals("Valid")) {//does nothing
                value = 0;
            }
            else if (properties[0][1].equals("False")) {//updates all values
                update();
                value = 1;
            }
        }
        else {
            value = 0;
        }
        return value;
    }
    
    public static ConfigurationManager getInstance() { 
        return ConfigurationManagerHolder.INSTANCE;
    }
    
    private static class ConfigurationManagerHolder {
        private static final ConfigurationManager INSTANCE = new ConfigurationManager();
    }
}