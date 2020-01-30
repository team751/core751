package frc.robot.core751.commands.camera;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class SwitchCameraCommand extends CommandBase {
    private int m_device;
    private UsbCamera[] cameras;
    private VideoSink server;

    public SwitchCameraCommand(UsbCamera[] cameras, int initialDevice, VideoSink server) {
        m_device = initialDevice;
        this.cameras = cameras;
        this.server = server;
        
        SmartDashboard.putData(this);
    }

    public void initialize() {
        int cameraNum = cameras.length;

        if(m_device + 1 >= cameraNum) {
            m_device = 0;
        } else {
            ++m_device;
        }
        server.setSource(cameras[m_device]);

        SmartDashboard.putNumber("Total Number of Cameras", cameraNum);
        SmartDashboard.putNumber("Camera Device Number", m_device);
    }

    public boolean isFinished() {
        return true;
    }

}
