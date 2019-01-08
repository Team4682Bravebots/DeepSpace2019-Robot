package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;

public class Robot extends IterativeRobot {
  private static final int kFrontLeftChannel = 3;
  private static final int kRearLeftChannel = 2;
  private static final int kFrontRightChannel = 4;
  private static final int kRearRightChannel = 1;

  private TalonSRX frontLeft;
  private TalonSRX rearLeft;
  private TalonSRX frontRight;
  private TalonSRX rearRight;

  private static final int kJoystickChannel = 0;
  private XboxController joystick;

  // TODO: Check the ping and echo channels
  private static final int kPingChannel = 1;
  private static final int kEchoChannel = 1;
  private Ultrasonic ultrasonic;
  // We need to measure what this height is
  private static final double kRaiseHeight_MM = 150; // ?
  private static final double kLowerHeight_MM = 20; // ?

  // Encoder
  // TODO: Check sourceA and sourceB
  private static final int kEncoderSourceA = 0;
  private static final int kEncoderSourceB = 1;
  private Encoder armEncoder;
  // We need figure out this value?
  private static final double kHeightEncoderCount = 100; // ?

  // state for arm
  private static boolean currentlyLifting;
  private static boolean currentlyLowering;

  // Pneumatics
  // TODO: check channels
  private static final int kSolenoidForwardChannel = 1;
  private static final int kSolenoidReverseChannel = 2;
  DoubleSolenoid solenoid;

  @Override
  public void robotInit() {
    frontLeft = new TalonSRX(kFrontLeftChannel);
    rearLeft = new TalonSRX(kRearLeftChannel);
    frontRight = new TalonSRX(kFrontRightChannel);
    rearRight = new TalonSRX(kRearRightChannel);

    // invert drive train from tank drive
    frontLeft.setInverted(true);
    rearRight.setInverted(true);

    // joystick
    joystick = new XboxController(kJoystickChannel);
  }

  @Override
  public void teleopPeriodic() {
    // drive
    DriveControl driveControl = MecanumDrive.drive(joystick.getX(Hand.kRight), joystick.getY(Hand.kRight), joystick.getX(Hand.kLeft));
    
    frontLeft.set(ControlMode.PercentOutput, driveControl.getFrontLeft());
    rearLeft.set(ControlMode.PercentOutput, driveControl.getRearLeft());
    frontRight.set(ControlMode.PercentOutput, driveControl.getFrontRight());
    rearRight.set(ControlMode.PercentOutput, driveControl.getRearRight());
  }
}
