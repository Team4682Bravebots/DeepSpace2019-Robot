/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;



import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SolenoidBase; 
import edu.wpi.first.wpilibj.SendableBase;
public class Robot extends IterativeRobot {
    DoubleSolenoid exampleDouble = new DoubleSolenoid(0, 1);
    public void robotInit(){
        exampleDouble.set(DoubleSolenoid.Value.kOff);
    exampleDouble.set(DoubleSolenoid.Value.kForward);
    exampleDouble.set(DoubleSolenoid.Value.kReverse);
    }
    
}
    






            