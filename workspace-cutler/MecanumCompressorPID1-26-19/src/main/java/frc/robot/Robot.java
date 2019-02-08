/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  // HARDWARE \\
  TalonSRX talonRR_1 = new TalonSRX(1); // Rear Right
  TalonSRX talonRL_2 = new TalonSRX(2); // Rear Left
  TalonSRX talonFL_3 = new TalonSRX(3); // Front Left
  TalonSRX talonFR_4 = new TalonSRX(4); // Front Right
  Joystick logJoy = new Joystick(0);

  // output smart dash values \\ 

  // loop tracker for smart dash prints \\ 

  // Compressor & Soloinoid values \\ 
  Compressor comp_0 = new Compressor(0);
  Solenoid sol_0 = new Solenoid(0);
  Solenoid sol_1 = new Solenoid(1);

  // Global variables \\ 
  double multi = 1;
  double joyLHY;
  double joyLHX;
  double joyRHX;
  double joyRHY;
  double motorOutput1;
  double motorOutput2;
  double motorOutput3;
  double motorOutput4;
  DriveControl controller;

  // Constants \\
  // private int kAButtonIndex =
  // private int k

  /*
  \\-------------------------//
  */

  
  @Override
  public void robotInit() {
    // Init compressor \\
    comp_0.setClosedLoopControl(true);

    // Factory Default all hardware to prevent unexpected behaviour \\
    talonRR_1.configFactoryDefault();
    talonRL_2.configFactoryDefault();
    talonFL_3.configFactoryDefault();
    talonFR_4.configFactoryDefault();

    // Invert oppsites for mecanum drive \\
    talonRR_1.setInverted(true);
    talonFL_3.setInverted(true);

    // Phase Sensors \\ 
    talonRR_1.setSensorPhase(true);
    //talonRL_2.setSensorPhase(true);
    talonFL_3.setSensorPhase(true);
    //talonFR_4.setSensorPhase(true);

    // Config Sensors used for wheel pid \\ 
    // Talon 1 \\ 
    talonRR_1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
    Constants.kPIDLoopIdx, 
    Constants.kTimeoutMs);
    //Talon 2 \\
    talonRL_2.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
    Constants.kPIDLoopIdx, 
    Constants.kTimeoutMs);
    //Talon 3 \\ 
    talonFL_3.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
    Constants.kPIDLoopIdx, 
    Constants.kTimeoutMs);
    //Talon 4 \\ 
    talonFR_4.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
    Constants.kPIDLoopIdx, 
    Constants.kTimeoutMs);

    // Configure peak and nominal outputs \\
    // Talon 1 \\
    talonRR_1.configNominalOutputForward(0, Constants.kTimeoutMs);
    talonRR_1.configNominalOutputReverse(0, Constants.kTimeoutMs);
    talonRR_1.configPeakOutputForward(1, Constants.kTimeoutMs);
    talonRR_1.configPeakOutputReverse(-1, Constants.kTimeoutMs);
    // Talon 2 \\
    talonRL_2.configNominalOutputForward(0, Constants.kTimeoutMs);
    talonRL_2.configNominalOutputReverse(0, Constants.kTimeoutMs);
    talonRL_2.configPeakOutputForward(1, Constants.kTimeoutMs);
    talonRL_2.configPeakOutputReverse(-1, Constants.kTimeoutMs);
    // Talon 3 \\
    talonFL_3.configNominalOutputForward(0, Constants.kTimeoutMs);
    talonFL_3.configNominalOutputReverse(0, Constants.kTimeoutMs);
    talonFL_3.configPeakOutputForward(1, Constants.kTimeoutMs);
    talonFL_3.configPeakOutputReverse(-1, Constants.kTimeoutMs);
    // Talon 4 \\
    talonFR_4.configNominalOutputForward(0, Constants.kTimeoutMs);
    talonFR_4.configNominalOutputReverse(0, Constants.kTimeoutMs);
    talonFR_4.configPeakOutputForward(1, Constants.kTimeoutMs);
    talonFR_4.configPeakOutputReverse(-1, Constants.kTimeoutMs);
  
  // Config the Velocity closed loop gains in slot0 \\
    // Talon 1 \\ RR \\ 5666
    talonRR_1.config_kF(Constants.kPIDLoopIdx, -0.181, Constants.kTimeoutMs);
    talonRR_1.config_kP(Constants.kPIDLoopIdx, -0.22, Constants.kTimeoutMs);
    talonRR_1.config_kI(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kI, Constants.kTimeoutMs);
    talonRR_1.config_kD(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kD, Constants.kTimeoutMs);
    // Talon 2 \\ RL \\ 6000
    talonRL_2.config_kF(Constants.kPIDLoopIdx, -0.1705, Constants.kTimeoutMs);
    talonRL_2.config_kP(Constants.kPIDLoopIdx, -0.11, Constants.kTimeoutMs);
    talonRL_2.config_kI(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kI, Constants.kTimeoutMs);
    talonRL_2.config_kD(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kD, Constants.kTimeoutMs);
    // Talon 3 \\ FL \\ 5984
    talonFL_3.config_kF(Constants.kPIDLoopIdx, -0.171, Constants.kTimeoutMs);
    talonFL_3.config_kP(Constants.kPIDLoopIdx, -0.22, Constants.kTimeoutMs);
    talonFL_3.config_kI(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kI, Constants.kTimeoutMs);
    talonFL_3.config_kD(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kD, Constants.kTimeoutMs);
    // Talon 4 \\ FR \\ 6055
    talonFR_4.config_kF(Constants.kPIDLoopIdx, -0.168, Constants.kTimeoutMs);
    talonFR_4.config_kP(Constants.kPIDLoopIdx, -0.17, Constants.kTimeoutMs);
    talonFR_4.config_kI(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kI, Constants.kTimeoutMs);
    talonFR_4.config_kD(Constants.kPIDLoopIdx, Constants.kGains_Velocit.kD, Constants.kTimeoutMs);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    
  }

  
  @Override
  public void teleopInit() {
    super.teleopInit();
    
  }


  @Override
  public void teleopPeriodic() {

    // SmartDashboard for encoder positions \\ 
    SmartDashboard.putNumber("ENC OUT, Rear Right", talonRR_1.getSelectedSensorPosition());
    SmartDashboard.putNumber("ENC OUT, Rear Left", talonRL_2.getSelectedSensorPosition());
    SmartDashboard.putNumber("ENC OUT, Front Left", talonFL_3.getSelectedSensorPosition());
    SmartDashboard.putNumber("ENC OUT, Front Right", talonFR_4.getSelectedSensorPosition());
    
    // Get Axis for Logitech gamepad \\ 
    joyLHX = logJoy.getRawAxis(0);
    joyRHX = logJoy.getRawAxis(4);
    joyLHY = logJoy.getRawAxis(1);
    joyRHY = logJoy.getRawAxis(5);

    // get Talon's Current output percentage \\
    motorOutput1 = talonRR_1.getMotorOutputPercent();
    motorOutput2 = talonRL_2.getMotorOutputPercent();
    motorOutput3 = talonFL_3.getMotorOutputPercent();
    motorOutput4 = talonFR_4.getMotorOutputPercent();


    // SmartDashboard for pid loops \\ 
    SmartDashboard.putNumber("\tout1:%", motorOutput1*100);
    SmartDashboard.putNumber("\tout2:%", motorOutput2*100);
    SmartDashboard.putNumber("\tout3:%", motorOutput3*100);
    SmartDashboard.putNumber("\tout4:%", motorOutput4*100);

    SmartDashboard.putNumber("\tspd1:u",(talonRR_1.getSelectedSensorVelocity(Constants.kPIDLoopIdx) ));
    SmartDashboard.putNumber("\tspd2:u",(talonRL_2.getSelectedSensorVelocity(Constants.kPIDLoopIdx) ));
    SmartDashboard.putNumber("\tspd3:u",(talonFL_3.getSelectedSensorVelocity(Constants.kPIDLoopIdx) ));
    SmartDashboard.putNumber("\tspd4:u",(talonFR_4.getSelectedSensorVelocity(Constants.kPIDLoopIdx) ));
   
   
    if(logJoy.getRawButton(2)){
      sol_0.set(true);
    }else if(logJoy.getRawButton(1)){
      sol_1.set(true);

    }else if(logJoy.getRawButton(3)){
      /**
			 * Convert 500 RPM to units / 100ms.
			 * 4096 Units/Rev * 500 RPM / 600 100ms/min in either direction:
			 * velocity setpoint is in units/100ms
			 */
      double targetVelocity_UnitsPer100ms = ((joyLHY + joyLHX) * 500.0 * 4096 / 600);
      double leftFrontVel = (joyLHY + joyLHX + joyRHX)* 500.0 * 4096 / 600;
      double leftRearVel = (-joyLHY + joyLHX - joyRHX)* 500.0 * 4096 / 600;
      double rightFrontVel = (joyLHY - joyLHX - joyRHX)* 500.0 * 4096 / 600;
      double rightRearVel = (-joyLHY - joyLHX + joyRHX)* 500.0 * 4096 / 600;
      // 500 rpm in either direction \\
      talonRR_1.set(ControlMode.Velocity, rightRearVel);
      talonRL_2.set(ControlMode.Velocity, leftRearVel);
      talonFL_3.set(ControlMode.Velocity, leftFrontVel);
      talonFR_4.set(ControlMode.Velocity, rightFrontVel);

      // Dispaly more signal to print \\
      SmartDashboard.putNumber("\terr1:", talonRR_1.getClosedLoopError(Constants.kPIDLoopIdx));
      SmartDashboard.putNumber("\terr2:", talonRL_2.getClosedLoopError(Constants.kPIDLoopIdx));
      SmartDashboard.putNumber("\terr3:", talonFL_3.getClosedLoopError(Constants.kPIDLoopIdx));
      SmartDashboard.putNumber("\terr4:", talonFR_4.getClosedLoopError(Constants.kPIDLoopIdx));



    }else{
      sol_0.set(false);
      sol_1.set(false);
      controller = MecanumDrive.drive(joyLHX, joyLHY, joyRHX);
      talonRR_1.set(ControlMode.PercentOutput, controller.getRearRight());
      talonRL_2.set(ControlMode.PercentOutput, controller.getRearLeft());
      talonFL_3.set(ControlMode.PercentOutput, controller.getFrontLeft());
      talonFR_4.set(ControlMode.PercentOutput, controller.getFrontRight());
    }

    

    







  }

  
  @Override
  public void testPeriodic() {
  }
}
