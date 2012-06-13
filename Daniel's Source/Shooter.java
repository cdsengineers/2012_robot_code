package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.CANJaguar;
public class Shooter {
     CANJaguar wheel;
     
     public Shooter() //constructer
     {
         try {
             wheel = new CANJaguar(2); //this is board ID of the CANJag we used, you have to use TI's program to set the board ID.
             wheel.changeControlMode(CANJaguar.ControlMode.kVoltage); //we control the motor by providing a Voltage.
             //wheel.configEncoderCodesPerRev(360); //use these if you use an encoder
             //wheel.setPID(.024, .002, .002);
             Text.dsWrite("CAN,pt.2",5); //ignore any Text.whatever, they just send info to our driverstation
             wheel.enableControl();//enables the CANJag to start controlling motor. You must have this.
             
         } catch(Exception e) {
             Text.dsWrite("Please Connect Serial Cable",5);
         }
         Text.updateLCD();
     }
     
     public void setVoltage(double Voltage) { //sets the canJag based on a value from 0.0 to 12.0
         try {
             wheel.setX(Voltage);
         }
         catch(Exception e)
         {
             Text.updateWith("setSpeed broken: ",5);
         }
     }
     
     public void stop() {//stops the wheel
         try {
             wheel.setX(0);
             Text.dsWrite("The shooter stopped",5);
         } catch(Exception e) {
             Text.dsWrite("Welp, stop failed",5);
         }
         Text.updateLCD();
     }
     
     public double getVoltage() {//returns voltages
         try {
             return wheel.getOutputVoltage();
         } catch(Exception e) {
             Text.updateWith("Retriving the speed failed tragically",5);
             return -1;
         }
     }
     public double getAmps () {
         try {
             return wheel.getOutputCurrent(); //this was for some of my experiments.
         }
         catch (Exception e) {
             Text.updateWith("Failed to amp it up", 5);
             return -1;
         }
     }
}