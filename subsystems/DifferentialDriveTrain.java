package frc.robot.core751.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.motorcontrol.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

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

    public MotorController[] leftArray;
    public MotorController[] rightArray;

    private MotorControllerGroup leftGroup;
    private MotorControllerGroup rightGroup;

    private DifferentialDrive differentialDrive;

    private MotorController[] controllers;

    private DriveTrainDirection direction = DriveTrainDirection.FORWARD;

    private static MotorControllerGroup arrayToGroup(MotorController[] sp) {
        //There might to be a better way to do this
        switch(sp.length) {
            case 4:
                return new MotorControllerGroup(sp[0], sp[1], sp[2], sp[3]);
            case 3:
                return new MotorControllerGroup(sp[0], sp[1], sp[2]);
            case 2:
                return new MotorControllerGroup(sp[0], sp[1]);
            case 1:
                return new MotorControllerGroup(sp[0]);
            default:
                return null;
        }
    }

    public DifferentialDriveTrain (int[] left, int[] right, driveMotor dm, boolean invertLeft, boolean invertRight) {
        switch (dm) {
            case kSparkMaxBrushless:
                leftArray = new CANSparkMax[left.length];
                rightArray = new CANSparkMax[right.length];
                for (int i = 0; i < leftArray.length; i++) {
                    leftArray[i] = new CANSparkMax(left[i], MotorType.kBrushless);
                }
                for (int i = 0; i < rightArray.length; i++) {
                    rightArray[i] = new CANSparkMax(right[i], MotorType.kBrushless);
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
        this.controllers = new MotorControllerGroup[this.leftArray.length + this.rightArray.length];
        
        this.leftGroup.setInverted(invertLeft);
        this.rightGroup.setInverted(invertRight);
        this.differentialDrive = new DifferentialDrive(leftGroup, rightGroup);

    }

    public DifferentialDriveTrain (int[] left, int[] right, driveMotor dm, SmartControllerProfile profile, boolean invertLeft, boolean invertRight) {
        switch (dm) {
            case kSparkMaxBrushless:
                leftArray = new CANSparkMax[left.length];
                rightArray = new CANSparkMax[right.length];


                for (int i = 0; i < leftArray.length; i++) {
                    leftArray[i] = new CANSparkMax(left[i], MotorType.kBrushless);
                    leftArray[i].setInverted(false);
                }
                for (int i = 0; i < rightArray.length; i++) {
                    rightArray[i] = new CANSparkMax(right[i], MotorType.kBrushless);
                    rightArray[i].setInverted(false);
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

        for (int i = 0; i < leftArray.length; i++) {
            this.controllers[i] = this.leftArray[i];
        }
        for (int i = 0; i < rightArray.length; i++) {
            this.controllers[leftArray.length+i] = this.rightArray[i];
        }

        switch (dm) {
            case kSparkMaxBrushless:
                for (MotorController sc : this.controllers) {
                    CANSparkMax sMax = (CANSparkMax)sc;
                    if (profile.idle != null) sMax.setIdleMode(profile.idle);
                    if (profile.rate != 0) sMax.setOpenLoopRampRate(profile.rate);
                    if (profile.rate != 0) sMax.setClosedLoopRampRate(profile.rate);
                    if (profile.limit != 0) sMax.setSmartCurrentLimit(profile.limit);
                    sMax.close();
                }
            default: //Add more cases if needed
            break;
        }

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
