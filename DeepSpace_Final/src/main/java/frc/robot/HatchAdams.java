package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class HatchAdams {
  private Solenoid _sol_deploy;
  private DoubleSolenoid _lickATongue;
  
  private static int kSolChannel_deploy = 2;
  private static final int kLickATongueForward = 4;
  private static final int kLickATongueReverse = 6;

  public HatchAdams() {
    _sol_deploy = new Solenoid(kSolChannel_deploy);
    _lickATongue = new DoubleSolenoid(kLickATongueForward, kLickATongueReverse);
  }

  public void deployHatch() {
    _sol_deploy.set(true);
  }

  public void turnOffHank() {
    _sol_deploy.set(false);
  }

  public void expand() {
    _lickATongue.set(Value.kForward);
  }

  public void contract() {
    _lickATongue.set(Value.kReverse);
  }

  public void turnOffLickATongue() {
    _lickATongue.set(Value.kOff);
  }
}
