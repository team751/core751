package frc.robot.core751.auton;

import java.util.List;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.math.util.Units;
import frc.robot.core751.CoreConstants;

public class TrajectoryHolder {

    Trajectory trajectory;
    public TrajectoryHolder(){
          //!!This is NOT a speed cap, you can and will go over this limit. It is just for planning the trajectory.
          DifferentialDriveVoltageConstraint autoVoltageConstraint = //This makes sure we don't plan to accelerate too fast. 
          new DifferentialDriveVoltageConstraint(
              new SimpleMotorFeedforward(
                  CoreConstants.ksVolts,
                  CoreConstants.kvVoltSecondsPerMeter,
                  CoreConstants.kaVoltSecondsSquaredPerMeter),
              CoreConstants.driveKinematics,
              CoreConstants.planningVoltageContraint);
  
          // Create config for trajectory
          TrajectoryConfig config =
          new TrajectoryConfig(
                  CoreConstants.maxSpeedMetersPerSecond,
                  CoreConstants.maxAccelerationMetersPerSecondSquared)
              // Add kinematics to ensure max speed is actually obeyed
              .setKinematics(CoreConstants.driveKinematics)
              // Apply the voltage constraint
              .addConstraint(autoVoltageConstraint);

        this.trajectory =
        TrajectoryGenerator.generateTrajectory(
            new Pose2d(0, 0, new Rotation2d(0)),
            List.of(new Translation2d(1, 1), new Translation2d(2, -1)),
            new Pose2d(3,0, new Rotation2d(0)),
            config);
        
    }
    /*Exampe Trajectory

    // Start at the origin facing the +X direction
    new Pose2d(0, 0, new Rotation2d(0)),

    // Pass through these two interior waypoints, making an 's' curve path
    List.of(new Translation2d(1, 1), new Translation2d(2, -1)),

    // End 3 meters straight ahead of where we started, facing forward
    new Pose2d(3,0, new Rotation2d(0)),

    */

    public Trajectory getTrajectory(){
        return trajectory;
    }
}

