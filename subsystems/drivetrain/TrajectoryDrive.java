package frc.robot.core751.subsystems.drivetrain;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CAN;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Robot;

import org.opencv.core.Core;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonPipelineResult;
import frc.robot.core751.CoreConstants;
import frc.robot.core751.wrappers.wDifferentialDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TrajectoryDrive extends SubsystemBase implements wDifferentialDrive {

  private final CANSparkMax leftMotorLeader = new CANSparkMax(CoreConstants.leftDrivetrainIDs[0],MotorType.kBrushless);
  private final CANSparkMax leftMotorFollower1 = new CANSparkMax(CoreConstants.leftDrivetrainIDs[1],MotorType.kBrushless);
  private final CANSparkMax leftMotorFollower2 = new CANSparkMax(CoreConstants.leftDrivetrainIDs[2],MotorType.kBrushless);
  
  private final CANSparkMax rightMotorLeader = new CANSparkMax(CoreConstants.rightDrivetrainIDs[0],MotorType.kBrushless);
  private final CANSparkMax rightMotorFollower1 = new CANSparkMax(CoreConstants.rightDrivetrainIDs[1],MotorType.kBrushless);
  private final CANSparkMax rightMotorFollower2 = new CANSparkMax(CoreConstants.rightDrivetrainIDs[2],MotorType.kBrushless);

  private final DifferentialDrive differentialDrive = new DifferentialDrive(leftMotorLeader, rightMotorLeader);
  private final DifferentialDriveOdometry odometry;

  private final RelativeEncoder leftEncoder;
  private final RelativeEncoder rightEncoder;

  private final Gyro m_gyro = CoreConstants.gyro;

  /**
   * Only works for 3-NEO drive as of now
   */
  public TrajectoryDrive(int[] leftDriveTrainIds,int[] rightDriveTrainIds,boolean invertLeft, boolean invertRight) {

    leftMotorLeader.setInverted(invertLeft);
    rightMotorLeader.setInverted(invertRight);

    rightMotorFollower1.follow(rightMotorLeader);
    rightMotorFollower2.follow(rightMotorLeader);

    leftMotorFollower1.follow(leftMotorLeader);
    leftMotorFollower2.follow(leftMotorLeader);

    leftEncoder = leftMotorLeader.getEncoder();
    rightEncoder = rightMotorLeader.getEncoder();
    leftEncoder.setPositionConversionFactor(CoreConstants.encoderDistancePerPulse);
    rightEncoder.setPositionConversionFactor(CoreConstants.encoderDistancePerPulse);
    leftEncoder.setVelocityConversionFactor(CoreConstants.encoderDistancePerPulse);
    rightEncoder.setVelocityConversionFactor(CoreConstants.encoderDistancePerPulse);

    resetEncoders();
    odometry = new DifferentialDriveOdometry(m_gyro.getRotation2d());
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Pose X",odometry.getPoseMeters().getX());
    SmartDashboard.putNumber("Pose Y",odometry.getPoseMeters().getY());
    SmartDashboard.putNumber("Pose Rotation",odometry.getPoseMeters().getRotation().getDegrees());

    SmartDashboard.putNumber("Left Speed", getWheelSpeeds().leftMetersPerSecond);
    SmartDashboard.putNumber("Right Speed", getWheelSpeeds().rightMetersPerSecond);

    odometry.update(
        m_gyro.getRotation2d(), leftEncoder.getPosition(), rightEncoder.getPosition());
  }

      /*-----------------/
     /-----Odomitry-----/
    /-----------------*/

  public Pose2d getPose() {
    return odometry.getPoseMeters();
  }

  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(leftEncoder.getVelocity() /60, rightEncoder.getVelocity()/60); //Get velocity is in RMP we need it in RPS
  }
  /**
   * Resets the odometry to the specified pose.
   * @param pose The pose to which to set the odometry.
   */
  public void resetOdometry(Pose2d pose) {
    resetEncoders();
    odometry.resetPosition(pose, m_gyro.getRotation2d());
  }

  /** Resets the drive encoders to currently read a position of 0. */
  public void resetEncoders() {
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
  }


  public double getAverageEncoderDistance() {
    return (leftEncoder.getPosition() + rightEncoder.getPosition()) / 2.0;
  }
  public RelativeEncoder getLeftEncoder() {
    return leftEncoder;
  }
  public RelativeEncoder getRightEncoder() {
    return rightEncoder;
  }

  /**
   * Sets the max output of the drive. Useful for scaling the drive to drive more slowly.
   *
   * @param maxOutput the maximum output to which the drive will be constrained
   */
  public void setMaxOutput(double maxOutput) {
    differentialDrive.setMaxOutput(maxOutput);
  }

  public void zeroHeading() {
    m_gyro.reset();
  }
  public double getHeading() {
    return m_gyro.getRotation2d().getDegrees();
  }

  /** @return degrees per second */
  public double getTurnRate() { 
    return -m_gyro.getRate(); 
  }

  /*----------------/
 /-----Driving-----/
/----------------*/

  public void tankDriveVolts(double leftVolts, double rightVolts) {
    leftMotorLeader.setVoltage(leftVolts);
    rightMotorLeader.setVoltage(rightVolts);
    differentialDrive.feed();
  }

  public PIDController leftController = new PIDController(0.5, 0, 0);
  public PIDController rightController = new PIDController(0.5, 0, 0);

  public static SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(CoreConstants.ksVolts, CoreConstants.kvVoltSecondsPerMeter, CoreConstants.kaVoltSecondsSquaredPerMeter);

  public void setWheelVelocity(double leftSpeed,double rightSpeed){
    leftController.setSetpoint(leftSpeed);
    rightController.setSetpoint(rightSpeed);

    double leftVolts = leftController.calculate(getWheelSpeeds().leftMetersPerSecond) ;
    double rightVolts = rightController.calculate(getWheelSpeeds().rightMetersPerSecond);

    leftVolts += feedforward.calculate(leftSpeed);
    rightVolts += feedforward.calculate(rightSpeed);

    leftMotorLeader.setVoltage(leftVolts);
    rightMotorLeader.setVoltage(rightVolts);

    differentialDrive.feed();

  }

  public void arcadeDrive(double fwd, double rot) {
    differentialDrive.arcadeDrive(fwd, rot);
  }

  @Override
  public DriveTrainDirection getDirection() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setDirection(DriveTrainDirection direction) {
    // TODO Auto-generated method stub
    return;
  }

  @Override
  public DifferentialDrive getDifferentialDrive() {
    return differentialDrive;
  }
}
