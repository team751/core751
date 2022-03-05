package frc.robot.core751.auton;

import edu.wpi.first.wpilibj.drive.DifferentialDrive.WheelSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.core751.subsystems.drivetrain.TrajectoryDrive;

public class AdjustToAngleSimple extends CommandBase{
    
    private TrajectoryDrive trajectoryDrive;

    private double goalAngle;
    private double angleTolerance;

    private double maxSpeed;

    public AdjustToAngleSimple(TrajectoryDrive trajectoryDrive,double goalAngle, double angleTolerance){
        this.goalAngle = goalAngle;
        this.trajectoryDrive = trajectoryDrive;
        this.angleTolerance = angleTolerance;
        addRequirements(trajectoryDrive);
    }

    @Override
    public void execute() {
        double error = goalAngle - trajectoryDrive.getHeading();
        if(error > 180){
            error -= 360;
        } else if(error < -180){
            error += 360;
        }

        // -speed , speed;
        double speed = (Math.abs(error) * error) * 0.01  + error * 0.1; 

        trajectoryDrive.setWheelVelocity(-speed, speed);
        
    }

    @Override
    public void end(boolean interrupted) {
        trajectoryDrive.setWheelVelocity(0, 0);
    }
    
    @Override
    public boolean isFinished() {
        return (Math.abs(trajectoryDrive.getHeading() - goalAngle) <= angleTolerance);
    }

}
