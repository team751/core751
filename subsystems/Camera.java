package frc.robot.core751.subsystems;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Camera extends SubsystemBase {
    int activeDevice;
    static UsbCamera[] usbCameras;

    public Camera(int device) {
        if(device == null){
            return;
        }
        activeDevice = device;
        usbCameras = new UsbCamera[UsbCamera.enumerateUsbCameras().length];

        usbCameras[device] = CameraServer.getInstance().startAutomaticCapture("USB Camera " + device, device);
    }

    public static void switchCamera(int toDeviceNum) {
        if(usbCameras[toDeviceNum] != null && activeDevice != toDeviceNum) {
            CameraServer.getInstance().getServer().setSource(usbCameras[toDeviceNum]);
            activeDevice = toDeviceNum;
        } else {
            usbCameras[toDeviceNum] = CameraServer.getInstance().startAutomaticCapture("USB Camera " + toDeviceNum, toDeviceNum);
        }
    }
}
