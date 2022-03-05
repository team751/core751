package frc.robot.core751.auton;

import javax.swing.text.TabExpander;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;
import frc.robot.core751.CoreConstants;
import frc.robot.core751.subsystems.drivetrain.TrajectoryDrive;

public class AdjustToAngle extends ProfiledPIDCommand {

    public static TrajectoryDrive trajectoryDrive; //I would prer this is nonstatic but the code is being dumb
    public static SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(CoreConstants.ksVolts, CoreConstants.kvVoltSecondsPerMeter, CoreConstants.kaVoltSecondsSquaredPerMeter);

    public AdjustToAngle(TrajectoryDrive trajectoryDrive,double goalAngle,double tolerance,double maxAnglePerSecond,double maxAcceleration){ //goal angle in degrees
        super(new ProfiledPIDController(0.001, 0, 0,new TrapezoidProfile.Constraints(
            maxAnglePerSecond,
            maxAcceleration)), 
        // Close loop on heading
        trajectoryDrive::getHeading,
        // Set reference to target
        goalAngle,
        // Pipe output to turn robot
        (output, setpoint) -> AdjustToAngle.feedForward(output),
        // Require the drive
        trajectoryDrive);

        this.trajectoryDrive = trajectoryDrive;
        getController().enableContinuousInput(-180, 180);
        getController().setTolerance(tolerance, maxAnglePerSecond);
    }

    static DifferentialDriveKinematics kinematics =
    new DifferentialDriveKinematics(CoreConstants.trackwidthMeters);
  
    // Example chassis speeds: 2 meters per second linear velocity,
    // 1 radian per second angular velocity.
    ChassisSpeeds chassisSpeeds = new ChassisSpeeds(0, 0, 1.0);
  
    // Convert to wheel speeds
    DifferentialDriveWheelSpeeds wheelSpeeds = kinematics.toWheelSpeeds(chassisSpeeds);
  
    // Left velocity
    double leftVelocity = wheelSpeeds.leftMetersPerSecond;
    
    // Right velocity
    double rightVelocity = wheelSpeeds.rightMetersPerSecond;
    public static void feedForward(double speed){
        double rads = Units.radiansToDegrees(speed);
        ChassisSpeeds chassisSpeeds = new ChassisSpeeds(0,0,rads);
        System.out.println("Degree per Seconds: " + speed);
        SmartDashboard.putNumber("Derees per Second", speed);
        trajectoryDrive.setWheelVelocity(kinematics.toWheelSpeeds(chassisSpeeds).leftMetersPerSecond, kinematics.toWheelSpeeds(chassisSpeeds).rightMetersPerSecond);
        //SmartDashboard.putNumber("PID value", speed);
        //trajectoryDrive.setWheelVelocity(1, -1);
    }

    @Override
    public boolean isFinished() {
        return getController().atSetpoint();
    }
}
