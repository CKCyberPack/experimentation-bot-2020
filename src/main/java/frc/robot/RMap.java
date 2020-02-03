/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.vision.VisionThread;

/**
 * The RMap holds all variables and objects throughout the code.
 * 
 * This is mainly for organizational purposes, to make cleaner, easier to
 * understand code.
 */
public class RMap {

    public int kidMode = 1;
    public double speedDiv = 2;
    public static VictorSP leftFrontMotor = new VictorSP(2);
    public static VictorSP leftBackMotor = new VictorSP(1);
    public static VictorSP rightFrontMotor = new VictorSP(3);
    public static VictorSP rightBackMotor = new VictorSP(4);

    /*
     * 
     * These are vision objects and variables, used to define...
     * 
     * 1. The image width that will be shown. 2. The image height.
     * 
     * 3. VisionThread, an object that runs the visual processing thread seperately
     * from the robot code 4. "centerX" the center x value of the detected target 5.
     * Robot drive encapsulates the 4 motors and allows for more simplified driving.
     * 
     * 6. imgLock, A variable to synchronize access to the data being simultaeously
     * updated by the image passes, as well as the code that's processing the
     * coordinates and steering the robot.
     * 
     */
    public static final int IMG_WIDTH = 320;
    public static final int IMG_HEIGHT = 240;
    public static VisionThread visionThread;
    public static double centerX = 0.0;
    public static DifferentialDrive drive;
    public static final Object imgLock = new Object();

    // public final DriverStation driver;

    public final static VictorSP ropeMotor = new VictorSP(5);

    public final static Solenoid gearGrabber = new Solenoid(0);

    public final static SpeedControllerGroup left_drive = new SpeedControllerGroup(leftFrontMotor, leftBackMotor);
    public final static SpeedControllerGroup right_drive = new SpeedControllerGroup(rightFrontMotor, rightBackMotor);

    public final static DifferentialDrive m_robotDrive = new DifferentialDrive(right_drive, left_drive);

    public final static XboxController xboxcontroller = new XboxController(0);

}
