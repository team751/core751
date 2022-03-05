package frc.robot.core751.auton;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Filesystem;
import frc.robot.core751.CoreConstants;

public class TrajectoryHolder {

    Trajectory[] trajectories;
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

       

    String trajectoryJSON1 = "PickUpBall1.wpilib.json";
    Trajectory trajectory1 = new Trajectory();

    String trajectoryJSON2 = "PickUpBall2.wpilib.json";
    Trajectory trajectory2 = new Trajectory();

    String trajectoryJSON3 = "PickUpBall3.wpilib.json";
    Trajectory trajectory3 = new Trajectory();

    String trajectoryJSON4 = "PickUpBall4.wpilib.json";
    Trajectory trajectory4 = new Trajectory();



    try {
      Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON1);
      trajectory1 = TrajectoryUtil.fromPathweaverJson(trajectoryPath);

      trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON2);
      trajectory2 = TrajectoryUtil.fromPathweaverJson(trajectoryPath);

      trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON3);
      trajectory3 = TrajectoryUtil.fromPathweaverJson(trajectoryPath);

      trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON4);
      trajectory4 = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
    } catch (IOException ex) {}

        trajectories = new Trajectory[] {trajectory1,trajectory2,trajectory3,trajectory4};
        
    }
    /*Exampe Trajectory

    // Start at the origin facing the +X direction
    new Pose2d(0, 0, new Rotation2d(0)),

    // Pass through these two interior waypoints, making an 's' curve path
    List.of(new Translation2d(1, 1), new Translation2d(2, -1)),

    // End 3 meters straight ahead of where we started, facing forward
    new Pose2d(3,0, new Rotation2d(0)),

    */

    public Trajectory[] getTrajectory(){
        return trajectories;
    }
}

