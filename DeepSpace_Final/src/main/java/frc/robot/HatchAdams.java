package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class HatchAdams {
  private Solenoid _sol_deploy;
  
  private static int kSolChannel_deploy = 2;

  public HatchAdams() {
    _sol_deploy = new Solenoid(kSolChannel_deploy);
  }

  public void deployHatch() {
    _sol_deploy.set(true);
  }

  public void turnOffHank() {
    _sol_deploy.set(false);
  }
}
