package frc.robot.core751.commands.Drivetrain;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.core751.subsystems.SmartDifferentialDriveTrain;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SmartArcadeDrive extends CommandBase {

    private Joystick driveStick;
    private SmartDifferentialDriveTrain differentialDriveTrain;
    private double divder;


/*
To do:

Add connectivity b/x other parts

*/

    public SmartArcadeDrive(int port, SmartDifferentialDriveTrain differentialDriveTrain) {
        this.driveStick = new Joystick(port);
        this.differentialDriveTrain = differentialDriveTrain;
        addRequirements(differentialDriveTrain);
    }
    

    @Override
    public void execute() {
        this.divder = SmartDashboard.getNumber("Speed Divider", 11);
        if (SmartDashboard.getBoolean("Anit-Brownout mode",true)){
            this.differentialDriveTrain.setSpeedCap(RobotController.getBatteryVoltage()/divder);
        } else{
            this.differentialDriveTrain.setSpeedCap( SmartDashboard.getNumber("Speed Cap Max", 0.9));
        }
 
        SmartDashboard.putNumber("Speed Cap",(this.differentialDriveTrain.getSpeedCap()));

        this.differentialDriveTrain.getDifferentialDrive().arcadeDrive(-driveStick.getY(), driveStick.getX());
    }


}
