package frc.robot;

public class DriveControl {
  private double _hatchElec;
  private double _ballElec;
  private double _hatchPn;
  private double _ballPn;

  public DriveControl(double he, double be, double hp, double bp) {
    _hatchElec = he;
    _ballElec = be;
    _hatchPn = hp;
    _ballPn = bp;
  }

  public double getHatchElec() {
    return _hatchElec;
  }

  public double getBallElec() {
    return _ballElec;
  }

  public double getHatchPn() {
    return _hatchPn;
  }

  public double getBallPn() {
    return _ballPn;
  }
}