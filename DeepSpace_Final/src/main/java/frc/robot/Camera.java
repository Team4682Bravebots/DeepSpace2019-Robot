// package frc.robot;

// import org.opencv.core.Mat;
// import org.opencv.core.Point;
// import org.opencv.core.Scalar;
// import org.opencv.imgproc.Imgproc;

// import edu.wpi.cscore.CvSink;
// import edu.wpi.cscore.CvSource;
// import edu.wpi.cscore.UsbCamera;
// import edu.wpi.first.cameraserver.CameraServer;
// import edu.wpi.first.wpilibj.vision.VisionThread;

// public class Camera {
//     private UsbCamera camera;
//     //private VisionThread visionThread;
//     private CvSink cvSink;
//     private CvSource outputStream;
//     private Mat mat;

//     public Camera() {
//          Thread m_visionThread = new Thread(() -> {
//             // Get the UsbCamera from CameraServer
//             camera = CameraServer.getInstance().startAutomaticCapture();
//             // Set the resolution
//             camera.setResolution(360, 360);
//             // // outputStream.setFPS(10);
//             // //outputStream.

//             // cvSink = CameraServer.getInstance().getVideo(camera);
//             // // Setup a CvSource. This will send images back to the Dashboard
//             // outputStream = CameraServer.getInstance().putVideo("Rectangle",  360, 360);
            
//             // mat = new Mat();
            

//             // Get a CvSink. This will capture Mats from the camera
//             cvSink = CameraServer.getInstance().getVideo();
//             // Setup a CvSource. This will send images back to the Dashboard
//             outputStream = CameraServer.getInstance().putVideo("Rectangle", 640, 480);

//             // Mats are very memory expensive. Lets reuse this Mat.
//              mat = new Mat();

//             // This cannot be 'true'. The program will never exit if it is. This
//             // lets the robot stop this thread when restarting robot code or
//             // deploying.
//            while (!Thread.interrupted()) {
//                 // Tell the CvSink to grab a frame from the camera and put it
//                 // in the source mat. If there is an error notify the output.
//                 if (cvSink.grabFrame(mat) == 0) {
//                     // Send the output the error.
//                     System.out.println(cvSink.getError());
//                     outputStream.notifyError(cvSink.getError());
//                     // skip the rest of the current iteration
//                     return;
//                 }
//                 // Put a rectangle on the image
//                 Imgproc.rectangle(mat, new Point(100, 100), new Point(400, 400), new Scalar(255, 255, 255), 5);
//                 // Give the output stream a new image to display
//                 outputStream.putFrame(mat);
//            }
//         });
//         m_visionThread.setDaemon(true);
//         m_visionThread.start();
//     }

//     public void stream() {
       
//         if (cvSink.grabFrame(mat) == 0) {
//             outputStream.notifyError(cvSink.getError());
//             System.out.println(cvSink.getError());
//             System.out.println("Can't get Frame!\n");
//             return;
//         }

//         // Put a rectangle on the image
//         Imgproc.rectangle(mat, new Point(0, 0), new Point(360, 360), new Scalar(255, 255, 255), 5);
//         // Give the output stream a new image to display
        
//         outputStream.putFrame(mat);
//     }

//     public void turnOff() {
//     }
// }