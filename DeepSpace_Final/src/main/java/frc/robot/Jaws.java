package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Jaws{
    private TalonSRX Teeth;
    private TalonSRX JawElec;
    private TalonSRX JawPne;

    private DoubleSolenoid electricSide;
    private DoubleSolenoid pneumaticSide;

    private static final double TeethSpeed = 1.0;
    private static final double jawSpeedNormal = 1.0;
    private static final double jawSpeedEndGame = 0.4;

    private boolean _endGame = false;

    private static final int kElectricBombForwardPort = 1;
    private static final int kElectricBombReversePort = 0;

    // private static final int kPneumaticBombForwardPort = 5;
    // private static final int kPneumaticBombReversePort = 7;
    private static final int kPneumaticBombForwardPort = 4;
    private static final int kPneumaticBombReversePort = 6;

    // private static final int kLickATongueForward = 4;
    // private static final int kLickATongueReverse = 6;

    public Jaws(int teeth, int jawElec, int jawPne) {
        Teeth = new TalonSRX(teeth);
        JawElec = new TalonSRX(jawElec);
        JawPne = new TalonSRX(jawPne);

        electricSide = new DoubleSolenoid(kElectricBombForwardPort, kElectricBombReversePort);
        pneumaticSide = new DoubleSolenoid(kPneumaticBombForwardPort, kPneumaticBombReversePort);
    }

    public void setEndGame(boolean eg) {
        _endGame = eg;
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

    private double getMulti() {
        return _endGame ? jawSpeedEndGame : jawSpeedNormal;
    }

    public void manualOpen() {
        JawElec.set(ControlMode.PercentOutput, -getMulti());
        JawPne.set(ControlMode.PercentOutput, getMulti());
    }

    public void manualClose() {
        JawElec.set(ControlMode.PercentOutput, getMulti());
        JawPne.set(ControlMode.PercentOutput, -getMulti());
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