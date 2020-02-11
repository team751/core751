package frc.robot.core751.commands.Drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.core751.subsystems.DifferentialDriveTrain;
import frc.robot.core751.subsystems.DifferentialDriveTrain.DriveTrainDirrection;

public class SwitchDriveDirrection extends CommandBase {

    private DifferentialDriveTrain differentialDriveTrain;

    public SwitchDriveDirrection(DifferentialDriveTrain differentialDriveTrain) {
        this.differentialDriveTrain = differentialDriveTrain;
        addRequirements(differentialDriveTrain);
    }

    @Override
    public void initialize() {
        if (this.differentialDriveTrain.getDirrection() == DriveTrainDirrection.FORWARD) {
            this.differentialDriveTrain.setDirrection(DriveTrainDirrection.BACKWARD);
        }else {
            this.differentialDriveTrain.setDirrection(DriveTrainDirrection.FORWARD);
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}