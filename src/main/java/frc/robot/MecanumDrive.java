package frc.robot;

public class MecanumDrive {
    private static final double kDefaultDeadband = 0.02;  
    private static final double multi = 1; 

    /*
    * LogiTech Controller Layout:
    *         
    *       -y             
    *       |__ +x
    *        \
    *         -z
    */
  public static DriveControl drive(double driveX, double driveY, double look) {
    // for some reason... forward is negative. Because that makes sense...
    // So lets invert it.
    driveY *= -1; 

    driveX = applyDeadband(limit(driveX), kDefaultDeadband);
    driveY = applyDeadband(limit(driveY), kDefaultDeadband);

    // driveY > 0.0 = move forward
    // driveY < 0.0 = move backward
    // driveX > 0.0 = move right
    // driveX < 0.0 = move left
    // look > 0.0 = clockwise
    // look < 0.0 = counter-clockwise
    
    double leftFrontVel = multi*(driveY + driveX + look);
    double leftRearVel = multi*(-driveY + driveX - look);
    double rightFrontVel = multi*(driveY - driveX - look);
    double rightRearVel = multi*(-driveY - driveX + look);

    return new DriveControl(leftFrontVel, leftRearVel, rightFrontVel, rightRearVel);
  }

  private static double limit(double value) {
    if (value > 1.0) {
      return 1.0;
    }
    if (value < -1.0) {
      return -1.0;
    }
    return value;
  }

  private static double applyDeadband(double value, double deadband) {
    if (Math.abs(value) > deadband) {
      if (value > 0.0) {
        return (value - deadband) / (1.0 - deadband);
      } else {
        return (value + deadband) / (1.0 - deadband);
      }
    } else {
      return 0.0;
    }
  }
}