package frc.robot.core751.subsystems.camera;



import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.core751.subsystems.camera.LimeLight.CameraType;

public class PhotonVision extends LimeLight{ //This might be better as it's own class idk.
    
    PhotonCamera camera = new PhotonCamera("photonvision");

    public PhotonVision(){
        super(CameraType.photonVision);
    }

    public List<PhotonTrackedTarget> getTargets(){
        return getResults().targets;
    }

    public PhotonTrackedTarget getBestTarget(){
        return getResults().getBestTarget();
    }

    public PhotonPipelineResult getResults(){
        return camera.getLatestResult();
    }


}
