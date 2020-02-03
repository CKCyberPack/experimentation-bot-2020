/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

/* To do this week (2020-01-29):

  - Change autonomous swing turn to pivot turn
  - Use tensorflow and see if it's better at object recogition than current pipeline algorithm


*/
package frc.robot;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.vision.VisionThread;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Solenoid;


/**

  This class holds the heart of the robot code. Each method is called upon when modes are changed in the driver station.

  Ex. 
  teleopInit runs once to initialize parts of the code. ex. starting a camera stream and setting resolution and fps.

    teleopPeriodic runs periodically while enabled. ex. to update motor controllers at a quick enough rate to reliably drive.

  For each mode in the Driver Station, methods are called to control it. This is how we differentiate from Autonomous, Teleop, etc.

 */
public class Robot extends TimedRobot 
{

  // final initializations

 


  @Override
  public void robotInit() // runs when code begins, no matter which drive mode
  {
   

    // Creatng the camera server for the USB cam

    UsbCamera cam = CameraServer.getInstance().startAutomaticCapture();

    cam.setResolution(RMap.IMG_WIDTH,RMap.IMG_HEIGHT);

    cam.setFPS(15); // Standard FPS for our vision, subject to change


/*

    visionThread constructor: begins capturing images from the camera
    asynchroously in a seperate thred. After processing each image, the pipeline
    computes a "bounding box" around the target is retrieved and it's center X
    value is computed.

    Very deprecated, looking for something newer and more efficient.

*/

  RMap.visionThread = new VisionThread(cam, new GripPipeline(), pipeline -> 
  {
        // if the pipeline detects an object, ex. "no contour outputs" is false

        if (!pipeline.filterContoursOutput().isEmpty()) 
        {

          // builds a rectangle around the detected object, and calculates the center x value of it.
          Rect r = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0)); 
          synchronized (RMap.imgLock)
          {

            RMap.centerX = r.x + (r.width / 2);

          }
        }

    });
    RMap.visionThread.start();

  }



  @Override

  public void autonomousPeriodic()
  {
    double centerX;

    // synchronizes data so code is quicker and more efficient
    synchronized (RMap.imgLock) 
    {
        centerX = RMap.centerX;
    }

    // Finds the turn value for the robot, and compares the center of x to half of the
    // camera width to determine if the object is to the left or to the right of the center of the camera.

    // The "turn" value will be negative if the robot needs to turn left to center the object, and vise versa.

    double turn = centerX - (RMap.IMG_WIDTH / 2);

    // Still implementing a proper steering mechanism. We want a pivot turn, not a swing turn (which is what it currently is).

    if ((RMap.IMG_WIDTH/2)>=centerX)
    {

      RMap.m_robotDrive.tankDrive(0, turn * -0.005);

    }
    else
      RMap.m_robotDrive.tankDrive(0, turn * 0.005);
    
  }
  



  @Override

  public void teleopPeriodic()
  {
  

    //gearGrabber.set(true);
   

    RMap.ropeMotor.set(RMap.xboxcontroller.getTriggerAxis (Hand.kLeft)/2); // Setting the speed of the rope motor

    RMap.m_robotDrive.tankDrive((RMap.xboxcontroller.getY(Hand.kRight)/2), (RMap.xboxcontroller.getY(Hand.kLeft)/2)); // setting drive, speed is halved for safety when testing

    
    
  // kid mode determination, currently commented out for experimentation
    
   /* 
   switch (kidMode)
    {
      case 0:
        m_robotDrive.tankDrive((xboxcontroller.getY(Hand.kRight)), (xboxcontroller.getY(Hand.kLeft)));
      break;


      case 1:
        m_robotDrive.tankDrive((xboxcontroller.getY(Hand.kRight)/2), (xboxcontroller.getY(Hand.kLeft)/2));
      break;

    }

    
      if ((xboxcontroller.getStartButtonReleased()==true)&&(xboxcontroller.getBackButtonPressed()==true))
      {

       switch (kidMode)
       {

          case 0:
            kidMode=1;
          break;

          case 1:
            kidMode=0;
          break;

       }

      }*/

    // 

    if (RMap.xboxcontroller.getXButtonPressed()==true)
    {

      RMap.gearGrabber.set(true);

    }   
    if (RMap.xboxcontroller.getXButtonReleased()==true)
    {

      RMap.gearGrabber.set(false);

    }

    
    

  }
 
}