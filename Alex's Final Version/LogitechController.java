import edu.wpi.first.wpilibj.Joystick;

public class LogitechController
{
    
    private Joystick driver, opperator;
    
    public LogitechController()
    {
        driver = new Joystick(3);
        opperator = new Joystick(2);
    }
    
    public double getRightThrottle()
    {
       return driver.getRawAxis(2);
    }
    
    public double getLeftThrottle()
    {
       return driver.getRawAxis(4);
    }
    
    public double getOpperatorYThrottle()
    {
        return -opperator.getRawAxis(2);
    }
    
    public boolean getOpperatorTrigger()
    {
        return opperator.getTrigger();
    }
    
    public boolean getShiftUp()
    {
        return driver.getRawButton(6);
    }
    
    public boolean getShiftDown()
    {
        return driver.getRawButton(8);
    }
    
    public boolean getBridgeUp()
    {
        return driver.getRawButton(5);
    }
    
    public boolean getBridgeDown()
    {
        return driver.getRawButton(7);
    }
    
    public boolean getElevatorUp()
    {
        return opperator.getRawButton(3);
    }
    
    public boolean getElevatorDown()
    {
        return opperator.getRawButton(2);
    }
    
    public boolean getAutoAlign()
    {
        return opperator.getRawButton(6);
    }
    
    public boolean getEndgameTrigger()
    {
        return driver.getRawButton(11);
    }
    
    public boolean getGyroReset()
    {
        return driver.getRawButton(12);
    }
    
    public boolean getShooting()
    {
        return opperator.getRawButton(2);
    }
}