package edu.wpi.first.wpilibj;

import com.sun.squawk.util.MathUtils;

import edu.wpi.first.wpilibj.parsing.IInputOutput;

public class DualAction implements IInputOutput{
	private DriverStation driverStation;
	private final int portValue;
	private final boolean isYInverted;
	private boolean[] pressed;
	
	public static class Daxis {
		public final int value;
		private Daxis(int i) {
			this.value = i;
		}
		
		public static final Daxis leftXaxis = new Daxis(1);
		public static final Daxis leftYaxis = new Daxis(2);
		public static final Daxis rightXaxis = new Daxis(3);
		public static final Daxis rightYaxis = new Daxis(4);
		public static final Daxis dpadUp = new Daxis(5);
		public static final Daxis dpadRight = new Daxis(6);
	}
	public static class Dbutton {
		public final int value;
		private Dbutton(int i) {
			this.value = i;
		}
		public static final Dbutton button1 = new Dbutton(1);
		public static final Dbutton button2 = new Dbutton(2);
		public static final Dbutton button3 = new Dbutton(3);
		public static final Dbutton button4 = new Dbutton(4);
		public static final Dbutton tlBumper = new Dbutton(5);
		public static final Dbutton trBumper = new Dbutton(6);
		public static final Dbutton blBumper = new Dbutton(7);
		public static final Dbutton brBumper = new Dbutton(8);
		public static final Dbutton button9 = new Dbutton(9);
		public static final Dbutton button10 = new Dbutton(10);
		public static final Dbutton leftThumb = new Dbutton(11);
		public static final Dbutton rightThumb = new Dbutton(12);
	}
	
	public DualAction (int port) {
		this(port, true);
	}
	public DualAction (int port, boolean inverted) {
		driverStation = DriverStation.getInstance();
		portValue = port;
		isYInverted = inverted;
		pressed = new boolean[12];
	}
	
	public double getRawAxis(int axis) {
		if (isYInverted  && (axis == Daxis.leftYaxis.value || axis == Daxis.rightYaxis.value)) return -driverStation.getStickAxis(portValue, axis);
		else return driverStation.getStickAxis(portValue, axis);
	}
	public double getAxis(Daxis axisType) {
		return getRawAxis(axisType.value);
	}
    public double getLeftMagnitude() {
        return Math.sqrt(MathUtils.pow(getRawAxis(1), 2) + MathUtils.pow(-getRawAxis(2), 2)); //y is opposite of original equation
    }
    public double getLeftDirectionRadians() {
        return MathUtils.atan2(getRawAxis(1), getRawAxis(2)); //y is opposite of original equation because y is flipped
    }
    public double getRightMagnitude() {
        return Math.sqrt(MathUtils.pow(getRawAxis(3), 2) + MathUtils.pow(-getRawAxis(4), 2)); //y is opposite of original equation
    }
    public double getRightDirectionRadians() {
        return MathUtils.atan2(getRawAxis(3), getRawAxis(4)); //y is opposite of original equation because y is flipped
    }
	public boolean getRawButton(int button) {
		boolean buttonState = ((0x1 << (button - 1)) & driverStation.getStickButtons(portValue)) != 0;
		if (buttonState) pressed[button-1] = true;	
		return buttonState;
	}
	public boolean getButton(Dbutton buttonType) {
		boolean buttonState = getRawButton(buttonType.value);
		if (buttonState) pressed[buttonType.value-1] = true;
		//else buttonType.pressed = false; may not be necessary
		return buttonState;
	}
	public boolean getReleasedButton(Dbutton buttonType) {
		if(!getButton(buttonType) && getPressedValue(buttonType.value)) { //will make buttonType.pressed true if button is pressed
			pressed[buttonType.value-1] = false;//will reset statement so only one event happens.
			return true;
		}
		else return false;		
	}
	private boolean getPressedValue(int button) {
		return pressed[button-1];
	}	
}
