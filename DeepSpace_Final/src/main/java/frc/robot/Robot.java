//Program created by team 4682

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.AnalogInput;
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
  

  // joystick constants \\
  private static final int kAButton = 1;
  private static final int kBButton = 2;
  private static final int kXButton = 3;
  private static final int kYButton = 4;
  private static final int kLeftTopButton = 5;
  private static final int kRightTopButton = 6;
  private static final int kStartButton_ball = 7;
  private static final int kSelectButton_hatch = 8;
  private static final int kLeftJoytstickButton = 9;
  private static final int kRightJoystickButton = 10;

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

  // talon ports \\
  // TODO: configure these
  private static final int kHatchPnePort_drive = 4;
  private static final int kHatchElecPort_drive = 2;
  private static final int kBallPnePort_drive = 3;
  private static final int kBallElecPort_drive = 1;

  private static final int kElevatorMotorPort = 5;
  private static final int kDartPort = 6;
  private static final int kTeethPort = 7;

  private static final int kJawsSpoonPortElec = 8;
  private static final int kJawsSpoonPortPne = 9;

  // sensor ports
  // TODO: Verify these!
  private static final int kHatchRedPort_ir = 3;
  private static final int kHatchBluePort_ir = 2;
  private static final int kHatchYellowPort_ir = 1;

  private static final int kBallRedPort_ir = 4;
  private static final int kBallBluePort_ir = 6;
  private static final int kBallYelloPort_ir = 7;

  private static final int kHatchPort_us = 0;
  private static final int kBallPort_us = 5;

  // joystick ports
  // DRIVER AND CO-DRIVER TO CONFIGURE THESE
  private static final int kDriverPort = 0;
  private static final int kCoDriverPort = 1;

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
  private Jaws _bewareTheDeep;

  private DriverAssist _roboEyes;
  private Ultrasonic _hatchUltra;
  private Ultrasonic _ballUltra;

  private Compressor _comp;

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
  private boolean _abortBombs = false;

  @Override
  public void robotInit() {
    _mechDrive = new MecanumDrive(kHatchElecPort_drive, kHatchPnePort_drive, kBallElecPort_drive, kBallPnePort_drive);
    _mechDrive.init(); // initialize the PID
    
    _elevator = new Elevator(kElevatorMotorPort);
    _elevator.init();

    _bewareTheDeep = new Jaws(kTeethPort, kJawsSpoonPortElec, kJawsSpoonPortPne);

    _dart = new Demogorgon(kDartPort);
    _hank = new HatchAdams();

    _hatchUltra = new Ultrasonic(kHatchPort_us);
    _ballUltra = new Ultrasonic(kBallPort_us);
    _roboEyes = new DriverAssist(kHatchRedPort_ir, kHatchBluePort_ir, kHatchYellowPort_ir,
      kBallRedPort_ir, kBallBluePort_ir, kBallYelloPort_ir, _hatchUltra, _ballUltra);

    _comp = new Compressor();
    _comp.setClosedLoopControl(true);

    _joy_driver = new Joystick(kDriverPort);
    _joy_coDriver = new Joystick(kCoDriverPort);
    
    CameraServer.getInstance().startAutomaticCapture();
  }

  private void debug() {
    SmartDashboard.putString("MAIN LOGIC", "");
    SmartDashboard.putBoolean("isDriverAssistRunning?", _isDriverAssistRunning);
    SmartDashboard.putBoolean("autoOverrideEnbaled_driver?", _autoOverrideEnabled_driver);
    SmartDashboard.putBoolean("autoOverrideEnbaled_coDriver?", _autoOverrideEnabled_coDriver);

    SmartDashboard.putBoolean("elevatorBypass_hatch?", getElevatorBypass_hatch());
    SmartDashboard.putBoolean("elevatingToLowHight_hatch", _elevateToLowHeight_hatch);
    SmartDashboard.putBoolean("elevatingToMidHight_hatch", _elevateToMidHeight_hatch);
    SmartDashboard.putBoolean("elevatingToHighHight_hatch", _elevateToHighHeight_hatch);

    SmartDashboard.putBoolean("elevatorBypass_ball?", getElevatorBypass_ball());
    SmartDashboard.putBoolean("elevatingToLowHight_ball", _elevateToLowHeight_ball);
    SmartDashboard.putBoolean("elevatingToMidHight_ball", _elevateToMidHeight_ball);
    SmartDashboard.putBoolean("elevatingToHighHight_ball", _elevateToHighHeight_ball);

    // debug
    _roboEyes.debug();
    _elevator.debug();
  }

  @Override
  public void robotPeriodic() {
    debug();

    // DRIVER
    // test code for drive
    // y is negative, so negate if needed
    // ORIGINAL
     _mechDrive.drive(-_joy_driver.getRawAxis(kRightJoystickAxis_x), _joy_driver.getRawAxis(kLeftJoystickAxis_y),
         -_joy_driver.getRawAxis(kLeftJoystickAxis_x));

    // interrupt any autonomous function by hitting the two top buttons at the same time
    // this only works for driver autonomous features (driver assist, climber)
    if (_joy_driver.getRawButton(kLeftTopButton) && _joy_driver.getRawButton(kRightTopButton)) {
      _autoOverrideEnabled_driver = true;
    }
    // have a way to stop the bombs if necessary
    if (_joy_driver.getRawButtonPressed(kRightTopButton)) {
      _abortBombs = true;
    }

    // if driver hits one of the reverse button, then reverse the drive from
    // its current state
    if (_joy_driver.getRawButtonPressed(kStartButton_ball) || _joy_driver.getRawButtonPressed(kSelectButton_hatch)) {
        _mechDrive.reverse();
        Utils.setRumble(_joy_driver, kReverseDirectionRumble_driver);
    }

    //test code for JAWS!!!!
    // Lower the jaw
    if (_joy_driver.getPOV() == kDPad_down  || _joy_driver.getRawAxis(kRightTriggerAxis) >= kTriggerThreshold) {
      _bewareTheDeep.manualOpen();
      // lift the jaw
    } else if (_joy_driver.getPOV() == kDPad_up) {
      _bewareTheDeep.manualClose();
      // spit out
    } else if (_joy_driver.getPOV() == kDPad_left || _joy_driver.getRawAxis(kLeftTriggerAxis) >= kTriggerThreshold) {
      _bewareTheDeep.upChuck();
      // take in
    } else if (_joy_driver.getPOV() == kDPad_right) {
        _bewareTheDeep.intake();
    } else {
      _bewareTheDeep.zero();
    }

    // fire bombs
    if (_joy_driver.getRawButtonPressed(kAButton) && !_abortBombs) {
      _bewareTheDeep.fireBombs();
      // retract bombs
    } else if (_joy_driver.getRawButtonPressed(kBButton) && !_abortBombs) {
      _bewareTheDeep.retractBombs();
    }
  
    if (_abortBombs) {
      // ABORT ABORT ABORT
      _bewareTheDeep.abortBombs();
      _abortBombs = false;
    }
  
    // test code for driver assist
    // if we sense a line. tell the driver
    SmartDashboard.putBoolean("ENABLE DRIVER ASSIST! DO IT!", false);
    if (_roboEyes.senseLine(_mechDrive.isReversed())) {
      //Utils.setRumble(_joy_driver, kDriverAssistRumble_driver);
      SmartDashboard.putBoolean("ENABLE DRIVER ASSIST! DO IT!", true);
    }

    // if driver enables driver assist line follow or if we are still running, then
    // keep following the lines
    // X button
    if (_joy_driver.getRawButton(kXButton) || _isDriverAssistRunning || _autoOverrideEnabled_driver) {
      _isDriverAssistRunning = true;
      if (_roboEyes.followLine(_mechDrive) || _autoOverrideEnabled_driver) {
        _isDriverAssistRunning = false;
        _autoOverrideEnabled_driver = false;
        Utils.setRumble(_joy_driver, kDriverAssistRumble_driver);
        Utils.setRumble(_joy_coDriver, kDriverAssistRumble_coDriver);
      }
    }

    // END OF DRIVER CONTROLS

    // CO-DRIVER
    // manual override for elevator
    // right joystick y value
    // y is negative, so negate
    _elevator.move(-_joy_coDriver.getRawAxis(kRightJoystickAxis_y));

    // co-driver can help drive the teeth when climbing
    //_bewareTheDeep.drive(-_joy_coDriver.getRawAxis(kRightJoystickAxis_y));

    // push left joystick to reset encoder
    if (_joy_coDriver.getRawButtonPressed(kLeftJoytstickButton)) {
      _elevator.resetEncoder();
    }

    // interrupt any autonomous function by hitting the two top buttons at the same time
    // this only works for co-driver autonomous features (elevator height)
    if (_joy_coDriver.getRawButton(kLeftTopButton) && _joy_coDriver.getRawButton(kRightTopButton)) {
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

        // right Joystick button
        if(_joy_coDriver.getRawButtonPressed(kRightJoystickButton)) {
          _elevator.reset();
        }
        // Left Joystikc button
        if(_joy_coDriver.getRawButtonPressed(kLeftJoytstickButton)) {
          _elevator.resetEncoder();
        }
        // if we are doing nothing, make sure it's off
      } else {
        _elevator.turnOff();
      }
    }

    // test code for dart (ball deploy)
    // if left D-Pad is pressed and held --
    // suck ball until button is lifted
    if (_joy_coDriver.getPOV() == kDPad_left) {
      _dart.suck();

      // if right D-Pad is pressed and held --
      // make sure it's lifted
      // deploy ball
    } else if (_joy_coDriver.getPOV() == kDPad_right) {
      _dart.push();

      // if we are doing nothing with the D-Pad, make sure dart is turned off
    } else {
      _dart.turnOff();
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
    }

    if (_joy_coDriver.getPOV() == kDPad_up) {
      _hank.expand();
    } else if (_joy_coDriver.getPOV() == kDPad_down) {
      _hank.contract();
    } else {
      _hank.turnOffLickATongue();
    }

    // X Button -- Deploy hatch
    if (_joy_coDriver.getRawButton(kXButton)) {
      _hank.deployHatch();
    } else {
      _hank.turnOffHank();
    }
  }

  private boolean getElevatorBypass_hatch() {
    return _elevateToLowHeight_hatch || _elevateToMidHeight_hatch || _elevateToHighHeight_hatch;
  }

  private boolean getElevatorBypass_ball() {
    return _elevateToLowHeight_ball || _elevateToMidHeight_ball || _elevateToHighHeight_ball;
  }

}
