/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */

  // joystick constants
  private static final int kAButton = 1;
  private static final int kBButton = 2;
  private static final int kXButton = 3;
  private static final int kYButton = 4;
  private static final int kLeftTopButton = 5;
  private static final int kRightTopButton = 6;
  private static final int kStartButton_ball = 7;
  private static final int kSelectButton_hatch = 8;

  private static final int kLeftJoystickAxis_x = 0;
  private static final int kLeftJoystickAxis_y = 1;
  private static final int kLeftTriggerAxis = 2;
  private static final int kRightTriggerAxis = 3;
  private static final int kRightJoystickAxis_x = 4;
  private static final int kRightJoystickAxis_y = 5;
  private static final double kTriggerThreshold = 0.3;

  private static final int kDPad_up = 0;
  private static final int kDPad_left = 90;
  private static final int kDPad_down = 180;
  private static final int kDPad_right = 270;

  // talon ports
  // TODO: configure these
  private static final int kHatchPnePort_drive = 1;
  private static final int kHatchElecPort_drive = 2;
  private static final int kBallPnePort_drive = 3;
  private static final int kBallElecPort_drive = 4;

  private static final int kTalonPort1_demo = 5;
  private static final int kTalonPort2_demo = 6;
  private static final int kTalonPort3_demo = 7;

  private static final int kElevatorMotorPort = 12;

  // sensor ports
  // TODO: Verify these!
  private static final int kHatchRedPort_ir = 0;
  private static final int kHatchBluePort_ir = 1;
  private static final int kHatchYelloPort_ir = 2;
  private static final int kBallRedPort_ir = 3;
  private static final int kBallBluePort_ir = 6;
  private static final int kBallYelloPort_ir = 7;

  private static final int kHatchPort_us = 4;
  private static final int kBallPort_us = 5;

  // joystick ports
  // DRIVER AND CO-DRIVER TO CONFIGURE THESE
  private static final int kDriverPort = 0;
  private static final int kCoDriverPort = 1;

  // values
  private static final double kDoubleTapThreshold = 2;

  // rumbles
  private static final RumbleType kDriverAssistRumble_driver = RumbleType.kRightRumble;
  private static final RumbleType kDriverAssistRumble_coDriver = RumbleType.kRightRumble;
  private static final RumbleType kReverseDirectionRumble_driver = RumbleType.kLeftRumble;
  private static final RumbleType kElevatorIsDoneRumble_coDriver = RumbleType.kLeftRumble;

  // members
  private MecanumDrive _mechDrive;
  private Elevator _elevator;
  private Demogorgon _dart;
  private HatchAdams _hank;
  private DriverAssist _lineFollow;
  private Ultrasonic _hatchUltra;
  private Ultrasonic _ballUltra;
  private Compressor _comp;

  private Timer _timer;

  private Joystick _joy_driver;
  private Joystick _joy_coDriver;

  // state
  private boolean _elevateToLowHeight_hatch = false;
  private boolean _elevateToMidHeight_hatch = false;
  private boolean _elevateToHighHeight_hatch = false;
  private boolean _elevateToLowHeight_ball = false;
  private boolean _elevateToMidHeight_ball = false;
  private boolean _elevateToHighHeight_ball = false;

  private boolean _isDriverAssistRunning = false;
  private boolean _autoOverrideEnabled_driver = false;
  private boolean _autoOverrideEnabled_coDriver = false;

  private int _hatchDirectionCount = 0;
  private int _ballDirectionCount = 0;

  @Override
  public void robotInit() {
    _mechDrive = new MecanumDrive(kHatchElecPort_drive, kHatchPnePort_drive, kBallElecPort_drive, kBallPnePort_drive);
   _mechDrive.init(); // initialize the PID
    
    _elevator = new Elevator(kElevatorMotorPort);
    _elevator.init();
    
    _dart = new Demogorgon(kTalonPort1_demo, kTalonPort2_demo, kTalonPort3_demo);
    _hank = new HatchAdams();
    _hatchUltra = new Ultrasonic(kHatchPort_us);
    _ballUltra = new Ultrasonic(kBallPort_us);
    _lineFollow = new DriverAssist(kHatchRedPort_ir, kHatchBluePort_ir, kHatchYelloPort_ir, kBallRedPort_ir,
        kBallBluePort_ir, kBallYelloPort_ir, _hatchUltra, _ballUltra);
    _comp = new Compressor();

    _joy_driver = new Joystick(kDriverPort);
    _joy_coDriver = new Joystick(kCoDriverPort);

    _timer = new Timer();

    // initialize camera
    Camera.init();
  }

  @Override
  public void robotPeriodic() {
    SmartDashboard.putString("MAIN LOGIC", "");
    SmartDashboard.putBoolean("isDriverAssistRunning?", _isDriverAssistRunning);
    SmartDashboard.putBoolean("autoOverrideEnbaled_driver?", _autoOverrideEnabled_driver);
    SmartDashboard.putBoolean("autoOverrideEnbaled_coDriver?", _autoOverrideEnabled_coDriver);

    SmartDashboard.putNumber("Ball Button Press Count", _ballDirectionCount);
    SmartDashboard.putNumber("Hatch Button Press Count", _hatchDirectionCount);

    SmartDashboard.putBoolean("elevatorBypass_hatch?", getElevatorBypass_hatch());
    SmartDashboard.putBoolean("elevatingToLowHight_hatch", _elevateToLowHeight_hatch);
    SmartDashboard.putBoolean("elevatingToMidHight_hatch", _elevateToMidHeight_hatch);
    SmartDashboard.putBoolean("elevatingToHighHight_hatch", _elevateToHighHeight_hatch);

    SmartDashboard.putBoolean("elevatorBypass_ball?", getElevatorBypass_ball());
    SmartDashboard.putBoolean("elevatingToLowHight_ball", _elevateToLowHeight_ball);
    SmartDashboard.putBoolean("elevatingToMidHight_ball", _elevateToMidHeight_ball);
    SmartDashboard.putBoolean("elevatingToHighHight_ball", _elevateToHighHeight_ball);

    _mechDrive.debug();
    _lineFollow.debug();
    _dart.debug();
    _elevator.debug();
  }

  @Override
  public void teleopInit() {
    _comp.setClosedLoopControl(true);
  }

  @Override
  public void teleopPeriodic() {
    // DRIVER
    // test code for drive
    // y is negative, so negate if needed
    _mechDrive.driveDisabledPID(_joy_driver.getRawAxis(kRightJoystickAxis_x), -_joy_driver.getRawAxis(kLeftJoystickAxis_y),
        _joy_driver.getRawAxis(kLeftJoystickAxis_x));

    // interrupt any autonomous function by hitting the two top buttons at the same
    // time
    // this only works for driver autonomous features (driver assist, climber)
    if (_joy_driver.getRawButtonPressed(kLeftTopButton) && _joy_driver.getRawButtonPressed(kRightTopButton)) {
      _autoOverrideEnabled_driver = true;
    }

    // if driver hits one of the reverse buttons twice, then reverse the drive from
    // it's current state
    if (_joy_driver.getRawButtonPressed(kStartButton_ball)) {
      _hatchDirectionCount = 0;
      _ballDirectionCount++;
      if (_ballDirectionCount == kDoubleTapThreshold) {
        _mechDrive.reverse();
        Utils.setRumble(_joy_driver, kReverseDirectionRumble_driver);
        _ballDirectionCount = 0;
      }
    } else if (_joy_driver.getRawButtonPressed(kSelectButton_hatch)) {
      _ballDirectionCount = 0;
      _hatchDirectionCount++;
      if (_hatchDirectionCount == kDoubleTapThreshold) {
        _mechDrive.reverse();
        Utils.setRumble(_joy_driver, kReverseDirectionRumble_driver);
        _hatchDirectionCount = 0;
      }
    }

    // test code for driver assist
    // if we sense a line. tell the driver
    if (_lineFollow.senseLine(_mechDrive.isReversed())) {
      Utils.setRumble(_joy_driver, kDriverAssistRumble_driver);
    }

    // if driver enables driver assist line follow or if we are still running, then
    // keep following the lines
    if (_joy_driver.getRawButton(kXButton) || _isDriverAssistRunning) {
      _isDriverAssistRunning = true;
      if (_lineFollow.followLine(_mechDrive) || _autoOverrideEnabled_driver) {
        _isDriverAssistRunning = false;
        _autoOverrideEnabled_driver = false;
        Utils.setRumble(_joy_driver, kDriverAssistRumble_driver);
        Utils.setRumble(_joy_coDriver, kDriverAssistRumble_coDriver);
      }
    }

    // CO-DRIVER

    // manual override for elevator
    // right joystick y value
    // y is negative, so negate
    _elevator.move(-_joy_coDriver.getRawAxis(kRightJoystickAxis_y));

    // interrupt any autonomous function by hitting the two top buttons at the same
    // time
    // this only works for co-driver autonomous features (elevator height)
    if (_joy_coDriver.getRawButtonPressed(kLeftTopButton) && _joy_coDriver.getRawButtonPressed(kRightTopButton)) {
      _autoOverrideEnabled_coDriver = true;
    }

    // test code for elevator
    // BALL - Auto Elevate
    // left trigger means you are doing something for balls
    // can't enable if elevator is already moving to a hatch level
    if ((_joy_coDriver.getRawAxis(kLeftTriggerAxis) >= kTriggerThreshold || getElevatorBypass_ball()) && !getElevatorBypass_hatch()) {
      // A Button -- ball low height
      if ((_joy_coDriver.getRawButtonPressed(kAButton) || _elevateToLowHeight_ball) && !_elevateToMidHeight_ball && !_elevateToHighHeight_ball) {
        _elevateToLowHeight_ball = true;
        if (_elevator.setBallLowHeight() || _autoOverrideEnabled_coDriver) {
          _elevateToLowHeight_ball = false;
          _autoOverrideEnabled_coDriver = false;
          Utils.setRumble(_joy_coDriver, kElevatorIsDoneRumble_coDriver);
        }

        // B Button -- ball mid height
      } else if ((_joy_coDriver.getRawButtonPressed(kBButton) || _elevateToMidHeight_ball) && !_elevateToHighHeight_ball) {
        _elevateToMidHeight_ball = true;
        if (_elevator.setBallMidHeight() || _autoOverrideEnabled_coDriver) {
          _elevateToMidHeight_ball = false;
          _autoOverrideEnabled_coDriver = false;
          Utils.setRumble(_joy_coDriver, kElevatorIsDoneRumble_coDriver);
        }

        // Y Button -- ball high height
      } else if (_joy_coDriver.getRawButtonPressed(kYButton) || _elevateToHighHeight_ball) {
        _elevateToHighHeight_ball = true;
        if (_elevator.setBallHighHeight() || _autoOverrideEnabled_coDriver) {
          _elevateToHighHeight_ball = false;
          _autoOverrideEnabled_coDriver = false;
          Utils.setRumble(_joy_coDriver, kElevatorIsDoneRumble_coDriver);
        }

        // if we are doing nothing, make sure it's off
      } else {
        _elevator.turnOff();
      }
    }

    // test code for dart (ball deploy)
    // if left D-Pad is pressed and held --
    // make sure it's lowered
    // suck ball until limit it hit or button is lifted
    if (_joy_coDriver.getPOV() == kDPad_left) {
      if (!_timer.isRunning()) {
        _timer.start();
      }
      _dart.suckBall(_timer.get());

      // if right D-Pad is pressed and held --
      // make sure it's lifted
      // deploy ball
    } else if (_joy_coDriver.getPOV() == kDPad_right) {
      if (!_timer.isRunning()) {
        _timer.start();
      }
      _dart.deployBall(_timer.get());

      // if we are doing nothing with the D-Pad, make sure dart is turned off
    } else {
      _dart.turnOff();
      _timer.reset();
    }

    // HATCH
    // right trigger means you are doing something for hatch
    // can't enable if a ball level is already being set
    if ((_joy_coDriver.getRawAxis(kRightTriggerAxis) >= kTriggerThreshold || getElevatorBypass_hatch()) && !getElevatorBypass_ball()) {
      // A button -- hatch low height
      // can't enable if the mid or high is enabled
      if ((_joy_coDriver.getRawButtonPressed(kAButton) || _elevateToLowHeight_hatch) && !_elevateToMidHeight_hatch && !_elevateToHighHeight_hatch) {
        _elevateToLowHeight_hatch = true;
        if (_elevator.setHatchLowHeight() || _autoOverrideEnabled_coDriver) {
          _elevateToLowHeight_hatch = false;
          _autoOverrideEnabled_coDriver = false;
          Utils.setRumble(_joy_coDriver, kElevatorIsDoneRumble_coDriver);
        }

        // B Button -- hatch mid height
      } else if ((_joy_coDriver.getRawButtonPressed(kBButton) || _elevateToMidHeight_hatch) && !_elevateToHighHeight_hatch) {
        _elevateToMidHeight_hatch = true;
        if (_elevator.setHatchMidHeight() || _autoOverrideEnabled_coDriver) {
          _elevateToMidHeight_hatch = false;
          _autoOverrideEnabled_coDriver = false;
          Utils.setRumble(_joy_coDriver, kElevatorIsDoneRumble_coDriver);
        }

        // Y Button -- hatch heigh height
      } else if (_joy_coDriver.getRawButtonPressed(kYButton) || _elevateToHighHeight_hatch) {
        _elevateToHighHeight_hatch = true;
        if (_elevator.setHatchHighHeight() || _autoOverrideEnabled_coDriver) {
          _elevateToHighHeight_hatch = false;
          _autoOverrideEnabled_coDriver = false;
          Utils.setRumble(_joy_coDriver, kElevatorIsDoneRumble_coDriver);
        }

        // if we are doing nothing, make sure it's off
      } else {
        _elevator.turnOff();
      }

      // X Button -- Deploy hatch
      if (_joy_coDriver.getRawButtonPressed(kXButton)) {
        _hank.deployHatch();
      } else {
        _hank.turnOff();
      }
    }

  }

  private boolean getElevatorBypass_hatch() {
    return _elevateToLowHeight_hatch || _elevateToMidHeight_hatch || _elevateToHighHeight_hatch;
  }

  private boolean getElevatorBypass_ball() {
    return _elevateToLowHeight_ball || _elevateToMidHeight_ball || _elevateToHighHeight_ball;
  }

}
