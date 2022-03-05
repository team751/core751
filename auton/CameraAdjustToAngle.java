package frc.robot.core751.auton;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.core751.subsystems.camera.PhotonVision;
import frc.robot.core751.subsystems.drivetrain.TrajectoryDrive;

public class CameraAdjustToAngle extends CommandBase {
    
    private TrajectoryDrive trajectoryDrive;
    private PhotonVision photonVision;

    private double lastDistance;
    private double yaw = photonVision.getYaw();
    private double goalAngle;

    private double deviationTolerance; 
    private double PIDtollerance;
    private double maxAnglePerSecond;

    private AdjustToAngle adjustToAngle;


    private enum Status{
        Searching,
        Running;
    }

    private Status status = Status.Searching;



    public CameraAdjustToAngle(TrajectoryDrive trajectoryDrive, PhotonVision photonVision,double PIDtolerance,double maxAnglePerSecond,double deviationTolerance){
        addRequirements(trajectoryDrive);
        this.maxAnglePerSecond = maxAnglePerSecond;
        this.deviationTolerance = deviationTolerance;
        this.PIDtollerance = PIDtollerance;
        
    }

    @Override
    public void execute() {
        if(status == Status.Searching){
            double distance = photonVision.getDistance();
            yaw = photonVision.getYaw();
            double deviation = Math.abs(lastDistance - photonVision.getDistance()); //Maybe I need to check more then a single frame deviation?
            lastDistance = distance;
            if(deviation <= deviationTolerance){
                status = Status.Running;
            }
        } else if(adjustToAngle == null){
            goalAngle = trajectoryDrive.getHeading() - yaw;
            //adjustToAngle = new AdjustToAngle(trajectoryDrive, goalAngle, PIDtollerance, maxAnglePerSecond);
        }
        
    }

    @Override
    public boolean isFinished() {
        if(status == Status.Running){
            return adjustToAngle.isFinished();
        }else{
            return false;
        }
        
    }

    @Override
    public void end(boolean interrupted) {
        if(!interrupted){
            trajectoryDrive.resetOdometry(new Pose2d(lastDistance,0,new Rotation2d(0)));
        }
    }

}
