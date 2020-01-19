package frc.robot.core751.commands.Drivetrain;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.core751.subsystems.DifferentialDriveTrain;

import frc.robot.Constants;

public class PIDTrajectoryDrive extends CommandBase {
    private DifferentialDriveTrain differentialDriveTrain;
    private Trajectory executingTrajectory;

    public PIDTrajectoryDrive(DifferentialDriveTrain differentialDriveTrain, Pose2d origin, 
                              List<Translation2D> waypoints, Pose2d endpoint) {
        DifferentialDriveKinematics diffDriveKinematics = new DifferentialDriveKinematics(
                                                                    Constants.trackWidthMeters);

        DifferentialDriveVoltageConstraint autoVoltageConstraint = 
                                new DifferentialDriveVoltageConstraint(
                                    new SimpleMotorFeedforward(Constants.ksVolts, 
                                                               Constants.kvVoltSecondsPerMeter,
                                                               Constants.kaVoltSecondsSquaredPerMeter), 
                                    diffDriveKinematics, 10);

        TrajectoryConfig trajectoryConfig = new TrajectoryConfig(Constants.maxPIDTrajectoryDriveVolts, 
                                                                 Constants.maxPIDTrajectoryDriveAcceleration);
        
        trajectoryConfig = trajectoryConfig.setKinematics(diffDriveKinematics).addConstraint(autoVoltageConstraint);

        this.differentialDriveTrain = differentialDriveTrain;
        this.executingTrajectory = TrajectoryGenerator.generateTrajectory(origin, waypoints, 
                                                                          endpoint, trajectoryConfig);

        RamseteCommand ramseteCommand = new RamseteCommand(
            executingTrajectory,
            m_robotDrive::getPose,
            new RamseteController(AutoConstants.kRamseteB, AutoConstants.kRamseteZeta),
            new SimpleMotorFeedforward(DriveConstants.ksVolts,
                                        DriveConstants.kvVoltSecondsPerMeter,
                                        DriveConstants.kaVoltSecondsSquaredPerMeter),
            DriveConstants.kDriveKinematics,
            m_robotDrive::getWheelSpeeds,
            new PIDController(DriveConstants.kPDriveVel, 0, 0),
            new PIDController(DriveConstants.kPDriveVel, 0, 0),
            // RamseteCommand passes volts to the callback
            m_robotDrive::tankDriveVolts,
            m_robotDrive
        );

        // Run path following command, then stop at the end.
        return ramseteCommand.andThen(() -> m_robotDrive.tankDriveVolts(0, 0));

        addRequirements(differentialDriveTrain);
    }

    @Override
    public void execute() {
        //
    }

}