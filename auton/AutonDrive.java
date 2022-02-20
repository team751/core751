package frc.robot.core751.auton;


import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.core751.CoreConstants;
import frc.robot.core751.subsystems.drivetrain.TrajectoryDrive;

public class AutonDrive{

    TrajectoryDrive trajectoryDrive;
    Trajectory trajectory;

    RamseteController ramseteController;
    CommandBase ramseteCommand;


    public AutonDrive(TrajectoryDrive trajectoryDrive,Trajectory trajectory){
        this.trajectoryDrive = trajectoryDrive;
        this.trajectory = trajectory;

        RamseteController controller =  new RamseteController(CoreConstants.ramseteB, CoreConstants.ramseteZeta);
        PIDController pidControllerLeft = new PIDController(CoreConstants.kPDriveVel,0,0);
        PIDController pidControllerRight = new PIDController(CoreConstants.kPDriveVel,0,0);
        RamseteCommand command = //Stolen from WPI them selves
            new RamseteCommand(
                trajectory,
                trajectoryDrive::getPose,
                controller,
                new SimpleMotorFeedforward(
                    CoreConstants.ksVolts,
                    CoreConstants.kvVoltSecondsPerMeter,
                    CoreConstants.kaVoltSecondsSquaredPerMeter),
                CoreConstants.driveKinematics,
                trajectoryDrive::getWheelSpeeds,
                pidControllerLeft,
                pidControllerRight,
                // RamseteCommand passes volts to the callback
                (leftVolts,rightVolts) -> {
                    trajectoryDrive.tankDriveVolts(leftVolts, rightVolts);
                },
                trajectoryDrive);
    
    
        // Reset odometry to the starting pose of the trajectory.
        trajectoryDrive.resetOdometry(trajectory.getInitialPose());
    
        // Run path following command, then stop at the end.\
        ramseteCommand = command.andThen(() -> trajectoryDrive.tankDriveVolts(0, 0));

    }

    public CommandBase getCommand(){
        return ramseteCommand;
    }

    
}
