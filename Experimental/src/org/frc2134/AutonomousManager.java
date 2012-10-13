//This file sucks, change it later.

package org.frc2134;

import java.io.*;
import javax.microedition.io.*;

public class AutonomousManager {
    InputConnection autonConnection = null;
    InputStream stream;
    int mode = Connector.READ;
    
    public AutonomousManager (int routine) {
        String fileUrl = "file:///autorountine"; //this and next 2 lines writes the file path
        fileUrl += Integer.toString(routine);
        fileUrl += ".txt";
        try {
            autonConnection = (InputConnection) Connector.open(fileUrl, mode);
            stream = autonConnection.openInputStream();
        } 
        catch (IOException ex) {}
    }
    
    public void executeAutonomousMode () { //
        String method = "";
        try {
            while(true) {
                if (stream.available() != -1) {
                    char ch = (char) stream.read();
                    if(ch == '[') {}
                    else if(ch == ']'){
                        if (method.equals("exampleMethod")) exampleMethod();
                        //add other  methods below here.
                    }
                    else if(ch == ';') //signifies end of file 
                    {
                        stream.close(); //saves memory
                    }
                    else {
                        method += ch;
                    }
                }
            }
        }
        catch(IOException ex) {}
    }
    
    //put auton methods below here.
    
    private void exampleMethod() {
        String[] param = new String [4]; //these lines keep reading the file, and store comma separated values to an array of strings.
        for(int i = 0; i<param.length; i++) {
            while(true) {
                try {    
                    char x = (char) stream.read();
                    if (x != ',') {
                        param[i] += x;
                    }
                    else {
                        break;
                    }
                }
                catch (IOException ex) {}
            }
        }
        //method stuff goes down here. Use the info in "param" to pass parameters.
    }
    
    public void cleanUp() {
        try {
            stream.close();
            autonConnection.close();
        } catch (IOException ex) {}
    }
}
