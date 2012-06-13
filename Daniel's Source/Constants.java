package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.DriverStationLCD.Line;

public class Constants {
    /* Channels are labeled 1 through 8 */
    /* These are correct regardless of what you think, as long as Analog is in 1. Slot and Digital is in 2. Slot */
    public static int SLOT_ANALOG = 1;
    public static int SLOT_DIGITAL = 1;
    
    public static int SLOT_ANALOG_GYRO_CHANNEL = 1;
    
    public static final int SLOT_RELAY_ELEVATOR_CHANNEL = 1;
    public static final int INTAKE_SENSOR_CHANNEL = 1;
    public static final int TOP_SENSOR_CHANNEL = 2;
    
    public static final Line[] DS_LINE_ORDER = {
    DriverStationLCD.Line.kMain6,
    DriverStationLCD.Line.kUser2,
    DriverStationLCD.Line.kUser3,
    DriverStationLCD.Line.kUser4,
    DriverStationLCD.Line.kUser5,
    DriverStationLCD.Line.kUser6};
}
