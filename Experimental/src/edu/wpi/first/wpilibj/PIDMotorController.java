package edu.wpi.first.wpilibj;

import edu.wpi.first.wpilibj.parsing.IUtility;
import edu.wpi.first.wpilibj.util.BoundaryException;
import java.util.TimerTask;

/**
 * Class implements a PID Control Loop.
 *
 * Creates a separate thread which reads the given PIDSource and takes
 * care of the integral calculations, as well as writing the given
 * PIDOutput
 */
public class PIDMotorController implements IUtility{
    private double P, I, D;
    private double maximumOutput = 1.0;	// |maximum output|
    private double minimumOutput = -1.0;	// |minimum output|
    private double maximumInput = 0.0;		// maximum input - limit setpoint to this
    private double minimumInput = 0.0;		// minimum input - limit setpoint to this
    private boolean continuous = false;	// do the endpoints wrap around? eg. Absolute encoder
    private boolean enabled = false; 			//is the pid controller enabled
    private double prevError = 0.0;	// the prior sensor input (used to compute velocity)
    private double totalError = 0.0; //the sum of the errors for use in the integral calc
    private double tolerance = 0.05;	//the percentage error that is considered on target
    private double setpoint = 0.0;
    private double error = 0.0;
    private double result = 0.0;
    private static double period = 0.10; //0.05 is default
    PIDSource pidInput;
    PIDOutput[] pidOutput;
    java.util.Timer controlLoop;

    private class PIDTask extends TimerTask {

        private PIDMotorController m_controller;

        public PIDTask(PIDMotorController controller) {
            if (controller == null) {
                throw new NullPointerException("Given PIDController was null");
            }
            m_controller = controller;
        }

        public void run() {
            m_controller.calculate();
        }
    }

    /**
     * Allocate a PID object with the given constants for P, I, D
     * @param Kp the proportional coefficient
     * @param Ki the integral coefficient
     * @param Kd the derivative coefficient
     * @param source The PIDSource object that is used to get values
     * @param output The PIDOutput object that is set to the output value
     * @param period the loop time for doing calculations. This particularly effects calculations of the
     * integral and differential terms. The default is 50ms.
     */
    public PIDMotorController(double Kp, double Ki, double Kd, PIDSource source, PIDOutput[] output, double iperiod) {
        if (source == null)
            throw new NullPointerException("Null PIDSource was given");
        if (output == null)
            throw new NullPointerException("Null PIDOutput was given");


        controlLoop = new java.util.Timer();


        P = Kp;
        I = Ki;
        D = Kd;

        pidInput = source;
        pidOutput = output;
        period = iperiod;

        controlLoop.schedule(new PIDMotorController.PIDTask(this), 0L, (long) (period * 1000)); //starts thread
    }

    public PIDMotorController(double Kp, double Ki, double Kd, PIDSource source, PIDOutput[] output) {
        this(Kp, Ki, Kd, source, output, period);
    }

    public void free() { //Free the PID object
        controlLoop.cancel();
        controlLoop = null;
    }

    /**
     * Read the input, calculate the output accordingly, and write to the output.
     * This should only be called by the PIDTask
     * and is created during initialization.
     */
    private void calculate() {
        synchronized (this) {
            if (this.pidInput == null) {
                return;
            }
            if (pidOutput == null) {
                return;
            }

        }
        if (enabled) {
            double input = pidInput.pidGet();

            synchronized (this) {
                error = setpoint - input;
                if (continuous) {
                    if (Math.abs(error) > (maximumInput - minimumInput) / 2) {
                        if (error > 0) {
                            error = error - maximumInput + minimumInput;
                        } else {
                            error = error + maximumInput - minimumInput;
                        }
                    }
                }
                if (((totalError + error) * I < maximumOutput) && ((totalError + error) * I > minimumOutput)) {
                    totalError += error;
                }
                result = (P * error + I * totalError + D * (error - prevError));
                prevError = error;
                if (result > maximumOutput) {
                    result = maximumOutput;
                } 
                else if (result < minimumOutput) {
                    result = minimumOutput;
                }
            }
            for (int i = 0; i < pidOutput.length; i++) {
                pidOutput[i].pidWrite(result);
            }   
        }
    }

    public synchronized void setPID(double p, double i, double d) { //Set the PID Controller gain parameters.
        P = p;
        I = i;
        D = d;
    }

    public double getP() { //Get the Proportional coefficient
        return P;
    }

    public double getI() { //Get the Integral coefficient
        return I;
    }

    public synchronized double getD() { //Get the Differential coefficient
        return D;
    }

    public synchronized double get() { //Return the current PID result. This is always centered on zero and constrained the the max and min outs
        return result;
    }

    /**
     *  Set the PID controller to consider the input to be continuous,
     *  Rather then using the max and min in as constraints, it considers them to
     *  be the same point and automatically calculates the shortest route to
     *  the setpoint.
     * @param continuous Set to true turns on continuous, false turns off continuous
     */
    public synchronized void setContinuous(boolean icontinuous) {
        continuous = icontinuous;
    }

    public synchronized void setInputRange(double iminimumInput, double imaximumInput) { //Sets the maximum and minimum values expected from the input.
        if (minimumInput > maximumInput) {
            throw new BoundaryException("Lower bound is greater than upper bound");
        }
        minimumInput = iminimumInput;
        maximumInput = imaximumInput;
        setSetpoint(setpoint);
    }

    public synchronized void setOutputRange(double iminimumOutput, double imaximumOutput) { //Sets the minimum and maximum values to write to the output.
        if (minimumOutput > maximumOutput) {
            throw new BoundaryException("Lower bound is greater than upper bound");
        }
        minimumOutput = iminimumOutput;
        maximumOutput = imaximumOutput;
    }

    public synchronized void setSetpoint(double isetpoint) { //Set the setpoint for the PIDController
        if (maximumInput > minimumInput) {
            if (isetpoint > maximumInput) {
                setpoint = maximumInput;
            } else if (isetpoint < minimumInput) {
                setpoint = minimumInput;
            } else {
                setpoint = isetpoint;
            }
        } else {
            setpoint = isetpoint;
        }
    }

    public synchronized double getSetpoint() { //Returns the current setpoint of the PIDController
        return setpoint;
    }

    public synchronized double getError() { //Returns the current difference of the input from the setpoint
        return error;
    }

    public synchronized void setTolerance(double percent) { //Set the percentage error which is considered tolerable for use with
        tolerance = percent;
        //OnTarget. (Input of 15.0 = 15 percent)
        //@param percent error which is tolerable
    }

    public synchronized boolean onTarget() { //Return true if the error is within the percentage of the total input range, determined by setTolerance. This assumes that the maximum and minimum input were set using setInput. @return true if the error is less than the tolerance
        return (Math.abs(error) < tolerance / 100 *
                (maximumInput - minimumInput));
    }

    public synchronized void enable() { //Begin running the PIDController
        enabled = true;
    }

    public synchronized void disable() { //Stop running the PIDController, this sets the output to zero before stopping.
        for (int i = 0; i < pidOutput.length; i++) {
            pidOutput[i].pidWrite(0.0);
        } 
        enabled = false;
    }
    
    public synchronized boolean isEnable() { //Return true if PIDController is enabled.
        return enabled;
    }
    
    public synchronized void reset() { //Reset the previous error,, the integral term, and disable the controller.
        disable();
        prevError = 0;
        totalError = 0;
        result = 0;
    }
}