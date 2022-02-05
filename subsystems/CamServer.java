package frc.robot.core751.subsystems;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.core751.wrappers.wCamera;

public class CamServer extends SubsystemBase{
    public wCamera currentCam = null;

    public CamServer(){
        //just so this is still a valid declaration
    }
    
    public CamServer(wCamera initialCam){
        switchCamera(initialCam);
    }

    public void switchCamera(wCamera cam) {
        if(cam == null){
            return;
        }
        if((currentCam == null) || cam.id != currentCam.id) {
            CameraServer.getServer().setSource(cam.usbCamera);
            currentCam = cam;
        }
    }
    
}
