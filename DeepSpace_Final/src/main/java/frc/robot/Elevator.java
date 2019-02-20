package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator {
  private TalonSRX _mc;

  // TODO: Get these values
  private static final double kBallLowHeight_rot = 10.0;
  private static final double kBallMidHeight_rot = 10.0;
  private static final double kBallHighHeight_rot = 10.0;
  private static final double kHatchLowHeight_rot = 10.0;
  private static final double kHatchMidHeight_rot = 10.0;
  private static final double kHatchHighHeight_rot = 10.0;

  // power
  private static final double kPower_max = 0.8;
  private static final double kPower_min = 0.3;
  private static final double kPower_descent = 0.2;
  private static final double kPower_off = 0.0;

  // PID
  private static final int kTimeout_ms = 30;
  private static final int kAllowedError = 0;
  private static final int kPidIdx = 0;
  private static final boolean kPhase = false;
  private static final boolean kIsInverted = false;
  private static final double kRotMulti = 16500.0;

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
    _mc.config_kP(kPidIdx, 0.2, kTimeout_ms); // TODO: Tune this
    _mc.config_kI(kPidIdx, 0.0, kTimeout_ms);
    _mc.config_kD(kPidIdx, 0.0, kTimeout_ms);

    //reset();
  }

  public void move(double joyVal) {
    joyVal = Utils.applyDeadband(Utils.limit(joyVal));

    // if we are lowering, we don't go negative. We go at the power to descend
    if (joyVal < 0.0) {
      joyVal *= kPower_descent;
    } else {
      joyVal *= kPower_max;
    }
    _mc.set(ControlMode.PercentOutput, joyVal);
  }

  public void turnOff() {
    move(0);
  }

  public void reset() {
    int absolutePosition = _mc.getSensorCollection().getPulseWidthPosition();
    absolutePosition &= 0xFFF;
    _mc.setSelectedSensorPosition(absolutePosition, kPidIdx, kTimeout_ms);
  }

  public boolean setBallLowHeight() {
    return setTargetMotorPos(kRotMulti * kBallLowHeight_rot);
  }

  public boolean setBallMidHeight() {
    return setTargetMotorPos(kRotMulti * kBallMidHeight_rot);
  }

  public boolean setBallHighHeight() {
    return setTargetMotorPos(kRotMulti * kBallHighHeight_rot);
  }

  public boolean setHatchLowHeight() {
    return setTargetMotorPos(kRotMulti * kHatchLowHeight_rot);
  }

  public boolean setHatchMidHeight() {
    return setTargetMotorPos(kRotMulti * kHatchMidHeight_rot);
  }

  public boolean setHatchHighHeight() {
    return setTargetMotorPos(kRotMulti * kHatchHighHeight_rot);
  }

  private boolean setTargetMotorPos(double pos) {
    if (_mc.getSelectedSensorPosition() < pos) {
      _mc.set(ControlMode.Position, pos);
      return false;
    }
    return true;
  }

  public void debug() {
    SmartDashboard.putString("ELEVATOR", "");
    SmartDashboard.putNumber("Elevator MC Pos", _mc.getSelectedSensorPosition());
    SmartDashboard.putNumber("Elevator MC Error", _mc.getClosedLoopError());
  }
}