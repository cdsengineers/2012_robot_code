package edu.wpi.first.wpilibj;

import com.sun.squawk.util.MathUtils;

import edu.wpi.first.wpilibj.parsing.IInputOutput;

public class Attack3 implements IInputOutput{
	private DriverStation driverStation;
	private final int portValue;
	private final boolean isYInverted;
	private boolean[] pressed;
	
	public static class Jaxis {
		public final int value;
		private Jaxis(int i) {
			this.value = i;
		}
		
		public static final Jaxis xaxis = new Jaxis(1);
		public static final Jaxis yaxis = new Jaxis(2);
		public static final Jaxis throttleaxis = new Jaxis(3);
	}
	public static class Jbutton {
		private final int value;
		private Jbutton(int i) {
			this.value = i;
		}
		public static final Jbutton trigger = new Jbutton(1);
		public static final Jbutton button2 = new Jbutton(2);
		public static final Jbutton button3 = new Jbutton(3);
		public static final Jbutton button4 = new Jbutton(4);
		public static final Jbutton button5 = new Jbutton(5);
		public static final Jbutton button6 = new Jbutton(6);
		public static final Jbutton button7 = new Jbutton(7);
		public static final Jbutton button8 = new Jbutton(8);
		public static final Jbutton button9 = new Jbutton(9);
		public static final Jbutton button10 = new Jbutton(10);
		public static final Jbutton button11 = new Jbutton(11);
		public static final Jbutton button12 = new Jbutton(12);
	}
	
	public Attack3 (int port) {
		this(port, true);
	}
	public Attack3 (int port, boolean inverted) {
		driverStation = DriverStation.getInstance();
		portValue = port;
		isYInverted = inverted;
		pressed = new boolean[12];
	}
	
	public double getRawAxis(int axis) {
		if (isYInverted  && axis == Jaxis.yaxis.value) return -driverStation.getStickAxis(portValue, axis);
		else return driverStation.getStickAxis(portValue, axis);
	}
	public double getAxis(Jaxis axisType) {
		return getRawAxis(axisType.value);
	}
	public double getX() {
		return getRawAxis(1);
	}
	public double getY() {
		return getRawAxis(2);
	}
	public double getThrottle() {
		return getRawAxis(3);
	}
    public double getMagnitude() {
        return Math.sqrt(MathUtils.pow(getX(), 2) + MathUtils.pow(-getY(), 2)); //y is inverted from original equation
    }
    public double getDirectionRadians() {
        return MathUtils.atan2(getX(), getY()); //y is opposite from original equation
    }
	public boolean getRawButton(int button) {
		boolean buttonState = ((0x1 << (button - 1)) & driverStation.getStickButtons(portValue)) != 0;
		if (buttonState) pressed[button-1] = true;	
		return buttonState;
	}
	public boolean getButton(Jbutton buttonType) {
		boolean buttonState = getRawButton(buttonType.value);
		if (buttonState) pressed[buttonType.value-1] = true;
		//else buttonType.pressed = false; may not be necessary
		return buttonState;
	}
	public boolean getReleasedButton(Jbutton buttonType) {
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
