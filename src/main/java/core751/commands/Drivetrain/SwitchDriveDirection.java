package frc.robot.core751.commands.Drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.core751.subsystems.DifferentialDriveTrain;
import frc.robot.core751.subsystems.DifferentialDriveTrain.DriveTrainDirection;

public class SwitchDriveDirection extends CommandBase {

    private DifferentialDriveTrain differentialDriveTrain;

    public SwitchDriveDirection(DifferentialDriveTrain differentialDriveTrain) {
        this.differentialDriveTrain = differentialDriveTrain;
        addRequirements(differentialDriveTrain);
    }

    @Override
    public void initialize() {
        if (this.differentialDriveTrain.getDirection() == DriveTrainDirection.FORWARD) {
            this.differentialDriveTrain.setDirection(DriveTrainDirection.BACKWARD);
        }else {
            this.differentialDriveTrain.setDirection(DriveTrainDirection.FORWARD);
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}