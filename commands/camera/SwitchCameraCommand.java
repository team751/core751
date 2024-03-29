package frc.robot.core751.commands.camera;

import edu.wpi.first.cscore.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.core751.CoreConstants;
import frc.robot.core751.subsystems.camera.CamServer;

public class SwitchCameraCommand extends CommandBase {
    private int m_device;
    private CamServer m_cameraSubsystem;

    public SwitchCameraCommand(CamServer cameraSubsystem, int initialDevice) {
        m_device = initialDevice;
        m_cameraSubsystem = cameraSubsystem;
        addRequirements(cameraSubsystem);

        SmartDashboard.putData(this);
    }

    public void initialize() {
        int cameraNum = UsbCamera.enumerateUsbCameras().length;
        
        if(m_device + 1 >= cameraNum) {
            m_device = 0;
        } else {
            ++m_device;
        }
        
        m_cameraSubsystem.switchCamera(CoreConstants.allCameras[m_device]);

        SmartDashboard.putNumber("Total Number of Cameras", cameraNum);
        SmartDashboard.putNumber("Camera Device Number", m_device);
    }

    public boolean isFinished() {
        return true;
    }

    public int getDeviceNumber() {
        return m_device;
    }
}
