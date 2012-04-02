package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class Text {

    
    private static DriverStationLCD _dash;
    private static long _offset = 0;
    private static int _line_num = 0;
    
    public Text () {
    }
    
    public static void consoleWrite(String s) {
        System.out.println((System.currentTimeMillis() - _offset) + ": " + s);
    }
    
    public static void scrollWrite(String s) { //dont use this, scrolling txt sucks.
        dsWrite(s, _line_num);
        _line_num++;
    }
    
    public static void dsWrite(String s, int line_num) {
        if(_dash == null)
            _dash = DriverStationLCD.getInstance();
        
        if (line_num >= Constants.DS_LINE_ORDER.length || line_num < 0) 
            line_num = 0;
        
        if(_offset == 0)
            _offset = System.currentTimeMillis();
        
        System.out.println((System.currentTimeMillis() - _offset) + ": " + s);
        _dash.println(Constants.DS_LINE_ORDER[line_num], 1, "\r");
        _dash.println(Constants.DS_LINE_ORDER[line_num], 1, s);
        //_dash.updateLCD();
        
        //_line_num = line_num;
    }
    
    public static void updateLCD() {
        _dash.updateLCD();
    }
    
    public static void clearLCD() {
        try {
        for(int i=0; i < Constants.DS_LINE_ORDER.length; i++) {
            _dash.println(Constants.DS_LINE_ORDER[i], 1, "\r");
        } }
        catch (Exception e) {
            
        }
    }
    
}

