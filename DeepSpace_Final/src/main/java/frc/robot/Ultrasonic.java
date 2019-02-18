package frc.robot;

import edu.wpi.first.wpilibj.AnalogInput;

public class Ultrasonic {

  private AnalogInput _ai;
  private static final double kDivisor = 0.0098;
  public static final double kThreshold = 7.5;

  public Ultrasonic(int usPort) {
    _ai = new AnalogInput(usPort);
  }

  public double getDistance() {
    return _ai.getVoltage() / kDivisor;
  }
}