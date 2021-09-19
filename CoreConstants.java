package frc.robot.core751;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.core751.subsystems.DifferentialDriveTrain;
import frc.robot.core751.subsystems.DifferentialDriveTrain.driveMotor;

public final class CoreConstants{

      /*-------------/
     /--DriveTrain--/
    /-------------*/

    public static int leftDrivetrainIDs[] = new int[] { 1, 2, 3 };
    public static int rightDrivetrainIDs[] = new int[] { 4, 5, 6 };
    public static DifferentialDriveTrain.SmartControllerProfile driveMotorProfile= new DifferentialDriveTrain.SmartControllerProfile(IdleMode.kCoast, 0.25, 35);
    public static DifferentialDriveTrain.driveMotor driveTrainMotorType = driveMotor.kSparkMaxBrushless;

    public static double speedCap = 1.0;
    public static bool smoothing = true;

    public static double maxSparkDeccelPeriod = 1;//0.5;
    public static double sparkDeccelThreshold = 0.5;
    public static int sparkDeccelSteps = 14;//7;
    public static double minPauseDeaccelThreshold = 0.5;


    public static int driveStickPort = 0;
    public static Button driveSwitchDirectionButton = new JoystickButton(driverStick, Controller.START.buttonNum);

      /*-------------/
     /----Camera----/
    /-------------*/
    public static Integer frontCameraId = null;
    public static Integer backCameraId = null;
    public static Camera frontCamera = new Camera(frontCameraId);
    public static Camera backCamera = new Camera(backCameraId);
}