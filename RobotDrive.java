import edu.wpi.first.wpilibj.Victor;


public class RobotDrive {
    
    public static final int DRIVE_TRAIN_ERROR = 20, SPEED_ERROR = 10;
    
    private Victor frontRightVictor, frontLeftVictor, backRightVictor, backLeftVictor;
    private double SHIFT_SCALE = .3;
    private boolean CAN_SHIFT = false;
    
    public RobotDrive()
    {
        frontRightVictor = new Victor(1,1);
        backRightVictor = new Victor(1,2);
        
        frontLeftVictor = new Victor(1,3);
        backLeftVictor = new Victor(1,4);
    }
    
    public double setLeftSide(double speed)
    {
        speed = speed * SHIFT_SCALE;
        
        frontLeftVictor.set(speed);
        backLeftVictor.set(speed);
        
        if(frontLeftVictor.get() == backLeftVictor.get() && frontLeftVictor.get() == speed)
            return speed;
        else if(frontLeftVictor.get() == backLeftVictor.get() && frontLeftVictor.get() != speed)
            return SPEED_ERROR;
        else
            return DRIVE_TRAIN_ERROR;
    }
    
    public double getLeftSide()
    {
        if(frontLeftVictor.get() == backLeftVictor.get())
            return frontLeftVictor.get();
        else
            return DRIVE_TRAIN_ERROR;
    }
    
    public double setRightSide(double speed)
    {   
        speed = -speed;
        speed = speed * SHIFT_SCALE;
        
        frontRightVictor.set(speed);
        backRightVictor.set(speed);
        
        if(frontRightVictor.get() == backRightVictor.get() && frontRightVictor.get() == speed)
            return speed;
        else if(frontRightVictor.get() == backRightVictor.get() && frontRightVictor.get() != speed)
            return SPEED_ERROR;
        else
            return DRIVE_TRAIN_ERROR;
    }
    
    public double getRightSide()
    {
        if(frontRightVictor.get() == backRightVictor.get())
            return frontRightVictor.get();
        else
            return DRIVE_TRAIN_ERROR;
    }
    
    public double shiftUp()
    {
        SHIFT_SCALE = ((SHIFT_SCALE + .1) < 1 && CAN_SHIFT)?(SHIFT_SCALE+=.1):(SHIFT_SCALE);
        return SHIFT_SCALE;
    }
    
    public double shiftDown()
    {
        SHIFT_SCALE = ((SHIFT_SCALE - .1) > .3 && CAN_SHIFT)?(SHIFT_SCALE-=.1):(SHIFT_SCALE);
        return SHIFT_SCALE;
    }
    
    public double getShiftValue()
    {
        return SHIFT_SCALE;
    }
    
    public void setCanShift(boolean can_shift)
    {
        CAN_SHIFT = can_shift;
    }
}
