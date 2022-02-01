package frc.robot.core751.commands.drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.core751.subsystems.PIDDriveTrain;
import frc.robot.core751.wrappers.OverrideableJoystick;

public class PIDDrive extends CommandBase {
    private OverrideableJoystick driveStick;
    private PIDDriveTrain differentialDriveTrain;

    public PIDDrive(OverrideableJoystick driveStick, PIDDriveTrain differentialDriveTrain) {
        this.driveStick = driveStick;
        this.differentialDriveTrain = differentialDriveTrain;
        addRequirements(differentialDriveTrain);
    }

    @Override
    public void execute() {
        this.differentialDriveTrain.PIDDrive(driveStick.getRawAxis(0), driveStick.getRawAxis(1));
    }
}
