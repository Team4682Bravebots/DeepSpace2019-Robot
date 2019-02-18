package frc.robot;

import edu.wpi.first.wpilibj.Solenoid;

public class HatchAdams {
  private Solenoid _sol_deploy;
  
  // TODO: Do we need this second one?
  // private Solenoid _sol_retract;

  private static int kSolChannel_deploy = 0;
  // private static int kSolChannel_retract = 1;

  public HatchAdams() {
    _sol_deploy = new Solenoid(kSolChannel_deploy);
    // _sol_retract = new Solenoid(kSolChannel_retract);
  }

  public void deployHatch() {
    _sol_deploy.set(true);
  }

  // public void retract() {
  // _sol_retract.set(true);
  // }

  public void turnOff() {
    _sol_deploy.set(false);
    // _sol_retract.set(false);
  }
}