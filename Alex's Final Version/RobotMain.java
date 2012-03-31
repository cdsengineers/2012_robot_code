import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Watchdog;

public class RobotMain extends IterativeRobot {

    LogitechController controller;
    RobotDrive drive;
    BallController collector;
    CameraController camera;
    
    double timeToShift;
    boolean isCollecting;
    
    public void robotInit() {
        controller = new LogitechController();
        drive = new RobotDrive();
        collector = new BallController();
        camera = new CameraController();
        
        timeToShift = 0;
        isCollecting = false;
        
        Watchdog.getInstance().setEnabled(false);
    }

    public void autonomousPeriodic() {
        
        camera.refreshTargets();
        Timer.delay(2);
        
    }

    public void teleopPeriodic() {
        Watchdog.getInstance().kill();
        
        //Shift Controller
        if(controller.getShiftUp())
        {
            drive.shiftUp();
            timeToShift = Timer.getFPGATimestamp() + .2;
        }
        
        if(controller.getShiftDown())
        {
            drive.shiftDown();
            timeToShift = Timer.getFPGATimestamp() + .2;
        }
        
        if(timeToShift >= Timer.getFPGATimestamp())
            drive.setCanShift(false);
        else
            drive.setCanShift(true);
        
        
        //Direct driving is always enabled
        drive.setLeftSide(controller.getLeftThrottle());
        drive.setRightSide(controller.getRightThrottle());
        
        
        //Ball Collector Code
        if(controller.getElevatorUp()) 
        {
            collector.startIntake();
            isCollecting = true;
        }
        else if(controller.getElevatorDown())
        {
            collector.reverseIntake();
            isCollecting = true;
        }
        else if(isCollecting)
        {
            collector.stopIntake();
        }
        
        //Ball Shooter Code
        if(controller.getOpperatorTrigger() && Math.abs(controller.getOpperatorYThrottle()) > .1)
            collector.setLauncherVoltage(controller.getOpperatorYThrottle() * 12);
        else
            collector.setLauncherVoltage(0);
    }
}
