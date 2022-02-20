package frc.robot.core751.commands.drivetrain; 

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.core751.subsystems.drivetrain.DifferentialDriveTrain;

public class ArcadeDrive extends CommandBase {

    private Joystick driveStick;
    private DifferentialDriveTrain differentialDriveTrain;

    public ArcadeDrive(Joystick driveStick, DifferentialDriveTrain differentialDriveTrain) {
        this.driveStick = driveStick;
        this.differentialDriveTrain = differentialDriveTrain;
        addRequirements(differentialDriveTrain);
    }

    @Override
    public void execute() {
        this.differentialDriveTrain.getDifferentialDrive().arcadeDrive(driveStick.getX(), driveStick.getY());
        SmartDashboard.putNumber("direction", this.differentialDriveTrain.getDirection().getMod());
    }

}