package frc.robot.core751.commands.drivetrain;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.core751.CoreConstants;
import frc.robot.core751.subsystems.drivetrain.DifferentialDriveTrain;
import frc.robot.core751.wrappers.wDifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ReversableArcadeDrive extends CommandBase {

    private Boolean smoothing = CoreConstants.smoothing;

    private Joystick driveStick;
    private wDifferentialDrive differentialDriveTrain;

    private double startYDistance;
    private double previousYDistance = 0;
    private double targetYDistance;
    private boolean inDeaccel = false;
    private double startStepDeaccelTime;

    private double speedCap;

    /**
     * @param driveStick
     * @param differentialDriveTrain you need to have this subsystem impliment wDifferentialDrive
     */
    public ReversableArcadeDrive(Joystick driveStick, SubsystemBase differentialDriveTrain) {
        this.driveStick = driveStick;
        this.differentialDriveTrain = (wDifferentialDrive)differentialDriveTrain;
        addRequirements(differentialDriveTrain);
        SmartDashboard.putNumber("Speed Cap", CoreConstants.speedCap);
    }

    @Override
    public void execute() {
        //int mod = differentialDriveTrain.getDirection().getMod();
        int mod = 1;
        double x = driveStick.getY()*-1;
        double y = driveStick.getX()*mod;

        speedCap = SmartDashboard.getNumber("Speed Cap", CoreConstants.speedCap);
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
    
            this.differentialDriveTrain.getDifferentialDrive().arcadeDrive(x, y*0.75);
        }
    }

}
