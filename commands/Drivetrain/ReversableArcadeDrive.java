package frc.robot.core751.commands.drivetrain;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.core751.CoreConstants;
import frc.robot.core751.subsystems.DifferentialDriveTrain;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ReversableArcadeDrive extends CommandBase {

    private Boolean smoothing = CoreConstants.smoothing;

    private Joystick driveStick;
    private DifferentialDriveTrain differentialDriveTrain;

    private double startYDistance;
    private double previousYDistance = 0;
    private double targetYDistance;
    private boolean inDeaccel = false;
    private double startStepDeaccelTime;

    private double speedCap;

    public ReversableArcadeDrive(Joystick driveStick, DifferentialDriveTrain differentialDriveTrain) {
        this.driveStick = driveStick;
        this.differentialDriveTrain = differentialDriveTrain;
        addRequirements(differentialDriveTrain);
    }

    @Override
    public void execute() {
        int mod = differentialDriveTrain.getDirection().getMod();
        double x = driveStick.getX()*-1;
        double y = driveStick.getY()*mod;

        speedCap = SmartDashboard.getNumber("Speed Cap", 1.0);
        if(smoothing){ //With Smoothing
            if(inDeaccel && edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startStepDeaccelTime >= CoreConstants.maxSparkDeccelPeriod / CoreConstants.sparkDeccelSteps) {
                startStepDeaccelTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp(); 

                previousYDistance += (targetYDistance - startYDistance) / CoreConstants.sparkDeccelSteps;

                if(previousYDistance == targetYDistance) {
                    inDeaccel = false;
                }
            } else {
                if((previousYDistance >= 0 && previousYDistance - x >= CoreConstants.sparkDeccelThreshold) || (previousYDistance <= -1 && y - previousYDistance >= CoreConstants.sparkDeccelThreshold)) {
                    inDeaccel = true;
                    startYDistance = previousYDistance;
                    targetYDistance = y;

                    previousYDistance += (targetYDistance - startYDistance) / CoreConstants.sparkDeccelSteps;

                    startStepDeaccelTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp(); //seconds
                } else {
                    previousYDistance = y;
                }
            }

            this.differentialDriveTrain.getDifferentialDrive().arcadeDrive(x, previousYDistance);
        } else { //No Smoothing
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

}
