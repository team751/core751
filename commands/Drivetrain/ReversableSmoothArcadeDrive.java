package frc.robot.core751.commands.Drivetrain;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.core751.subsystems.DifferentialDriveTrain;

public class ReversableSmoothArcadeDrive extends CommandBase {

    private Joystick driveStick;
    private DifferentialDriveTrain differentialDriveTrain;

    private double startYDistance;
    private double previousYDistance = 0;
    private double targetYDistance;
    private boolean inDeaccel = false;
    private double startStepDeaccelTime;

    public ReversableSmoothArcadeDrive(Joystick driveStick, DifferentialDriveTrain differentialDriveTrain) {
        this.driveStick = driveStick;
        this.differentialDriveTrain = differentialDriveTrain;
        addRequirements(differentialDriveTrain);
    }

    @Override
    public void execute() {
        int mod = differentialDriveTrain.getDirection().getMod();
        double x = driveStick.getX()*-1;
        double y = driveStick.getY()*mod;

        if(inDeaccel && 
           edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startStepDeaccelTime >= 
           Constants.maxSparkDeccelPeriod / Constants.sparkDeccelSteps) {
            startStepDeaccelTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp(); 

            previousYDistance += (targetYDistance - startYDistance) / Constants.sparkDeccelSteps;

            if(previousYDistance == targetYDistance) {
                inDeaccel = false;
            }
        } else {
            if((previousYDistance >= 0 && previousYDistance - x >= Constants.sparkDeccelThreshold) ||
               (previousYDistance <= -1 && y - previousYDistance >= Constants.sparkDeccelThreshold)) {
                inDeaccel = true;
                startYDistance = previousYDistance;
                targetYDistance = y;

                previousYDistance += (targetYDistance - startYDistance) / Constants.sparkDeccelSteps;

                startStepDeaccelTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp(); //seconds
            } else {
                previousYDistance = y;
            }
        }

        this.differentialDriveTrain.getDifferentialDrive().arcadeDrive(x, previousYDistance);
    }

}
