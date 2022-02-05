package frc.robot.core751.wrappers;

import edu.wpi.first.cscore.*;
import edu.wpi.first.cameraserver.CameraServer;

public class wCamera {
    public Integer id;
    public UsbCamera usbCamera;

    public wCamera(int device) {
        id = device;
        usbCamera = CameraServer.startAutomaticCapture("USB Camera " + device, device);
    }
    
}
