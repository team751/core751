package frc.robot.core751.commands.Drivetrain;

import java.util.List;

import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj.controller.PIDController; 
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import frc.robot.core751.commands.i2cmultiplexer.MultiplexedI2CCommandBase;
import frc.robot.core751.subsystems.DifferentialDriveTrain;
import frc.robot.core751.subsystems.I2CMultiplexer;
import frc.robot.Constants;

public class PIDTrajectoryDrive extends MultiplexedI2CCommandBase {
    private DifferentialDriveTrain differentialDriveTrain;
    private SequentialCommandGroup ramseteCommand;
    private Trajectory executingTrajectory;

    public PIDTrajectoryDrive(DifferentialDriveTrain differentialDriveTrain,
                              Pose2d origin, 
                              List<Translation2d> waypoints, Pose2d endpoint,
                              I2CMultiplexer multiplexer, 
                              int multiplexerI2CDeviceId) {
        super(multiplexer, multiplexerI2CDeviceId);

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
            this.differentialDriveTrain::getPose,
            new RamseteController(Constants.ramseteB, Constants.ramseteZeta),
            new SimpleMotorFeedforward(Constants.ksVolts,
                                       Constants.kvVoltSecondsPerMeter,
                                       Constants.kaVoltSecondsSquaredPerMeter),
            diffDriveKinematics,
            this.differentialDriveTrain::getWheelSpeeds,
            new PIDController(Constants.kPDriveVel, 0, 0),
            new PIDController(Constants.kPDriveVel, 0, 0),
            this.differentialDriveTrain::setVolts,
            this.differentialDriveTrain
        );

        this.ramseteCommand = ramseteCommand.andThen(() -> this.differentialDriveTrain.setVolts(0, 0));

        addRequirements(differentialDriveTrain);
    }

    @Override
    public void execute() {
        super.execute();

        ramseteCommand.execute();
    }

    @Override
    public boolean isFinished() {
        return ramseteCommand.isFinished();
    }

}