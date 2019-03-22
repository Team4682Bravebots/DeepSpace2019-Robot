/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Add your docs here.
 */
public class MiniChassis {
    private boolean isLimitRunning;
    private static final boolean _kSensorPhase = true;
    private boolean inverted;
    private TalonSRX HE_C_1$; // Hatch Electornics Climber (SIM) _______________LINKED \/
    private TalonSRX HP_C_2$; // Hatch Pnumatics Climber (SIM)_______________LINKED /\
    private TalonSRX BP_C_3$; // Ball Pnumatics Climb (BAG)
    private TalonSRX BE_C_4$; // Ball Electronics Climb (BAG)
    private TalonSRX miniDrive_5; // Motor powering the mini-Chassis drive axle, it is mounted on a shaft spanning between talBR_C_3 and talBL_C_4
    private DigitalInput one;
    private DigitalInput two;
    private DigitalInput three;
    private DigitalInput four;
    private    int abspos;
    private double targetPositionRotations;
    private static final double rotMulti = 10.0; //MUST TUNE
    private static final double countperRot = 4096; //MUST TUNE
    private AnalogInput HatchUltrasonic;
    private static final double liftSpeed = 0.2;
    public MiniChassis(int HEC,int HPC,int BEC,int BPC,int twidelyBit){
        HE_C_1$ = new TalonSRX(HEC);                      // -hatch---
        HP_C_2$ = new TalonSRX(HPC);                      // | 1 | 2 |
        BP_C_3$ = new TalonSRX(BPC);                      // | 4 | 3 | 
        BE_C_4$ = new TalonSRX(BEC);                       // --ball---
        miniDrive_5 = new TalonSRX(twidelyBit);
        one = new DigitalInput(1);
        two = new DigitalInput(2);
        three = new DigitalInput(3);
        four = new DigitalInput(4);
       // HatchUltrasonic = new AnalogInput(4);
    }
    public void initPosPID(
    TalonSRX _talon, 
    double _kP, 
    double _kI,
    double _kD,
    double _kF, 
    int _kIzone,
    double _kPeakOutput,
    int _kTimeoutMs,
    boolean _kInverted)
     {
        _talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,0,_kTimeoutMs);
        _talon.setSensorPhase(_kSensorPhase);
        _talon.setInverted(_kInverted);
        inverted = _kInverted;
        _talon.configNominalOutputForward(0, _kTimeoutMs);
        _talon.configNominalOutputReverse(0,_kTimeoutMs);
        _talon.configPeakOutputForward(1,_kTimeoutMs);
        _talon.configPeakOutputReverse(-1, _kTimeoutMs);
        _talon.configAllowableClosedloopError(0, 0, _kTimeoutMs);
        _talon.config_kF(0, _kF);
        _talon.config_kP(0, _kP);
        _talon.config_kI(0, _kI);
        _talon.config_kD(0, _kD);
        abspos = _talon.getSensorCollection().getPulseWidthPosition();
    }
    public void initLimits(){
        isLimitRunning = true;
        
    }

    public void debug(){
        SmartDashboard.putNumber("Motor 1", HE_C_1$.getSelectedSensorPosition());
        SmartDashboard.putNumber("Motor 2", HP_C_2$.getSelectedSensorPosition());
        SmartDashboard.putNumber("Motor 3", HE_C_1$.getSelectedSensorPosition());
        SmartDashboard.putNumber("Motor 4", HE_C_1$.getSelectedSensorPosition());

        SmartDashboard.putBoolean("Limit 1", one.get());
        SmartDashboard.putBoolean("Limit 2", two.get());
        SmartDashboard.putBoolean("Limit 3", three.get());
        SmartDashboard.putBoolean("Limit 4", four.get());
    }

    public void initMiniChassis(){
        initPosPID(HE_C_1$, 0, 0, 0, 0, 0, 1.0, 30,false);
        initPosPID(HP_C_2$, 0, 0, 0, 0, 0, 1.0, 30, false);
        initPosPID(BP_C_3$, 0, 0, 0, 0, 0, 1.0, 30, false);
        initPosPID(BE_C_4$, 0, 0, 0, 0, 0, 1.0, 30, false);
        reset(HE_C_1$);
        reset(HP_C_2$);
        reset(BP_C_3$);
        reset(BE_C_4$);


    }
    public void reset(TalonSRX _talon){

        abspos &= 0xFFF;
        if (_kSensorPhase) { abspos *= -1; }
        if (inverted) { abspos *= -1; }

        /* Set the quadrature (relative) sensor to match absolute */
        _talon.setSelectedSensorPosition(abspos, 0, 30);
    }

    public void getTo(double theGoal, TalonSRX usingMotor){
       
        targetPositionRotations = theGoal * rotMulti * countperRot;
        usingMotor.set(ControlMode.Position, targetPositionRotations);
    }

    public void TurnOff() {
        HE_C_1$.set(ControlMode.PercentOutput, 0);
        HP_C_2$.set(ControlMode.PercentOutput, 0);
        BE_C_4$.set(ControlMode.PercentOutput, 0);
        BP_C_3$.set(ControlMode.PercentOutput, 0);
    }
    public void testClimb(Joystick WOW){
        
            HE_C_1$.set(ControlMode.PercentOutput, -WOW.getRawAxis(0));
            HP_C_2$.set(ControlMode.PercentOutput,  WOW.getRawAxis(0)); 
            BE_C_4$.set(ControlMode.PercentOutput, -WOW.getRawAxis(0));
            BP_C_3$.set(ControlMode.PercentOutput, WOW.getRawAxis(0));
        

    }

    public boolean runClimbPhaseOne(){
        // Elevate to max height
         if(one.get() == false){
             //run it
             /* Position Closed Loop */

            /* 10 Rotations * 4096 u/rev in either direction */
          //getTo(0.4,HE_C_1$);
          HE_C_1$.set(ControlMode.PercentOutput, -liftSpeed);

         } else{
             // don't
             HE_C_1$.set(ControlMode.PercentOutput, 0);
         }

         if(two.get() == false){
             //run it
            // getTo(0.4, HP_C_2$);
            HP_C_2$.set(ControlMode.PercentOutput, liftSpeed);
         }else{
             //don't
         }

         if(three.get() == false){
             // run it
             BP_C_3$.set(ControlMode.PercentOutput, liftSpeed);
         }else{
             //don't
         }
         // TODO: Fix four
         if(three.get() == false){
             //run it
             BE_C_4$.set(ControlMode.PercentOutput, -liftSpeed);
         }else{
             //don't
         }
         if(one.get()&&two.get()&&three.get()&&four.get()){
             return true;
         }else {
             return false;
         }

    }
    public boolean runClimbPhaseTwo(){
        while(HatchUltrasonic.getVoltage()/0.098 >16){
            //twidle forward
            miniDrive_5.set(ControlMode.PercentOutput, 0.7);
        }
        miniDrive_5.set(ControlMode.PercentOutput, 0);
        return true;
    }
    public boolean runClimbPhaseThree(){
        //retract front axle
        getTo(0, HE_C_1$);
        getTo(0, HP_C_2$);
        Timer.delay(2);
        if(
            (one.get() == false && two.get() == false)
            ||
            ((HE_C_1$.getSelectedSensorPosition()>=-100 && HE_C_1$.getSelectedSensorPosition()<=100) 
            && (HP_C_2$.getSelectedSensorPosition()>=-100 && HP_C_2$.getSelectedSensorPosition()<=100))){
            return true;
        }else{
            return false;
        }
        
    }
    public boolean runClimbPhaseFour(){
        while(HatchUltrasonic.getVoltage()/0.098 >8){
        //twidle forward
        miniDrive_5.set(ControlMode.PercentOutput, 0.7);
    }
    miniDrive_5.set(ControlMode.PercentOutput, 0);
    return true;

    }

    public boolean runClimbPhaseFive(){
        //rase back legs
        getTo(0, BP_C_3$);
        getTo(0, BE_C_4$);
        if((three.get() == false&& four.get() == false) ||(BP_C_3$.getSelectedSensorPosition() <= 100 && BP_C_3$.getSelectedSensorPosition()>=-100)){
            return true;
        }else{
            return false;
        }

    }
    public void Climb(){
        while(runClimbPhaseOne()== false){
            runClimbPhaseOne();
        }
        // while(runClimbPhaseTwo() == false){
        //     runClimbPhaseTwo();
        // }
        // while(runClimbPhaseThree() == false){
        //     runClimbPhaseThree();
        // } 
        // while(runClimbPhaseFour() == false){
        //     runClimbPhaseFour();
        // }
        // while(runClimbPhaseFive() == false) {
        //     runClimbPhaseFive();
        // }
        //done
    }
}
