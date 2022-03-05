package frc.robot.core751.subsystems.camera;



import java.time.Period;
import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.core751.CoreConstants;
import frc.robot.core751.subsystems.camera.LimeLight.CameraType;

public class PhotonVision {
    
private final PhotonCamera photonCamera;

    public PhotonVision(){
        this(CoreConstants.mainPipeline);
    }

    public PhotonVision(int pipeline){
        this(pipeline,"gloworm");
    }

    public PhotonVision(int pipeline, String name){
        photonCamera = new PhotonCamera(name);
        photonCamera.setPipelineIndex(pipeline);
    }

    public double getDistance(){
        if (hasTargets()) {
            double range =
                    PhotonUtils.calculateDistanceToTargetMeters(
                            1.016, //Camera Height 
                            2.5654, //Target Height
                            Units.degreesToRadians(10.5), //Angle of camera
                            Units.degreesToRadians(getBestTarget().getPitch()));
            return range;
        }else{
            return Double.MAX_VALUE;
        }
    }


    public double getYaw(){
        if(hasTargets()){
            return getBestTarget().getYaw();
        }else{
            return Double.MAX_VALUE;
        }
    }

    public boolean hasTargets(){
        return getResults().hasTargets();
    }

    public List<PhotonTrackedTarget> getTargets(){
        return getResults().targets;
    }

    public PhotonTrackedTarget getBestTarget(){
        return getResults().getBestTarget();
    }

    public PhotonPipelineResult getResults(){
        return photonCamera.getLatestResult();
    }


}
