package frc.robot.core751.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import frc.robot.core751.wrappers.BNO055;
import frc.robot.core751.wrappers.RevEncoder;
import frc.robot.core751.wrappers.WCANSparkMax;

public class DifferentialDriveTrain extends SubsystemBase {

    public enum driveMotor {
        kSparkMaxBrushless,
        kPWMVictorSPX,
    }

    private SpeedController[] leftArray;
    private SpeedController[] rightArray;

    private SpeedControllerGroup leftGroup;
    private SpeedControllerGroup rightGroup;

    private DifferentialDrive differentialDrive;
    private DifferentialDriveOdometry differentialDriveOdometry;

    private Gyro bno055;
    private RevEncoder mainLeftEncoder;
    private RevEncoder mainRightEncoder;

    private static SpeedControllerGroup arrayToGroup(SpeedController[] sp) {
        //There has to be a better way to do this
        switch(sp.length) {
            case 4:
                return new SpeedControllerGroup(sp[0], sp[1], sp[2], sp[3]);
            case 3:
                return new SpeedControllerGroup(sp[0], sp[1], sp[2]);
            case 2:
                return new SpeedControllerGroup(sp[0], sp[1]);
            default:
                return new SpeedControllerGroup(sp[0]);
        }
    }

    public DifferentialDriveTrain (int[] left, int[] right, driveMotor dm) {
        bno055 = BNO055.getInstance(BNO055.opmode_t.OPERATION_MODE_IMUPLUS,
                                    BNO055.vector_type_t.VECTOR_EULER);

        switch (dm) {
            case kSparkMaxBrushless:
                leftArray = new WCANSparkMax[left.length];
                rightArray = new WCANSparkMax[right.length];
                for (int i = 0; i < leftArray.length; i++) {
                    leftArray[i] = new WCANSparkMax(left[i], MotorType.kBrushless);

                    if(mainLeftEncoder == null) {
                        mainLeftEncoder = new RevEncoder(((WCANSparkMax)leftArray[i]).getEncoder());
                    }
                }
                for (int i = 0; i < rightArray.length; i++) {
                    rightArray[i] = new WCANSparkMax(right[i], MotorType.kBrushless);

                    if(mainRightEncoder == null) {
                        mainRightEncoder = new RevEncoder(((WCANSparkMax)rightArray[i]).getEncoder());
                    }
                }
            break;
            case kPWMVictorSPX:
                leftArray = new PWMVictorSPX[left.length];
                rightArray = new PWMVictorSPX[right.length];
                for (int i = 0; i < leftArray.length; i++) {
                    leftArray[i] = new PWMVictorSPX(left[i]);
                }
                for (int i = 0; i < rightArray.length; i++) {
                    rightArray[i] = new PWMVictorSPX(right[i]);
                }
            break;
        }

        this.leftGroup = arrayToGroup(leftArray);
        this.rightGroup = arrayToGroup(rightArray);

        this.differentialDrive = new DifferentialDrive(leftGroup, rightGroup);

        if(mainLeftEncoder != null && mainRightEncoder != null && 
           bno055 != null) {
            if(((BNO055)(bno055)).isSensorPresent()) {
                System.out.println("Encoders and IMU present. PID is supported");

                bno055.calibrate();

                differentialDriveOdometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()));

                return;
            } else {
                System.out.println("Encoders present, but IMU isn't. ");
            }    
        } else {
            if(((BNO055)(bno055)).isSensorPresent()) {
                System.out.println("IMU is present, but encoders aren't.");
            } else {
                System.out.println("Encoders and IMU are not present.");
            }
        }
        System.out.println("PID is not supported");
    }

    public double getHeading() {
        return Math.IEEEremainder(bno055.getAngle(), 360) /** (DriveConstants.kGyroReversed ? -1.0 : 1.0)*/;
    }

    public Pose2d getPose() {
        return differentialDriveOdometry.getPoseMeters();
    }
    
    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(mainLeftEncoder.getRate(), 
                                                mainRightEncoder.getRate());
    }

    public DifferentialDrive getDifferentialDrive() {
        return this.differentialDrive;
    }

    public void setVolts(double leftVolts, double rightVolts) {
        leftGroup.setVoltage(leftVolts);
        rightGroup.setVoltage(-rightVolts);
    }

    public void updateOdometry() {
        differentialDriveOdometry.update(Rotation2d.fromDegrees(getHeading()), 
                                         mainLeftEncoder.getDistance(), 
                                         mainRightEncoder.getDistance());
    }
}