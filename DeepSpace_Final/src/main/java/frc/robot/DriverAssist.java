package frc.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriverAssist {
  private AnalogInput _irHatchRed;
  private AnalogInput _irHatchBlue;
  private AnalogInput _irHatchYellow;
  private AnalogInput _irBallRed;
  private AnalogInput _irBallBlue;
  private AnalogInput _irBallYellow;
  private Ultrasonic _usHatch;
  private Ultrasonic _usBall;
  private static final double kIR_Black = 3000.0;
  private static final double kIR_White = 1500.0;
  private static final double kBasePower = 0.3;

  public DriverAssist(int irHatchRedPort, int irHatchBluePort, int irHatchYellowPort, int irBallRedPort,
      int irBallBluePort, int irBallYellowPort, Ultrasonic usHatch, Ultrasonic usBall) {
    _irHatchRed = new AnalogInput(irHatchRedPort);
    _irHatchBlue = new AnalogInput(irHatchBluePort);
    _irHatchYellow = new AnalogInput(irHatchYellowPort);
    _irBallRed = new AnalogInput(irBallRedPort);
    _irBallBlue = new AnalogInput(irBallBluePort);
    _irBallYellow = new AnalogInput(irBallYellowPort);
    _usHatch = usHatch;
    _usBall = usBall;
  }

  public boolean followLine(MecanumDrive driver) {
    if (driver.isReversed()) {
      return followLine(driver, _irBallRed, _irBallBlue, _irBallYellow, _usBall);
    }
    return followLine(driver, _irHatchRed, _irHatchBlue, _irHatchYellow, _usHatch);
  }

  public boolean senseLine(boolean isReversed) {
    if (isReversed) {
      return senseLine(_irBallRed, _irBallBlue, _irBallYellow);
    }
    return senseLine(_irHatchRed, _irHatchBlue, _irHatchYellow);
  }

  private boolean senseLine(AnalogInput red, AnalogInput blue, AnalogInput yellow) {
    return (seesWhite(blue)) || (seesWhite(yellow)) || (seesWhite(red));
  }

  private boolean seesWhite(AnalogInput ir) {
    return ir.getValue() <= kIR_White;
  }

  private boolean followLine(MecanumDrive driver, AnalogInput red, AnalogInput blue, AnalogInput yellow,
      Ultrasonic ultra) {
    boolean redSeeWhite = seesWhite(red);
    boolean blueSeeWhite = seesWhite(blue);
    boolean yellowSeeWhite = seesWhite(yellow);

    double distance = ultra.getDistance();

    if ((!blueSeeWhite) && (!yellowSeeWhite)) {
      driver.driveDisabledPID(0.0, kBasePower, 0.0);

    } else if ((!blueSeeWhite) && (yellowSeeWhite)) {
      driver.driveDisabledPID(0.0, 0.0, kBasePower);

    } else if ((blueSeeWhite) && (!yellowSeeWhite)) {
      driver.driveDisabledPID(0.0, 0.0, -kBasePower);

    } else if ((blueSeeWhite) && (yellowSeeWhite) && (redSeeWhite)) {
      if (distance >= Ultrasonic.kThreshold) {
        driver.driveDisabledPID(0.0, kBasePower, 0.0);
      } else {
        driver.driveDisabledPID(0.0, 0.0, 0.0);
        return true;
      }
    }

    return false;
  }

  public void debug() {
    SmartDashboard.putString("DRIVER ASSIST", "");
    debugIR("Hatch Side Red", _irHatchRed);
    debugIR("Hatch Side Blue", _irHatchBlue);
    debugIR("Hatch Side Yellow", _irHatchYellow);
    SmartDashboard.putNumber("Hatch Ultrasonic", _usHatch.getDistance());

    debugIR("Ball Side Red", _irBallRed);
    debugIR("Ball Side Blue", _irBallBlue);
    debugIR("Ball Side Yellow", _irBallYellow);
    SmartDashboard.putNumber("Ball Ultrasonic", _usBall.getDistance());
  }

  private void debugIR(String key, AnalogInput ir) {
    SmartDashboard.putNumber(key + "Value", ir.getValue());
    SmartDashboard.putBoolean(key + "Sees white?", seesWhite(ir));
  }
}