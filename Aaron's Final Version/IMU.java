package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.smartdashboard.*;

public class IMU {
    public static SendableGyro yGyro;
    
    public static final int YGYRO = 0;
    
    public IMU () {
        yGyro = new SendableGyro(Constants.SLOT_ANALOG, Constants.SLOT_ANALOG_GYRO_CHANNEL);
        sendToDashboard();
    }
    
    public static double getVerticalAngle () {
        return yGyro.getAngle();
    }
    
    public static void reset() {
        yGyro.reset();
    }
    
    public static void sendToDashboard() {
        SmartDashboard.putData("Gyro Angle",yGyro);        
    }
    
    public static void cleanUp () {
        yGyro.free();
    }
    
    public static SendableGyro get_rel_gyro(int device) {
        switch(device) {
            case YGYRO: return yGyro;
        }
        return null;
    }
    
}
