package frc.robot.core751.subsystems;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Camera extends SubsystemBase {
    int m_activeDevice;
    static UsbCamera[] m_usbCameras;

    public Camera(int device) {
        m_activeDevice = device;
        m_usbCameras = new UsbCamera[UsbCamera.enumerateUsbCameras().length];

        m_usbCameras[device] = CameraServer.getInstance().startAutomaticCapture("USB Camera " + device, device);
    }

    public static void switchCamera(int toDeviceNum) {
        if(m_usbCameras[toDeviceNum] != null) {
            CameraServer.getInstance().getServer().setSource(m_usbCameras[toDeviceNum]);
        } else {
            m_usbCameras[toDeviceNum] = CameraServer.getInstance().startAutomaticCapture("USB Camera 0", toDeviceNum);
        }
    }
}
