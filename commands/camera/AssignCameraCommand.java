package frc.robot.core751.commands.camera;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.core751.CoreConstants;
import frc.robot.core751.subsystems.camera.CamServer;

public class AssignCameraCommand extends CommandBase {
    private int m_device;
    private CamServer m_cameraSubsystem;

    public AssignCameraCommand(CamServer cameraSubsystem, int initialDevice) {
        m_device = initialDevice;
        m_cameraSubsystem = cameraSubsystem;
        addRequirements(cameraSubsystem);

        SmartDashboard.putNumber("Camera Device Number", m_device);
    }
    
    @Override
    public void execute() {
        double deviceSmartDashboard = SmartDashboard.getNumber("Camera Device Number", 
                                                               m_device);

        if(deviceSmartDashboard != m_device) {
            m_device = (int)deviceSmartDashboard;

            m_cameraSubsystem.switchCamera(CoreConstants.allCameras[m_device]);
        }
    }

    public boolean isFinished() {
        return false;
    }

    public int getDeviceNumber() {
        return m_device;
    }
}
