package edu.wpi.first.wpilibj.templates;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.smartdashboard.*;
/**
 *
 * @author Jake
 */
public class Elevator {
    Relay elevatorMotor;
    int speed = 0;
    
    public final int INTAKE_SENSOR = 0;
    public final int TOP_SENSOR = 1;
    public boolean ballEntering;
    
    DigitalInput intake;
    DigitalInput topSensor;
    
    public Elevator(){
        elevatorMotor = new Relay(Constants.SLOT_RELAY_ELEVATOR_CHANNEL);
        intake = new DigitalInput(Constants.SLOT_DIGITAL, Constants.INTAKE_SENSOR_CHANNEL);
        topSensor = new DigitalInput(Constants.SLOT_DIGITAL, Constants.TOP_SENSOR_CHANNEL);
    }
    
    public void moveElevator(boolean upButton, boolean downButton) {
        if (upButton == true) { 
            goUp();
        }
        else if(downButton == true) {
            goDown();
        }
        
        /*
        else if (getElevatorSensorValue(INTAKE_SENSOR)){
            goUp();
            ballEntering = true;
        }
        else if (getElevatorSensorValue(TOP_SENSOR) ) {
            goUp();
            ballEntering = false;
        }
        else if (!ballEntering && !getElevatorSensorValue(TOP_SENSOR)) {
            stop();
            ballEntering = true;
        }
        else if(ballEntering && getSpeed() == 1) {
            goUp();
        }
        
        */
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
    public boolean getElevatorSensorValue(int sensor) {//use if you want a boolean value
        switch(sensor) {
            case INTAKE_SENSOR: return intake.get();
            case TOP_SENSOR: return intake.get() == true;
        }
        return false;
    }
    
    public DigitalInput getElevatorSensor(int sensor) {//use if you want an sensor object.
        switch(sensor) {
            case INTAKE_SENSOR: return intake;
            case TOP_SENSOR: return topSensor;
        }
        return null;
    }
    public void sendToDashboard () {
        SmartDashboard.putInt("Elevator Speed",getSpeed());
        SmartDashboard.putBoolean("Intake Sensor", intake.get());
    }
}
