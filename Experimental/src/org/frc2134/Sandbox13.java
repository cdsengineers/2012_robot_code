package org.frc2134;

import edu.wpi.first.wpilibj.Attack3;
import edu.wpi.first.wpilibj.Attack3.Jbutton;
import edu.wpi.first.wpilibj.DualAction;
import edu.wpi.first.wpilibj.DualAction.Dbutton;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Sandbox13 extends IterativeRobot {
    ConfigurationManager co = ConfigurationManager.getInstance(); //put this first!
    DualAction drivepad = new DualAction(1);
    Attack3 shootstick = new Attack3(2);
    DriveManager dr;
    AutonomousManager am;
    
    boolean shiftUp, shiftDown;

    public void robotInit() {
        co.init("file:///config.txt", 20); //put this first, so that other classes can get data.
        dr = new DriveManager(3,1,2,4);
        
    }
    public void autonomousInit() {
        am = new AutonomousManager(1); //TODO: put a button on the driverstation, or something in the config file, to select this.
        am.executeAutonomousMode();
        am.cleanUp();
    }
    public void autonomousPeriodic() {

    }
    public void teleopPeriodic() {
    	if (drivepad.getReleasedButton(Dbutton.trBumper)) dr.shift(true);
    	if (drivepad.getReleasedButton(Dbutton.tlBumper)) dr.shift(true);
    }
}
