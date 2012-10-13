package edu.wpi.first.wpilibj;

//import com.sun.squawk.*;
import edu.wpi.first.wpilibj.communication.FRCControl;
import edu.wpi.first.wpilibj.Utility;

public class RobotManager extends RobotBase{
	int period = 30; //in milliseconds.
	int time;
    private boolean m_disabledInitialized, m_autonomousInitialized, m_teleopInitialized;
    ShortPeriod sp;
    LongPeriod lp;
    
    public RobotManager() {
        // set status for initialization of disabled, autonomous, and teleop code.
        m_disabledInitialized = false;
        m_autonomousInitialized = false;
        m_teleopInitialized = false;
        sp = new ShortPeriod();
        lp = new LongPeriod();
    }
	
	public void startCompetition() {
		robotInit();
		Thread ts = null;
		Thread tl = null;
		time = getTime();
		while(true) {
			if ((time + 20) >= getTime()) {			
				if (ts == null || !ts.isAlive()) { //TODO: Pick one condition
					ts = new Thread(sp);
					ts.start();
				}
				else if (ts != null || ts.isAlive()) { //TODO: Pick one condition
					ts.yield(); //TODO: Figure out if this will interupt a thread. Add DEBUG output
					ts = null;
				}
			}
			else if ((time + 40) >= getTime()) {
				time = getTime();
				if (ts == null || !ts.isAlive()) { //TODO: Pick one condition
					tl = new Thread(lp);
					tl.start();
				}
				else if (ts != null || ts.isAlive()) { //TODO: Pick one condition
					tl.yield();
					tl = null;
				}
			}			
		}
	
	}	
	
	class ShortPeriod extends Thread {
		
		public void run (){
            // Call the appropriate function depending upon the current robot mode
            if (isOperatorControl()) {
                if (!m_teleopInitialized) { // call Teleop_Init() if this is the first time we've entered teleop_mode
                    teleopInit();
                    m_teleopInitialized = true;
                    m_autonomousInitialized = false; // reset the initialization flags for the other modes
                    m_disabledInitialized = false;
                }
                if (nextPeriodReady()) {
                    getWatchdog().feed();
                    FRCControl.observeUserProgramTeleop();
                    teleopPeriodic();
                }
            }
            else if (isDisabled()) {             
                if (!m_disabledInitialized) { // call DisabledInit() if we are now just entering disabled mode from either a different mode or from power-on
                    disabledInit();
                    m_disabledInitialized = true;
                    m_autonomousInitialized = false; 
                    m_teleopInitialized = false;
                }
                if (nextPeriodReady()) {
                    FRCControl.observeUserProgramDisabled();
                }
            } 
            else if (isAutonomous()) {
                if (!m_autonomousInitialized) { // call Autonomous_Init() if this is the first time we've entered autonomous_mode
                    autonomousInit();
                    m_autonomousInitialized = true;
                    m_teleopInitialized = false;
                    m_disabledInitialized = false;
                }
                if (nextPeriodReady()) {
                    getWatchdog().feed();
                    FRCControl.observeUserProgramAutonomous();
                    autonomousPeriodic();
                }
            }
		}
	}
	class LongPeriod extends Thread {
		public void run (){
            // Call the appropriate function depending upon the current robot mode
            if (isOperatorControl()) {
                if (!m_teleopInitialized) { // call Teleop_Init() if this is the first time we've entered teleop_mode
                    teleopInit();
                    m_teleopInitialized = true;
                    m_autonomousInitialized = false; // reset the initialization flags for the other modes
                    m_disabledInitialized = false;
                }
                if (nextPeriodReady()) {
                    getWatchdog().feed();
                    FRCControl.observeUserProgramTeleop();
                    teleopPeriodicLong();
                }
            }
            else if (isDisabled()) {             
                if (!m_disabledInitialized) { // call DisabledInit() if we are now just entering disabled mode from either a different mode or from power-on
                    disabledInit();
                    m_disabledInitialized = true;
                    m_autonomousInitialized = false; 
                    m_teleopInitialized = false;
                }
                if (nextPeriodReady()) {
                    FRCControl.observeUserProgramDisabled();
                    disabledPeriodicLong();
                }
            } 
            else if (isAutonomous()) {
                if (!m_autonomousInitialized) {  // call Autonomous_Init() if this is the first time we've entered autonomous_mode
                    autonomousInit();
                    m_autonomousInitialized = true;
                    m_teleopInitialized = false;
                    m_disabledInitialized = false;
                }
                if (nextPeriodReady()) {
                    getWatchdog().feed();
                    FRCControl.observeUserProgramAutonomous();
                    autonomousPeriodicLong();
                }
            }
		}
	}
		
    private boolean nextPeriodReady() {
        return m_ds.isNewControlData();
    }
    
    private int getTime() {
    	return (int) Utility.getFPGATime() * 1000;
    }
	
    /* ----------- Overridable initialization code -----------------*/
	
    public void robotInit() { //Robot-wide initialization code should go here. It will be called exactly 1 time.
        
    }

    public void disabledInit() { //Initialization code for disabled mode should go here. will be called each time the robot enters disabled mode.
        
    }

    public void autonomousInit() { //Initialization code for autonomous mode should go here. will be called each time the robot enters autonomous mode.

    }

    public void teleopInit() { //Initialization code for teleop mode should go here. will be called each time the robot enters teleop mode.

    }

   /* ----------- Overridable short periodic code -----------------*/

    public void autonomousPeriodic() { //Periodic code for autonomous mode should go here. will be called periodically at a regular rate while the robot is in autonomous mode.
        Timer.delay(0.001);
    }

    public void teleopPeriodic() { //Periodic code for teleop mode should go here. will be called periodically at a regular rate while the robot is in teleop mode.
        Timer.delay(0.001);
    }
    
    /* ----------- Overridable long periodic code -----------------*/

    public void disabledPeriodicLong() { //Periodic code for disabled mode should go here. will be called periodically at a regular rate while the robot is in disabled mode.
        Timer.delay(0.001);
    }

    public void autonomousPeriodicLong() { //Periodic code for autonomous mode should go here. will be called periodically at a regular rate while the robot is in autonomous mode.
        Timer.delay(0.001);
    }

    public void teleopPeriodicLong() { //Periodic code for teleop mode should go here. will be called periodically at a regular rate while the robot is in teleop mode.
        Timer.delay(0.001);
    }
}
