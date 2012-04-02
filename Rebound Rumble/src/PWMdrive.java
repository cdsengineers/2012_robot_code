
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PWMdrive {
    public static final int BOTH_SIDES = -2;
    public static final int LEFT_SIDE = -1;
    public static final int RIGHT_SIDE = 0;
    protected static final int motorQuadrant[] = {3,1,2,4}; // lays out motors in the quadrants of a cartesian plane.
    Victor spc[] = new Victor[4];
    
    protected static final int DEFAULT_SHIFT = 7;
    protected static double SHIFT_MULTIPLIER_INCREMENT = 0.1;
    protected static double SHIFT_MULTIPLIER; //with the "SHIFT_MULTIPLIER_INCREMENT" = 0.1, "SHIFT" of 7 = "SHIFT_MULTIPLIER_INCREMENT" of 1, "SHIFT" 6 = 0.9 ...
    public int SHIFT; //virtual shifter is a set change in the sensitivity of the robot controls, with no increase in torque for a lower gear.
    
    public PWMdrive () {
        for(int i = 0; i< spc.length; i++) {
            spc[i] = new Victor (Constants.SLOT_DIGITAL,motorQuadrant[i]);
        }
        shift(DEFAULT_SHIFT);
    }
    
    public double getSpeed(int side) { //get the speed of one, two, or both sides.
        if (side == LEFT_SIDE) return spc[2].get();
        else if (side == LEFT_SIDE) return spc[1].get();
        else if (side == LEFT_SIDE) return (spc[2].get() + spc[1].get())/2;
        else return 0;
    }
    
    public void setSpeed(int side, double speed) { //sets speeds without virtual shifter
        Victor[] arr = get_rel_obj(side);
        if(side == RIGHT_SIDE) {
            for(int i = 0; i < arr.length; i++) arr[i].set(-speed);
        }
        else if(side == LEFT_SIDE) {
            for(int i = 0; i < arr.length; i++) arr[i].set(speed);
        }
        else if(side == BOTH_SIDES)
        {
            arr[0].set(-speed);
            arr[1].set(speed);
            arr[2].set(speed);
            arr[3].set(-speed);
        }
        else {
            for(int i = 0; i < arr.length; i++) arr[i].set(speed);
        }        
    }
    public void setSpeed (int side, double speed, boolean SHIFT){ //sets speed with virutal shifter
        Victor[] arr = get_rel_obj(side);
        if(side == RIGHT_SIDE) {
            for(int i = 0; i < arr.length; i++) arr[i].set(-speed*SHIFT_MULTIPLIER);
        }
        else if(side == LEFT_SIDE) {
            for(int i = 0; i < arr.length; i++) arr[i].set(-speed*SHIFT_MULTIPLIER);
        }
        else if(side == BOTH_SIDES)
        {
            arr[0].set(-speed*SHIFT_MULTIPLIER);
            arr[1].set(speed*SHIFT_MULTIPLIER);
            arr[2].set(speed*SHIFT_MULTIPLIER);
            arr[3].set(-speed*SHIFT_MULTIPLIER);
        }
        else {
            for(int i = 0; i < arr.length; i++) arr[i].set(speed*SHIFT_MULTIPLIER);
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
    
    public void sendToDashboard () {
        SmartDashboard.putDouble("Left",getSpeed(LEFT_SIDE));
        SmartDashboard.putDouble("Right",getSpeed(RIGHT_SIDE));
        SmartDashboard.putDouble("Shift", getShift());
    }    
    
    public Victor[] get_rel_obj(int side) {
        switch(side) {
            case LEFT_SIDE: return new Victor[]{spc[1], spc[2]};
            case RIGHT_SIDE: return new Victor[]{spc[0], spc[3]};
            case BOTH_SIDES: return new Victor[]{spc[0],spc[1],spc[2],spc[3]};
        }
        return null;
    }
}