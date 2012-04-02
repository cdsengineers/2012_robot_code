package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
public class Shooter {
     CANJaguar wheel;
     
     public Shooter()
     {
         try {
             wheel = new CANJaguar(2);
             wheel.changeControlMode(CANJaguar.ControlMode.kVoltage);
             //wheel.configEncoderCodesPerRev(360);
             //wheel.setPID(.024, .002, .002);
             Text.dsWrite("CAN,pt.2",5);
             wheel.enableControl();
             
         } catch(Exception e) {
             Text.dsWrite("Please Connect Serial Cable",5);
         }
         Text.updateLCD();
     }
     
     public void setVoltage(double Voltage)
     {
         try {
             wheel.setX(Voltage);
         }
         catch(Exception e)
         {
             Text.updateWith("setSpeed broken: ",5);
         }
     }
     
     public void stop() {
         try {
             wheel.setX(0);
             Text.dsWrite("The shooter stopped",5);
         } catch(Exception e) {
             Text.dsWrite("Welp, stop failed",5);
         }
         Text.updateLCD();
     }
     
     public double getVoltage() {
         try {
             return wheel.getOutputVoltage();
         } catch(Exception e) {
             Text.updateWith("Retriving the speed failed tragically",5);
             return -1;
         }
     }
     public double getAmps () {
         try {
             return wheel.getOutputCurrent();
         }
         catch (Exception e) {
             Text.updateWith("Failed to amp it up", 5);
             return -1;
         }
     }
}