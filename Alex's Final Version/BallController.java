
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

public class BallController {
    
    private Relay ballCollectorMotor;
    private Victor bridgeVictor1, bridgeVictor2;
    private CANJaguar wheel;
    
    public BallController()
    {
        ballCollectorMotor = new Relay(1);
        
        
        bridgeVictor1 = new Victor(1, 5);
        bridgeVictor2 = new Victor(1, 6);
        try
        {
            wheel = new CANJaguar(2);
            wheel.changeControlMode(CANJaguar.ControlMode.kVoltage);
            wheel.enableControl();
        } catch (CANTimeoutException ex) {}
    }
    
    public void setLauncherVoltage(double Voltage)
    {
        try {wheel.setX(Voltage);}
        catch (CANTimeoutException ex) {}
    }
     
    public double getLauncherVoltage()
    {
       try {return wheel.getOutputVoltage();}
       catch(CANTimeoutException e) {return -1;}
    }
    
    public void moveBridgeUp()
    {
        bridgeVictor1.set(.3);
        bridgeVictor2.set(.3);
    }
    
    public void moveBridgeDown()
    {
        bridgeVictor1.set(-.3);
        bridgeVictor2.set(-.3);
    }
    
    public void stopBridge()
    {
        bridgeVictor1.set(0);
        bridgeVictor2.set(0);
    }
    
    public boolean startIntake(){
        ballCollectorMotor.set(Relay.Value.kForward);
        
        return true;
    }
    
    
    public boolean reverseIntake(){
        ballCollectorMotor.set(Relay.Value.kReverse);
        
        return true;
    }
    
    public boolean stopIntake(){
        ballCollectorMotor.set(Relay.Value.kOff);
        
        return true;
    }   
}