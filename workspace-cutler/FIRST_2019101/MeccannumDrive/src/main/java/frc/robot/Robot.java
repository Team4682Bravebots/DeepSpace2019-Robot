package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.cameraserver.CameraServer;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
  private static final int kFrontLeftChannel = 3;
  private static final int kRearLeftChannel = 2;
  private static final int kFrontRightChannel = 4;
  private static final int kRearRightChannel = 1;
  private double FLmax = 0;
  private double RLmax = 0;
  private double FRmax = 0;
  private double RRmax = 0;

  Faults _faults = new Faults();



  private TalonSRX frontLeft;
  private TalonSRX rearLeft;
  private TalonSRX frontRight;
  private TalonSRX rearRight;

  private static final int kJoystickChannel = 0;
  private XboxController joystick;

  

  // Encoder
  // TODO: Check sourceA and sourceB
  private static final int kleftFrontEncoderSourceA = 0;
  private static final int kleftFrontEncoderSourceB = 1;
  private Encoder leftFrontEncoder;

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
    CameraServer.getInstance().startAutomaticCapture();
    

    

    // invert drive train from tank drive
    frontLeft.setInverted(true);
    rearRight.setInverted(true);

    // joystick
    joystick = new XboxController(kJoystickChannel);

    //encoders
    leftFrontEncoder = new Encoder(kleftFrontEncoderSourceA, kleftFrontEncoderSourceB);
  
    // initialize
   
  }

  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("FR Sensor Vel", frontRight.getSelectedSensorVelocity());
    SmartDashboard.putNumber("FR Sensor pos",frontRight.getSelectedSensorPosition());
    SmartDashboard.putNumber("FR out %", frontRight.getMotorOutputPercent());
    SmartDashboard.putNumber("FL Sensor Vel", frontLeft.getSelectedSensorVelocity());
    SmartDashboard.putNumber("FL Sensor pos",frontLeft.getSelectedSensorPosition());
    SmartDashboard.putNumber("FL out %", frontLeft.getMotorOutputPercent());
    SmartDashboard.putNumber("RR Sensor Vel", rearRight.getSelectedSensorVelocity());
    SmartDashboard.putNumber("RR Sensor pos",rearRight.getSelectedSensorPosition());
    SmartDashboard.putNumber("RR out %", rearRight.getMotorOutputPercent());
    SmartDashboard.putNumber("RL Sensor Vel", rearLeft.getSelectedSensorVelocity());
    SmartDashboard.putNumber("RL Sensor pos",rearLeft.getSelectedSensorPosition());
    SmartDashboard.putNumber("RL out %", rearLeft.getMotorOutputPercent());

    SmartDashboard.putNumber("RLmax", RLmax);
    SmartDashboard.putNumber("RRmax", RRmax);
    SmartDashboard.putNumber("FRmax", FRmax);
    SmartDashboard.putNumber("FLmax", FLmax);

    SmartDashboard.putNumber("F", (1*1023)/RLmax);

  }

  @Override
  public void teleopPeriodic() {
    // drive
    DriveControl driveControl = MecanumDrive.drive(joystick.getX(Hand.kRight), joystick.getY(Hand.kRight), joystick.getX(Hand.kLeft));
    /*
    if(RLmax<rearLeft.getSelectedSensorVelocity()){
      RLmax = rearLeft.getSelectedSensorVelocity();
    }
    if(RRmax<rearRight.getSelectedSensorVelocity()){
      RRmax = rearRight.getSelectedSensorVelocity();
    }
    if(RLmax<frontLeft.getSelectedSensorVelocity()){
      FLmax = frontLeft.getSelectedSensorVelocity();
    }
    if(RLmax<frontRight.getSelectedSensorVelocity()){
      FRmax = frontRight.getSelectedSensorVelocity();
    }
    */
    if(joystick.getAButton()){
      frontLeft.set(ControlMode.PercentOutput, 1);
    rearLeft.set(ControlMode.PercentOutput, 1);
    frontRight.set(ControlMode.PercentOutput, 1);
    rearRight.set(ControlMode.PercentOutput, 1);
    }else{
      frontLeft.set(ControlMode.PercentOutput, 0);
    rearLeft.set(ControlMode.PercentOutput, 0);
    frontRight.set(ControlMode.PercentOutput, 0);
    rearRight.set(ControlMode.PercentOutput, 0);
    }


    


    frontLeft.set(ControlMode.PercentOutput, driveControl.getFrontLeft());
    rearLeft.set(ControlMode.PercentOutput, driveControl.getRearLeft());
    frontRight.set(ControlMode.PercentOutput, driveControl.getFrontRight());
    rearRight.set(ControlMode.PercentOutput, driveControl.getRearRight());
  }
}
