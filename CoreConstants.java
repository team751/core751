package frc.robot.core751;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Joystick;
<<<<<<< HEAD
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.core751.subsystems.CamServer;
import frc.robot.core751.subsystems.DifferentialDriveTrain;
import frc.robot.core751.subsystems.DifferentialDriveTrain.driveMotor;
=======
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.core751.commands.camera.LimeLight.SwitchCameraMode;
import frc.robot.core751.subsystems.camera.CamServer;
import frc.robot.core751.subsystems.camera.LimeLight;
import frc.robot.core751.subsystems.drivetrain.DifferentialDriveTrain;
import frc.robot.core751.subsystems.drivetrain.DifferentialDriveTrain.driveMotor;
>>>>>>> 8fe6274 (Lots of changes for auton stuff)
import frc.robot.core751.wrappers.OverrideableJoystick;
import frc.robot.core751.wrappers.wCamera;

public final class CoreConstants{

  public enum Controller { // Button mappings for the XBOX One controller
    A(1), B(2), X(3), Y(4), LB(5), RB(6), 
    LT(2), // Must use .getRawAxis()
    RT(3), // Must use .getRaxAxis()
    BACK(7), START(8), 
    LEFT_AXIS_PRESS(9), // X-Axis: -1.000 to 1.000 (stick.GetX())
    RIGHT_AXIS_PRESS(10); // Y-Axis: -1.000 to 1.000 (stick.GetY())

    private int buttonNum;

    private Controller(int value) {
        this.buttonNum = value;
    }

    public int getButtonMapping() {
        return this.buttonNum;
    }

    

}

      /*-------------/
     /--DriveTrain--/
    /-------------*/
    public static int driveStickPort = 0;
    public static OverrideableJoystick driverStick = new OverrideableJoystick(CoreConstants.driveStickPort);
    
    

    public static int leftDrivetrainIDs[] = new int[] { 1, 2, 3 };
    public static int rightDrivetrainIDs[] = new int[] { 4, 5, 6 };
    public static DifferentialDriveTrain.SmartControllerProfile driveMotorProfile= new DifferentialDriveTrain.SmartControllerProfile(IdleMode.kCoast, 0.25, 35);
    public static DifferentialDriveTrain.driveMotor driveTrainMotorType = driveMotor.kSparkMaxBrushless;
    public static boolean driveInvertLeft = false;
    public static boolean driveInvertRight = false;

    public static double speedCap = 0.5;
    public static Boolean smoothing = true;

    public static double maxSparkDeccelPeriod = 1;//0.5;
    public static double sparkDeccelThreshold = 0.5;
    public static int sparkDeccelSteps = 14;//7;
    public static double minPauseDeaccelThreshold = 0.5;

    public static Button driveSwitchDirectionButton = new JoystickButton(driverStick, Controller.START.buttonNum);

      /*-------------/
     /----Camera----/
    /-------------*/
<<<<<<< HEAD
    public static wCamera frontCamera = new wCamera(0);

    public static wCamera[] allCameras = {frontCamera};
    public static CamServer camServer = new CamServer(frontCamera);
=======
    //public static wCamera frontCamera = new wCamera(0);

    public static wCamera[] allCameras = {}; //Just for usb camera 
    //public static CamServer camServer = new CamServer(frontCamera);

 
      /*--------------/
     /-----Auton-----/
    /--------------*/

    public static Gyro gyro = new ADXRS450_Gyro();
    static{
      gyro.calibrate();
    }

    public static double trackwidthMeters = 0.66; //Find experminetally if needed
    public static DifferentialDriveKinematics driveKinematics =
        new DifferentialDriveKinematics(trackwidthMeters);

    public static double gearRatio = 7.44; //Gear ratio of drivetrain
    public static int encoderCPR = 1; // 1 for NEO 
    public static double wheelDiameterMeters = 0.203;
    public static double encoderDistancePerPulse =
        (wheelDiameterMeters * Math.PI) / (double) encoderCPR / gearRatio;

    public static double planningVoltageContraint = 5;

    //-----------Feed Forward------------ (Remember to make these match your drivetrain)
    public static double ksVolts = 0.14529;
    public static double kvVoltSecondsPerMeter = 1.4637;
    public static double kaVoltSecondsSquaredPerMeter = 0.25498;
    public static double kPDriveVel = 1.3387;


    //-------------Ramsete---------------
    public static final double maxSpeedMetersPerSecond = 1; //Essentially Speedcap
    public static final double maxAccelerationMetersPerSecondSquared = 3; //Essentially rotatonial speed

    public static final double ramseteB = 2; // Reasonable baseline value
    public static final double ramseteZeta = 0.7; // Reasonable baseline value
>>>>>>> 8fe6274 (Lots of changes for auton stuff)
}