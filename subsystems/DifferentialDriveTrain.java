package frc.robot.core751.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.core751.wrappers.WCANSparkMax;

public class DifferentialDriveTrain extends SubsystemBase {

    public enum DriveMotor {
        kSparkMaxBrushless, kPWMVictorSPX,
    }

    private CANEncoder[] leftEncoderArray;
    private CANEncoder[] rightEncoderArray;

    private SpeedController[] leftArray;
    private SpeedController[] rightArray;

    private SpeedControllerGroup leftGroup;
    private SpeedControllerGroup rightGroup;

    private DifferentialDrive differentialDrive;

    private static SpeedControllerGroup arrayToGroup(SpeedController[] sp) {
        // There has to be a better way to do this
        switch (sp.length) {
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

    public DifferentialDriveTrain(int[] left, int[] right, DriveMotor dm) {
        switch (dm) {
        case kSparkMaxBrushless:
            leftArray = new WCANSparkMax[left.length];
            leftEncoderArray = new CANEncoder[left.length];
            rightArray = new WCANSparkMax[right.length];
            rightEncoderArray = new CANEncoder[right.length];
            for (int i = 0; i < leftArray.length; i++) {
                leftArray[i] = new WCANSparkMax(left[i], MotorType.kBrushless);
            }
            for (int i = 0; i < rightArray.length; i++) {
                rightArray[i] = new WCANSparkMax(right[i], MotorType.kBrushless);
            }
            for (int i = 0; i < leftEncoderArray.length; ++i) {
                leftEncoderArray[i] = ((WCANSparkMax) (leftArray[i])).getEncoder();
            }
            for (int i = 0; i < rightEncoderArray.length; ++i) {
                rightEncoderArray[i] = ((WCANSparkMax) (rightArray[i])).getEncoder();
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

}