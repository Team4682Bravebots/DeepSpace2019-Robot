package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Demogorgon {

  private TalonSRX _mc1;
  private TalonSRX _mc2;
  private TalonSRX _mc3;

  private DigitalInput _limSwitch;
  private DoubleSolenoid _sol;

  private boolean _isLifted = false;
  private boolean _isLowered = true;

  // TODO: Check these ports
  private static final int kLimitPort = 0;
  private static final int kSolForward = 2;
  private static final int kSolReverse = 3;

  private static final double kPower_suck = 1.0;
  private static final double kPower_push = -1.0;
  private static final double kPower_off = 0.0;

  private static final long kTimeToLift_ms = 500;
  private static final long kTimeToLower_ms = 500;

  public Demogorgon(int m1, int m2, int m3) {
    _mc1 = new TalonSRX(m1);
    _mc2 = new TalonSRX(m2);
    _mc3 = new TalonSRX(m3);

    _limSwitch = new DigitalInput(kLimitPort);
    _sol = new DoubleSolenoid(kSolForward, kSolReverse);
  }

  public boolean suckBall(long currTime) {
    if (!_isLowered) {
      lower(currTime);
      return false;
    }

    boolean isLimitHit = _limSwitch.get();
    if (isLimitHit) {
      turnOff();
    } else {
      suck();
    }
    return isLimitHit;
  }

  public void deployBall(long currTime) {
    if (!_isLifted) {
      lift(currTime);
      return;
    }

    push();
  }

  public void turnOff() {
    runMotors(kPower_off);
    stopSolenoid();
  }

  public void lift(long currTime) {
    if (currTime <= kTimeToLift_ms) {
      _sol.set(DoubleSolenoid.Value.kForward);
      _isLifted = false;
      _isLowered = false;
    } else {
      stopSolenoid();
      _isLifted = true;
    }
  }

  public void lower(long currTime) {
    if (currTime <= kTimeToLower_ms) {
      _sol.set(DoubleSolenoid.Value.kReverse);
      _isLifted = false;
      _isLowered = false;
    } else {
      stopSolenoid();
      _isLowered = true;
    }
  }

  private void stopSolenoid() {
    _sol.set(DoubleSolenoid.Value.kOff);
  }

  private void suck() {
    runMotors(kPower_suck);
  }

  private void push() {
    runMotors(kPower_push);
  }

  private void runMotors(double power) {
    _mc1.set(ControlMode.Velocity, power);
    _mc2.set(ControlMode.Velocity, power);
    _mc3.set(ControlMode.Velocity, power);
  }

  public void debug() {
    SmartDashboard.putString("DART", "the Demogorgon");
    SmartDashboard.putBoolean("isLifted?", _isLifted);
    SmartDashboard.putBoolean("isLowered?", _isLowered);
    SmartDashboard.putBoolean("hasBall?", _limSwitch.get());
  }
}