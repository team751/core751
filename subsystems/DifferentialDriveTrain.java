package frc.robot.core751.subsystems;

import com.revrobotics.SparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.core751.wrappers.WCANSparkMax;

public class DifferentialDriveTrain extends SubsystemBase {

    public enum driveMotor {
        kSparkMaxBrushless,
        kPWMVictorSPX,
    }

    public enum DriveTrainDirrection {
        FORWARD(1),
        BACKWARD(-1);

        private int mod;
        public int getMod(){return this.mod;}
        private DriveTrainDirrection(int mod) {
            this.mod = mod;
        }

    }

    private SpeedController[] leftArray;
    private SpeedController[] rightArray;

    private SpeedControllerGroup leftGroup;
    private SpeedControllerGroup rightGroup;

    private DifferentialDrive differentialDrive;

    private SpeedController[] controllers;

    private DriveTrainDirrection dirrection = DriveTrainDirrection.FORWARD;

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
        switch (dm) {
            case kSparkMaxBrushless:
                leftArray = new WCANSparkMax[left.length];
                rightArray = new WCANSparkMax[right.length];
                for (int i = 0; i < leftArray.length; i++) {
                    leftArray[i] = new WCANSparkMax(left[i], MotorType.kBrushless);
                }
                for (int i = 0; i < rightArray.length; i++) {
                    rightArray[i] = new WCANSparkMax(right[i], MotorType.kBrushless);
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

        this.differentialDrive = new DifferentialDrive(leftGroup, rightGroup);

    }

    public DifferentialDriveTrain (int[] left, int[] right, driveMotor dm, SmartControllerProfile profile) {
        switch (dm) {
            case kSparkMaxBrushless:
                leftArray = new WCANSparkMax[left.length];
                rightArray = new WCANSparkMax[right.length];
                for (int i = 0; i < leftArray.length; i++) {
                    leftArray[i] = new WCANSparkMax(left[i], MotorType.kBrushless);
                }
                for (int i = 0; i < rightArray.length; i++) {
                    rightArray[i] = new WCANSparkMax(right[i], MotorType.kBrushless);
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

        this.differentialDrive = new DifferentialDrive(leftGroup, rightGroup);

    }

    public DifferentialDrive getDifferentialDrive() {
        return this.differentialDrive;
    }

    public DriveTrainDirrection getDirrection() {
        return this.dirrection;
    }

    public void setDirrection(DriveTrainDirrection dirrection) {
        this.dirrection = dirrection;
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