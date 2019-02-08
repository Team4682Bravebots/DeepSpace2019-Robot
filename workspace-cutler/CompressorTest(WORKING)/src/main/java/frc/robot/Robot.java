/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends IterativeRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  Compressor c = new Compressor(0);
  Joystick Logi = new Joystick(0);
  //DoubleSolenoid soli = new DoubleSolenoid(0, 1);
  Solenoid s1 = new Solenoid(0);
  Solenoid s2 = new Solenoid(1);

  double m = 1;

  TalonSRX RR = new TalonSRX(1);
  TalonSRX RL = new TalonSRX(2);
  TalonSRX FL = new TalonSRX(3);
  TalonSRX FR = new TalonSRX(4);
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
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
    m_autoSelected = m_chooser.getSelected();
    // autoSelected = SmartDashboard.getString("Auto Selector",
    // defaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */

   @Override
   public void teleopInit() {
     super.teleopInit();
     c.setClosedLoopControl(true);
     //soli.set(DoubleSolenoid.Value.kOff);

   }
  @Override
  public void teleopPeriodic() {
    /*
  if(Logi.getRawButton(1))   {
    soli.set(DoubleSolenoid.Value.kForward);
  }else if(Logi.getRawButton(1)== false){
    soli.set(DoubleSolenoid.Value.kReverse);
    soli.set(DoubleSolenoid.Value.kOff);
  }
  
*/

if(Logi.getRawButton(2)){
  s1.set(true);
  
}else if(Logi.getRawButton(1)){
  s2.set(true);
}else{
  s1.set(false);
  s2.set(false);
  
}

FL.set(ControlMode.PercentOutput, Logi.getRawAxis(1)*m);
FR.set(ControlMode.PercentOutput, -Logi.getRawAxis(5)*m);
RL.set(ControlMode.PercentOutput, Logi.getRawAxis(1)*m);
RR.set(ControlMode.PercentOutput, -Logi.getRawAxis(5)*m);


  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
