package frc.robot.core751.commands.drivetrain;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.core751.CoreConstants;
import frc.robot.core751.subsystems.drivetrain.TrajectoryDrive;

public class PIDdrive extends CommandBase {
    
    private TrajectoryDrive trajectoryDrive;
    private double speedCap;
    private Joystick joystick = CoreConstants.driverStick;

    /**
     * 
     * @param trajectoryDrive
     * @param speedCap IN METERS PER SECOND!!
     */
    public PIDdrive(TrajectoryDrive trajectoryDrive, double speedCap){
        this.trajectoryDrive = trajectoryDrive;
        this.speedCap = speedCap;
        
        addRequirements(trajectoryDrive);
    }

    @Override
    public void execute() {
        double x = -joystick.getX();
        double y = -joystick.getY();

        if (Math.abs(x) <= 0.1){
            x = 0;
        }
        if (Math.abs(y) <= 0.1){
            y = 0;
        }

        double leftSpeed = y + x;
        double rightSpeed = y - x;

        // //"Normilize" I know it's not acutally normilized but shhh. 
        if(leftSpeed != 0 || rightSpeed != 0){
            leftSpeed /= Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
            rightSpeed /= Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
        }
      
        leftSpeed *= speedCap;
        rightSpeed *= speedCap;

        SmartDashboard.putNumber("leftVelocityIdeal", leftSpeed);
        SmartDashboard.putNumber("rightVelocityIdeal", rightSpeed);

        trajectoryDrive.setWheelVelocity(leftSpeed, rightSpeed);
    }

}
