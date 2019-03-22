package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Demogorgon {

  private TalonSRX _mc1;

  private static final double kPower_suck =-1.0;
  private static final double kPower_push = 1.0;
  private static final double kPower_off = 0.0;


  public Demogorgon(int m1) {
    _mc1 = new TalonSRX(m1);
  }

  public void turnOff() {
    runMotors(kPower_off);
  }

  public void suck() {
    runMotors(kPower_suck);
  }

  public void push() {
    runMotors(kPower_push);
  }

  private void runMotors(double power) {
    _mc1.set(ControlMode.PercentOutput, power);
  }
}