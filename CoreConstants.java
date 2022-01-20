package frc.robot.core751;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.core751.subsystems.Camera;
import frc.robot.core751.subsystems.DifferentialDriveTrain;
import frc.robot.core751.subsystems.DifferentialDriveTrain.driveMotor;

public final class CoreConstants{

  public enum Controller { // Button mappings for the XBOX One controller
    A(1), B(2), X(3), Y(4), LB(5), RB(6), LT(2), // Must use .getRawAxis()
    RT(3), // Must use .getRaxAxis()
    BACK(7), START(8), LEFT_AXIS_PRESS(9), // X-Axis: -1.000 to 1.000 (stick.GetX())
                                           // Y-Axis: -1.000 to 1.000 (stick.GetY())
    RIGHT_AXIS_PRESS(10);

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
    public static Joystick driverStick = new Joystick(CoreConstants.driveStickPort);
    public static int driverStickPort = 0;

    public static int leftDrivetrainIDs[] = new int[] { 1, 2, 3 };
    public static int rightDrivetrainIDs[] = new int[] { 4, 5, 6 };
    public static DifferentialDriveTrain.SmartControllerProfile driveMotorProfile= new DifferentialDriveTrain.SmartControllerProfile(IdleMode.kCoast, 0.25, 35);
    public static DifferentialDriveTrain.driveMotor driveTrainMotorType = driveMotor.kSparkMaxBrushless;

    public static double speedCap = 1.0;
    public static Boolean smoothing = true;

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