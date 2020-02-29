package frc.robot.core751.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
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
import frc.robot.core751.wrappers.ArduinoGyro;
import frc.robot.core751.wrappers.BNO055;
import frc.robot.core751.wrappers.RevEncoder;
import frc.robot.core751.wrappers.WCANSparkMax;

public class DifferentialDriveTrain extends SubsystemBase {

    public enum driveMotor {
        kSparkMaxBrushless,
        kPWMVictorSPX,
    }

    public enum DriveTrainDirection {
        FORWARD(1),
        BACKWARD(-1);

        private int mod;
        public int getMod(){return this.mod;}
        private DriveTrainDirection(int mod) {
            this.mod = mod;
        }

    }

    private SpeedController[] leftArray;
    private SpeedController[] rightArray;

    private SpeedControllerGroup leftGroup;
    private SpeedControllerGroup rightGroup;

    private SpeedController[] controllers;

    private DifferentialDrive differentialDrive;
    private DifferentialDriveOdometry differentialDriveOdometry;

    private Gyro bno055;
    private RevEncoder mainLeftEncoder;
    private RevEncoder mainRightEncoder;

    public static DriveTrainDirection direction = DriveTrainDirection.FORWARD;

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

    public DifferentialDriveTrain (int[] left, int[] right, driveMotor dm, boolean invertLeft, boolean invertRight) {
        bno055 = new ArduinoGyro();

        switch (dm) {
            case kSparkMaxBrushless:
                leftArray = new WCANSparkMax[left.length];
                rightArray = new WCANSparkMax[right.length];
                for (int i = 0; i < leftArray.length; i++) {
                    leftArray[i] = new WCANSparkMax(left[i], MotorType.kBrushless);

                    if(mainLeftEncoder == null) {
                        mainLeftEncoder = new RevEncoder(((WCANSparkMax) leftArray[i]).getEncoder());
                    }
                }
                for (int i = 0; i < rightArray.length; i++) {
                    rightArray[i] = new WCANSparkMax(right[i], MotorType.kBrushless);

                    if (mainRightEncoder == null) {
                        mainRightEncoder = new RevEncoder(((WCANSparkMax) rightArray[i]).getEncoder());
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
        this.controllers = new SpeedController[this.leftArray.length + this.rightArray.length];
        
        this.leftGroup.setInverted(invertLeft);
        this.rightGroup.setInverted(invertRight);
        this.differentialDrive = new DifferentialDrive(leftGroup, rightGroup);

        if(mainLeftEncoder != null && mainRightEncoder != null && 
           bno055 != null) {

            System.out.println("Encoders and IMU present. PID is supported");

            //bno055.calibrate();

            differentialDriveOdometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()));

            return;
        } else {
            if(((BNO055)(bno055)).isSensorPresent()) {
                System.out.println("IMU is present, but encoders aren't.");
            } else {
                System.out.println("Encoders and IMU are not present.");
            }
        }
        System.out.println("PID is not supported");
    }

    public DifferentialDriveTrain (int[] left, int[] right, driveMotor dm, SmartControllerProfile profile, boolean invertLeft, boolean invertRight) {
        bno055 = new ArduinoGyro();
        System.out.println("initalized");
        
        switch (dm) {
            case kSparkMaxBrushless:
                leftArray = new WCANSparkMax[left.length];
                rightArray = new WCANSparkMax[right.length];


                for (int i = 0; i < leftArray.length; i++) {
                    leftArray[i] = new WCANSparkMax(left[i], MotorType.kBrushless);
                    leftArray[i].setInverted(false);

                    if(mainLeftEncoder == null) {
                        mainLeftEncoder = new RevEncoder(((WCANSparkMax) leftArray[i]).getEncoder());
                    }
                }
                for (int i = 0; i < rightArray.length; i++) {
                    rightArray[i] = new WCANSparkMax(right[i], MotorType.kBrushless);
                    rightArray[i].setInverted(false);

                    if(mainRightEncoder == null) {
                        mainRightEncoder = new RevEncoder(((WCANSparkMax) rightArray[i]).getEncoder());
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
        this.leftGroup.setInverted(invertLeft);
        this.rightGroup.setInverted(invertRight);

        this.controllers = new SpeedController[this.leftArray.length + this.rightArray.length];
        for (int i = 0; i < leftArray.length; i++) {
            this.controllers[i] = this.leftArray[i];
        }
        for (int i = 0; i < rightArray.length; i++) {
            this.controllers[leftArray.length+i] = this.rightArray[i];
        }

        switch (dm) {
            case kSparkMaxBrushless:
                for (SpeedController sc : this.controllers) {
                    WCANSparkMax sMax = (WCANSparkMax)sc;
                    if (profile.idle != null) sMax.setBreakMode(profile.idle);
                    if (profile.rate != 0) sMax.setOpenLoopRampRate(profile.rate);
                    if (profile.rate != 0) sMax.setClosedLoopRampRate(profile.rate);
                    if (profile.limit != 0) sMax.setCurrentLimit(profile.limit);
                }
            break;
            case kPWMVictorSPX:
                //TODO: Add suport for this
            break;
        }

        if(mainLeftEncoder != null && mainRightEncoder != null && 
            bno055 != null) {

            System.out.println("Encoders and IMU present. PID is supported");

            //bno055.calibrate();

            differentialDriveOdometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()));

            for(int i = 0; controllers.length > i; ++i) {
                System.out.println(i + "=" + controllers[i].getInverted());
            }
            System.out.println("left=" + leftGroup.getInverted());
            System.out.println("right=" + rightGroup.getInverted());

            return;
        } else {
            if(((BNO055)(bno055)).isSensorPresent()) {
                System.out.println("IMU is present, but encoders aren't.");
            } else {
                System.out.println("Encoders and IMU are not present.");
            }
        }
        System.out.println("PID is not supported");

        this.differentialDrive = new DifferentialDrive(leftGroup, rightGroup);
    }

    public DifferentialDrive getDifferentialDrive() {
        return this.differentialDrive;
    }

    public DriveTrainDirection getDirection() {
        return this.direction;
    }

    public void setDirection(DriveTrainDirection direction) {
        this.direction = direction;
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

    public void setVolts(double leftVolts, double rightVolts) {
        System.out.println("left " + leftVolts + " right " + rightVolts);
        leftGroup.setVoltage(leftVolts);
        //rightGroup.setVoltage(rightVolts);
    }

    public void updateOdometry() {
        differentialDriveOdometry.update(Rotation2d.fromDegrees(getHeading()), 
                                         mainLeftEncoder.getDistance(), 
                                         mainRightEncoder.getDistance());
    }

    public static class SmartControllerProfile {
        public IdleMode idle;
        public double rate;
        public int limit;

        public SmartControllerProfile(IdleMode idle, double rate, int limit) {
            this.idle = idle;
            this.rate = rate;
            this.limit = limit;
        }

        public SmartControllerProfile(IdleMode idle, double rate) {
            this.idle = idle;
            this.rate = rate;
        }

        public SmartControllerProfile(IdleMode idle) {
            this.idle = idle;
        }

        public SmartControllerProfile(double rate, int limit) {
            this.rate = rate;
            this.limit = limit;
        }

        public SmartControllerProfile(int limit) {
            this.limit = limit;
        }

        public SmartControllerProfile(IdleMode idle, int limit) {
            this.idle = idle;
            this.limit = limit;
        }

        public SmartControllerProfile(double rate) {
            this.rate = rate;
        }

    }
}