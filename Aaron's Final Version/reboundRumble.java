/*----------------------------------------------------------------------------
/* Copyright (c) FIRST 2008. All Rights Reserved.                             
/* Open Source Software - may be modified and shared by FRC teams. The code   
/* must be accompanied by the FIRST BSD license file in the root directory of 
/* the project.                                                               
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.camera.AxisCamera;
//import edu.wpi.first.wpilibj.smartdashboard.*; 

public class reboundRumble extends IterativeRobot {
    Controls cs;
    PWMdrive dr;
    Elevator ev;
//    BridgeArm ba;
    Text tx;
    Shooter st;
    static IMU imu;
    boolean shiftUp, shiftDown;
    AxisCamera cam;
    Joystick left = new Joystick(1);
    Joystick right = new Joystick(2);
    Joystick pad = new Joystick(3);
    
    String gyroDriveStatus; double gyroValue;
    
    public void robotInit() {
        cam = AxisCamera.getInstance("192.168.0.90");
        dr = new PWMdrive();
        //cs = new Controls();
        ev = new Elevator();
        //tx = new Text();
        //imu = new IMU();
//        ba = new BridgeArm();
        st = new Shooter();
        Watchdog.getInstance().setEnabled(false);
        tx.dsWrite("The dog is dead. Cheer!", 1);
        }
    
    public void autonomousInit() {//camera stuffs here.
            Timer.delay(1); //accounts for any weird CAN things
            st.setVoltage(10.2); //changes based on ball squishy-ness
            Timer.delay(2); //waits for stuff to spin up
            ev.goUp(); //shoot the first ball
            Timer.delay(1.5);
            ev.stop();
            Timer.delay(2); //wait for shooter to spin up again
            ev.goUp(); //shoot again
            Timer.delay(2); //1/2 second longer to account for weird ball placement
            ev.stop();
    }
    
    public void autonomousPeriodic() {
    }
    
    public void teleopInit() {
        //tx.clearLCD();
        tx.dsWrite("Operator Initialized", 1);
    }
    
    int shift_cur = 1;
    boolean saw_shift_up_disabled = true;
    boolean saw_shift_down_disabled = true;
    int cycle = 0;
    
    public void teleopPeriodic() {
        double leftY = -pad.getRawAxis(2); /* Left */
        double rightY = -pad.getRawAxis(4); /* Right */
        double msRight = -right.getY();
        boolean shiftUp = pad.getRawButton(6); /* R1 */
        boolean shiftDown = pad.getRawButton(8); /* R2 */
        
        if(shiftUp && saw_shift_up_disabled) { saw_shift_up_disabled = false; shift_cur--; }
        if(!shiftUp) { saw_shift_up_disabled = true; }
        if(shiftDown &&  saw_shift_down_disabled) { saw_shift_down_disabled = false; shift_cur++; }
        if(!shiftDown) { saw_shift_down_disabled = true; }
        
        shift_cur = (shift_cur < 1) ? 1 : shift_cur;
        shift_cur = (shift_cur > 7) ? 7 : shift_cur;
        
        double calculatedMultiplier = (1 - (shift_cur) / 10.0);
//        Text.dsWrite("Shift Gear: " + (8 - shift_cur) + "\r\n", 1);
        if(cycle % 10 == 0)
            Text.updateLCD();
        
        cycle++;
        // System.out.println("Shift Gear: " + shift_cur + " Maximum: " + calculatedMultiplier);
        dr.setSpeed(PWMdrive.LEFT_SIDE, leftY * calculatedMultiplier);
        dr.setSpeed(PWMdrive.RIGHT_SIDE, rightY * calculatedMultiplier);
                
        if(right.getTrigger()) {
            System.out.println(msRight);
        }
        if(right.getTrigger() && Math.abs(msRight) >= 0.1) {
            System.out.println("Manual Control.");
            st.setVoltage(msRight * 12);
        }
        
        if(right.getRawButton(10))
            st.setVoltage(7.4);
        
        if(right.getRawButton(11))
            st.setVoltage(7.4);
        
        if(right.getTrigger() && Math.abs(msRight) < 0.1) {
            System.out.println("Stop.");
            st.stop();
        }
        
        if(right.getRawButton(3))
            ev.goUp();
        
        if(right.getRawButton(2))
            ev.goDown();
        
        if(!right.getRawButton(2) && !right.getRawButton(3))
            ev.stop();
        
        /*cs.updateButtons(); //don't touch.
        if (cs.buttons[2][11]) imu.reset(); //only resets gyro. for now...
        if (cs.buttons[2][cs.GAMEPAD_R1]) shiftUp = true; //start of shifting code
        else if(cs.buttons[2][cs.GAMEPAD_R2]) shiftDown = true;
        else if(shiftUp && !cs.buttons[2][cs.GAMEPAD_R1]) { //only allows shift if button has been pushed, then released to allow one shift per click.
            dr.shift(true);
            shiftUp = false;
        }
        else if(shiftDown && !cs.buttons[2][cs.GAMEPAD_R2]) {
            dr.shift(false);
            shiftDown = false;
        }
        dr.setSpeed(PWMdrive.LEFT_SIDE, cs.getRawAxis(cs.GAMEPAD, cs.GAMEPAD_LEFT_CONTROL), true);
        dr.setSpeed(PWMdrive.RIGHT_SIDE, -cs.getRawAxis(cs.GAMEPAD, cs.GAMEPAD_RIGHT_CONTROL), true);

//        ba.bridgeDown(cs.buttons[2][4], cs.buttons[2][6]);
        
//        if (cs.buttons[1][0]) st.setSpeed(cs.getY(2));
        if (cs.buttons[1][2]) {
            ev.goUp();
        }
        else if (cs.buttons[1][1]) {
            ev.goDown();
        }
        else {
            ev.stop();
        }
        //st.setSpeed(1.00);
        tx.dsWrite("Gyro" + imu.getVerticalAngle(), 3);
        tx.dsWrite("Left:" + dr.getSpeed(dr.LEFT_SIDE) + " Right:" + dr.getSpeed(dr.RIGHT_SIDE), 1);
        tx.dsWrite("SHIFT:" + dr.getShift(), 2);
        tx.updateLCD();
        //sendInfoToDashboard();
        */
    }
    
    public void disabledInit() {
        //tx.clearLCD();
        tx.dsWrite("I want a banana.", 1);
    }
    public void sendInfoToDashboard() { //don't add stuff here unless you REALLY need to.
        IMU.sendToDashboard();
        ev.sendToDashboard();
        dr.sendToDashboard();
    }
}
