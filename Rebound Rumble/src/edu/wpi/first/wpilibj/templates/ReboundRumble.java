package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.camera.AxisCamera;

public class ReboundRumble extends IterativeRobot {
    Controls cs;
    PWMdrive dr;
    Elevator ev;
    Text tx;
    Shooter st;
    AxisCamera cam;

    boolean shiftUp, shiftDown;
    boolean isCollecting = false;
    int loopcount = 0;
    
    public void robotInit() {
        cam = AxisCamera.getInstance("192.168.0.90");
        dr = new PWMdrive();
        cs = new Controls();
        ev = new Elevator();
        tx = new Text();
        st = new Shooter();
        try {
            Watchdog.getInstance().setEnabled(false);
            tx.dsWrite("The dog is dead. Cheer!", 1);
        }
        catch (Exception e){
            tx.dsWrite("The dog is Immortal", 1);
        }
        finally {
            tx.updateLCD();
        }
    }

    public void autonomousInit() {
        tx.updateWith("Auton Initialized", 0);
        Timer.delay(1); //accounts for any weird CAN things
        st.setVoltage(10); //changes based on ball squishy-ness
        Timer.delay(3); //waits for stuff to spin up
        ev.goUp(); //shoot the first ball
        Timer.delay(1.5);
        ev.stop();
        Timer.delay(2); //wait for shooter to spin up again
        ev.goUp(); //shoot again
        Timer.delay(2); //1/2 second longer to account for weird ball placement
        ev.stop();
        tx.updateWith("Autonomous complete", 0);
    }

    public void teleopInit() {
        tx.updateWith("Teleop Initialized", 0);
    }
    public void teleopPeriodic() {
        cs.updateButtons(); //don't touch. Refreshes array of all buttons
        
        //if (cs.buttons[2][11]) imu.reset(); //only resets gyro. for now...must get more sensors
        
        //start of shifting code
        if (cs.getButton(1, cs.GAMEPAD_R1)) shiftUp = true;
        else if(cs.getButton(1, cs.GAMEPAD_R2)) shiftDown = true;
        else if(shiftUp && !cs.getButton(1, cs.GAMEPAD_R1)) { //only allows shift if button has been pushed, then released, to allow one shift per click.
            dr.shift(true);
            shiftUp = false;
        }
        else if(shiftDown && !cs.getButton(1, cs.GAMEPAD_R2)) {
            dr.shift(false);
            shiftDown = false;
        }
        
        //sets the drivetrain speed
        dr.setSpeed(PWMdrive.LEFT_SIDE, cs.getRawAxis(1, cs.GAMEPAD_LEFT_CONTROL), true); 
        dr.setSpeed(PWMdrive.RIGHT_SIDE, -cs.getRawAxis(1, cs.GAMEPAD_RIGHT_CONTROL), true);
        
        //shooter code
        if (cs.buttons[1][0]) st.setVoltage(cs.getY(2)*13); //manual shooter
        else if (cs.buttons[1][12]) st.setVoltage(7.4); //shoot from fender for 3 pts, button 3
        else if (cs.buttons[1][11]) st.setVoltage(7.4); //shoot from fender side for 3 pts, button 2
        //else if (cs.buttons[1][7]) st.setVoltage(6.0); //shoot with a robot in front
        //else if (cs.buttons[1][8]) st.setVoltage(9.8); //shoot from key...maybe..
        
        //elevator code
        if (cs.buttons[1][2]) {
            ev.goUp();
        }
        else if (cs.buttons[1][1]) {
            ev.goDown();
        }        
        else {
            ev.stop();
        }
        
        //spits stuff out to the driverstation
        if (loopcount == 25) {
            tx.dsWrite("Left:" + dr.getSpeed(dr.LEFT_SIDE) + " Right:" + dr.getSpeed(dr.RIGHT_SIDE), 1);
            tx.dsWrite("SHIFT:" + dr.getShift(), 3);
            //tx.dsWrite(ev.inteligentOutput(st.getAmps()), 4);
            tx.dsWrite("Shooter:"+ st.getVoltage(),5);
            tx.updateLCD();
            loopcount = -1;
        }
        loopcount++;
    }
    
}
