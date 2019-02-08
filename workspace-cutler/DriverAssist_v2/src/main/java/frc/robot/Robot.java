/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.AnalogInput;
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
  private AnalogInput irLeft_0;
  private AnalogInput irRight_1;
  private AnalogInput irFront_2;
  private final double kIR_Black = 3000;
  private final double kIR_White = 1000;
  TalonSRX _talon1;
  TalonSRX _talon2;
  TalonSRX _talon3;
  TalonSRX _talon4;
  Joystick _joystick;
  AnalogInput ultraSonic;
  
  

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    irLeft_0 = new AnalogInput(0);
    irRight_1 = new AnalogInput(1);
    irFront_2 = new AnalogInput(2);
    _talon1 = new TalonSRX(1);
    _talon2 = new TalonSRX(2);
    _talon3 = new TalonSRX(3);
    _talon4 = new TalonSRX(4);
    _joystick = new Joystick(0);
    ultraSonic = new AnalogInput(3);
    
    _talon1.setInverted(true);
    _talon3.setInverted(true);
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
    SmartDashboard.putNumber("left",irLeft_0.getValue());
    SmartDashboard.putNumber("right",irRight_1.getValue());
    SmartDashboard.putNumber("front",irFront_2.getValue());
  
    boolean leftWhite = irLeft_0.getValue() <= kIR_White;
    boolean rightWhite = irRight_1.getValue() <= kIR_White;
    boolean frontWhite = irFront_2.getValue() <= kIR_White;

    SmartDashboard.putBoolean("left sees white?", leftWhite);
    SmartDashboard.putBoolean("right sees white?", rightWhite);
    SmartDashboard.putBoolean("front sees white?", frontWhite);

    SmartDashboard.putBoolean("black??", false);

    // if all three are detected then tell smart dashboard values and move motor 
    // if (irLeft_0.getValue() >= kIR_Black && irRight_1.getValue() >= kIR_Black && irFront_2.getValue() >= kIR_Black) {
    //   SmartDashboard.putBoolean("black??", true);
      
    //   if (_joystick.getRawButton(1)) { // A button
    //     _talon1.set(ControlMode.PercentOutput, 1);
    //     _talon2.set(ControlMode.PercentOutput, 1);
    //     _talon3.set(ControlMode.PercentOutput, 1);
    //     _talon4.set(ControlMode.PercentOutput, 1);
    //   }

    //   if (_joystick.getRawButton(2)) { // B Button
    //     _talon1.set(ControlMode.PercentOutput, 0);
    //     _talon2.set(ControlMode.PercentOutput, 0);
    //     _talon3.set(ControlMode.PercentOutput, 0);
    //     _talon4.set(ControlMode.PercentOutput, 0);
    //   }

    //   if (_joystick.getRawButton(9)){ // Left stick
    //     _talon1.set(ControlMode.PercentOutput, 1);
    //     _talon3.set(ControlMode.PercentOutput, 1);
    //     _talon2.set(ControlMode.PercentOutput, -1);
    //     _talon4.set(ControlMode.PercentOutput, -1);
    //   }

      
    // }
    double basePower = 1.0;
    double distance_ultra = ultraSonic.getVoltage() / 0.0098;
    SmartDashboard.putNumber("ULTRA IN", distance_ultra);
    DriveControl driver  = new DriveControl(0, 0, 0, 0);

    SmartDashboard.putNumber("talon1_RR:", driver.getRearRight());
    SmartDashboard.putNumber("talon2_RL:", driver.getRearLeft());
    SmartDashboard.putNumber("talon3_FL:", driver.getFrontLeft());
    SmartDashboard.putNumber("talon4_FR:", driver.getFrontRight());

    if(_joystick.getRawButton(4) == true){//y
      // drive forward slowly untill encounters line
      if(leftWhite == false && rightWhite == false){
         driver = MecanumDrive.drive(0,-basePower,0);

      }else if(leftWhite == false && rightWhite == true){
        driver = MecanumDrive.drive(0, 0, basePower+.1);
        // Look Right
      }else if (leftWhite == true && rightWhite == false){
        // look left
        driver = MecanumDrive.drive(0,0,-basePower+.1);

      }else if(leftWhite == true && rightWhite == true&& frontWhite ==true && irFront_2.getValue() <= kIR_White){
        if(distance_ultra>=7.5){
          driver = MecanumDrive.drive(0, -basePower, 0);
        } else {
          driver = MecanumDrive.drive(0, 0, 0);
        }
      }

      SmartDashboard.putNumber("talon1_RR:", driver.getRearRight());
      SmartDashboard.putNumber("talon1_RL:", driver.getRearLeft());
      SmartDashboard.putNumber("talon1_FL:", driver.getFrontLeft());
      SmartDashboard.putNumber("talon1_FR:", driver.getFrontRight());

      _talon1.set(ControlMode.PercentOutput, driver.getRearRight());
      _talon2.set(ControlMode.PercentOutput, driver.getRearLeft());
      _talon3.set(ControlMode.PercentOutput, driver.getFrontLeft());
      _talon4.set(ControlMode.PercentOutput, driver.getFrontRight());
  } else {
    _talon1.set(ControlMode.PercentOutput, 0);
    _talon2.set(ControlMode.PercentOutput, 0);
    _talon3.set(ControlMode.PercentOutput, 0);
    _talon4.set(ControlMode.PercentOutput, 0);
  }

    
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
  public void teleopPeriodic() {
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
