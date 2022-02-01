package frc.robot.core751.subsystems;

import java.util.spi.CurrencyNameProvider;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.motorcontrol.*;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.core751.wrappers.RevEncoder;

public class PIDDriveTrain extends SubsystemBase {

    public enum DriveTrainDirection {
        FORWARD(1),
        BACKWARD(-1);

        private int mod;
        public int getMod(){return this.mod;}
        private DriveTrainDirection(int mod) {
            this.mod = mod;
        }

    }

    public CANSparkMax[] leftArray;
    public CANSparkMax[] rightArray;

    private MotorControllerGroup leftGroup;
    private MotorControllerGroup rightGroup;

    private DifferentialDrive differentialDrive;

    private MotorController[] controllers;

    private DriveTrainDirection direction = DriveTrainDirection.FORWARD;

    //PID
    private PIDController leftPIDController; 
    private double currentLeftSpeed = 0;

    private PIDController rightPIDController;
    private double currentRightSpeed = 0;

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

    public PIDDriveTrain (int[] left, int[] right, SmartControllerProfile profile, boolean invertLeft, boolean invertRight,PIDController leftPIDController,PIDController righPIDController) {
        
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

        //Grouping
        this.leftGroup = arrayToGroup(leftArray);
        this.rightGroup = arrayToGroup(rightArray);
        this.leftGroup.setInverted(invertLeft);
        this.rightGroup.setInverted(invertRight);

        //PID
        this.leftPIDController = leftPIDController;
        this.rightPIDController = rightPIDController;


        for (int i = 0; i < leftArray.length; i++) {
            this.controllers[i] = this.leftArray[i];
        }
        for (int i = 0; i < rightArray.length; i++) {
            this.controllers[leftArray.length+i] = this.rightArray[i];
        }

        for (MotorController sc : this.controllers) {
            CANSparkMax sMax = (CANSparkMax)sc;
            if (profile.idle != null) sMax.setIdleMode(profile.idle);
            if (profile.rate != 0) sMax.setOpenLoopRampRate(profile.rate);
            if (profile.rate != 0) sMax.setClosedLoopRampRate(profile.rate);
            if (profile.limit != 0) sMax.setSmartCurrentLimit(profile.limit);
            sMax.close();
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


    // PID driving functions.
    public void PIDDrive(double xAxis, double yAxis){  
        double mathedY = Math.pow(Math.cos(yAxis),2);   //nice naming lol
        double mathedX = Math.pow(Math.sin(xAxis),2);

        leftPIDController.setSetpoint(mathedY+mathedX);
        rightPIDController.setSetpoint(mathedY-mathedX);
    }

    @Override
    public void periodic() {
       currentLeftSpeed = leftPIDController.calculate(currentLeftSpeed);
       currentRightSpeed = rightPIDController.calculate(currentRightSpeed);
       leftGroup.set(currentLeftSpeed);
       rightGroup.set(currentRightSpeed);
    }

    @Override
    public void simulationPeriodic() {
        currentLeftSpeed = leftPIDController.calculate(currentLeftSpeed);
        currentRightSpeed = rightPIDController.calculate(currentRightSpeed);
        System.out.println("Left: " + currentLeftSpeed);
        System.out.println("Right: " + currentRightSpeed);
    }

}
