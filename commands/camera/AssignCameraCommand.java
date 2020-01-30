package frc.robot.core751.commands.camera;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AssignCameraCommand extends CommandBase {
    private int m_device;
    private UsbCamera[] cameras;
    private VideoSink server;

    public AssignCameraCommand(UsbCamera[] cameras, int initialDevice, VideoSink server) {
        m_device = initialDevice;
        this.cameras = cameras;
        this.server = server;
        
        SmartDashboard.putNumber("Camera Device Number", m_device);
        SmartDashboard.putData(this);
    }
    
    
    @Override
    public void execute() {
        double deviceSmartDashboard = SmartDashboard.getNumber("Camera Device Number", 
                                                               m_device);

        if(deviceSmartDashboard != m_device) {
            m_device = (int)deviceSmartDashboard;
            try{
                server.setSource(cameras[m_device]);
            } catch(Exception e){

            }
         }
    }

    public boolean isFinished() {
        return false;
    }

    public int getDeviceNumber() {
        return m_device;
    }
}
