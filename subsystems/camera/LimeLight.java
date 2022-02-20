package frc.robot.core751.subsystems.camera;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class LimeLight extends SubsystemBase{

    public enum CameraType{
        limeLight,
        photonVision

    }
    
    public enum CamMode{
        Processing,
        DriveCam
    }

   private NetworkTable table;

   private NetworkTableEntry target;

   private NetworkTableEntry x;
   private NetworkTableEntry y;
   private NetworkTableEntry a;
   private NetworkTableEntry s;

   private NetworkTableEntry cameraMode;

   private CamMode currentCamMode = CamMode.Processing;

   public CameraType cameraType;

    public LimeLight(CameraType cameraType){
        this.cameraType = cameraType;
        switch(cameraType){
            case limeLight:
                table = NetworkTableInstance.getDefault().getTable("limelight");
                target = table.getEntry("tv");
                x = table.getEntry("tx");
                y = table.getEntry("ty");
                a = table.getEntry("ta");
                s = table.getEntry("ts");
                cameraMode = table.getEntry("camMode");
                break;
            
            case photonVision:
                table = NetworkTableInstance.getDefault().getTable("photonvision/gloworm");
                target = table.getEntry("targetFound");
                x = table.getEntry("targetPixelsX");
                y = table.getEntry("targetPixelsY");
                a = table.getEntry("targetArea");
                s = table.getEntry("targetSkew");
                break;
        }
        switchCamMode(currentCamMode);
    }

   

    public boolean target(){
       return target.getDouble(0.0) == (1.0);
    }

    public double getX() {
        return x.getDouble(0.0);
    }

    public double getY() {
        return y.getDouble(0.0);
    }

    public double getA() {
        return a.getDouble(0.0);
    }

    public double getS() {
        return s.getDouble(0.0);
    }

    public void switchCamMode(){
        if(currentCamMode == CamMode.Processing){
            switchCamMode(CamMode.DriveCam);
        }else{
            switchCamMode(CamMode.Processing);
        }
    }

    public void switchCamMode(CamMode camMode){
        if(camMode == null){
            switchCamMode();
        }else{
            currentCamMode = camMode;
            switch(cameraType){
                case limeLight:
                    cameraMode.setDouble(camMode.ordinal());
                    break;
                case photonVision:
                    
                    break;
            }
           
        }
    }

    public CameraType getCameraType() {
        return cameraType;
    }


    
}
