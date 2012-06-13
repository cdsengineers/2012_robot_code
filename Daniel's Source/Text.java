package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.DriverStationLCD;

public class Text {
    private static DriverStationLCD dash;
    private static long offset;
    private static int currentLineNumber = 0;
    private static String blank = "                     ";
    
    public Text () {
    }
    
    public static void consoleWrite(String s) {
        System.out.println((System.currentTimeMillis() - offset) + ": " + s);
    }
    
    public static void scrollWrite(String s) { //dont use this, scrolling txt sucks.
        if (currentLineNumber > 6) currentLineNumber  = 0;
        dsWrite(s, currentLineNumber);
        currentLineNumber++;
    }
    
    public static void dsWrite(String message, int lineNumber) {
        if(dash == null)
            dash = DriverStationLCD.getInstance();
        if (lineNumber >= Constants.DS_LINE_ORDER.length || lineNumber < 0) 
            lineNumber = 0;
        offset = System.currentTimeMillis();
        System.out.println((System.currentTimeMillis() - offset) + ": " + message);
        dash.println(Constants.DS_LINE_ORDER[lineNumber], 1, blank);
        dash.println(Constants.DS_LINE_ORDER[lineNumber], 1, message);
        //_dash.updateLCD();
        
        //_line_num = line_num;
    }
    
    public static void updateWith(String message, int lineNumber) {
        dsWrite(message,lineNumber);
        updateLCD();
    }
    
    public static void updateLCD() {
        dash.updateLCD();
    }    
}