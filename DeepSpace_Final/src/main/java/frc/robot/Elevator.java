package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
//import com.sun.tools.classfile.RuntimeParameterAnnotations_attribute;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import jdk.jfr.internal.settings.CutoffSetting;

public class Elevator {
  private boolean bHatch = false;
  private boolean bBall =false;

  private TalonSRX _mc;

  // TODO: Get these values
  private static final double kBallLowHeight_rot = 2.83;
  private static final double kBallMidHeight_rot = 8.71;
  private static final double kBallHighHeight_rot = 13.6;

  private static final double kHatchLowHeight_rot = 0.84;
  private static final double kHatchMidHeight_rot = 6.6;
  private static final double kHatchHighHeight_rot = 12.5;

  private static final double kPosThreshold = 500;

  // power
  private static final double kPower_max = 1.0;
  private static final double kPower_min = 0.3;
  private static final double kPower_descent = 0.2;
  private static final double kPower_off = 0.0;

  // PID
  private static final int kTimeout_ms = 30;
  private static final int kAllowedError = 0;
  private static final int kPidIdx = 0;
  private static final boolean kPhase = true;
  private static final boolean kIsInverted = true;
  private static final double kEncoderTicksPerRot = 4096.0;
  private static final double kRotMulti = kEncoderTicksPerRot * 4; // gear ratio is 4:1

  // Tune these
  private final double pValue = (kPower_max * 1023) / kEncoderTicksPerRot;
  private final double iValue = pValue / 100;
  private final double dValue = pValue * 10;

  public Elevator(int m) {
    _mc = new TalonSRX(m);
  }

  public void init() {
    _mc.configFactoryDefault();
  
    _mc.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, kPidIdx, kTimeout_ms);
    _mc.setSensorPhase(kPhase);
    _mc.setInverted(kIsInverted);
    _mc.setSelectedSensorPosition(0);

    _mc.configNominalOutputForward(0.0, kTimeout_ms);
    _mc.configNominalOutputReverse(0.0, kTimeout_ms);
    _mc.configPeakOutputForward(1.0, kTimeout_ms);
    _mc.configPeakOutputReverse(-1.0, kTimeout_ms);

    _mc.configAllowableClosedloopError(kPidIdx, kAllowedError, kTimeout_ms);

    _mc.config_kF(kPidIdx, 0.0, kTimeout_ms); // kF is 0 for positional PIDs
    _mc.config_kP(kPidIdx, pValue, kTimeout_ms);
    _mc.config_kI(kPidIdx, iValue, kTimeout_ms);
    _mc.config_kD(kPidIdx, 0.0, kTimeout_ms);
    
    /**j
		 * Grab the 360 degree position of the MagEncoder's absolute
		 * position, and intitally set the relative sensor to match.
		 */
		int absolutePosition = 0;//_mc.getSensorCollection().getPulseWidthPosition();

		// /* Mask out overflows, keep bottom 12 bits */
		// absolutePosition &= 0xFFF;
		// if (kPhase) { absolutePosition *= -1; }
		// if (kIsInverted) { absolutePosition *= -1; }
		
		/* Set the quadrature (relative) sensor to match absolute */
		_mc.setSelectedSensorPosition(absolutePosition, kPidIdx, kTimeout_ms);

    //reset();
  }

  public void move(double joyVal) {
    joyVal = Utils.applyDeadband(Utils.limit(joyVal));
    joyVal *= kPower_max;
    _mc.set(ControlMode.PercentOutput, joyVal);
  }

  public void turnOff() {
    move(0);
  }

  public void reset() {
    if(getTo(0)) {
      resetEncoder();
    }
  }

  public void resetEncoder() {
    _mc.setSelectedSensorPosition(0, kPidIdx, kTimeout_ms);
  }

  public boolean setBallLowHeight() {
    //return setTargetMotorPos(kRotMulti * kBallLowHeight_rot);
    return getTo(kRotMulti*kBallLowHeight_rot);
  }

  public boolean setBallMidHeight() {
    //return setTargetMotorPos(kRotMulti * kBallMidHeight_rot);
    return getTo(kRotMulti*kBallMidHeight_rot);
  }

  public boolean setBallHighHeight() {
    //return setTargetMotorPos(kRotMulti * kBallHighHeight_rot);
    return getTo(kRotMulti*kBallHighHeight_rot);
  }

  public boolean setHatchLowHeight() {
    //return setTargetMotorPos(kRotMulti * kHatchLowHeight_rot);
    return getTo(kRotMulti*kHatchLowHeight_rot);
  }

  public boolean setHatchMidHeight() {
    //return setTargetMotorPos(kRotMulti * kHatchMidHeight_rot);
    return getTo(kRotMulti*kHatchMidHeight_rot);
  }

  public boolean setHatchHighHeight() {
   //return setTargetMotorPos(kRotMulti * kHatchHighHeight_rot);
   return getTo(kRotMulti*kHatchHighHeight_rot);
  }

  private boolean getTo(double pos){
    double currentPos = _mc.getSelectedSensorPosition();

    if (Math.abs(currentPos-pos) <= kPosThreshold) {
      return true;
    }
    
    _mc.set(ControlMode.Position, pos);
    
    return false;
  }

  public void debug() {
    SmartDashboard.putString("ELEVATOR", "");
    SmartDashboard.putNumber("Elevator MC Pos", _mc.getSelectedSensorPosition());
    SmartDashboard.putNumber("Elevator Rotations", _mc.getSelectedSensorPosition()/kRotMulti);
    SmartDashboard.putNumber("Elevator MC Error", _mc.getClosedLoopError());

    SmartDashboard.putNumber("Elevator P Value", pValue);
    SmartDashboard.putNumber("Elevator I Value", iValue);
    SmartDashboard.putNumber("Elevator D Value", dValue);
  }
}