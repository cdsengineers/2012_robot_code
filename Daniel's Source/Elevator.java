package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;

public class Elevator {
    Relay elevatorMotor;
    DigitalInput intakeSensor;
    
    int speed = 0, ballsInIntake;
    public boolean ballEntering,inTimeVar,outTimeVar;
    
    public Elevator(){
        elevatorMotor = new Relay(Constants.SLOT_RELAY_ELEVATOR_CHANNEL);
        intakeSensor = new DigitalInput(1, 1);
    }
    public void moveElevator(boolean upButton, boolean downButton) {
        if (upButton == true) { 
            goUp();
        }
        else if(downButton == true) {
            goDown();
        }
        else {
            stop();
        }
    }
    public  void goUp(){
        elevatorMotor.set(Relay.Value.kForward);
        speed = 1;
    }
    public void goDown(){
        elevatorMotor.set(Relay.Value.kReverse);
        speed = -1;
    }
    public void stop(){
        elevatorMotor.set(Relay.Value.kOff);
        speed = 0;
    }
    public int getSpeed(){
        return speed;
    }
    public String inteligentOutput(double amps) {
        if (intakeSensor.get() && inTimeVar) {
            ballsInIntake++;
            inTimeVar = false;
            return "ball in intake";
        }
        else if (!intakeSensor.get()) { //might add !inTimeVar
            inTimeVar = true;
            return "balls" + ballsInIntake;
        } 
        if (amps > 10) {
            ballsInIntake--;
            outTimeVar = false;
            return "ball shot";
        }
        else if (amps < 10) { //might add !outTimeVar 
            outTimeVar = true;
            return "balls" + ballsInIntake;
        }  
        else {
            return "balls" + ballsInIntake;
        }
    }
}