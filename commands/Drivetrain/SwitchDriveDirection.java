package frc.robot.core751.commands.Drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.core751.subsystems.DifferentialDriveTrain;
import frc.robot.core751.subsystems.DifferentialDriveTrain.DriveTrainDirection;
import frc.robot.core751.subsystems.Camera;

public class SwitchDriveDirection extends CommandBase {

    private DifferentialDriveTrain differentialDriveTrain;
    private Integer frontCamera;
    private Integer backCamera;

    public SwitchDriveDirection(DifferentialDriveTrain differentialDriveTrain){
        this(differentialDriveTrain,null,null);
    }
    public SwitchDriveDirection(DifferentialDriveTrain differentialDriveTrain, Integer camera){
        this(differentialDriveTrain,camera,camera);
    }
    public SwitchDriveDirection(DifferentialDriveTrain differentialDriveTrain, Integer frontCamera, Integer backCamera) {
        this.differentialDriveTrain = differentialDriveTrain;
        this.frontCamera = frontCamera;
        this.backCamera = backCamera;
        addRequirements(differentialDriveTrain);
    }

    @Override
    public void initialize() {
        
        if (this.differentialDriveTrain.getDirection() == DriveTrainDirection.FORWARD) {
            this.differentialDriveTrain.setDirection(DriveTrainDirection.BACKWARD);
            if(backCamera != null){
                Camera.switchCamera(backCamera);
            }
        }else {
            this.differentialDriveTrain.setDirection(DriveTrainDirection.FORWARD);
            if(frontCamera != null){
                Camera.switchCamera(frontCamera);
            }
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}