package frc.robot.core751.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SmartDifferentialDriveTrain extends DifferentialDriveTrain {

    private CappedSpeedControllerGroup leftGroup;
    private CappedSpeedControllerGroup rightGroup;

    public SmartDifferentialDriveTrain(int[] left, int[] right, DifferentialDriveTrain.DriveMotor dm) {
        super(left, right, dm);
    }

    public void setSpeedCap(double cap) {
        if (cap > SmartDashboard.getNumber("Speed Cap Max", 0.9)) {
            cap = SmartDashboard.getNumber("Speed Cap Max", 0.9);
        }

        leftGroup.setSpeedCap(cap);
        rightGroup.setSpeedCap(cap);
    }

    public double getSpeedCap() {
        return Math.min(leftGroup.getSpeedCap(), rightGroup.getSpeedCap());
        // in case they are somehow diffrent
    }

}