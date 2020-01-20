package frc.robot.core751.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.core751.wrappers.WCANSparkMax;

public class SmartDifferentialDriveTrain extends SubsystemBase {

    public enum smartDriveMotor {
        kSparkMaxBrushless,
        kPWMVictorSPX,
    }

    private SpeedController[] leftArray;
    private SpeedController[] rightArray;

    private CappedSpeedControllerGroup leftGroup;
    private CappedSpeedControllerGroup rightGroup;

    private DifferentialDrive differentialDrive;

    private static CappedSpeedControllerGroup arrayToGroup(SpeedController[] sp) {
        //There has to be a better way to do this
        switch(sp.length) {
            case 4:
                return new CappedSpeedControllerGroup(sp[0], sp[1], sp[2], sp[3]);
            case 3:
                return new CappedSpeedControllerGroup(sp[0], sp[1], sp[2]);
            case 2:
                return new CappedSpeedControllerGroup(sp[0], sp[1]);
            default:
                return new CappedSpeedControllerGroup(sp[0]);
        }
    }

    public SmartDifferentialDriveTrain (int[] left, int[] right, smartDriveMotor dm) {
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

        this.differentialDrive = new DifferentialDrive(leftGroup, rightGroup);

    }

    public DifferentialDrive getDifferentialDrive() {
        return this.differentialDrive;
    }
    public void setSpeedCap(double cap) {
        if(cap > SmartDashboard.getNumber("Speed Cap Max", 0.9)){
            cap = SmartDashboard.getNumber("Speed Cap Max", 0.9);
        }
        
        leftGroup.setSpeedCap(cap);
        rightGroup.setSpeedCap(cap);
    }
    public double getSpeedCap(){
        return Math.min(leftGroup.getSpeedCap(),rightGroup.getSpeedCap());//in case they are somehow diffrent
    }


}