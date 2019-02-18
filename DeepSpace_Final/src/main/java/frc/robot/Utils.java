package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Utils {
  private static final double kDefaultDeadband = 0.02;

  private static final double kRumbleVal_on = 1.0;
  private static final double kRumbleVal_off = 0.0;
  private static final double kRumbleLength_secs = 0.5;

  public static double limit(double value) {
    if (value > 1.0) {
      return 1.0;
    }
    if (value < -1.0) {
      return -1.0;
    }
    return value;
  }

  public static double applyDeadband(double value) {
    if (Math.abs(value) > kDefaultDeadband) {
      if (value > 0.0) {
        return (value - kDefaultDeadband) / (1.0 - kDefaultDeadband);
      } else {
        return (value + kDefaultDeadband) / (1.0 - kDefaultDeadband);
      }
    } else {
      return 0.0;
    }
  }

  public static void setRumble(Joystick joy, edu.wpi.first.wpilibj.GenericHID.RumbleType type) {
    joy.setRumble(type, kRumbleVal_on);
    Timer.delay(kRumbleLength_secs);
    joy.setRumble(type, kRumbleVal_off);
  }
}