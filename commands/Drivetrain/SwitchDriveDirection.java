package frc.robot.core751.commands.drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.core751.subsystems.CamServer;
import frc.robot.core751.subsystems.DifferentialDriveTrain;
import frc.robot.core751.subsystems.DifferentialDriveTrain.DriveTrainDirection;
import frc.robot.core751.CoreConstants;

public class SwitchDriveDirection extends CommandBase {

    private DifferentialDriveTrain differentialDriveTrain;

    private CamServer camServer;
    private Integer frontCamera;
    private Integer backCamera;

    public SwitchDriveDirection(DifferentialDriveTrain differentialDriveTrain) {
        this(differentialDriveTrain,null,null,null);
    }
    public SwitchDriveDirection(DifferentialDriveTrain differentialDriveTrain,CamServer camServer, Integer camera) {
        this(differentialDriveTrain,camServer,camera,null);
    }
    public SwitchDriveDirection(DifferentialDriveTrain differentialDriveTrain,CamServer camServer,Integer frontCamera,Integer backCamera) {
        this.differentialDriveTrain = differentialDriveTrain;
        this.frontCamera = frontCamera;
        this.backCamera = backCamera;
        this.camServer = camServer;
        addRequirements(differentialDriveTrain);
    }

    @Override
    public void initialize() {
        
        if (this.differentialDriveTrain.getDirection() == DriveTrainDirection.FORWARD) {
            this.differentialDriveTrain.setDirection(DriveTrainDirection.BACKWARD);
            if(backCamera != null){
                camServer.switchCamera(CoreConstants.allCameras[1]);
            }
        }else{
            this.differentialDriveTrain.setDirection(DriveTrainDirection.FORWARD);
            if(frontCamera != null){
                camServer.switchCamera(CoreConstants.allCameras[0]);
            }
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}