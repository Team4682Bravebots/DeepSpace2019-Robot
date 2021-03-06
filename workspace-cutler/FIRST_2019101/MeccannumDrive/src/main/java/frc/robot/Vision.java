package frc.robot;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import edu.wpi.cscore.*;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Vision extends IterativeRobot {
    
    
    public void robotInit() {
            new Thread(() -> {
                UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
                camera.setResolution(640, 360);

               // UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
                //camera.setVideoMode(VideoMode.kMJPEG, 640, 360, 30);

                
                CvSink cvSink = CameraServer.getInstance().getVideo();
                CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);
                
                Mat source = new Mat();
                Mat output = new Mat();
                
                while(!Thread.interrupted()) {
                    cvSink.grabFrame(source);
                    Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
                    outputStream.putFrame(output);
                }
            }).start();

            
    }
}