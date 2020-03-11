package frc.robot.core751.commands.Drivetrain;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.core751.subsystems.DifferentialDriveTrain;
import frc.robot.core751.wrappers.OverrideableJoystick;

public class ReversableArcadeDrive extends CommandBase {

    private double startYDistance;
    private double previousYDistance = 0;
    private double targetYDistance;
    private boolean inDeaccel = false;
    private double startStepDeaccelTime;

    private Joystick driveStick;
    private DifferentialDriveTrain differentialDriveTrain;
    private double speedCap;

    public ReversableArcadeDrive(OverrideableJoystick driveStick, DifferentialDriveTrain differentialDriveTrain) {
        this.driveStick = driveStick;
        this.differentialDriveTrain = differentialDriveTrain;
        this.speedCap = 1.0;

        SmartDashboard.putNumber("Speed Cap", this.speedCap);
        addRequirements(differentialDriveTrain);
    }

    @Override
    public void execute() {
        int mod = differentialDriveTrain.getDirection().getMod();

        double x = -driveStick.getRawAxis(4);
        double y = driveStick.getRawAxis(5)*mod;

        speedCap = SmartDashboard.getNumber("Speed Cap", 1.0);

        if(x < 0) {
            x = Math.max(-speedCap, x);
        } else {
            x = Math.min(speedCap, x);
        }

        if(y < 0) {
            y = Math.max(-speedCap, y);
        } else {
            y = Math.min(speedCap, y);
        }

        this.differentialDriveTrain.getDifferentialDrive().arcadeDrive(x, y);
    }

}
