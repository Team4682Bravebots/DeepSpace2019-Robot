package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Jaws{
    private TalonSRX Teeth;
    private TalonSRX JawElec;
    private TalonSRX JawPne;

    private DoubleSolenoid electricSide;
    private DoubleSolenoid pneumaticSide;

    private static final double TeethSpeed = 1.0;
    private static final double manulMulti = 1.0;

    // PID
    private static final int kTimeout_ms = 30;
    private static final int kAllowedError = 0;
    private static final int kPidIdx = 0;
    private static final boolean kPhase = true;
    private static final boolean kIsInverted = true;
    private static final double kRotMulti = (4096.0 * 3.4 * 3.4 * 3.4 * 20);
    private static final double inchesPerRotation = 10;

    private static final double rotateToIntake = 10;
    private static final double rotateToVerticle = 0;


    private static final int kElectricBombForwardPort = 1;
    private static final int kElectricBombReversePort = 0;
    private static final int kPneumaticBombForwardPort = 5;
    private static final int kPneumaticBombReversePort = 7;

    public Jaws(int teeth, int jawElec, int jawPne) {
        Teeth = new TalonSRX(teeth);
        JawElec = new TalonSRX(jawElec);
        JawPne = new TalonSRX(jawPne);

        electricSide = new DoubleSolenoid(kElectricBombForwardPort, kElectricBombReversePort);
        pneumaticSide = new DoubleSolenoid(kPneumaticBombForwardPort, kPneumaticBombReversePort);
    }

    public void intake() {
        Teeth.set(ControlMode.PercentOutput, TeethSpeed);
    }

    public void upChuck() {
        Teeth.set(ControlMode.PercentOutput, -TeethSpeed);
    }

    public void drive(double val) {
        val = Utils.applyDeadband(Utils.limit(val));

        Teeth.set(ControlMode.PercentOutput, val);
    }

    public void manualOpen() {
        JawElec.set(ControlMode.PercentOutput, -manulMulti);
        JawPne.set(ControlMode.PercentOutput, -manulMulti);
    }

    public void manualClose() {
        JawElec.set(ControlMode.PercentOutput, manulMulti);
        JawPne.set(ControlMode.PercentOutput, manulMulti);
    }

    public void chew() {
        JawElec.set(ControlMode.Position, kRotMulti*rotateToVerticle);
        JawPne.set(ControlMode.Position, kRotMulti*rotateToVerticle);
    }

    public void openWide() {
        JawElec.set(ControlMode.Position, kRotMulti*rotateToIntake);
        JawPne.set(ControlMode.Position, kRotMulti*rotateToIntake);
    }

    public void zero() {
        turnOffJaw();
        turnOffTeeth();
    }

    public void turnOffJaw() {
        JawElec.set(ControlMode.PercentOutput, 0);
        JawPne.set(ControlMode.PercentOutput, 0);
    }

    public void turnOffTeeth() {
        Teeth.set(ControlMode.PercentOutput, 0);
    }

    // step 1: Lift bar to higher than climbing height
    // step 2: Drive forward
    // step 3: Lower bar
    // step 4: Once the arm starts engaging, then fire pistons
    //  --- May need to abort pistons and go back up
    // step 5: once we are in downward dog, then turn on the teeth

    public void fireBombs() {
        electricSide.set(Value.kForward);
        pneumaticSide.set(Value.kForward);
    }

    public void retractBombs() {
        electricSide.set(Value.kReverse);
        pneumaticSide.set(Value.kReverse);
    }

    public void abortBombs() {
        electricSide.set(Value.kOff);
        pneumaticSide.set(Value.kOff);
    }
}