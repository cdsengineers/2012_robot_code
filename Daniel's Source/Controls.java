package edu.wpi.first.wpilibj.templates;

//import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.KinectStick;

public class Controls {
    public static final int JOYSTICK_LEFT = 1;
    public static final int JOYSTICK_RIGHT = 2;
    public static final int GAMEPAD = 3;
    public static final int KINECT_LEFT = 4;
    public static final int KINECT_RIGHT = 5;
    
    public static final int GAMEPAD_LEFT_CONTROL = 2;
    public static final int GAMEPAD_RIGHT_CONTROL = 4;
    public static final int GAMEPAD_R1 = 5;// these two are best guesses, test
    public static final int GAMEPAD_R2 = 7;
    public static final int GAMEPAD_L1 = 4;
    public static final int GAMEPAD_L2 = 8;

    private Joystick leftJoy, rightJoy, gamePad;
    private KinectStick kinectLeft, kinectRight;
    //private DriverStation driverInput;
    
    public boolean buttons[][] = new boolean[3][12]; //holds button values.
    public boolean driverStation[] = new boolean [8];
    
    public Controls() {
        leftJoy = new Joystick(JOYSTICK_LEFT);
        rightJoy = new Joystick(JOYSTICK_RIGHT);
        gamePad = new Joystick(GAMEPAD);
        kinectLeft = new KinectStick(KINECT_LEFT - 3);//this number must be one.
        kinectRight = new KinectStick(KINECT_RIGHT - 3);//this number must be two.
    }
    
    public double getX(int dev) {
        return _get_rel_joystick(dev).getX();
    }
    
    public double getY(int dev) {
        return -_get_rel_joystick(dev).getY(); //is now reversed over here!
    }
    
    public double getZ(int dev) {
        return _get_rel_joystick(dev).getZ();
    }
    
    public double getRawAxis(int dev, int axis) {
        return _get_rel_joystick(dev).getRawAxis(axis);
    }

    public void updateButtons() { //call this at the beginning of every loop to store buttons values to an easily access variable.
        try {
            for (int i = 0; i < 3; i++) {
                for(int j = 0; j < 12; j++){
                        buttons[i][j] = _get_rel_joystick(i+1).getRawButton(j+1);
                }
            }
        }
        catch (Exception ex){
            System.err.print(ex);
        }
    }
    
    public boolean getButton(int devicePort, int button) {
        return buttons[devicePort-1][button-1];
    }
    
    private GenericHID _get_rel_joystick(int dev) {//returns a joystick or kinectstick object based on a integer value.
        switch(dev) {
            case JOYSTICK_LEFT: return leftJoy;
            case JOYSTICK_RIGHT: return rightJoy;
            case GAMEPAD: return gamePad;
            case KINECT_LEFT: return kinectLeft;
            case KINECT_RIGHT: return kinectRight;
        }      
        return null;
    }
}