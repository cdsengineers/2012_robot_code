package org.frc2134;


import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDMotorController;
import edu.wpi.first.wpilibj.Victor;


public class DriveManager {
    Victor victor[] = new Victor[4];
    private int PWMQuadrant[]; // lays out PWM inputs in the quadrants of a cartesian plane.
    public static final int sideBoth = -2, sideLeft = -1, sideRight = 0;
    double leftValue, rightValue;
    
    Encoder rightEncoder, leftEncoder;
    
    PIDMotorController rightPID, leftPID;
    
    protected static final int DEFAULT_SHIFT = 7;
    private static double SHIFT_MULTIPLIER_INCREMENT = 0.1;
    double SHIFT_MULTIPLIER; //with the "SHIFT_MULTIPLIER_INCREMENT" = 0.1, "SHIFT" of 7 = "SHIFT_MULTIPLIER_INCREMENT" of 1, "SHIFT" 6 = 0.9 ...
    public int SHIFT; //virtual shifter is a set change in the sensitivity of the robot controls, with no increase in torque for a lower gear.
    
    public DriveManager (int fr, int fl, int rl, int rr) {
        PWMQuadrant[0] = fr;
        PWMQuadrant[1] = fl;
        PWMQuadrant[2] = rl;
        PWMQuadrant[3] = rr;
        for(int i = 0; i< victor.length; i++) {
            victor[i] = new Victor (PWMQuadrant[i]);
        }
        rightEncoder = new Encoder(1,2,1,2,true);
        leftEncoder = new Encoder(1,2,1,2,false);
        rightPID = new PIDMotorController(0.2, 0.2, 0.2, rightEncoder, get_rel_obj(sideRight));
        leftPID = new PIDMotorController(0.2, 0.2, 0.2, leftEncoder, get_rel_obj(sideLeft));
        setEnabled(true);
        shift(DEFAULT_SHIFT);
    }
    
    public void setSpeed(double rightSetPoint, double leftSetPoint ) {
        rightValue = rightSetPoint * -SHIFT_MULTIPLIER_INCREMENT;
        leftValue = leftSetPoint * -SHIFT_MULTIPLIER_INCREMENT;
        rightPID.setSetpoint(rightValue); //input logic is in the PID class.
        leftPID.setSetpoint(leftValue);
    }   
    public double getSpeed(int side) {
        if (side == sideRight) {
            return rightPID.get();
        }
        else if (side == sideLeft) {
            return leftPID.get();
        }
        else if (side == sideBoth) {
            return (rightPID.get() + leftPID.get()) / 2;
        }
        else {
            return 0.0;
        }
    }
    public void setEnabled(boolean state) {
        if (state) {
            rightPID.enable();
            leftPID.enable();
        }
        else if (!state) {
            rightPID.disable();
            leftPID.disable();
        }
    }
    
    public void shift (boolean SHIFT_UP) { //performs a shift up/down depending on a true/false value respectivly
        if(SHIFT_UP && SHIFT < 7) {
            SHIFT++;
        }
        else if(!SHIFT_UP && SHIFT > 1) {
            SHIFT--;
        }
        SHIFT_MULTIPLIER = (SHIFT+3)*SHIFT_MULTIPLIER_INCREMENT;
    }
    public void shift (int value) { //shifts to a value
        if(value < 7 || value > 1) {
            SHIFT=value;
        }
        SHIFT_MULTIPLIER = (SHIFT+3)*SHIFT_MULTIPLIER_INCREMENT;
    } 
    
    public int getShift() {
        return SHIFT;
    }
    
    private Victor[] get_rel_obj(int side) {
        switch(side) {
            case sideLeft: return new Victor[]{victor[1], victor[2]};
            case sideRight: return new Victor[]{victor[0], victor[3]};
            case sideBoth: return new Victor[]{victor[0],victor[1],victor[2],victor[3]};
        }
        return null;
    }
}
